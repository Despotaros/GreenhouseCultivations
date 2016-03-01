package atzios.greenhouse.cultivations.activities;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import atzios.greenhouse.cultivations.DialogPickDate;
import atzios.greenhouse.cultivations.Greenhouse;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.contents.ContentCultivation;
import atzios.greenhouse.cultivations.contents.ContentGreenhouseCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperCultivation;

import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouseCultivation;

/**
 * NewCultivationActivity
 * Δημιουργει,ενημερωνει και διαγραφει μια καλλιεργεια ενος θερμοκηπιου
 * Created by Atzios on 26/12/2015.
 */
public class NewCultivationActivity extends AppCompatActivity {
    /* Η καλλιεργεια του θερμοκηπιου */
    private ContentGreenhouseCultivation greenhouseCultivation = new ContentGreenhouseCultivation();
    /* Τα ειδη καλλιεργειων */
    private ArrayList<ContentCultivation> cultivations = new ArrayList<>();
    /* Αν ειναι το activity σε edit mode */
    private boolean edit = false;
    /* Το id της καλλιεργειας (Ειναι -1 οταν ειναι να δημιουργησουμε θερμοκηπιο) */
    private int cId = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cultivation);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /* Βλεπουμε αν το activity εχει καλεστει σε edit mode */
        if(getIntent().getExtras() != null )
        {
            edit = getIntent().getExtras().getBoolean("edit",false);
            cId = getIntent().getExtras().getInt("id", -1);
        }
        if(edit)
            getSupportActionBar().setTitle(R.string.edit_cultivation);

        loadData();

        final Button btn = (Button)findViewById(R.id.btnDate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogPickDate dialogPickDate = new DialogPickDate(NewCultivationActivity.this,
                        NewCultivationActivity.this.findViewById(R.id.content));
                dialogPickDate.setOnDatePickListener(new DialogPickDate.PickDateListener() {
                    @Override
                    public void onDatePicked(int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,month,day,0,0,0);
                        calendar.set(Calendar.MILLISECOND,0);
                        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(NewCultivationActivity.this);
                        btn.setText(dateFormat.format(calendar.getTime()));
                        greenhouseCultivation.setDate(calendar.getTime().getTime());

                    }
                });
                dialogPickDate.create().show();
            }
        });

        Button b = (Button)findViewById(R.id.btnAdd);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCult = new Intent(NewCultivationActivity.this, JobsAndCultActivity.class);
                addCult.putExtra("page", 1);
                startActivityForResult(addCult, 89);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 89 && resultCode == -1) {
            loadData();
        }
    }

    /**
     * Δημιουργει την καλλιεργεια στην βαση
     */
    private void createGreenhouseCultivation() {
        CheckBox cb = (CheckBox)findViewById(R.id.cbActive);
        greenhouseCultivation.setGreenhouseId(Greenhouse.getInstance().getContent().getId());
        greenhouseCultivation.setActive(cb.isChecked());

        EditText ed = (EditText)findViewById(R.id.edDuration);
        try {
            greenhouseCultivation.setDuration(Integer.parseInt(ed.getText().toString()));
        }
        catch (NumberFormatException e) {
            greenhouseCultivation.setDuration(-1);
        }
        if(greenhouseCultivation.getDate() == 0) {
            greenhouseCultivation.setDate(Calendar.getInstance().getTime().getTime());
        }
        ed = (EditText)findViewById(R.id.edComments);
        greenhouseCultivation.setComments(ed.getText().toString());

        if(greenhouseCultivation.getCultivationId() != -1) {
            Context context = getApplicationContext();
            DataHelperGreenhouseCultivation helper = new DataHelperGreenhouseCultivation(context);
            if (edit)
                helper.update(greenhouseCultivation);
            else
                helper.create(greenhouseCultivation);

            finish();
        }
        else
            Toast.makeText(getApplicationContext(),R.string.select_cultivation_type,Toast.LENGTH_LONG).show();


    }

    /**
     * Αρχικοποιει τις τιμες των components
     * Αν ειναι και σε edit mode (Φορτωνει τις τιμες της καλλιεργειας προς ενημερωση
     */
    private void loadData() {
        if(edit) {
            DataHelperGreenhouseCultivation h = new DataHelperGreenhouseCultivation(this);
            greenhouseCultivation = h.get(cId);

            CheckBox cb = (CheckBox)findViewById(R.id.cbActive);
            cb.setChecked(greenhouseCultivation.isActive());

            EditText ed = (EditText)findViewById(R.id.edDuration);
            ed.setText(Integer.toString(greenhouseCultivation.getDuration()));

            ed = (EditText)findViewById(R.id.edComments);
            ed.setText(greenhouseCultivation.getComments());

            Button btn = (Button)findViewById(R.id.btnDate);
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(NewCultivationActivity.this);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(greenhouseCultivation.getDate()));
            btn.setText(dateFormat.format(calendar.getTime()));
        }
          /* Διαβασε ολα τα ειδη καλλιεργειων απο την βαση */
        DataHelperCultivation helper = new DataHelperCultivation(this);
        cultivations = helper.getAll();

        /* Δημιουργισε πινακα μονο με τα ονοματα των καλλιεργειων */
        ArrayList<String> names = new ArrayList<>();
        int pos = -1, index = -1;
        for(ContentCultivation cult : cultivations) {
            index ++;
            if(cult.getId() == greenhouseCultivation.getCultivationId())
                pos = index;
            names.add(cult.getName() +" - " + cult.getComments());
        }
        /* Αρχικοποιούμε το spinner μας */
        Spinner spinner = (Spinner)findViewById(R.id.spCultNames);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,names);
        spinner.setAdapter(adapter);
        /* Αν υπαρχει επιλεγμενη εργασια ενημερωσε την θεση του spinner */
        if(pos >= 0)
            spinner.setSelection(pos);
        else
            greenhouseCultivation.setCultivationId(-1);
        /* Οταν επιλεγετε καλλιεργεια */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /* Ενημερωνουμε την καλλιεργεια με το ειδος καλλιεργειας */
                greenhouseCultivation.setCultivationId(cultivations.get(position).getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                /* Δεν κανουμε τιποτα */
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Δημιουργια μενου */
        if(edit)
            getMenuInflater().inflate(R.menu.menu_edit_cultivation,menu);
        else
            getMenuInflater().inflate(R.menu.menu_new_greenhouse, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Η επιλογες του μενου του Activity */
        switch (item.getItemId()) {
            case android.R.id.home: /* Το βελακι πισω */
                finish();
                break;
            case R.id.action_done: /* Το βελακι επιβεβαιωσης */
                createGreenhouseCultivation();
                break;
            case R.id.action_delete: /* Ο καδος για διαγραφη */
                deleteCultivation();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Εμφανιζει διαλογο απου ρωτα τον χρηστη για
     * την διαγραφη της καλλιεργειας
     */
    private void deleteCultivation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setTitle(R.string.confirm_delete);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DataHelperGreenhouseCultivation h = new DataHelperGreenhouseCultivation(NewCultivationActivity.this);
                h.delete(greenhouseCultivation.getId());
                finish();
            }
        });
        builder.create().show();
    }
}
