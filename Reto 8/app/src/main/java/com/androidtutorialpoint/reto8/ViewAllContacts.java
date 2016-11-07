package com.androidtutorialpoint.reto8;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.androidtutorialpoint.reto8.Model.Contact;
import com.androidtutorialpoint.reto8.DB.ContactOperations;

import java.util.List;


public class ViewAllContacts extends AppCompatActivity {

    private ContactOperations contactOps;
    List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_contacts);

        ListView listView = (ListView) findViewById(R.id.list);
        contactOps = new ContactOperations(this);
        contactOps.open();
        contacts = contactOps.getAllContacts();
        contactOps.close();
        final ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        listView.setAdapter(adapter);

        Button filterButton = (Button) findViewById(R.id.button_filter_contact);
        final CheckBox consultingCheckbox = (CheckBox) findViewById(R.id.check_type_filter_consulting);
        final CheckBox developerCheckbox = (CheckBox) findViewById(R.id.check_type_filter_developer);
        final CheckBox softwareFactoryCheckbox = (CheckBox) findViewById(R.id.check_type_filter_softwarefactory);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactOps.open();
                contacts = contactOps.getFilteredContacts(
                        consultingCheckbox.isChecked(),
                        developerCheckbox.isChecked(),
                        softwareFactoryCheckbox.isChecked());
                contactOps.close();
                adapter.clear();
                adapter.addAll(contacts);
            }
        });
    }
}


