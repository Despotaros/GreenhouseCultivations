package atzios.greenhouse.cultivations.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import atzios.greenhouse.cultivations.contents.ContentGreenhouse;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouse;
import atzios.greenhouse.cultivations.R;

/**
 * NewGreenhouseActivity
 * Δημιουργει νεο θερμοκηπιο
 * Created by Atzios on 7/9/2015.
 */
public class NewGreenhouseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_greenhouse);

        /* θετουμε δικο μας toolbar σαν actionbar */
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Ενεργοποιουμε το βελακι για τερματισμο του activity */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_greenhouse,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); //Τερματισε το activity
                break;
            case R.id.action_done:
                /* Δημιουργησε το θερμοκηπιο */
                createGreenhouse();
                /* Τερματισε το activity με αποτελεσμα ΟΚ */
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Δημιουργησε το θερμοκηπιο
     */
    private void createGreenhouse () {
        /* Διαβασε τις τιμες του θερμοκηπιου απο τα components */
        ContentGreenhouse contentGreenhouse = new ContentGreenhouse();

        EditText ed = (EditText)findViewById(R.id.edName);
        contentGreenhouse.setName(ed.getText().toString());

        ed = (EditText)findViewById(R.id.edArea);
        double area = 0;

        try {
            area = Double.parseDouble(ed.getText().toString());
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        contentGreenhouse.setArea(area);
        ed = (EditText)findViewById(R.id.edAddress);
        contentGreenhouse.setAddress(ed.getText().toString());
        /* Γραψε το θερμοκηπιο στην βαση */
        if(!contentGreenhouse.getName().equals("")) {
            DataHelperGreenhouse dHelper = new DataHelperGreenhouse(this);
            dHelper.create(contentGreenhouse);
        }
        else
            Toast.makeText(this,R.string.greenhouse_empty_name,Toast.LENGTH_LONG).show();
    }
}
