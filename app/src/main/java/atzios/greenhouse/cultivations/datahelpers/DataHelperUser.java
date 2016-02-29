package atzios.greenhouse.cultivations.datahelpers;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import atzios.greenhouse.cultivations.contents.ContentUser;

/**
 * Created by Panos on 22/12/2014.
 */
public class DataHelperUser {
    private String CLASS_TAG = "DataHelperUser";
    private Context context;

    public DataHelperUser(Context context) {
        this.context = context;
    }

    public boolean addUser(ContentUser user) {
        boolean added = false;
        try {

        }
        catch(SQLiteException e) {
            Log.e(CLASS_TAG+":addUser",e.getMessage());
        }
        return added;
    }


}
