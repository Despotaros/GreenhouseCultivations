package atzios.greenhouse.cultivations.datahelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Κλάση DatabaseOpenHelper
 * Δημιουργει και επιστρέφει σύνδεση με την βάση
 * Created by Atzios Vasilis on 16/12/2014.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static String CLASS_TAG = "DatabaseOpenHelper";
    private static int version = 1; //Database Version
    private static String name = "greenhouse.db"; //Database Name

    /**
     * Constructor
     * @param context Application Context
     */
    public DatabaseOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //Τρέχει την πρώτη φορά για να δημιουργησει την βάση
            //Create Users table
            db.execSQL("create table USERS(ID integer primary key,USERNAME text,PASSWORD text,USES_PASS integer,NAME text," +
                    "LAST_NAME text,PHONE text,ADDRESS text)");
            //Create Greenhouses table
            db.execSQL("create table GREENHOUSE(ID integer primary key autoincrement,USER_ID integer,GREENHOUSE_NAME text," +
                    "AREA real,ADDRESS text,IMAGE_PATH text)");
            //Create Cultivations table
            db.execSQL("create table CULTIVATION(ID integer primary key autoincrement,CATEGORY_ID integer,CULTIVATION_NAME text,COMMENTS text)");
            //Create Cultivation categories table
            db.execSQL("create table CULTIVATION_CATEGORY(ID integer primary key autoincrement,CATEGORY_NAME text)");
            //Create greenhouse cultivations table
            db.execSQL("create table GREENHOUSE_CULTIVATION(ID integer primary key autoincrement,GREENHOUSE_ID integer,CULTIVATION_ID integer," +
                    "START_DATE numeric,END_DATE numeric,COMMENTS text,ACTIVE integer)");
            //Create Jobs table
            db.execSQL("create table JOB(ID integer primary key autoincrement,JOB_NAME text,COMMENTS text)");
            //Create Works table
            db.execSQL("create table WORK(ID integer primary key autoincrement,USER_ID integer,GREENHOUSE_ID integer," +
                    "CULTIVATION_ID integer,JOB_ID integer,PENDING integer,DATE numeric,COMMENTS text)");
        }
        catch (SQLiteException e) {
            Log.e(CLASS_TAG,e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Τρεχει κάθε φορά που αλάζει το version της βάσης
    }
}
