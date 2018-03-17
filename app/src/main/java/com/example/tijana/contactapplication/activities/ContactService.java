package com.example.tijana.contactapplication.activities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.tijana.contactapplication.db.Contact;
import com.example.tijana.contactapplication.db.DatabaseHelper;
import com.example.tijana.contactapplication.receiver.ContactReceiver;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactService extends Service {
    private List<Contact> contact = null;

    public ContactService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Ovaj deo sluzi da cestita rodjendan kada se aplikacija pokrene...
        DatabaseHelper helper = new DatabaseHelper(this);
        Dao<Contact, Integer> contactDao = null;
        try {
            contactDao = helper.getContactDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            contact = contactDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<String> contactList = new ArrayList<>();
        for (Contact c : contact) {
            contactList.add(c.getmName() + " " + c.getmSurname());
            Intent i = new Intent(ContactReceiver.HAPPY_BIRTHDAY);
            i.putExtra(ContactReceiver.BIRTHDAY, c.getmBirthday());
            i.putExtra(ContactReceiver.BIRTHDAY_NAME, c.getmName());
            sendBroadcast(i);
        }

        return START_NOT_STICKY;
    }
}
