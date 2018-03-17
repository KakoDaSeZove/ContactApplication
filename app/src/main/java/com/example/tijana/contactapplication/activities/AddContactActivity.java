package com.example.tijana.contactapplication.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.tijana.contactapplication.R;
import com.example.tijana.contactapplication.db.Contact;
import com.example.tijana.contactapplication.db.DatabaseHelper;
import com.example.tijana.contactapplication.fragment.DIalogTimeFragment;
import com.example.tijana.contactapplication.receiver.ContactReceiver;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.sql.SQLException;

import static com.example.tijana.contactapplication.activities.DetailActivity.EXTRA_NO;

public class AddContactActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private Contact contact = null;
    private ImageView preview;
    private String imagePath = null;

    private DIalogTimeFragment dialog;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        preview = (ImageView) findViewById(R.id.add_contact_edit_image);

        if (getIntent().getExtras() != null) {
            //! We are performing update action
            Integer contactNo = (Integer) getIntent().getExtras().get(EXTRA_NO);

            DatabaseHelper helper = new DatabaseHelper(this);
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

            EditText contactName = (EditText) findViewById(R.id.add_contact_edit_name);
            contactName.setText(contact.getmName());
            EditText contactSurname = (EditText) findViewById(R.id.add_contact_edit_surname);
            contactSurname.setText(contact.getmSurname());
            EditText contactAdress = (EditText) findViewById(R.id.add_contact_edit_adress);
            contactAdress.setText(contact.getmAdress());



            //! Must, must, must!!!
            if (imagePath == null) {
                //! Opening update for first time
                imagePath = contact.getmImage();
            }

            if (imagePath != null) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    preview.setImageBitmap(myBitmap);
                }
            }
        } else {
            //! We are performing add action
            if (imagePath != null) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    preview.setImageBitmap(myBitmap);
                }
            }
        }
    }

    public void onClickOK(View v) {
        // dohvati sve UI komponente
        EditText contactName = (EditText) findViewById(R.id.add_contact_edit_name);
        EditText contactSurname = (EditText) findViewById(R.id.add_contact_edit_surname);
        EditText contactAdress = (EditText) findViewById(R.id.add_contact_edit_adress);


        DatabaseHelper helper = new DatabaseHelper(this);

        Dao<Contact, Integer> contactDao = null;
        try {
            contactDao = helper.getContactDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (getIntent().getExtras() == null) {
//Ovo je novi contact
            Contact contactDB = new Contact();
            contactDB.setmName(contactName.getText().toString());
            contactDB.setmSurname(contactSurname.getText().toString());
            contactDB.setmAdress(contactAdress.getText().toString());
            contactDB.setmBirthday(dialog.getHourOfDay() + ":" + dialog.getMinute());

            contactDB.setmImage(imagePath);

            try {
                contactDao.create(contactDB);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
//Ovo je edit contact
            Integer contactNo = (Integer) getIntent().getExtras().get(EXTRA_NO);

            Contact contactDB = new Contact();
            contactDB.setmId(contactNo);
            contactDB.setmName(contactName.getText().toString());
            contactDB.setmSurname(contactSurname.getText().toString());
            contactDB.setmAdress(contactAdress.getText().toString());
            contactDB.setmBirthday(dialog.getHourOfDay() + ":" + dialog.getMinute());

            contactDB.setmImage(imagePath);

            try {
                contactDao.update(contactDB);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Intent i = new Intent(ContactReceiver.HAPPY_BIRTHDAY);
        i.putExtra(ContactReceiver.BIRTHDAY, dialog.getHourOfDay() + ":" + dialog.getMinute());
        i.putExtra(ContactReceiver.BIRTHDAY_NAME, contactName.getText().toString());
        sendBroadcast(i);

        finish();
    }

    public void onClickCancel(View v) {
        finish();
    }


    public void onSelectImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    /**
     * Sismtemska metoda koja se automatksi poziva ako se
     * aktivnost startuje u startActivityForResult rezimu
     * <p>
     * Ako je ti slucaj i ako je sve proslo ok, mozemo da izvucemo
     * sadrzaj i to da prikazemo. Rezultat NIJE sliak nego URI do te slike.
     * Na osnovu toga mozemo dobiti tacnu putnaju do slike ali i samu sliku
     */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                imagePath = getRealPathFromURI(getApplicationContext(), selectedImageUri);

                //! onResume will be called after this function return
            }
        }
    }

    public static String getRealPathFromURI(Context context, Uri uri) {

        String filePath = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && uri.getHost().contains("com.android.providers.media")) {
            // Image pick from recent

            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } else {
            // image pick from gallery
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    return cursor.getString(columnIndex);
                }
                cursor.close();
            }
            return null;
        }

    }

    public void onShowDialog(View view) {
        dialog = new DIalogTimeFragment();
        dialog.show(getSupportFragmentManager(), "tag_name");

    }


}
