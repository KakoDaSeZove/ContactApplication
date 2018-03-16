package com.example.tijana.contactapplication.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by tijana on 13.3.18..
 */
@DatabaseTable(tableName = Contact.TABLE_NAME_CONTACT)
public class Contact {

    public static final String TABLE_NAME_CONTACT = "actors";

    public static final String FIELD_NAME_ID     = "_id";
    public static final String FIELD_NAME_NAME   = "name";
    public static final String FIELD_NAME_SURNAME   = "surname";
    public static final String FIELD_NAME_ADRESS   = "adress";
    public static final String FIELD_NAME_PHONE_NUMBERS = "phone_numbers";
    public static final String FIELD_NAME_IMAGE = "image";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String mName;

    @DatabaseField(columnName = FIELD_NAME_SURNAME)
    private String mSurname;

    @DatabaseField(columnName = FIELD_NAME_ADRESS)
    private String mAdress;

    @DatabaseField(columnName = FIELD_NAME_IMAGE)
    private String mImage;

    @ForeignCollectionField(columnName = FIELD_NAME_PHONE_NUMBERS, eager = true)
    private ForeignCollection<PhoneNumber> mPhoneNumber;

    public Contact() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSurname() {
        return mSurname;
    }

    public void setmSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    public String getmAdress() {
        return mAdress;
    }

    public void setmAdress(String mAdress) {
        this.mAdress = mAdress;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public ForeignCollection<PhoneNumber> getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(ForeignCollection<PhoneNumber> mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    @Override
    public String toString() {
        return  mName;
    }
}
