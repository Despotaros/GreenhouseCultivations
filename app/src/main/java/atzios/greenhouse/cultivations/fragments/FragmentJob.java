package atzios.greenhouse.cultivations.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import atzios.greenhouse.cultivations.CustomTextView;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.contents.ContentJob;
import atzios.greenhouse.cultivations.datahelpers.DataHelperJob;

/**
 * Fragment Job
 * Δημιουργει , ενημερωνει και διαγραφει ειδη εργασιων
 * Created by Atzios on 2/11/2015.
 */
public class FragmentJob extends Fragment {
    /* Το Tag του fragment */
    public final static String TAG = "FragmentJob";
    /* Το View του fragment */
    private View mView;
    /* Πινακας που κραταει ολα τα ειδη εργασιων */
    private ArrayList<ContentJob> jobs = new ArrayList<>();
    /* Callbacks του fragment job */
    private FragmentJobCallbacks callbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_jobs,container,false);
        return mView;
    }

    /**
     * LoadData
     * Ενημερωνει την lista με τα ειδη εργασιων απο την βαση
     */
    private void loadData() {
        DataHelperJob dHelper = new DataHelperJob(getActivity());
        jobs = dHelper.getAll();
        ListView listView = (ListView)mView.findViewById(R.id.lsJobs);
        JobListAdapter adapter = new JobListAdapter(getActivity(),R.layout.list_item,jobs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateJobDialog(position);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        /* Καθε φορα που το fragment γινεται active ενημερωσε το listview για τυχον αλλαγες */
        loadData();
    }

    /**
     * UpdateJobDialog
     * Διαλογος οπου ενημερωνει ή διαγραφει ενα ειδος εργασιας
     * @param position Η θεση του ειδους στο array list
     */
    private void updateJobDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.update);
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_new, (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content), false);

        builder.setView(layout);

        final ContentJob job = jobs.get(position);
        CustomTextView tv = (CustomTextView)layout.findViewById(R.id.tvWarning);
        tv.setText(R.string.warning_delete_work);
        final EditText name = (EditText)layout.findViewById(R.id.edName);
        name.setText(job.getName());
        final EditText comments = (EditText)layout.findViewById(R.id.edComments);
        comments.setText(job.getComments());
        tv.setVisibility(View.VISIBLE);
        builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                job.setName(name.getText().toString());
                job.setComments(comments.getText().toString());
                DataHelperJob helperJob = new DataHelperJob(getActivity());
                helperJob.update(job);
                loadData();
                if(callbacks!=null)
                    callbacks.onJobsEdit();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DataHelperJob helperJob = new DataHelperJob(getActivity());
                helperJob.delete(job.getId(),true);
                loadData();
                if(callbacks!=null)
                    callbacks.onJobsEdit();
            }
        });

        builder.create().show();
    }

    /**
     * ShowNewDialog
     * Εμφανιζει εναν διαλογο για την δημιουργια καινουριου ειδους εργασιας
     */
    public void showNewJobDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.new_job);
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_new, (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content),false);
        builder.setView(layout);
        final EditText name = (EditText)layout.findViewById(R.id.edName);
        name.setHint(R.string.hint_job_name);
        final EditText comments = (EditText)layout.findViewById(R.id.edComments);
        comments.setHint(R.string.hint_comments);
        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (!name.getText().toString().equals("")) {
                    ContentJob job = new ContentJob();
                    job.setName(name.getText().toString());
                    job.setComments(comments.getText().toString());
                    DataHelperJob dHelper = new DataHelperJob(getActivity());
                    dHelper.create(job);
                    loadData();
                    if(callbacks!=null)
                        callbacks.onJobsEdit();

                } else
                    Toast.makeText(getActivity(), R.string.empty_name, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }
    public void setFragmentJobCallbacks(FragmentJobCallbacks callbacks) {
        this.callbacks = callbacks;
    }
    public interface FragmentJobCallbacks {
        void onJobsEdit();
    }
    /**
     * JobListAdapter
     * Κλαση βοηθος οπου προσαρμοζει τα ειδη εργασιων σε ενα listView
     */
    public static class JobListAdapter extends BaseAdapter {
        private int resLayoutID;
        private Context context;
        ArrayList<ContentJob> contents;

        //Constructor
        public JobListAdapter(Context context, int layoutID, ArrayList<ContentJob> contents) {

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
                holder.name = (CustomTextView) row.findViewById(R.id.tvTitle);
                holder.comments = (CustomTextView) row.findViewById(R.id.tvComments);


                row.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextViews
                holder = (ViewHolder) row.getTag();
            }
            //Set values of objects for every item

            holder.name.setText(contents.get(position).getName());
            holder.comments.setText(contents.get(position).getComments());

            return row;


        }

        //Holds the values of every item
        static class ViewHolder {
            CustomTextView name;
            CustomTextView comments;
        }
    }
}
