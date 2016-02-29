package atzios.greenhouse.cultivations.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import atzios.greenhouse.cultivations.activities.NewCultivationActivity;
import atzios.greenhouse.cultivations.R;

import atzios.greenhouse.cultivations.ViewPagerAdapter;


/**
 * Created by Panos on 9/9/2015.
 */
public class FragmentCultivations extends Fragment {
    public static final String TAG = "FragmentCultivations";
    private ViewPagerAdapter mViewPagerAdapter;
    private View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cultivations_home, container, false);
        invalidateChildrenFragments();
        FloatingActionButton fab = (FloatingActionButton)mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newCult = new Intent(getActivity(), NewCultivationActivity.class);
                startActivity(newCult);
            }
        });
        return mView;
    }

    public void refresh() {
        try {
            FragmentActiveCultivations fragment = (FragmentActiveCultivations) mViewPagerAdapter.getItem(0);
            fragment.refresh();
            FragmentCompletedCultivations fragmen = (FragmentCompletedCultivations) mViewPagerAdapter.getItem(1);
            fragmen.refresh();
        }
        catch (NullPointerException e) {
           // Log.e("FragmentCultivations",e.getMessage());
        }
    }

    private void invalidateChildrenFragments() {

        TabLayout tabLayout;
        ViewPager mViewPager;
        mViewPager = (ViewPager)mView.findViewById(R.id.viewpager);

        mViewPagerAdapter = new ViewPagerAdapter(getParentFragmentManager(),getActivity());


        mViewPagerAdapter.addFragment(new FragmentActiveCultivations(), FragmentActiveCultivations.TAG, getText(R.string.active).toString());
        mViewPagerAdapter.addFragment(new FragmentCompletedCultivations(), FragmentCompletedCultivations.TAG, getText(R.string.completed).toString());

        tabLayout = (TabLayout) mView.findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // you can change Tab text colors by using setTabTextColors function.
        tabLayout.setTabTextColors(getResources().getColor(R.color.drawer_item_text_default), Color.WHITE);

        mViewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(0);
    }

    private FragmentManager getParentFragmentManager() {
        return ((AppCompatActivity)getActivity()).getSupportFragmentManager();
    }
    public void notifyMe() {
        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {

        notifyMe();
        super.onResume();
    }


}
