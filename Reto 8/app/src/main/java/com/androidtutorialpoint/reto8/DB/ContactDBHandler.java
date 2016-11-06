package com.androidtutorialpoint.reto8.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ContactDBHandler extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "developercontacts.db";
    private static final int DATABASE_VERSION = 1;


    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "conId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CONSULTING = "isConsulting";
    public static final String COLUMN_DEVELOPMENT = "isDevelopment";
    public static final String COLUMN_SOFTWARE_FACTORY = "isSoftwareFactory";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_CONTACTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_URL + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_CONSULTING + " INTEGER, " +
                    COLUMN_DEVELOPMENT + " INTEGER, " +
                    COLUMN_SOFTWARE_FACTORY + " INTEGER " +
                    ")";


    public ContactDBHandler(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CONTACTS);
        db.execSQL(TABLE_CREATE);
    }
}
