package com.example.tijana.contactapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tijana.contactapplication.R;
import com.example.tijana.contactapplication.db.Contact;
import com.example.tijana.contactapplication.receiver.ContactReceiver;

public class HappyBirthdayActivity extends AppCompatActivity {

    private Contact contact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy_birthday);

        TextView birthdayContact = (TextView)findViewById(R.id.happy_birthday_contact);

        birthdayContact.setText(getIntent().getExtras().getString(ContactReceiver.BIRTHDAY_NAME));
    }
}
