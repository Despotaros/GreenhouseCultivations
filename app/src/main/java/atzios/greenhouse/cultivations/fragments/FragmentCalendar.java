package atzios.greenhouse.cultivations.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import atzios.greenhouse.cultivations.CustomTextView;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.contents.ContentWork;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouse;
import atzios.greenhouse.cultivations.datahelpers.DataHelperJob;
import atzios.greenhouse.cultivations.datahelpers.DataHelperWork;

/**
 * Created by Ατζιος on 11/9/2015.
 */
public class FragmentCalendar extends Fragment {
    public static final String TAG = "FragmentCalendar";
    private CalendarPickerView calendar;
    private View mView;
    private ArrayList<ContentWork> works;
    private ArrayList<Date> dates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
     //   getDatabaseContent();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDatabaseContent();
        initializeCalendar(true);
    }

    public void getDatabaseContent() {
        works = new ArrayList<>();

        DataHelperWork dWork = new DataHelperWork(getActivity());
        works.addAll(dWork.getPendingWorks(-1));

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
        //initializeCalendar(true);
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
        /** Βρισκουμε την μεγιστη και ελαχιστη ημερομηνια των εργασιων  **/

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

                if(findDate(date))
                    showDateContent(date).show();
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });
        if(goToNow)
            calendar.scrollToDate(Calendar.getInstance().getTime());

    }
    private ArrayList<ContentWork> filterWorksByDate(Date date) {
        ArrayList<ContentWork> filterWorks = new ArrayList<>();
        for(ContentWork w : works) {
            if(date.compareTo(trimDate(new Date(w.getDate())))==0 ) {
                filterWorks.add(w);
            }
        }
        return filterWorks;
    }
    /**
     * Μηδενιζει στην ημερομηνια την ωρα
     * @param date Αρχικη ημερομηνια
     * @return  Η ημερομηνια με 0 ωρα
     */
    private Date trimDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
            /* Μηδενιζουμε στην ημερομηνια την ωρα */
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        return c.getTime();
    }
    private boolean findDate(Date date) {

        for(Date d :dates) {
            if(date.compareTo(trimDate(d))==0)
                return true;
        }
        return false;
    }
    private Dialog showDateContent(Date date) {

        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dateFormat.format(date));
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_calendar_work,(ViewGroup)mView,false);
        ListView lv = (ListView)layout.findViewById(R.id.listWorks);
        ContentWorkListAdapter adapter = new ContentWorkListAdapter(getActivity(),R.layout.list_item_calendar,filterWorksByDate(date));
        lv.setAdapter(adapter);
        builder.setView(layout);
        builder.setPositiveButton(R.string.cancel,null);
        //filterWorksByDate(date);
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
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    public static class ContentWorkListAdapter extends BaseAdapter {
        private int resLayoutID;
        private Context context;
        ArrayList<ContentWork> contents;

        //Constructor
        public ContentWorkListAdapter(Context context, int layoutID, ArrayList<ContentWork> contents) {

            this.context = context;
            this.contents = contents;
            resLayoutID = layoutID;
        }

        public int getCount() {
            return contents.size();
        }

        public Object getItem(int position) {
            return contents.get(position);
        }
        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            //Create item view
            ViewHolder holder;
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(resLayoutID, parent, false);

                holder = new ViewHolder();

                //Handle the objects of a list item
                holder.name = (CustomTextView) row.findViewById(R.id.tvWorkName);
                holder.comments = (CustomTextView) row.findViewById(R.id.tvWorkComments);
                holder.greenhouse = (CustomTextView) row.findViewById(R.id.tvGreenhouse);
                holder.completed = (CustomTextView) row.findViewById(R.id.tvCompleted);
                row.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextViews
            holder = (ViewHolder) row.getTag();
        }
        //Set values of objects for every item

        holder.comments.setText(context.getText(R.string.comments) + ":" + (contents.get(position).getComments()));

        DataHelperJob helperJob = new DataHelperJob(context);
        holder.name.setText(helperJob.get(contents.get(position).getJobId()).getName() +" - " +
                helperJob.get(contents.get(position).getJobId()).getComments());
        if(contents.get(position).isPending())
            holder.completed.setText(R.string.pending);
        else
            holder.completed.setText(R.string.completed);

            DataHelperGreenhouse g = new DataHelperGreenhouse(context);

            holder.greenhouse.setText(g.get(contents.get(position).getGreenhouseId()).getName());


        return row;
    }

    //Holds the values of every item
    static class ViewHolder {

        CustomTextView name;
        CustomTextView comments;
        CustomTextView greenhouse;
        CustomTextView completed;
    }


}
}
