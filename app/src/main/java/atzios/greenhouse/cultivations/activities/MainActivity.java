package atzios.greenhouse.cultivations.activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import java.util.ArrayList;

import atzios.greenhouse.cultivations.Greenhouse;
import atzios.greenhouse.cultivations.Preferences;
import atzios.greenhouse.cultivations.StoreFragmentManager;
import atzios.greenhouse.cultivations.contents.ContentGreenhouse;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouse;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.fragments.FragmentCalendar;
import atzios.greenhouse.cultivations.fragments.FragmentCultivations;
import atzios.greenhouse.cultivations.fragments.FragmentInfo;
import atzios.greenhouse.cultivations.fragments.FragmentWorks;

/**
 * Class Main
 * Ειναι το βασικο Activity οπου κραταει ολα τα fragments καθως και το κεντρικο μενου,
 * οπου ο χρήστης μπορει να αλληλεπιδρα με την εφαρμογη.
 * Created by Atzios on 2/08/2015.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /* Στατικες προκαθορισμενες μεταβλητες του συστηματος μας */
    public static final int REQUEST_NEW_GREENHOUSE = 20;
    private static final long DRAWER_CLOSE_DELAY_MS = 350;

    /* Ενας host manager οπου κραταει τα fragments για ευκολη προσβαση σε αυτα */
    private static StoreFragmentManager<Fragment> storeFragmentManager;
    /* Ενας handler οπου καθηστερουμε λιγο την αλλαγη των fragments */
    private Handler mDrawerActionHandler = new Handler() ;
    /* Το layout του μενου συρταρι */
    private DrawerLayout mDrawerLayout;
    /* Το κουμπι οπου ανοιγει και κλεινει το συρταρι */
    private ActionBarDrawerToggle mDrawerToggle;
    /* Το id του fragment οπου ειναι ενεργο καθε φορα */
    private int mNavItemId;
    /* Η θεση του θερμοκηπιου που ειναι επιλεγμενο */
    private static int gPos = 0;
    /* Η λίστα με ολα θα θερμοκηπια μας */
    private ArrayList<ContentGreenhouse> greenhouseContents = new ArrayList<>();
    /* Το επιλεγμενο drawer header */
    private View currentHeader;
    private NavigationView navigationView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /* Ο storeFragmentManager κραταει ολα τα fragment μας */
        storeFragmentManager = new StoreFragmentManager<>();

        /* Θετουμε ενα Toolbar σαν action bar για το activity */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // listen for navigation events
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        currentHeader = navigationView.getHeaderView(0);

        /* Δημιουργουμε το εικονιδιο του hamburger καθος και την λειτουργικοτητα του */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open,
                R.string.close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        /* Η ρυθμισεις μας */
        Preferences pref  = new Preferences(this);
        /* Αν ειναι η πρωτη φορα που τρεχει η εφαρμογη διχνουμε στον χρηστη το συρταρι */
        if(!pref.getBoolean(Preferences.DRAWER_STATE,false)) {
            pref.put(Preferences.DRAWER_STATE,true);
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        /* Οταν το savedInstanceState ειναι null τοτε ειναι η πρωτη φορα που καλειτε η Main,
         * και αρχικοποιουμε τα fragments μας μαζι και τις παραμετρους της εφαρμογης.
         */
        if(savedInstanceState == null) {
            FragmentInfo fragmentInfo = new FragmentInfo();
            /* Τα events που μας επιστρεφει το Fragment info */
            fragmentInfo.setFragmentCallbacks(new FragmentInfo.FragmentInfoCallbacks() {
                @Override
                public void onNameUpdated() {
                    /* Οταν ενημερωνετε το ονομα ενημερωσε και το ονομα στο spinner */
                    greenhouseSpinnerInvalidate();
                }

                @Override
                public void onPictureTaken(String path) {
                    /* Οταν τραβαμε νεα φωτοφραφια ενημερωσε το drawer */
                    setDrawerImage(path);
                }

                @Override
                public void onFragmentFocus(int id) {
                    /* το fragment που επιλεγετε μεσα απο το fragment info */
                    navigate(id);
                }
            });
            storeFragmentManager.addFragment(fragmentInfo, FragmentInfo.TAG, null);
            storeFragmentManager.addFragment(new FragmentCalendar(),FragmentCalendar.TAG,null);
            storeFragmentManager.addFragment(new FragmentCultivations(),FragmentCultivations.TAG,null);
            storeFragmentManager.addFragment(new FragmentWorks(),FragmentWorks.TAG,null);
            navigate(R.id.drawer_info);

        }
        /* Δημιουργουμε τα events της Main */
        drawerHeaderViewsEvents();
    }

    /**
     * Περιέχει ολα τα events απο τα views της Main (onClick,Touch etc)
     */
    private void drawerHeaderViewsEvents() {


        /* Οταν πατιετε το κουμπι New στο drawer καλεσε το activity οπου δημιουργει νεο θερμοκηπιο */
        Button btnNewGreenhouse = (Button)currentHeader.findViewById(R.id.btnNew);
        btnNewGreenhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGreenhouse = new Intent(MainActivity.this, NewGreenhouseActivity.class);
                startActivityForResult(newGreenhouse, REQUEST_NEW_GREENHOUSE);
            }
        });
        /* Δημιουγησε το spinner με τα ονοματα των θερμοκηπιων */
        greenhouseSpinnerInvalidate();
    }

    /**
     * Φορτωνει ολα τα θερμοκηπια απο την βαση, δημιουργει το spinner με τα ονοματα των θερμοκηπιων,
     * καθως και την λειτουργικοτητα οταν επιλεγετε ενα θερμοκηπιο
     */
    private void greenhouseSpinnerInvalidate() {
        /* Απο την βαση περνουμε ολα τα θερμοκηπια */
        final DataHelperGreenhouse dataHelperGreenhouse = new DataHelperGreenhouse(this);
        greenhouseContents = dataHelperGreenhouse.getAll();
        if(greenhouseContents.size() >= 1) {
            Greenhouse.getInstance().setContent(greenhouseContents.get(0));
            Greenhouse.getInstance().setReport(dataHelperGreenhouse.getReport(greenhouseContents.get(0).getId()));

        }
        /* Φορτωνουμε τα ονομα τους σε ενα Spinner για να μπορει ο χρηστης να τα επιλέγει*/
        Spinner spinner = (Spinner) currentHeader.findViewById(R.id.spGreenhouses);
        ArrayList<String> names = dataHelperGreenhouse.getNames();
        if(names.size() == 0)
            names.add(getText(R.string.create_greenhouse).toString());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,names);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        if(gPos != -1)
            spinner.setSelection(gPos);
        /* Οταν ο χρηστης επιλεγει ενα θερμοκηπιο ενημερωσε τα fragments */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /* Ενημερωνουμε ποιο θερμοκηπιο ειναι το επιλεγμενο */
                if (position < greenhouseContents.size()) {
                    gPos = position;
                    Greenhouse.getInstance().setContent(greenhouseContents.get(gPos));
                    Greenhouse.getInstance().setReport(dataHelperGreenhouse.getReport(greenhouseContents.get(gPos).getId()));
                    updateFragments();
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("MainActivity", "Greenhouse Spinner. Nothing selected!");
            }
        });
    }

    /**
     * Ενημερωνουμε τα fragments οταν επιλεγετε νεο θερμοκηπιο
     */
    private void updateFragments() {
        Fragment fragment = storeFragmentManager.getFragment(FragmentInfo.TAG);
        ((FragmentInfo)fragment).refresh();
        fragment = storeFragmentManager.getFragment(FragmentCultivations.TAG);
        ((FragmentCultivations)fragment).refresh();

        fragment = storeFragmentManager.getFragment(FragmentWorks.TAG);
        ((FragmentWorks)fragment).refresh();


    }
    /**
     * Κανει ολη την δουλεια οταν επιλεγετε απο το μενου ενα fragment
     */
    private void navigate(final int itemId) {
        /* Ενημερωνουμε ποιο fragment ειναι το επιλεγμένο,δεν επιλέγετε ποτε τα jobs_cultivation
         * , και τα settings γιατι δεν ειναι menu fragments. */
        if(itemId != R.id.drawer_jobs_cult_types) {
            navigationView.getMenu().findItem(itemId).setChecked(true);
            mNavItemId = itemId;
        }
        switch (itemId) {
            case R.id.drawer_info:
                getSupportActionBar().setTitle(R.string.greenhouse_info);
                getFragmentManager().beginTransaction().replace(R.id.content, storeFragmentManager.getFragment(FragmentInfo.TAG)).commit();
                break;
            case R.id.drawer_calendar:
                getSupportActionBar().setTitle(R.string.calendar);
                getFragmentManager().beginTransaction().replace(R.id.content, storeFragmentManager.getFragment(FragmentCalendar.TAG)).commit();
                break;
            case R.id.drawer_cultivations:
                getSupportActionBar().setTitle(R.string.cultivations);
                storeFragmentManager.removeFragment(FragmentCultivations.TAG);
                storeFragmentManager.addFragment(new FragmentCultivations(), FragmentCultivations.TAG, null);
                getFragmentManager().beginTransaction().replace(R.id.content, storeFragmentManager.getFragment(FragmentCultivations.TAG)).commit();
                break;
            case R.id.drawer_work:
                getSupportActionBar().setTitle(R.string.works);
                storeFragmentManager.removeFragment(FragmentWorks.TAG);
                storeFragmentManager.addFragment(new FragmentWorks(), FragmentWorks.TAG, null);
                getFragmentManager().beginTransaction().replace(R.id.content, storeFragmentManager.getFragment(FragmentWorks.TAG)).commit();
                break;
            case R.id.drawer_jobs_cult_types:
                Intent jobCult = new Intent(this,JobsAndCultActivity.class);
                startActivity(jobCult);
                break;
            default:
                break;
        }
    }
    /**
     * Callback event listener που ενεργοποιείται οταν επιλεγετε ενα μενου απο το συρταρι
     */
    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {

        /* Κλείσε το συρταρι */
        mDrawerLayout.closeDrawer(GravityCompat.START);
        /* Δεν ξανα επιλεγετε το ιδιο τρεχων fragment */
        if(menuItem.getItemId() != mNavItemId) {
            // update highlighted item in the navigation menu

            mDrawerActionHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigate(menuItem.getItemId());
                }
            }, DRAWER_CLOSE_DELAY_MS);
        }
        return true;
    }
    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Θετει μια εικονα στο drawer μας
     * @param imagePath Το μονοπατι της εικονας
     */
    public void setDrawerImage(String imagePath) {

        View view = mDrawerLayout.findViewById(R.id.drawer_parent);
        if(imagePath!=null && !imagePath.equals("")) {
            try {
                view.setBackgroundDrawable(new BitmapDrawable(getThumbnailBitmap(imagePath, 300)));
                //Hide image icon
                view = mDrawerLayout.findViewById(R.id.imagePreview);
                view.setVisibility(View.INVISIBLE);
            } catch (NullPointerException e) {
                Log.e("MainActivity", "setDrawerImage->" + e.getMessage());
            }
        }
    }
    /**
     * Φορτωνει μια εικονα απο το μονοπατι και την επιστρεφει με συγκεκριμενο μεγεθος.
     * @param path Το μονοπατι της εικονας
     * @param thumbnailSize Το μεγεθος που θελουμε να εχει.
     * @return Η εικονα σε μορφη bitmap
     */
    private Bitmap getThumbnailBitmap(String path, int thumbnailSize) {
        try {
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, bounds);
            if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
                return null;
            }
            int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                    : bounds.outWidth;
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = originalSize / thumbnailSize;
            return BitmapFactory.decodeFile(path, opts);
        }
        catch (NullPointerException e) {
            Log.e("MainActivity","getThumbnailBitmap->"+e.getMessage());
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Προστεθηκε νεο θερμοκηπιο ενημερωσε το spinner */
        if(requestCode == REQUEST_NEW_GREENHOUSE && resultCode == RESULT_OK) {
            greenhouseSpinnerInvalidate();
        }
    }

}
