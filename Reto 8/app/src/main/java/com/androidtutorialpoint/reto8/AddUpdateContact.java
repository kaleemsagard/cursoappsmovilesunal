package com.androidtutorialpoint.reto8;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.androidtutorialpoint.reto8.Model.Contact;
import com.androidtutorialpoint.reto8.DB.ContactOperations;


public class AddUpdateContact extends AppCompatActivity {

    private static final String EXTRA_EMP_ID = "edu.unal.reto8.conId";
    private static final String EXTRA_ADD_UPDATE = "edu.unal.reto8.add_update";
    private EditText nameEditText;
    private EditText urlEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText descriptionEditText;
    private CheckBox isConsultingCheckBox;
    private CheckBox isDeveloperCheckBox;
    private CheckBox isSoftwareFactoryCheckBox;
    private Contact newContact;
    private Contact oldContact;
    private String mode;
    private ContactOperations contactOps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_contact);
        newContact = new Contact();
        oldContact = new Contact();
        nameEditText = (EditText)findViewById(R.id.edit_text_name);
        urlEditText = (EditText)findViewById(R.id.edit_text_url);
        emailEditText = (EditText) findViewById(R.id.edit_text_email);
        phoneEditText = (EditText)findViewById(R.id.edit_text_phone);
        descriptionEditText = (EditText)findViewById(R.id.edit_text_description);
        isConsultingCheckBox = (CheckBox) findViewById(R.id.check_type_consulting);
        isDeveloperCheckBox = (CheckBox) findViewById(R.id.check_type_developer);
        isSoftwareFactoryCheckBox = (CheckBox) findViewById(R.id.check_type_softwarefactory);
        Button addUpdateButton = (Button) findViewById(R.id.button_add_update_contact);

        contactOps = new ContactOperations(this);
        mode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);

        if(mode.equals("Update")) {
            addUpdateButton.setText("Actualizar contacto");
            initializeContact(getIntent().getLongExtra(EXTRA_EMP_ID, 0));
        }

        isConsultingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                newContact.setConsulting(b ? 1 : 0);
                if(mode.equals("Update")) {
                    oldContact.setConsulting(b ? 1 : 0);
                }
            }
        });

        isDeveloperCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                newContact.setDevelopment(b ? 1 : 0);
                if(mode.equals("Update")) {
                    oldContact.setDevelopment(b ? 1 : 0);
                }
            }
        });

        isSoftwareFactoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                newContact.setSoftwareFactory(b ? 1 : 0);
                if(mode.equals("Update")) {
                    oldContact.setSoftwareFactory(b ? 1 : 0);
                }
            }
        });

        addUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               contactOps.open();
               if(mode.equals("Add")) {
                   newContact.setName(nameEditText.getText().toString());
                   newContact.setUrl(urlEditText.getText().toString());
                   newContact.setEmail(emailEditText.getText().toString());
                   newContact.setPhone(phoneEditText.getText().toString());
                   newContact.setDescription(descriptionEditText.getText().toString());
                   newContact.setConsulting(isConsultingCheckBox.isChecked() ? 1 : 0);
                   newContact.setDevelopment(isDeveloperCheckBox.isChecked() ? 1 : 0);
                   newContact.setSoftwareFactory(isSoftwareFactoryCheckBox.isChecked() ? 1 : 0);
                   contactOps.addContact(newContact);
                   contactOps.close();
                   Toast.makeText(AddUpdateContact.this, newContact.getName() + " creado exitosamente",
                           Toast.LENGTH_SHORT).show();
                   finish();
               } else {
                   oldContact.setName(nameEditText.getText().toString());
                   oldContact.setUrl(urlEditText.getText().toString());
                   oldContact.setEmail(emailEditText.getText().toString());
                   oldContact.setPhone(phoneEditText.getText().toString());
                   oldContact.setDescription(descriptionEditText.getText().toString());
                   oldContact.setConsulting(isConsultingCheckBox.isChecked() ? 1 : 0);
                   oldContact.setDevelopment(isDeveloperCheckBox.isChecked() ? 1 : 0);
                   oldContact.setSoftwareFactory(isSoftwareFactoryCheckBox.isChecked() ? 1 : 0);
                   contactOps.updateContact(oldContact);
                   contactOps.close();
                   Toast.makeText(AddUpdateContact.this, oldContact.getName() + " actualizado exitosamente",
                           Toast.LENGTH_SHORT).show();
                   finish();
               }
            }
        });
    }


    // MÉTODOS PRIVADOS ///

    private void initializeContact(long empId) {
        contactOps.open();
        oldContact = contactOps.getContact(empId);
        contactOps.close();

        nameEditText.setText(oldContact.getName());
        urlEditText.setText(oldContact.getUrl());
        emailEditText.setText(oldContact.getEmail());
        phoneEditText.setText(oldContact.getPhone());
        descriptionEditText.setText(oldContact.getDescription());
        isConsultingCheckBox.setChecked(oldContact.isConsulting() == 1 ? Boolean.TRUE : Boolean.FALSE);
        isDeveloperCheckBox.setChecked(oldContact.isDevelopment() == 1 ? Boolean.TRUE : Boolean.FALSE);
        isSoftwareFactoryCheckBox.setChecked(oldContact.isSoftwareFactory() == 1 ? Boolean.TRUE : Boolean.FALSE);
    }
}


