package com.example.tijana.contactapplication.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by tijana on 13.3.18..
 */
@DatabaseTable(tableName = PhoneNumber.TABLE_NAME_PHONE_NUMBER)
public class PhoneNumber {
    public static final String TABLE_NAME_PHONE_NUMBER = "phone_number";

    public static final String FIELD_NAME_ID = "_id";
    public static final String FIELD_NAME_TELEPHONE_NUMBER = "telephone_number";
    public static final String FIELD_NAME_HOME_NUMBER = "home_number";
    public static final String FIELD_NAME_WORK_NUMBER = "work_number";
    public static final String FIELD_NAME_MOBILE_NUMBER = "mobile_number";
    public static final String FIELD_NAME_CONTACT = "contact";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_TELEPHONE_NUMBER)
    private String mTelephoneNumber;

    @DatabaseField(columnName = FIELD_NAME_HOME_NUMBER)
    private String mHomeNumber;

    @DatabaseField(columnName = FIELD_NAME_WORK_NUMBER)
    private String mWorkNumber;

    @DatabaseField(columnName = FIELD_NAME_MOBILE_NUMBER)
    private String mMobileNumber;

    @DatabaseField(columnName = FIELD_NAME_CONTACT, foreign = true, foreignAutoRefresh =
            true)
    private Contact mContact;

    public PhoneNumber() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTelephoneNumber() {
        return mTelephoneNumber;
    }

    public void setmTelephoneNumber(String mTelephoneNumber) {
        this.mTelephoneNumber = mTelephoneNumber;
    }

    public String getmHomeNumber() {
        return mHomeNumber;
    }

    public void setmHomeNumber(String mHomeNumber) {
        this.mHomeNumber = mHomeNumber;
    }

    public String getmWorkNumber() {
        return mWorkNumber;
    }

    public void setmWorkNumber(String mWorkNumber) {
        this.mWorkNumber = mWorkNumber;
    }

    public String getmMobileNumber() {
        return mMobileNumber;
    }

    public void setmMobileNumber(String mMobileNumber) {
        this.mMobileNumber = mMobileNumber;
    }

    public Contact getmContact() {
        return mContact;
    }

    public void setmContact(Contact mContact) {
        this.mContact = mContact;
    }

    @Override
    public String toString() {
        return mTelephoneNumber;
    }
}
