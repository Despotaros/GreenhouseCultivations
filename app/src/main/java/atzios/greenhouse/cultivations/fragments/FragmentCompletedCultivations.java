package atzios.greenhouse.cultivations.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import atzios.greenhouse.cultivations.Greenhouse;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.activities.NewCultivationActivity;
import atzios.greenhouse.cultivations.contents.ContentGreenhouseCultivation;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouseCultivation;

/**
 * Created by BASILIS on 12/12/2015.
 */
public class FragmentCompletedCultivations extends Fragment {
    public final static String TAG = "FragmentCompletedCultivations";
    private View mView;
    private ArrayList<ContentGreenhouseCultivation> cultivations = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_completed_cultivations,container,false);
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
        cultivations = dHelper.getAll(Greenhouse.getInstance().getContent().getId(),false);
        ListView listView = (ListView)mView.findViewById(R.id.lsCompletedCultivations);
        FragmentActiveCultivations.ContentGreenhouseCultivationListAdapter adapter = new FragmentActiveCultivations.ContentGreenhouseCultivationListAdapter(getActivity(),R.layout.list_item_gcultivation,cultivations);
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

}
