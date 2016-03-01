package atzios.greenhouse.cultivations.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import atzios.greenhouse.cultivations.CustomTextView;
import atzios.greenhouse.cultivations.Greenhouse;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.activities.NewCultivationActivity;
import atzios.greenhouse.cultivations.contents.ContentGreenhouseCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouseCultivation;


/**
 * Created by Panos on 11/9/2015.
 */
public class FragmentActiveCultivations extends Fragment {
    public static final String TAG = "FragmentActiveCultivations";
    private View mView;
    private ArrayList<ContentGreenhouseCultivation> cultivations = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_active_cultivations,container,false);
    }
    @Override
    public void onResume() {
        super.onResume();
        /* Καθε φορα που το fragment γινεται active ενημερωσε το listview για τυχον αλλαγες */
        loadData();
    }
    public void refresh() {
        loadData();
    }
    /**
     * LoadData
     * Ενημερωνει την lista με τα ειδη εργασιων απο την βαση
     */
    private void loadData() {
        DataHelperGreenhouseCultivation dHelper = new DataHelperGreenhouseCultivation(getActivity());
        cultivations = dHelper.getAll(Greenhouse.getInstance().getContent().getId(),true);
        ListView listView = (ListView)mView.findViewById(R.id.lsActiveCultivations);
        ContentGreenhouseCultivationListAdapter adapter = new ContentGreenhouseCultivationListAdapter(getActivity(),R.layout.list_item_gcultivation,cultivations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),NewCultivationActivity.class);
                intent.putExtra("edit",true);
                intent.putExtra("id",cultivations.get(position).getId());
                startActivity(intent);
            }
        });

    }

    public static class ContentGreenhouseCultivationListAdapter extends BaseAdapter {
        private int resLayoutID;
        private Context context;
        ArrayList<ContentGreenhouseCultivation> contents;

        //Constructor
        public ContentGreenhouseCultivationListAdapter(Context context, int layoutID, ArrayList<ContentGreenhouseCultivation> contents) {

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
            ViewHolder holder ;
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(resLayoutID, parent, false);


                holder = new ViewHolder();
                //Handle the objects of a list item
                holder.cult = (CustomTextView) row.findViewById(R.id.tvCultivation);
                holder.comments = (CustomTextView) row.findViewById(R.id.tvComments);
                holder.date = (CustomTextView) row.findViewById(R.id.tvDate);
                holder.duration = (CustomTextView) row.findViewById(R.id.tvDuration);


                row.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextViews
                holder = (ViewHolder) row.getTag();
            }
            //Set values of objects for every item
            DataHelperCultivation cult = new DataHelperCultivation(context);
            String name = cult.get(contents.get(position).getCultivationId()).getName();
            holder.cult.setText(name + "-" + cult.get(contents.get(position).getCultivationId()).getComments() );
            holder.comments.setText(context.getText(R.string.comments)+":"+(contents.get(position).getComments()));
            int duration = contents.get(position).getDuration();
            if(duration == -1)
                holder.duration.setText(context.getText(R.string.duration)+": - ");
            else
                holder.duration.setText(context.getText(R.string.duration)+":"+duration);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(contents.get(position).getDate()));
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);

            holder.date.setText(context.getText(R.string.date)+":"+dateFormat.format(calendar.getTime()));

            return row;


        }

        //Holds the values of every item
        static class ViewHolder {
            CustomTextView cult;
            CustomTextView date;
            CustomTextView duration;
            CustomTextView comments;
        }
    }


}
