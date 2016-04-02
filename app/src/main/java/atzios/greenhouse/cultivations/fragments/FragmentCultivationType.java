package atzios.greenhouse.cultivations.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import atzios.greenhouse.cultivations.contents.ContentCultivation;
import atzios.greenhouse.cultivations.contents.ContentJob;
import atzios.greenhouse.cultivations.datahelpers.DataHelperCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperJob;


/**
 * Created by Atzios on 2/11/2015.
 */
public class FragmentCultivationType extends Fragment {
    public final static String TAG = "FragmentCultivationType";
    private View mView;
    private FragmentCultivationTypeCallbacks callbacks;
    private ArrayList<ContentCultivation> cultivations = new ArrayList<>();//Lista opou kratame oles tis ergasises
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cultivations_type,container,false);


        return mView;
    }

    private void loadData() {
        cultivations = new DataHelperCultivation(getActivity()).getAll();
        ListView list = (ListView)mView.findViewById(R.id.lsCultivationsType);

        CultivationTypesAdapter adapter = new CultivationTypesAdapter(getActivity(),R.layout.list_item,cultivations);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateCultivationDialog(position);
            }
        });

    }
    public void showNewCultivationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.new_cultivation);
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_new, (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content),false);
        builder.setView(layout);

        final EditText name = (EditText)layout.findViewById(R.id.edName);
        name.setHint(R.string.hint_cultivation_name);
        final EditText comments = (EditText)layout.findViewById(R.id.edComments);
        comments.setHint(R.string.hint_comments);
        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(!name.getText().toString().equals("")) {
                    ContentCultivation cult = new ContentCultivation();
                    cult.setName(name.getText().toString());
                    cult.setComments(comments.getText().toString());
                    DataHelperCultivation dHelper = new DataHelperCultivation(getActivity());
                    dHelper.create(cult);
                    loadData();

                    if(callbacks!=null)
                        callbacks.onCultivationAdded();

                }
                else
                    Toast.makeText(getActivity(), R.string.empty_name, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }
    private void updateCultivationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.update);
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_new, (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content), false);

        builder.setView(layout);
        CustomTextView tv = (CustomTextView)layout.findViewById(R.id.tvWarning);
        tv.setText(R.string.warning_deleting_cultivation);
        tv.setVisibility(View.VISIBLE);
        final ContentCultivation cultivation = cultivations.get(position);

        final EditText name = (EditText)layout.findViewById(R.id.edName);
        name.setText(cultivation.getName());
        final EditText comments = (EditText)layout.findViewById(R.id.edComments);
        comments.setText(cultivation.getComments());

        builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cultivation.setName(name.getText().toString());
                cultivation.setComments(comments.getText().toString());
                DataHelperCultivation helperCultivation = new DataHelperCultivation(getActivity());
                helperCultivation.update(cultivation);
                loadData();
                if(callbacks!=null)
                    callbacks.onCultivationAdded();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DataHelperCultivation helperCultivation = new DataHelperCultivation(getActivity());
                helperCultivation.delete(cultivation.getId(),true);

                loadData();
                if(callbacks!=null)
                    callbacks.onCultivationAdded();
            }
        });

        builder.create().show();
    }
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
    public void setFragmentCultivationTypeCallbacks(FragmentCultivationTypeCallbacks callbacks) {
        this.callbacks = callbacks;
    }
    public interface FragmentCultivationTypeCallbacks {
        void onCultivationAdded();
    }
    /**
     * Cultivation types ListView adatper class
     */
    public static class CultivationTypesAdapter extends BaseAdapter {
        private int resLayoutID; // To id tou list item
        private Context context; // context efarmoghs
        ArrayList<ContentCultivation> contents; //Oles i egasies

        //Constructor
        public CultivationTypesAdapter(Context context, int layoutID, ArrayList<ContentCultivation> contents) {

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

