package atzios.greenhouse.cultivations.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.ViewPagerAdapter;
import atzios.greenhouse.cultivations.fragments.FragmentCultivationType;

import atzios.greenhouse.cultivations.fragments.FragmentJob;


/**
 * JobsAndCultActivity
 * Δημιουργει, ενημερωνει  και διαγραφει δουλειες και ειδη καλλιεργειών
 * Created by Atzios on 2/11/2015.
 */
public class JobsAndCultActivity extends AppCompatActivity {
    /* Adapter οπου προσαρμοζει τα fragment σε ενα ViewPager αντικειμενο */
    private ViewPagerAdapter mViewPagerAdapter;

    /* View pager οπου μας επιτρεπει να πηγενουμε απο το ενα fragment στο αλλο με scroll */
    private ViewPager mViewPager;
    /* Η θεση του επιλεγμενου fragment */
    private int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jobs_cultivations);

        /* Θετουμε σαν Action Bar ενα δικο μας toolbar */
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        /* Ενεργοποιουμε το βελακι του πισω */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /* Αρχικοποιουμε το viewpager */
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);

        /* θετουμε listener για τα callbacks του fragment cultivation type */
        FragmentCultivationType fragmentCultivationType = new FragmentCultivationType();
        fragmentCultivationType.setFragmentCultivationTypeCallbacks(new FragmentCultivationType.FragmentCultivationTypeCallbacks() {
            @Override
            public void onCultivationAdded() {
                setResult(-1);
            }
        });
        FragmentJob jobFragment = new FragmentJob();
        jobFragment.setFragmentJobCallbacks(new FragmentJob.FragmentJobCallbacks() {
            @Override
            public void onJobsEdit() {
                setResult(-1);
            }
        }) ;
         /* προσθετουμε τα fragment μας στον adapter μας */
        mViewPagerAdapter.addFragment(jobFragment, FragmentJob.TAG, getText(R.string.jobs).toString());
        mViewPagerAdapter.addFragment(fragmentCultivationType,FragmentCultivationType.TAG,getText(R.string.cultivations).toString());

        /* Αρχικοποιουμε το tablayout */

        TabLayout tabLayout;
        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(getResources().getColor(R.color.drawer_item_text_default), Color.WHITE);

        /* Θετουμε το viewpager και το tablayout να αλληλεπιδρούν μεταξυ τους */
        mViewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        /* Αν υπαρχει αποθηκευμενη θεση ανακτησε την */
        if(savedInstanceState!=null)
            pos = savedInstanceState.getInt("pos",0);
        mViewPager.setCurrentItem(pos);

        /* Fab button click event */
        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Αναλογα ποιο fragment ειναι το επιλεγμενο οταν παταει ο χρηστης το fab,
                 * καλειται και αντιστοιχος διαλογος
                 */
                if (mViewPager.getCurrentItem() == 0) {
                    FragmentJob fJob = (FragmentJob) mViewPagerAdapter.getItem(0);
                    fJob.showNewJobDialog();
                } else {
                    FragmentCultivationType fCultType = (FragmentCultivationType) mViewPagerAdapter.getItem(1);
                    fCultType.showNewCultivationDialog();
                }
            }
        });

        /* Αν το activity εχει καλεστει με συγκεκριμενη σελιδα επιλεξε την  */
        if(getIntent()!=null) {
            pos = this.getIntent().getIntExtra("page", 0);
            mViewPager.setCurrentItem(pos);
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /* Αποθηκευουμε την θεση του επιλεγμενου fragment στο outState */
        outState.putInt("pos",mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Οταν ο χρηστης πατησει το βελακι πισω
                finish(); //Τερματιζει το activity
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
