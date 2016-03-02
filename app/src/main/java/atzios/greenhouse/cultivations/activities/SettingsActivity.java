package atzios.greenhouse.cultivations.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import atzios.greenhouse.cultivations.R;

/**
 * Created by Panos on 1/3/2016.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        /* Θετουμε σαν Action Bar ενα δικο μας toolbar */
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        /* Ενεργοποιουμε το βελακι του πισω */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        getFragmentManager().beginTransaction().replace(R.id.container,new SettingsFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
