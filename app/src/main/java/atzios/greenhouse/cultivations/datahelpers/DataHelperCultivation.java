package atzios.greenhouse.cultivations.datahelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import atzios.greenhouse.cultivations.contents.ContentCultivation;
import atzios.greenhouse.cultivations.contents.ContentJob;

/**
 * Created by Ατζιος on 22/12/2014.
 */
public class DataHelperCultivation {
    private Context context;
    public DataHelperCultivation(Context context) {
        this.context = context;
    }
    public void create(ContentCultivation cultivation) {
        try {

            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "insert into CULTIVATION(CATEGORY_ID,CULTIVATION_NAME,COMMENTS) values(" + cultivation.getCategoryId() + ",'" + cultivation.getName()
                    + "','" + cultivation.getComments()+"')";
            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id,boolean deleteGHCultivations) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "delete from CULTIVATION where id="+id;
            db.execSQL(query);
            if(deleteGHCultivations) {
                query = "delete from GREENHOUSE_CULTIVATION where CULTIVATION_ID="+id;
                db.execSQL(query);
            }
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }

    }
    public void update(ContentCultivation cultivation) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();;
            String query = "update CULTIVATION set CULTIVATION_NAME='" + cultivation.getName() + "',COMMENTS='" + cultivation.getComments() +
                    " where id="+cultivation.getId();
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
                    cult.setComments(cursor.getString(3));
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
                cultivation.setComments(cursor.getString(3));
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
