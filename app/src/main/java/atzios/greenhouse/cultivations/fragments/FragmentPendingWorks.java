package atzios.greenhouse.cultivations.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import atzios.greenhouse.cultivations.Greenhouse;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.activities.NewWorkActivity;
import atzios.greenhouse.cultivations.contents.ContentWork;
import atzios.greenhouse.cultivations.datahelpers.DataHelperWork;

/**
 * Created by Ατζιος on 29/12/2015.
 */
public class FragmentPendingWorks extends Fragment {
    public final static String TAG = "FragmentPendingWorks";
    private ArrayList<ContentWork> works = new ArrayList<>();
    private View mView;
    private int cId = -1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_pending_works,container,false);
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
        Log.e("PendingcID",Integer.toString(cId));
        works = dHelper.getAll(Greenhouse.getInstance().getContent().getId(),true,cId);
        ListView listView = (ListView)mView.findViewById(R.id.lsPendingWorks);
        FragmentCompletedWorks.ContentWorkListAdapter adapter = new FragmentCompletedWorks.ContentWorkListAdapter(getActivity(),R.layout.list_item_work,works);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewWorkActivity.class);
                intent.putExtra("edit", true);
                intent.putExtra("id", works.get(position).getId());
                intent.putExtra("cID",works.get(position).getCultivationId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void setArguments(Bundle args) {
        if(args!=null)
            cId = args.getInt("cId",-1);
        super.setArguments(args);
    }
}
