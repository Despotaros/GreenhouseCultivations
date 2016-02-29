package atzios.greenhouse.cultivations.fragments;


import android.app.Fragment;
import android.graphics.AvoidXfermode;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;

import atzios.greenhouse.cultivations.R;

/**
 * Created by Panos on 11/9/2015.
 */
public class FragmentCalendar extends Fragment {
    public static final String TAG = "FragmentCalendar";
    private CalendarPickerView calendar;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_calendar,container,false);
        calendar = (CalendarPickerView) mView.findViewById(R.id.calendar);
        final FloatingActionButton fab = (FloatingActionButton)mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setBackgroundTintMode(PorterDuff.Mode.DARKEN);

                //fab.showContextMenu();
            }
        });
        registerForContextMenu(fab);
        initializeCalendar(true);
        return mView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    private void initializeCalendar(boolean goToNow) {
        Calendar nextYear = Calendar.getInstance();
        Calendar lastYear = Calendar.getInstance();

        lastYear.set(nextYear.get(Calendar.YEAR) - 1, 0, 1);
        nextYear.set(nextYear.get(Calendar.YEAR) + 2, 0, 1);


        calendar.init(lastYear.getTime(), nextYear.getTime());
        if(goToNow)
            calendar.scrollToDate(Calendar.getInstance().getTime());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_today) {
            calendar.selectDate(Calendar.getInstance().getTime(),true);
           // calendar.scrollToDate();
        }
        return super.onOptionsItemSelected(item);
    }
}
