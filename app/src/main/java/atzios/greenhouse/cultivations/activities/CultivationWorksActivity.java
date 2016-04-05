package atzios.greenhouse.cultivations.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.ViewPagerAdapter;
import atzios.greenhouse.cultivations.contents.ContentGreenhouseCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouseCultivation;
import atzios.greenhouse.cultivations.fragments.FragmentCompletedWorks;
import atzios.greenhouse.cultivations.fragments.FragmentPendingWorks;

/**
 * Activity CultivationWorks
 * Προβαλει ολες τις εργασιες μιας καλλιεργειας χωρισμενες σε ολοκληρωμένες και μη,
 * Δημιουργει μια νεα εργασια για την καλλιεργεια ,
 * Τροποποιει την καλλιεργεια
 * Created by Atzios on 4/4/2016.
 */
public class CultivationWorksActivity extends AppCompatActivity {
    private ViewPagerAdapter mViewPagerAdapter;
    private ContentGreenhouseCultivation cultivation = new ContentGreenhouseCultivation();
    private int cId = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivation_works);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Ενεργοποιουμε το βελακι του πισω */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        /** Διαβαζουμε το id της καλλιεργειας ωστε να φορτωσουμε τις εργασιες που ειναι δικιες της */
        if(getIntent().getExtras()!=null) {
            cId = getIntent().getExtras().getInt("cId",-1);
            Log.e("cID",Integer.toString(cId));
        }
        final DataHelperGreenhouseCultivation dHelper = new DataHelperGreenhouseCultivation(this);
        cultivation = dHelper.get(cId);
        getSupportActionBar().setTitle(R.string.cultivation);
        getSupportActionBar().setSubtitle(dHelper.getCultivationName(cId));


        final Button btn = (Button)findViewById(R.id.btnCompleted);
        toggleActiveButton();
        btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cultivation.setActive(!cultivation.isActive());
                    cultivation.setEndDate(Calendar.getInstance().getTime().getTime());
                    dHelper.update(cultivation);
                    toggleActiveButton();
                }
            });

        /* Fab button click event */
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Αναλογα ποιο fragment ειναι το επιλεγμενο οταν παταει ο χρηστης το fab,
                 * καλειται και αντιστοιχος διαλογος
                 */
                Intent newWork = new Intent(CultivationWorksActivity.this, NewWorkActivity.class);
                newWork.putExtra("cID",cultivation.getId());
                startActivity(newWork);
            }
        });

        /** Δημιουργουμε τα fragments των εργασιων */
        invalidateChildrenFragments();
    }

    private void toggleActiveButton() {
        final Button btn = (Button)findViewById(R.id.btnCompleted);
        if(cultivation.isActive())
        {
            btn.setText(R.string.not_completed);
            btn.setBackgroundColor(getResources().getColor(R.color.tomato));
        }
        else {
            btn.setText(R.string.completed_work);
            btn.setBackgroundColor(getResources().getColor(R.color.primary));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cultivation_works,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Δημιουργουμε τα fragments των εργασιων
     */
    private void invalidateChildrenFragments() {

        TabLayout tabLayout;
        ViewPager mViewPager;
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        Bundle b = new Bundle();
        b.putInt("cId",cId);
        FragmentPendingWorks pendingWorks = new FragmentPendingWorks();
        pendingWorks.setArguments(b);
        FragmentCompletedWorks completedWorks = new FragmentCompletedWorks();
        completedWorks.setArguments(b);
        mViewPagerAdapter.addFragment(pendingWorks, FragmentPendingWorks.TAG, getText(R.string.pending).toString());
        mViewPagerAdapter.addFragment(completedWorks, FragmentCompletedWorks.TAG, getText(R.string.completed).toString());

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // you can change Tab text colors by using setTabTextColors function.
        tabLayout.setTabTextColors(getResources().getColor(R.color.drawer_item_text_default), Color.WHITE);

        mViewPager.setAdapter(mViewPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Οταν ο χρηστης πατησει το βελακι πισω
                finish(); //Τερματιζει το activity
                break;
            case R.id.action_edit:
                Intent edit = new Intent(CultivationWorksActivity.this,NewCultivationActivity.class);
                edit.putExtra("edit",true);
                edit.putExtra("id",cultivation.getId());
                startActivityForResult(edit,20);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 20) {
            if(resultCode == 22) {
                /* Ενημερωσε αν αλλαξε το ειδος καλλιεργειας */
                final DataHelperGreenhouseCultivation dHelper = new DataHelperGreenhouseCultivation(this);
                cultivation = dHelper.get(cId);
                //getSupportActionBar().setTitle(R.string.cultivation);
                getSupportActionBar().setSubtitle(dHelper.getCultivationName(cId));
            }
            else if (resultCode == 21) {
                /* Η καλλιεργεια διαγραφηκε βγες απο την προβολη εργασιων */
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
