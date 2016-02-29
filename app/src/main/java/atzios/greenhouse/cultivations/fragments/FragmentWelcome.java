package atzios.greenhouse.cultivations.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import atzios.greenhouse.cultivations.R;

/**
 * FragmentWelcome
 * Απλος fragment οπου εμφανιζει ενα μηνυμα καλος ορισματος
 * Created by Atzios on 30/10/2015.
 */
public class FragmentWelcome extends Fragment {
    public final static String TAG = "FragmentWelcome";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome,container,false);
    }
}
