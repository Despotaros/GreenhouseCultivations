package atzios.greenhouse.cultivations.activities;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import atzios.greenhouse.cultivations.Preferences;
import atzios.greenhouse.cultivations.R;

/**
 * Settings Activity
 * Activity οπου ο χρηστης μπορει να ρυθμισει τις βασικες λειτουργιες τις εφαρμογης
 * Created by Atzios on 1/3/2016.
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

        /* Φορτωνουμε το Preferences fragment */
        getFragmentManager().beginTransaction().replace(R.id.container,new SettingsFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * SettingsFragment ειναι το fragment που περιεχει ολες τις ρυθμισεις
     */
    public static class SettingsFragment extends PreferenceFragment {
        private final String CLASS_TAG = "SettingsFragment";
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            /* Δημιουργουμε τις ρυθμισεις απο το xml αρχειο */
            addPreferencesFromResource(R.xml.preferences);

           /* final Preferences preferences = new Preferences(getActivity());
            /* Το Switch για την ενεργοποιηση των ειδοποιησεων */
           /* SwitchPreference sPref = (SwitchPreference)findPreference("notifications");
            sPref.setChecked(preferences.getBoolean(Preferences.NOTIFICATIONS_STATUS,true));
            sPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ((SwitchPreference)preference).setChecked((boolean)newValue);
                    preferences.put(Preferences.NOTIFICATIONS_STATUS,(boolean)newValue);
                    return (boolean)newValue;
                }
            });
            /* Το Edit text για των ορισμο των μερων ειδοποιησης πρωτου ολοκληρωθει μια εργασια */
           /* EditTextPreference ePref = (EditTextPreference)findPreference("notification_offset");
            ePref.setText(Integer.toString(preferences.getInt(Preferences.DAYS_BEFORE_NOTIFY,5)));
            ePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    try {
                        preferences.put(Preferences.DAYS_BEFORE_NOTIFY, Integer.parseInt((String) newValue));
                        ((EditTextPreference)preference).setText((String)newValue);
                    }
                    catch (NumberFormatException e) {
                        Log.e(CLASS_TAG,e.getMessage());
                    }
                    return false;
                }
            }); */
        }
    }
}
