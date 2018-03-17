package com.example.tijana.contactapplication.activities;

import android.app.AlertDialog;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Toast;

import com.example.tijana.contactapplication.R;
import com.example.tijana.contactapplication.db.Contact;
import com.example.tijana.contactapplication.db.DatabaseHelper;

import com.example.tijana.contactapplication.receiver.ContactReceiver;
import com.j256.ormlite.dao.Dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;

public class MainActivity extends AppCompatActivity {

    private ListView listView = null;
    private List<Contact> contact = null;
    private DrawerLayout mDrawerLayout;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_PICTURE = 1;

    private ContactReceiver contactReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactReceiver = new ContactReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ContactReceiver.HAPPY_BIRTHDAY);
        registerReceiver(contactReceiver, filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id)

                    {
                        Intent intent = new Intent(MainActivity.this,
                                DetailActivity.class);
                        intent.putExtra(DetailActivity.EXTRA_NO, contact.get(position).getmId());
                        startActivity(intent);
                    }


                };
        listView = (ListView) findViewById(R.id.list_of_contact);
        listView.setOnItemClickListener(itemClickListener);


        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_camera:
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                }
                                return true;

                            case R.id.nav_gallery:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                                return true;
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        Intent i = new Intent(this, ContactService.class);
        startService(i);
    }


    @Override
    protected void onResume() {
        super.onResume();

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
        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                this,
                simple_list_item_1,
                contactList) {
        };
        listView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //! Add
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean allowToast = sharedPreferences.getBoolean(getString(R.string.preferences_toast_key), false);
        boolean allowNotification = sharedPreferences.getBoolean(getString(R.string.preferences_notification_key), false);


        switch (item.getItemId()) {
            case R.id.action_add:
                if (allowToast == true) {
                    Toast.makeText(getApplicationContext(), "Contact data will be added", Toast.LENGTH_LONG).show();
                }
                if (allowNotification == true) {
                    String channelId = "contact_channel_id";

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this, channelId);
                    mBuilder.setSmallIcon(R.drawable.ic_notification_add);
                    mBuilder.setContentTitle(getString(R.string.notification_add_title));
                    mBuilder.setContentText(getString(R.string.notification_add_text));

                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    //! For Android Oreo add  this Notification Channel
                    NotificationChannel channel = new NotificationChannel(channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);

                    mNotificationManager.createNotificationChannel(channel);

                    mNotificationManager.notify(001, mBuilder.build());



                   /* android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(getApplicationContext());
                    builder.setContentTitle("Add Actor");
                    builder.setSmallIcon(R.drawable.ic_notification_add);
                    builder
                    builder.setContentText("New Actor has been added in the list");
                    builder.setPriority(1);

                    NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(2, builder.build());*/
                }

                Intent intent = new Intent(MainActivity.this,
                        AddContactActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:

                intent = new Intent(MainActivity.this,
                        PreferenceSettingsActivity.class);
                startActivity(intent);

                return true;

            case R.id.action_about:
                AlertDialog.Builder basicAlertDialogBuilder = new AlertDialog.Builder(this);
                basicAlertDialogBuilder.setMessage(R.string.alert_dialog);
                basicAlertDialogBuilder.setPositiveButton(R.string.alert_button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_activity), "sjajan autor", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
                basicAlertDialogBuilder.create().show();
                return true;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
