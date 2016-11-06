package com.androidtutorialpoint.reto8;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.androidtutorialpoint.reto8.Model.Contact;
import com.androidtutorialpoint.reto8.DB.ContactOperations;

import java.util.List;


public class ViewAllContacts extends ListActivity {

    private ContactOperations contactOps;
    List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_contacts);
        contactOps = new ContactOperations(this);
        contactOps.open();
        contacts = contactOps.getAllContacts();
        contactOps.close();
        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contacts);
        setListAdapter(adapter);
    }
}


