package com.example.tijana.contactapplication.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tijana.contactapplication.R;
import com.example.tijana.contactapplication.db.Contact;
import com.example.tijana.contactapplication.db.DatabaseHelper;
import com.example.tijana.contactapplication.db.PhoneNumber;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_NO = "contactNo";

    private Contact contact = null;
    private List<PhoneNumber> number = null;
    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int actorNo = (Integer) getIntent().getExtras().get(EXTRA_NO);

        DatabaseHelper helper = new DatabaseHelper(this);
        Dao<Contact, Integer> contactDao = null;
        try {
            contactDao = helper.getContactDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            contact = contactDao.queryForId(actorNo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ImageView contactImage = (ImageView) findViewById(R.id.detail_image);
        if (contact.getmImage() != null) {
            File imgFile = new File(contact.getmImage());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                contactImage.setImageBitmap(myBitmap);
            }
        }

        TextView contactName = (TextView) findViewById(R.id.detail_name);
        contactName.setText(contact.getmName());

        TextView contactSurname = (TextView) findViewById(R.id.detail_surname);
        contactSurname.setText(contact.getmSurname());

        TextView contactAdress = (TextView) findViewById(R.id.detail_adress);
        contactAdress.setText(contact.getmAdress());

        TextView contactBirthday = (TextView) findViewById(R.id.detail_birthday);
        contactBirthday.setText(contact.getmBirthday());

        listView = (ListView) findViewById(R.id.list_of_phone_numbers);

        ArrayList<String> numberList = new ArrayList<>();
        for (PhoneNumber pn : contact.getmPhoneNumber()) {
            if (pn.getmHomeNumber() != null) {
                numberList.add("Home number: " + pn.getmHomeNumber());
            } else if (pn.getmMobileNumber() != null) {
                numberList.add("Mobile number: " + pn.getmMobileNumber());
            } else if (pn.getmWorkNumber() != null) {
                numberList.add("Work number: " + pn.getmWorkNumber());
            }
        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                this,
                simple_list_item_1,
                numberList) {
        };
        listView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //! Add
        getMenuInflater().inflate(R.menu.action_bar_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean allowToast = sharedPreferences.getBoolean(getString(R.string.preferences_toast_key), false);
        boolean allowNotification = sharedPreferences.getBoolean(getString(R.string.preferences_notification_key), false);

        switch (item.getItemId()) {
            case R.id.action_add_number:

                if (allowToast == true) {
                    Toast.makeText(getApplicationContext(), "Data about movie will be added", Toast.LENGTH_LONG).show();
                }
                if (allowNotification == true) {

                    String channelId = "actor_channel_id";

                    android.support.v4.app.NotificationCompat.Builder mBuilder =
                            new android.support.v4.app.NotificationCompat.Builder(this, channelId);
                    mBuilder.setSmallIcon(R.drawable.ic_notification_add_number);
                    mBuilder.setContentTitle(getString(R.string.notification_add_number_title));
                    mBuilder.setContentText(getString(R.string.notification_add_number_text));

                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    //! For Android Oreo add  this Notification Channel
                    NotificationChannel channel = new NotificationChannel(channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);

                    mNotificationManager.createNotificationChannel(channel);

                    mNotificationManager.notify(001, mBuilder.build());
                }
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
//                    builder.setContentTitle("Add Movie");
//                    builder.setSmallIcon(R.drawable.ic_notification_add_movie);
//                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
//                            R.drawable.ic_notification_add_movie));
//                    builder.setContentText("New movie has been added in the list");
//
//                    NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                    manager.notify(3, builder.build());
//                }
                Intent intent = new Intent(DetailActivity.this,
                        AddNumberActivity.class);
                intent.putExtra(DetailActivity.EXTRA_NO, contact.getmId());
                startActivity(intent);
                return true;

            case R.id.action_edit:

                if (allowToast == true) {
                    Toast.makeText(getApplicationContext(), "Data about contact will be edited", Toast.LENGTH_LONG).show();
                }
                if (allowNotification == true) {

                    String channelId = "contact_channel_id";

                    android.support.v4.app.NotificationCompat.Builder mBuilder =
                            new android.support.v4.app.NotificationCompat.Builder(this, channelId);
                    mBuilder.setSmallIcon(R.drawable.ic_notification_edit);
                    mBuilder.setContentTitle(getString(R.string.notification_edit_title));
                    mBuilder.setContentText(getString(R.string.notification_edit_text));

                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    //! For Android Oreo add  this Notification Channel
                    NotificationChannel channel = new NotificationChannel(channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);

                    mNotificationManager.createNotificationChannel(channel);

                    mNotificationManager.notify(003, mBuilder.build());
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
//                    builder.setContentTitle("Edit Actor");
//                    builder.setSmallIcon(R.drawable.ic_notification_edit);
//                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
//                            R.drawable.ic_notification_edit));
//                    builder.setContentText("The data about the Actor are about to be editted");
//
//                    NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                    manager.notify(4, builder.build());

                }

                intent = new Intent(DetailActivity.this,
                        AddContactActivity.class);
                intent.putExtra(DetailActivity.EXTRA_NO, contact.getmId());
                startActivity(intent);

                return true;
            case R.id.action_delete:

                if (allowToast == true) {
                    Toast.makeText(getApplicationContext(), "Data about contact will be deleted", Toast.LENGTH_LONG).show();
                }
                if (allowNotification == true) {

                    String channelId = "actor_channel_id";

                    android.support.v4.app.NotificationCompat.Builder mBuilder =
                            new android.support.v4.app.NotificationCompat.Builder(this, channelId);
                    mBuilder.setSmallIcon(R.drawable.ic_notification_delete);
                    mBuilder.setContentTitle(getString(R.string.notification_delete_title));
                    mBuilder.setContentText(getString(R.string.notification_delete_text));

                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    //! For Android Oreo add  this Notification Channel
                    NotificationChannel channel = new NotificationChannel(channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);

                    mNotificationManager.createNotificationChannel(channel);

                    mNotificationManager.notify(001, mBuilder.build());
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
//                    builder.setContentTitle("Delete Actor");
//                    builder.setSmallIcon(R.drawable.ic_notification_delete);
//                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
//                            R.drawable.ic_notification_delete));
//                    builder.setContentText("The Actor has been deeted from the list");
//
//                    NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                    manager.notify(5, builder.build());

                }

                int actorNo = (Integer) getIntent().getExtras().get(EXTRA_NO);
                DatabaseHelper helper = new DatabaseHelper(this);

                Dao<Contact, Integer> contactDao = null;
                try {
                    contactDao = helper.getContactDao();
                    contactDao.deleteById(actorNo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
