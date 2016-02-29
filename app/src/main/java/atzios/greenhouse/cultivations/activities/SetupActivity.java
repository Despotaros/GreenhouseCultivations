package atzios.greenhouse.cultivations.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import atzios.greenhouse.cultivations.CustomTextView;
import atzios.greenhouse.cultivations.fragments.FragmentCreateGreenhouse;
import atzios.greenhouse.cultivations.fragments.FragmentWelcome;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.ViewPagerAdapter;

/**
 * Setup activity
 * Βοηθαει τον χρηστη να δημιουργισει το πρωτο του θερμοκηπιο
 * Created by Atzios on 30/10/2015.
 */
public class SetupActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        /* Θετουμε ενα δικο μας toolbar σαν actionbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Αρχικοποιουμε τον viewpager */
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);

        /* Προσθετουμε τα fragment στον adapter */
        mViewPagerAdapter.addFragment(new FragmentWelcome(),FragmentWelcome.TAG, getText(R.string.welcome).toString());
        mViewPagerAdapter.addFragment(new FragmentCreateGreenhouse(),FragmentCreateGreenhouse.TAG, getText(R.string.create_greenhouse).toString());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);


        final ImageButton iBtn = (ImageButton)findViewById(R.id.btnStart);
        final CustomTextView cTv = (CustomTextView)findViewById(R.id.cTv);
        mViewPagerAdapter.setOnFinishUpdateListener(new ViewPagerAdapter.onFinishUpdate() {
            @Override
            public void onFinishUpdate() {
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        getSupportActionBar().setTitle(R.string.app_name);
                        iBtn.setVisibility(View.VISIBLE);
                        cTv.setVisibility(View.VISIBLE);
                        iBtn.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
                        break;
                    case 1:
                        getSupportActionBar().setTitle(R.string.new_greenhouse);
                        iBtn.setVisibility(View.INVISIBLE);
                        cTv.setVisibility(View.INVISIBLE);
                        break;
                    default:
                }
            }
        });
        iBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });


    }
}
