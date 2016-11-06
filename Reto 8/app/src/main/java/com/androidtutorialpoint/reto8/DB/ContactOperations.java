package com.androidtutorialpoint.reto8.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.androidtutorialpoint.reto8.Model.Contact;
import java.util.ArrayList;
import java.util.List;


public class ContactOperations {
    public static final String LOGTAG = "DEVCONT_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            ContactDBHandler.COLUMN_ID,
            ContactDBHandler.COLUMN_NAME,
            ContactDBHandler.COLUMN_URL,
            ContactDBHandler.COLUMN_PHONE,
            ContactDBHandler.COLUMN_EMAIL,
            ContactDBHandler.COLUMN_DESCRIPTION,
            ContactDBHandler.COLUMN_CONSULTING,
            ContactDBHandler.COLUMN_DEVELOPMENT,
            ContactDBHandler.COLUMN_SOFTWARE_FACTORY
    };

    public ContactOperations(Context context){
        dbhandler = new ContactDBHandler(context);
    }

    public void open() {
        Log.i(LOGTAG,"Database Opened");
        database = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    public Contact addContact(Contact contact){
        ContentValues values  = new ContentValues();
        values.put(ContactDBHandler.COLUMN_NAME, contact.getName());
        values.put(ContactDBHandler.COLUMN_URL, contact.getUrl());
        values.put(ContactDBHandler.COLUMN_PHONE, contact.getPhone());
        values.put(ContactDBHandler.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactDBHandler.COLUMN_DESCRIPTION, contact.getDescription());
        values.put(ContactDBHandler.COLUMN_CONSULTING, contact.isConsulting());
        values.put(ContactDBHandler.COLUMN_DEVELOPMENT, contact.isDevelopment());
        values.put(ContactDBHandler.COLUMN_SOFTWARE_FACTORY, contact.isSoftwareFactory());

        long insertId = database.insert(ContactDBHandler.TABLE_CONTACTS, null, values);
        contact.setConId(insertId);

        return contact;
    }

    /**
     * Obtiene un contacto existente.
     *
     * @param id identificador del contacto a obtener
     * @return contacto identificado con el id indicado.
     */
    public Contact getContact(long id) {

        Cursor cursor = database.query(ContactDBHandler.TABLE_CONTACTS, allColumns,
                ContactDBHandler.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new Contact(Long.parseLong(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                cursor.getInt(6), cursor.getInt(7), cursor.getInt(8));
    }

    public List<Contact> getAllContacts() {

        Cursor cursor = database.query(ContactDBHandler.TABLE_CONTACTS, allColumns, null, null, null, null, null);

        List<Contact> contacts = new ArrayList<>();
        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                Contact contact = new Contact();
                contact.setConId(cursor.getLong(cursor.getColumnIndex(ContactDBHandler.COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(ContactDBHandler.COLUMN_NAME)));
                contact.setUrl(cursor.getString(cursor.getColumnIndex(ContactDBHandler.COLUMN_URL)));
                contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactDBHandler.COLUMN_PHONE)));
                contact.setEmail(cursor.getString(cursor.getColumnIndex(ContactDBHandler.COLUMN_EMAIL)));
                contact.setDescription(cursor.getString(cursor.getColumnIndex(ContactDBHandler.COLUMN_DESCRIPTION)));
                contact.setConsulting(cursor.getInt(cursor.getColumnIndex(ContactDBHandler.COLUMN_CONSULTING)));
                contact.setDevelopment(cursor.getInt(cursor.getColumnIndex(ContactDBHandler.COLUMN_DEVELOPMENT)));
                contact.setSoftwareFactory(cursor.getInt(cursor.getColumnIndex(ContactDBHandler.COLUMN_SOFTWARE_FACTORY)));
                contacts.add(contact);
            }
        }

        return contacts;
    }

    /**
     * Actualiza un contacto.
     *
     * @param contact contacto a actualizar
     * @return identificador del contacto actualizado.
     */
    public int updateContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactDBHandler.COLUMN_NAME, contact.getName());
        values.put(ContactDBHandler.COLUMN_URL, contact.getUrl());
        values.put(ContactDBHandler.COLUMN_PHONE, contact.getPhone());
        values.put(ContactDBHandler.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactDBHandler.COLUMN_DESCRIPTION, contact.getDescription());
        values.put(ContactDBHandler.COLUMN_CONSULTING, contact.isConsulting());
        values.put(ContactDBHandler.COLUMN_DEVELOPMENT, contact.isDevelopment());
        values.put(ContactDBHandler.COLUMN_SOFTWARE_FACTORY, contact.isSoftwareFactory());

        return database.update(ContactDBHandler.TABLE_CONTACTS, values,
                ContactDBHandler.COLUMN_ID + "=?", new String[] { String.valueOf(contact.getConId())});
    }

    public void removeContact(Contact employee) {
        database.delete(ContactDBHandler.TABLE_CONTACTS, ContactDBHandler.COLUMN_ID + "=" + employee.getConId(), null);
    }
}
