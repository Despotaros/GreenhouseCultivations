package atzios.greenhouse.cultivations.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import atzios.greenhouse.cultivations.contents.ContentGreenhouseCultivation;
import atzios.greenhouse.cultivations.contents.ContentJob;
import atzios.greenhouse.cultivations.contents.ContentWork;
import atzios.greenhouse.cultivations.datahelpers.DataHelperCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouseCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperJob;
import atzios.greenhouse.cultivations.datahelpers.DataHelperWork;

/**
 * NewWorkActivity
 * Δημιουργει,ενημερωνει και διαγραφει μια εργασια ενος θερμοκηπιου
 * Created by Atzios on 29/12/2015.
 */
public class NewWorkActivity extends AppCompatActivity {
    /* Η εργασια του θερμοκηπιου */
    private ContentWork work = new ContentWork();
    /* Πινακας με τις διαθεσιμες εργασιες */
    private ArrayList<ContentJob> jobs = new ArrayList<>();
    /* Πινακας με τις προγραμματισμενες καλλιεργειες */
    private ArrayList<ContentGreenhouseCultivation> cultivations = new  ArrayList<>();
    /* Αν το activity ειναι σε edit mode */
    private boolean edit = false;
    /* Το id της εργασιας. (Ειναι -1 οταν δημιουργουμε νεα εργασια */
    private int wId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_work);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /* Βλεπουμε αν το activity εχει καλεστει σε edit mode */
        if(getIntent().getExtras() != null )
        {
            edit = getIntent().getExtras().getBoolean("edit",false);
            wId = getIntent().getExtras().getInt("id", -1);
        }
        if(edit)
            getSupportActionBar().setTitle(R.string.edit_work);

        loadData();

        final Button btn = (Button)findViewById(R.id.btnDate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogPickDate dialogPickDate = new DialogPickDate(NewWorkActivity.this,
                        NewWorkActivity.this.findViewById(R.id.content));
                dialogPickDate.setOnDatePickListener(new DialogPickDate.PickDateListener() {
                    @Override
                    public void onDatePicked(int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,month,day,0,0,0);

                        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(NewWorkActivity.this);
                        btn.setText(dateFormat.format(calendar.getTime()));
                        work.setDate(calendar.getTime().getTime());

                    }
                });
                dialogPickDate.create(work.getDate()).show();

            }
        });

        Button b = (Button)findViewById(R.id.btnAddJob);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCult = new Intent(NewWorkActivity.this, JobsAndCultActivity.class);
                addCult.putExtra("page", 0);
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
     * Αρχικοποιουμε το spinner με τα ονοματα των ενεργων καλλιεργειων του
     * επιλεγμενου θερμοκηπιου
     */
    private void invalidateCultivationSpinner() {
        DataHelperGreenhouseCultivation cultHelper = new DataHelperGreenhouseCultivation(this);
        cultivations = cultHelper.getAll(Greenhouse.getInstance().getContent().getId(),true);

        /* Δημιουργουμε την λιστα με τα ονοματα των καλλιεργειων */
        ArrayList<String> names = new ArrayList<>();
        names.add(getText(R.string.not_selected).toString());
        DataHelperCultivation cultTypeHelper = new DataHelperCultivation(this);
        int pos = -1,index = -1;
        for(ContentGreenhouseCultivation cult : cultivations) {
            index++;
            if(work.getCultivationId() == cult.getId())
                pos = index;
            names.add(cultTypeHelper.get(cult.getCultivationId()).getName());
        }

        Spinner spinner = (Spinner)findViewById(R.id.spCultNames);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,names);
        spinner.setAdapter(adapter);
        if(pos >= 0)
            spinner.setSelection(pos);
        else
            work.setCultivationId(-1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >0)
                work.setCultivationId(cultivations.get(position-1).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    /**
     * Αρχηκοποιουμε το spinner με τα ονοματα των εργασιων του
     * επιλεγμενου θερμοκηπιου
     */
    private void invalidateJobsSpinner() {
        DataHelperJob helper = new DataHelperJob(this);
        jobs = helper.getAll();

        ArrayList<String> names = new ArrayList<>();
        int pos = -1;
        int index = -1;
        for(ContentJob job : jobs) {
            index ++;
            if(job.getId() == work.getJobId())
                pos = index;
            names.add(job.getName());
        }

        Spinner spinner = (Spinner)findViewById(R.id.spJobNames);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,names);
        spinner.setAdapter(adapter);
        if(pos >= 0)
            spinner.setSelection(pos);
        else
            work.setJobId(-1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                work.setJobId(jobs.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    /**
     * Αρχικοποιει τις τιμες των components
     * Αν ειναι και σε edit mode (Φορτωνει τις τιμες της καλλιεργειας προς ενημερωση
     */
    private void loadData() {

        if(edit) {
            DataHelperWork helperWork = new DataHelperWork(this);
            work = helperWork.get(wId);

            CheckBox cb = (CheckBox)findViewById(R.id.cbActive);
            cb.setChecked(work.isPending());

            EditText ed ;

            ed = (EditText)findViewById(R.id.edComments);
            ed.setText(work.getComments());

            Button btn = (Button)findViewById(R.id.btnDate);
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(NewWorkActivity.this);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(work.getDate()));
            btn.setText(dateFormat.format(calendar.getTime()));


        }
        invalidateCultivationSpinner();
        invalidateJobsSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(edit)
            getMenuInflater().inflate(R.menu.menu_edit_cultivation,menu);
        else
            getMenuInflater().inflate(R.menu.menu_new_greenhouse, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_done:
                createGreenhouseCultivation();
                break;
            case R.id.action_delete:
                deleteWork();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Δημιουργει την εργασια στην βαση
     */
    private void createGreenhouseCultivation() {
        CheckBox cb = (CheckBox)findViewById(R.id.cbActive);
        work.setGreenhouseId(Greenhouse.getInstance().getContent().getId());
        work.setPending(cb.isChecked());

        EditText ed;

        if(work.getDate() == 0) {
            work.setDate(Calendar.getInstance().getTime().getTime());
        }
        ed = (EditText)findViewById(R.id.edComments);
        work.setComments(ed.getText().toString());

        if(work.getJobId() != -1) {
            Context context = getApplicationContext();
            DataHelperWork helper = new DataHelperWork(context);
            if (edit)
                helper.update(work);
            else
                helper.create(work);

            finish();
        }
        else
            Toast.makeText(getApplicationContext(), R.string.select_job_type, Toast.LENGTH_LONG).show();


    }

    /**
     * Εμφανιζει διαλογο oπου ρωτα τον χρηστη για
     * την διαγραφη της εργασιας
     */
    private void deleteWork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setTitle(R.string.confirm_delete);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DataHelperWork h = new DataHelperWork(NewWorkActivity.this);
                h.delete(work.getId());
                finish();
            }
        });
        builder.create().show();
    }



}
