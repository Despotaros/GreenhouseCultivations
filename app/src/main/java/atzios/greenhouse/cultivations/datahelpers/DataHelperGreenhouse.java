package atzios.greenhouse.cultivations.datahelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;



import java.util.ArrayList;

import atzios.greenhouse.cultivations.contents.ContentGreenhouse;
import atzios.greenhouse.cultivations.contents.ContentGreenhouseReport;

/**
 * Data helper class pou perixei oles tis sinartiseis gia diavasma,egrafi,enimerosh,diagrafi thermokipiou
 * apo tin vasi
 * Created by Atzios Vasilis on 22/12/2014.
 */
public class DataHelperGreenhouse {
    private final String CLASS_TAG = "DataHelperGreenhouse";
    private Context context;
    public DataHelperGreenhouse(Context context) {
        this.context = context ;
    }

    /**
     * Dimiourgia neou thermokipiou
     * @param greenhouse Greenhouse info
     */
    public void create(ContentGreenhouse greenhouse) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "insert into GREENHOUSE(USER_ID,GREENHOUSE_NAME,AREA,ADDRESS,IMAGE_PATH)"
                    + " values("+greenhouse.getUserId()+",'"+greenhouse.getName()+"'," + greenhouse.getArea() +
                    ",'"+greenhouse.getAddress()+"','"+greenhouse.getImagePath()+"')";

            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Epistrefi MONO ta onomata olwn ton thermokipion
     * @return ArrayList of the greenhouses names
     */
    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "select GREENHOUSE_NAME from GREENHOUSE";
            Cursor cursor = db.rawQuery(query, null);
            boolean hasRow = cursor.moveToFirst();
            while (hasRow) {
                names.add(cursor.getString(0));
                hasRow= cursor.moveToNext();
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        return names;
    }

    /**
     * Epistrefi to sigkekrimeno thermokipio pou exei to id
     * @param id To id tou thermokipiou
     * @return To theromokipio
     */
    public ContentGreenhouse get(int id) {
        ContentGreenhouse gh = null;
        SQLiteDatabase db = new DatabaseOpenHelper(context).getReadableDatabase();
        String query = "select * from GREENHOUSE where id="+id;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()) {
            gh = new ContentGreenhouse();
            gh.setId(id);
            gh.setName(cursor.getString(2));
            gh.setArea(cursor.getDouble(3));
            gh.setAddress(cursor.getString(4));
            gh.setImagePath(cursor.getString(5));
        }
        cursor.close();
        db.close();
        return gh;
    }

    /**
     * Epistrefi ola ta thermokipia
     * @return Dinamikos pinakas me ola ta thermokipia
     */
    public ArrayList<ContentGreenhouse> getAll() {
        ArrayList<ContentGreenhouse> greenhouses = new ArrayList<>();
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "select * from GREENHOUSE";
            Cursor cursor = db.rawQuery(query, null);
            boolean hasRow = cursor.moveToFirst();
            while (hasRow) {
                ContentGreenhouse content = new ContentGreenhouse();
                content.setId(cursor.getInt(0));
                content.setUserId(cursor.getInt(1));
                content.setName(cursor.getString(2));
                content.setArea(cursor.getDouble(3));
                content.setAddress(cursor.getString(4));
                content.setImagePath(cursor.getString(5));
                greenhouses.add(content);
                hasRow = cursor.moveToNext();
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }

        return greenhouses;
    }

    public void update(ContentGreenhouse gh) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();

            String query = "update GREENHOUSE set USER_ID="+gh.getUserId()+",GREENHOUSE_NAME='"+gh.getName()+"',AREA=" +
                    +gh.getArea()+"',ADDRESS='"+gh.getAddress()+"'" +
                    ",IMAGE_PATH='"+gh.getImagePath()+"' where id="+gh.getId();

            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }

    }
    public boolean delete(int id,boolean all) {
        boolean deleted = false;
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "delete from GREENHOUSE where ID="+id;
            db.execSQL(query);
            if(all) {
                query = "delete from GREENHOUSE_CULTIVATION where GREENHOUSE_ID="+id;
                db.execSQL(query);
                query = "delete from WORK where GREENHOUSE_ID="+id;
                db.execSQL(query);
            }
            db.close();
            deleted = true;
        }
        catch (SQLiteException e) {
            Log.e(CLASS_TAG,e.getMessage());
        }

        return deleted;
    }
    /**
     * Synartish opou mas epistrefei thn anafora kaliergiwn kai ergasiwn enos thermokipiou
     * @param id To id tou themokipiou
     * @return H anafora
     */
    public ContentGreenhouseReport getReport(int id) {
        ContentGreenhouseReport report = new ContentGreenhouseReport();

        try {
            //Pernoume tin vasi gia anagnosh
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            //Diavazoume to plithos twn kaliergiwn
            String query = "select count(id) from GREENHOUSE_CULTIVATION where GREENHOUSE_ID="+id;
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                report.setTotalCultivations(cursor.getInt(0));
            }
           // cursor.close();
            //Diavazoume to plithos twn energwn kaliergiwn
            query ="select count(id) from GREENHOUSE_CULTIVATION where GREENHOUSE_ID="+id+" and ACTIVE=1";
            cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                report.setActiveCultivations(cursor.getInt(0));
            }
         //   cursor.close();
            //Diavazoume to plithos twn ergasiwn
            query ="select count(id) from WORK where GREENHOUSE_ID="+id; // and ACTIVE=1";
            cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                report.setTotalWorks(cursor.getInt(0));
            }
       //     cursor.close();
            //Diavazoume to plithos twn ergasiwn
            query ="select count(id) from WORK where GREENHOUSE_ID="+id+" and PENDING=1"; // and ACTIVE=1";
            cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                report.setPendingWorks(cursor.getInt(0));
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        return report;
    }


}
