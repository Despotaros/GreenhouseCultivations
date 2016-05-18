package atzios.greenhouse.cultivations.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import atzios.greenhouse.cultivations.Preferences;
import atzios.greenhouse.cultivations.R;
import atzios.greenhouse.cultivations.activities.MainActivity;
import atzios.greenhouse.cultivations.contents.ContentGreenhouse;
import atzios.greenhouse.cultivations.datahelpers.DataHelperGreenhouse;

/**
 * FragmentCreateGreenhouse Dimiourgi neo thermokipoiw
 * Created by Ατζιος on 31/10/2015.
 */
public class FragmentCreateGreenhouse extends Fragment {
    public final static String TAG = "FragmentCreateGreenhouse";
    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_create_greenhouse,container,false);
        setHasOptionsMenu(true);
        return mView;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                //Create greenhouse and exit the activity
                createGreenhouse();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_greenhouse,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    /**
     * Grapse to thermokipio stin vash
     */
    private void createGreenhouse () {
        //Dimiourgise ena antikeimeno typou GreenhouseContent
        ContentGreenhouse contentGreenhouse = new ContentGreenhouse();
        //Pare ta dedomena
        EditText ed = (EditText)mView.findViewById(R.id.edName);
        contentGreenhouse.setName(ed.getText().toString());
        //et areaG
        ed = (EditText)mView.findViewById(R.id.edArea);
        double area = 0;
        try {
            area = Double.parseDouble(ed.getText().toString());
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        contentGreenhouse.setArea(area);
        ed = (EditText)mView.findViewById(R.id.edAddress);
        contentGreenhouse.setAddress(ed.getText().toString());
                /* Γραψε το θερμοκηπιο στην βαση */
        if(!contentGreenhouse.getName().equals("")) {
            DataHelperGreenhouse dHelper = new DataHelperGreenhouse(getActivity());
            dHelper.create(contentGreenhouse);
            /* Ξεκινα την main activity */
            Preferences pre = new Preferences(getActivity());
            pre.put(Preferences.FIRST_RUN, false);
            Intent main = new Intent(getActivity(),MainActivity.class);
            startActivity(main);
            getActivity().finish();
        }
        else
            Toast.makeText(getActivity(), R.string.greenhouse_empty_name, Toast.LENGTH_LONG).show();
    }
}
