package com.androidtutorialpoint.reto8;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidtutorialpoint.reto8.DB.ContactOperations;
import com.androidtutorialpoint.reto8.Model.Contact;

public class MainActivity extends AppCompatActivity {

    private Button addContactButton;
    private Button editContactButton;
    private Button deleteContactButton;
    private Button viewAllContactButton;
    private ContactOperations contactOps;
    private static final String EXTRA_EMP_ID = "edu.unal.reto8.conId";
    private static final String EXTRA_ADD_UPDATE = "edu.unal.reto8.add_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        addContactButton = (Button) findViewById(R.id.button_add_contact);
        editContactButton = (Button) findViewById(R.id.button_edit_contact);
        deleteContactButton = (Button) findViewById(R.id.button_delete_contact);
        viewAllContactButton = (Button)findViewById(R.id.button_view_contact);
        contactOps = new ContactOperations(MainActivity.this);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddUpdateContact.class);
                i.putExtra(EXTRA_ADD_UPDATE, "Add");
                startActivity(i);
            }
        });

        editContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConIdAndUpdateCon();
            }
        });
        deleteContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConIdAndRemoveCon();
            }
        });
        viewAllContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewAllContacts.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*int id = item.getItemId();
        if (id == R.id.menu_item_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }


    public void getConIdAndUpdateCon() {

        LayoutInflater li = LayoutInflater.from(this);
        View getEmpIdView = li.inflate(R.layout.dialog_get_emp_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog_get_emp_id.xml to alertdialog builder
        alertDialogBuilder.setView(getEmpIdView);

        final EditText userInput = (EditText) getEmpIdView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                Intent i = new Intent(MainActivity.this, AddUpdateContact.class);
                                i.putExtra(EXTRA_ADD_UPDATE, "Update");
                                i.putExtra(EXTRA_EMP_ID, Long.parseLong(userInput.getText().toString()));
                                startActivity(i);
                            }
                        }).create()
                .show();
    }


    public void getConIdAndRemoveCon(){

        LayoutInflater li = LayoutInflater.from(this);
        View getEmpIdView = li.inflate(R.layout.dialog_get_emp_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog_get_emp_id.xml to alertdialog builder
        alertDialogBuilder.setView(getEmpIdView);

        final EditText userInput = (EditText) getEmpIdView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // get user input and set it to result
                        // edit text
                        showConfirmationDialog(contactOps.getContact(Long.parseLong(userInput.getText().toString())));
                    }
                }).create()
                .show();
    }

    private void showConfirmationDialog(final Contact contact) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("¿Estás seguro de descartar " + contact.getName() + "?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactOps.removeContact(contact);
                        Toast.makeText(MainActivity.this,"Contacto descartado exitosamente!",Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(contactOps != null) {
            contactOps.open();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(contactOps != null) {
            contactOps.close();
        }
    }
}


