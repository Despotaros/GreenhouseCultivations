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
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import atzios.greenhouse.cultivations.CustomTextView;
import atzios.greenhouse.cultivations.Greenhouse;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.activities.NewWorkActivity;
import atzios.greenhouse.cultivations.contents.ContentGreenhouseCultivation;
import atzios.greenhouse.cultivations.contents.ContentWork;
import atzios.greenhouse.cultivations.datahelpers.DataHelperCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouseCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperJob;
import atzios.greenhouse.cultivations.datahelpers.DataHelperWork;

/**
 * Created by panos on 29/12/2015.
 */
public class FragmentCompletedWorks extends Fragment {
    public final static String TAG = "FragmentCompletedWorks";
    private ArrayList<ContentWork> works = new ArrayList<>();
    private View mView;
    private FragmentPendingWorks.OnFragmentInvalidated callback;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_completed_works,container,false);
    }
    public void setOnFragmentInvalidated(FragmentPendingWorks.OnFragmentInvalidated listener) {
        callback = listener;
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
        final DataHelperWork dHelper = new DataHelperWork(getActivity());
        works = dHelper.getAll(Greenhouse.getInstance().getContent().getId(),false,-1);
        ListView listView = (ListView)mView.findViewById(R.id.lsCompletedWorks);
        ContentWorkListAdapter adapter = new ContentWorkListAdapter(getActivity(),R.layout.list_item_work,works);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewWorkActivity.class);
                intent.putExtra("edit", true);
                intent.putExtra("id", works.get(position).getId());
                startActivity(intent);
            }
        });

        adapter.setCallbackListener(new ContentWorkListAdapter.OnImageClicked() {
            @Override
            public void onImageClicked(int pos) {
                works.get(pos).setPending(true);
                dHelper.update(works.get(pos));
                if(callback!=null)
                    callback.invalidated();

            }
        });

    }





    public static class ContentWorkListAdapter extends BaseAdapter {
        private int resLayoutID;
        private Context context;
        ArrayList<ContentWork> contents;
        private OnImageClicked callback;

        public void setCallbackListener(OnImageClicked listener) {
            callback = listener;
        }

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
                holder.job = (CustomTextView) row.findViewById(R.id.tvJobName);
                holder.comments = (CustomTextView) row.findViewById(R.id.tvComments);
                holder.date = (CustomTextView) row.findViewById(R.id.tvDate);
                holder.cult = (CustomTextView) row.findViewById(R.id.tvCult);
                holder.imgButton = (ImageView) row.findViewById(R.id.imgActive);


                row.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextViews
                holder = (ViewHolder) row.getTag();
            }
            //Set values of objects for every item
            DataHelperGreenhouseCultivation helperGreenhouseCultivation = new DataHelperGreenhouseCultivation(context);
            ContentGreenhouseCultivation cult = helperGreenhouseCultivation.get(contents.get(position).getCultivationId());

            DataHelperCultivation cultHelper = new DataHelperCultivation(context);
            String name = cultHelper.get(cult.getCultivationId()).getName();
            String comments = cultHelper.get(cult.getCultivationId()).getComments();
            if(name == null || name.equals(""))
                name = context.getText(R.string.not_selected).toString();
            if(comments == null || comments.equals(""))
                comments = " ";
            holder.cult.setText(context.getText(R.string.on)+":"+name +" - "+comments);
            holder.comments.setText(context.getText(R.string.comments) + ":" + (contents.get(position).getComments()));

            DataHelperJob helperJob = new DataHelperJob(context);
            holder.job.setText(helperJob.get(contents.get(position).getJobId()).getName() +" - " +
                    helperJob.get(contents.get(position).getJobId()).getComments());
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(contents.get(position).getDate()));
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
            holder.date.setText(context.getText(R.string.date) + ":" + dateFormat.format(calendar.getTime()));

            if(contents.get(position).isPending()) {
                holder.imgButton.setImageResource(R.drawable.ok);
            }
            else
                holder.imgButton.setImageResource(R.drawable.not);

            holder.imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callback!=null)
                        callback.onImageClicked(position);
                }
            });

            return row;


        }

        //Holds the values of every item
        static class ViewHolder {
            CustomTextView cult;
            CustomTextView date;
            CustomTextView job;
            CustomTextView comments;
            ImageView imgButton;
        }

        public interface OnImageClicked {
            void onImageClicked(int pos);
        }
    }
}
