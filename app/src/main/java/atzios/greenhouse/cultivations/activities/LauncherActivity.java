package atzios.greenhouse.cultivations.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import atzios.greenhouse.cultivations.Preferences;
import atzios.greenhouse.cultivations.activities.MainActivity;
import atzios.greenhouse.cultivations.activities.SetupActivity;

/**
 * Launcher Activity
 * Ειναι το activity που τρεχει καθε φορα πρωτο και ελεγχει αν ειναι πρωτη φορα που
 * τρεχει η ερφαμογη

 * Created by Atzios on 30/10/2015.
 */
public class LauncherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Preferences preferences = new Preferences(this);
        /* Αν ειναι πρωτη φορα που τρεχει */
        boolean firstRun = preferences.getBoolean(Preferences.FIRST_RUN,true);
        if(firstRun) {
            /* Καλεσε πρωτα τον βοηθο για την δημιουργια θερμοκηπιου */
            Intent setup = new Intent(this, SetupActivity.class);
            startActivity(setup);
        }
        else {
            /* Αλλιως καλεσε την main */
            Intent main = new Intent(this,MainActivity.class);
            startActivity(main);
        }
        finish();
        super.onCreate(savedInstanceState);
    }
}
