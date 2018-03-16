package com.example.tijana.contactapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.tijana.contactapplication.R;
import com.example.tijana.contactapplication.db.Contact;
import com.example.tijana.contactapplication.db.DatabaseHelper;
import com.example.tijana.contactapplication.db.PhoneNumber;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import static com.example.tijana.contactapplication.activities.DetailActivity.EXTRA_NO;

public class AddNumberActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Contact contact = null;
    private int phoneCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);
        Spinner spinnerNumber = (Spinner) findViewById(R.id.add_number_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_number_category, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumber.setAdapter(adapter);
        spinnerNumber.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        phoneCategory = pos;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void onClickOK(View v) {
        // dohvati sve UI komponente
        EditText phoneNumber = (EditText) findViewById(R.id.add_number);

        DatabaseHelper helper = new DatabaseHelper(this);

        Dao<PhoneNumber, Integer> numberDao = null;
        try {
            numberDao = helper.getNumberDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int contactNo = (Integer) getIntent().getExtras().get(EXTRA_NO);

        Dao<Contact, Integer> contactDao = null;
        try {
            contactDao = helper.getContactDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            contact = contactDao.queryForId(contactNo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PhoneNumber numberDB = new PhoneNumber();

        if (phoneCategory == 0) {
            numberDB.setmHomeNumber(phoneNumber.getText().toString());
        } else if (phoneCategory == 1) {
            numberDB.setmWorkNumber(phoneNumber.getText().toString());
        } else if (phoneCategory == 2) {
            numberDB.setmMobileNumber(phoneNumber.getText().toString());
        }
        numberDB.setmContact(contact);

        try {
            helper.getNumberDao().create(numberDB);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        finish();
    }

    public void onClickCancel(View v) {
        finish();
    }

}
