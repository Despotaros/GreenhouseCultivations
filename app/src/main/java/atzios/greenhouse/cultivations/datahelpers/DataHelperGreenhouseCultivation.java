package atzios.greenhouse.cultivations.datahelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import atzios.greenhouse.cultivations.contents.ContentGreenhouseCultivation;

/**
 * DataHelperGreenhouseCultivation
 * Κλαση οπου δημιουργει,ενημερωνει,επιστρεφει και διαγραφει καλλιεργειες ενος
 * θερμοκηπιου στην βαση.
 * Created by Atzios on 22/12/2014.
 */
public class DataHelperGreenhouseCultivation {
    private Context context;

    /**
     * Constructor
     * @param context To context της εφαρμογης
     */
    public DataHelperGreenhouseCultivation(Context context) {
        this.context = context;
    }

    /**
     * Δημιουργουμε μια νεα καλλιεργεια ενος θερμοκηπιου
     * @param cultivation Τα δεδομενα της καλλιεργειας
     */
    public void create(ContentGreenhouseCultivation cultivation) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "insert into GREENHOUSE_CULTIVATION(GREENHOUSE_ID,CULTIVATION_ID,START_DATE," +
                    "DURATION,COMMENTS,ACTIVE) values("+cultivation.getGreenhouseId()+","+cultivation.getCultivationId()+
                    ","+cultivation.getDate()+","+cultivation.getDuration()+",'"+cultivation.getComments()
                    +"',"+cultivation.getActive()+")";

            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            Log.e("GreenhouseCultivation",e.getMessage());
        }
    }

    /**
     * Επιστρεφει μια καλλιεργεια με το συγκεκριμενο id
     * @param id το id
     * @return η καλλιεργεια του θερμοκηπιου
     */
    public ContentGreenhouseCultivation get(int id) {
        ContentGreenhouseCultivation content = new ContentGreenhouseCultivation();
        SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
        String query = "select * from GREENHOUSE_CULTIVATION where id="+id;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()) {
            content.setId(id);
            content.setGreenhouseId(cursor.getInt(1));
            content.setCultivationId(cursor.getInt(2));
            content.setDate(cursor.getLong(3));
            content.setDuration(cursor.getInt(4));
            content.setComments(cursor.getString(5));
            content.setActive(cursor.getInt(6));
        }
        cursor.close();
        db.close();
        return content;
    }

    /**
     * Επιστρεφει ολες τις καλλιεργειες ενος θερμοκηπιου
     * @param greenhouseId το id του θερμοκηπιου
     * @return ολες η καλλιεργειες σε ενα arraylist
     */
    public ArrayList<ContentGreenhouseCultivation> getAll(int greenhouseId,boolean active) {
        ArrayList<ContentGreenhouseCultivation> gCults = new ArrayList<>();
        SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
        String query = "select * from GREENHOUSE_CULTIVATION where GREENHOUSE_ID="+greenhouseId+
                " and ACTIVE="+(active?1:0);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()) {
            do {
                ContentGreenhouseCultivation content = new ContentGreenhouseCultivation();
                content.setId(cursor.getInt(0));
                content.setGreenhouseId(cursor.getInt(1));
                content.setCultivationId(cursor.getInt(2));
                content.setDate(cursor.getLong(3));
                content.setDuration(cursor.getInt(4));
                content.setComments(cursor.getString(5));
                content.setActive(cursor.getInt(6));
                gCults.add(content);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return gCults;
    }

    /**
     * Ενημερωνη την καλλιεργεια θερμοκηπιου
     * @param cultivation  η καλλιεργεια
     */
    public void update(ContentGreenhouseCultivation cultivation) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "update GREENHOUSE_CULTIVATION set CULTIVATION_ID="+cultivation.getCultivationId()+
                    ",START_DATE="+cultivation.getDate()+",DURATION="+cultivation.getDuration()+
                    ",COMMENTS='"+cultivation.getComments()+"',ACTIVE="+cultivation.getActive()
                    +" where id="+cultivation.getId();

            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            Log.e("GreenhouseCultivation",e.getMessage());
        }

    }

    /**
     * Διαγραφη την θερμοκηπιακη καλλιεργεια
     * @param id
     */
    public void delete(int id) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "delete from GREENHOUSE_CULTIVATION where id="+id;
            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }

    }

}
