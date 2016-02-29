package atzios.greenhouse.cultivations.datahelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import atzios.greenhouse.cultivations.contents.ContentCultivation;
import atzios.greenhouse.cultivations.contents.ContentJob;

/**
 * Created by Panos on 22/12/2014.
 */
public class DataHelperCultivation {
    private Context context;
    public DataHelperCultivation(Context context) {
        this.context = context;
    }
    public void create(ContentCultivation cultivation) {
        try {

            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "insert into CULTIVATION(CATEGORY_ID,CULTIVATION_NAME,MONTH_DURATION,COMMENTS) values(" + cultivation.getCategoryId() + ",'" + cultivation.getName() + "'," +
                    +cultivation.getMonthDuration()+",'"+cultivation.getComments()+"')";
            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "delete from CULTIVATION where id="+id;
            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }

    }
    public void update(ContentCultivation cultivation) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();;
            String query = "update CULTIVATION set CULTIVATION_NAME='" + cultivation.getName() + "',COMMENTS='" + cultivation.getComments() + "'," +
                    " MONTH_DURATION="+cultivation.getMonthDuration()+" where id="+cultivation.getId();
            db.execSQL(query);
            db.close();

        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Epistrefei ola ta periexomena tou pinaka job se ena arraylist
     * @return O pinakas
     */
    public ArrayList<ContentCultivation> getAll() {
        //O pinakas
        ArrayList<ContentCultivation> cultivations = new ArrayList<>();
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "select * from CULTIVATION";

            Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst()) {
                do {
                    ContentCultivation cult = new ContentCultivation();
                    cult.setId(cursor.getInt(0));
                    cult.setCategoryId(cursor.getInt(1));
                    cult.setName(cursor.getString(2));
                    cult.setMonthDuration(cursor.getInt(3));
                    cult.setComments(cursor.getString(4));
                    cultivations.add(cult);
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        return cultivations;
    }
    public ContentCultivation get(int id) {
        ContentCultivation cultivation = new ContentCultivation();
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "select * from CULTIVATION where id="+id;
            Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst()) {
                cultivation.setId(cursor.getInt(0));
                cultivation.setCategoryId(cursor.getInt(1));
                cultivation.setName(cursor.getString(2));
                cultivation.setMonthDuration(cursor.getInt(3));
                cultivation.setComments(cursor.getString(4));
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        return cultivation;
    }
}
