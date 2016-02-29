package atzios.greenhouse.cultivations.datahelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


import java.util.ArrayList;


import atzios.greenhouse.cultivations.contents.ContentGreenhouseCultivation;
import atzios.greenhouse.cultivations.contents.ContentWork;


/**
 * Created by Panos on 22/12/2014.
 */
public class DataHelperWork {
    private Context context;
    public DataHelperWork(Context context) {
        this.context = context;
    }

    /**
     * Επιστρεφει μια εργασια ενος θερμοκηπιου
     * @param id το id της εργασιας
     * @return η εργασια
     */
    public ContentWork get(int id) {
        ContentWork work = new ContentWork();
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "Select * from WORK where ID="+id;
            Cursor cursor = db.rawQuery(query,null);
            if(cursor.moveToFirst()) {
                work.setId(id);
                work.setUserId(cursor.getInt(1));
                work.setGreenhouseId(cursor.getInt(2));
                work.setCultivationId(cursor.getInt(3));
                work.setJobId(cursor.getInt(4));
                work.setPending(cursor.getInt(5));
                work.setDate(cursor.getLong(6));
                work.setComments(cursor.getString(7));
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e) {
            Log.e("DataHelperWork",e.getMessage());
        }

        return work;
    }
    /**
     * Δημιουργουμε μια νεα εργασια ενος θερμοκηπιου
     * @param work Τα δεδομενα της εργασιας
     */
    public void create(ContentWork work) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "insert into WORK(USER_ID,GREENHOUSE_ID,CULTIVATION_ID,JOB_ID,PENDING," +
                    "DATE,COMMENTS) values("+work.getUserId()+","+work.getGreenhouseId()+
                    ","+work.getCultivationId()+","+work.getJobId()+","+work.getPending()
                    +","+work.getDate()+",'"+work.getComments()+"')";

            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            Log.e("GreenhouseCultivation",e.getMessage());
        }
    }

    /**
     * Επιστρεφει ολες τις εργασιες ενος θερμοκηπιου
     * @param greenhouseId το id του θερμοκηπιου
     * @param pending Η εργασιες που εκρεμουν
     * @return ολες η καλλιεργειες σε ενα arraylist
     */
    public ArrayList<ContentWork> getAll(int greenhouseId,boolean pending) {
        ArrayList<ContentWork> gWorks = new ArrayList<>();
        SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
        String query = "select * from WORK where GREENHOUSE_ID="+greenhouseId+
                " and PENDING="+(pending?1:0);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()) {
            do {
                ContentWork content = new ContentWork();
                content.setId(cursor.getInt(0));
                content.setUserId(cursor.getInt(1));
                content.setGreenhouseId(cursor.getInt(2));
                content.setCultivationId(cursor.getInt(3));
                content.setJobId(cursor.getInt(4));
                content.setPending(cursor.getInt(5));
                content.setDate(cursor.getLong(6));
                content.setComments(cursor.getString(7));

                gWorks.add(content);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return gWorks;
    }


    /**
     * Ενημερωνη την καλλιεργεια θερμοκηπιου
     * @param work  η καλλιεργεια
     */
    public void update(ContentWork work) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "update WORK set CULTIVATION_ID="+work.getCultivationId()+
                    ",DATE="+work.getDate()+",GREENHOUSE_ID="+work.getGreenhouseId()+
                    ",JOB_ID="+work.getJobId()+
                    ",COMMENTS='"+work.getComments()+"',PENDING="+work.getPending()
                    +" where id="+work.getId();

            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            Log.e("DataHelperWork",e.getMessage());
        }

    }

    /**
     * Διαγραφη την θερμοκηπιακη εργασια
     * @param id
     */
    public void delete(int id) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "delete from WORK where id="+id;
            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }

    }

}
