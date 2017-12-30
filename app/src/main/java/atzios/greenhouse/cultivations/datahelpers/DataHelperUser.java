package atzios.greenhouse.cultivations.datahelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import atzios.greenhouse.cultivations.contents.ContentUser;

/**
 * Created by Ατζιος on 22/12/2014.
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
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "insert into USERS(ID,USERNAME,NAME,LAST_NAME) values(" + user.getId() + ",'" + user.getUsername()
            + "','" + user.getName()+ "','"+user.getLastname()+"')";
            db.execSQL(query);
            db.close();
        }
        catch(SQLiteException e) {
            Log.e(CLASS_TAG+":addUser",e.getMessage());
        }
        return added;
    }
    public boolean updateUser(ContentUser user) {
        boolean added = false;
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "update USERS set NAME='"+user.getName()+
                    "',LASTNAME='"+user.getLastname()+"' where ID="+user.getId();
            db.execSQL(query);
            db.close();
        }
        catch(SQLiteException e) {
            Log.e(CLASS_TAG+":updateUser",e.getMessage());
        }
        return added;
    }



}
