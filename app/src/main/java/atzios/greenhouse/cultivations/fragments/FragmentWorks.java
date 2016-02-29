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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.ViewPagerAdapter;
import atzios.greenhouse.cultivations.activities.NewCultivationActivity;
import atzios.greenhouse.cultivations.activities.NewWorkActivity;

/**
 * Created by panos on 29/12/2015.
 */
public class FragmentWorks extends Fragment {
    public static final String TAG = "FragmentWorks";
    private ViewPagerAdapter mViewPagerAdapter;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_works_home, container, false);
        invalidateChildrenFragments();
        FloatingActionButton fab = (FloatingActionButton)mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newCult = new Intent(getActivity(), NewWorkActivity.class);
                startActivity(newCult);
            }
        });
        return mView;
    }
    public void refresh() {
        try {
            FragmentCompletedWorks fragment = (FragmentCompletedWorks) mViewPagerAdapter.getItem(1);
            fragment.refresh();
            FragmentPendingWorks fragmen = (FragmentPendingWorks) mViewPagerAdapter.getItem(0);
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


        mViewPagerAdapter.addFragment(new FragmentPendingWorks(), FragmentPendingWorks.TAG, getText(R.string.pending).toString());
        mViewPagerAdapter.addFragment(new FragmentCompletedWorks(), FragmentCompletedWorks.TAG, getText(R.string.completed).toString());

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
