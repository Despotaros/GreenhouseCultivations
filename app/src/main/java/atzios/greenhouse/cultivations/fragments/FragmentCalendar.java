package atzios.greenhouse.cultivations.fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import atzios.greenhouse.cultivations.Greenhouse;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.contents.ContentGreenhouseCultivation;
import atzios.greenhouse.cultivations.contents.ContentWork;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouseCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperWork;

/**
 * Created by Panos on 11/9/2015.
 */
public class FragmentCalendar extends Fragment {
    public static final String TAG = "FragmentCalendar";
    private CalendarPickerView calendar;
    private View mView;
    private ArrayList<ContentGreenhouseCultivation> cultivations;
    private ArrayList<ContentWork> works;
    private ArrayList<Date> dates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getDatabaseContent();
    }
    public void getDatabaseContent() {
        cultivations = new ArrayList<>();
        works = new ArrayList<>();

        DataHelperGreenhouseCultivation dHelper = new DataHelperGreenhouseCultivation(getActivity());
        cultivations.addAll(dHelper.getAll(-1,true));
        cultivations.addAll(dHelper.getAll(-1,false));

        DataHelperWork dWork = new DataHelperWork(getActivity());
        works.addAll(dWork.getAll(-1,true,-1));
        works.addAll(dWork.getAll(-1,false,-1));


      //  Log.e("PendingWork",Integer.toString(dWork.getPendingWorks().size()));
       // Log.e("PendingCult",Integer.toString(dHelper.getAlmostCompletedWorks(Greenhouse.getInstance().getContent().getId(),5).size()));


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
               // fab.setBackgroundTintMode(PorterDuff.Mode.DARKEN);

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
        dates = new ArrayList<>();
        long maxDate = Calendar.getInstance().getTime().getTime() ;
        long minDate = Calendar.getInstance().getTime().getTime() ;
        /** Βρισκουμε την μεγιστη και ελαχιστη ημερομηνια των εργασιων και των καλλιεργειων **/
        for(ContentGreenhouseCultivation c : cultivations) {
            if(c.getStartDate() > maxDate)
                maxDate = c.getStartDate();
            if(c.getStartDate() < minDate)
                minDate = c.getStartDate();
            dates.add(new Date(c.getStartDate()));
        }
        for(ContentWork w : works) {
            if(w.getDate() > maxDate)
                maxDate = w.getDate();
            if(w.getDate() < minDate)
                minDate = w.getDate();

            dates.add(new Date(w.getDate()));
        }
        /** Αρχικοποιουμε το ημερολογιο στην καταλληλη ημερομηνια **/
        Calendar nextYear = Calendar.getInstance();
        Calendar lastYear = Calendar.getInstance();

        nextYear.setTime(new Date(maxDate));
        lastYear.setTime(new Date(minDate));

        lastYear.set(lastYear.get(Calendar.YEAR) , 0, 1);
        nextYear.set(nextYear.get(Calendar.YEAR) + 1, 0, 1);

        calendar.clearHighlightedDates();
        calendar.init(lastYear.getTime(), nextYear.getTime());
        calendar.highlightDates(dates);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                showDateContent(date).show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        if(goToNow)
            calendar.scrollToDate(Calendar.getInstance().getTime());




    }
    private Dialog showDateContent(Date date) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dateFormat.format(date));

        return builder.create();

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
