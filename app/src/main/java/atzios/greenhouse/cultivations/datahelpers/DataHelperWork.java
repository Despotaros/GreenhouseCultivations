package atzios.greenhouse.cultivations.datahelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


import java.util.ArrayList;
import java.util.Calendar;

import atzios.greenhouse.cultivations.contents.ContentWork;


/**
 * Class DataHelperWork
 * Περιεχει ολα τα CRUD για τον πινακα Work
 * Created by Atzios on 22/12/2014.
 */
public class DataHelperWork {
    private final String CLASS_TAG = "DataHelperWork";
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
            Log.e(CLASS_TAG,"get:"+e.getMessage());
        }

        return work;
    }

    /**
     * Επιστρεφει ολες τις εργασιες που εκκρεμουν μεχρι την σημερινη ημερομηνια
     * @param greenhouseId Το id του θερμοκηπιου
     * @return Πινακας με τις εργασιες
     */
    public ArrayList<ContentWork> getPendingWorks(int greenhouseId) {
        ArrayList<ContentWork> pending = new ArrayList<>();
        try {
            String subQ ;
            if(greenhouseId == -1)
                subQ = "where pending=1 ";
            else
                subQ = "where GREENHOUSE_ID="+greenhouseId+" and pending=1 ";
            SQLiteDatabase db = new DatabaseOpenHelper(context).getReadableDatabase();
            String query = "select * from WORK "+subQ+" and date <="
                    + Calendar.getInstance().getTime().getTime();
            Cursor c = db.rawQuery(query,null);
            c.moveToFirst();
            do {
                ContentWork content = new ContentWork();
                content.setId(c.getInt(0));
                content.setUserId(c.getInt(1));
                content.setGreenhouseId(c.getInt(2));
                content.setCultivationId(c.getInt(3));
                content.setJobId(c.getInt(4));
                content.setPending(c.getInt(5));
                content.setDate(c.getLong(6));
                content.setComments(c.getString(7));

                pending.add(content);

            } while (c.moveToNext());

            c.close();
            db.close();
        }
        catch (SQLiteException e) {
            Log.e(CLASS_TAG,"getPendingWorks:"+e.getMessage());
        }

        return pending;
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
            Log.e(CLASS_TAG,"create:"+e.getMessage());
        }
    }

    /**
     * Επιστρεφει ολες τις εργασιες ενος θερμοκηπιου
     * @param greenhouseId το id του θερμοκηπιου ενα ειναι -1 επιστρεφει απο ολα τα θερμοκηπια
     * @param pending Οι εργασιες που εκκρεμουν
     * @param cultivationId Το id της καλλιεργειας
     * @return ολες οι καλλιεργειες
     */
    public ArrayList<ContentWork> getAll(int greenhouseId,boolean pending,int cultivationId) {
        ArrayList<ContentWork> gWorks = new ArrayList<>();
        try {
            String subQ ;
            if(greenhouseId == -1)
                subQ = "where PENDING="+(pending ? 1 : 0);
            else
                subQ = "where GREENHOUSE_ID="+greenhouseId+" and PENDING="+(pending ? 1 : 0)+" and CULTIVATION_ID="+cultivationId;
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "select * from WORK "+subQ;
            Log.e("query",query);
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
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
        }
        catch (SQLiteException e) {
            Log.e(CLASS_TAG,"getAll:"+e.getMessage());
        }
        return gWorks;
    }


    /**
     * Ενημερωνει την καλλιεργεια θερμοκηπιου
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
            Log.e(CLASS_TAG,"update:"+e.getMessage());
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
            Log.e(CLASS_TAG,"delete:"+e.getMessage());
        }

    }
    public void deleteByCultId(int cId) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "delete from WORK where CULTIVATION_ID="+cId;
            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            Log.e(CLASS_TAG,"deleteByCultId:"+e.getMessage());
        }
    }


}
