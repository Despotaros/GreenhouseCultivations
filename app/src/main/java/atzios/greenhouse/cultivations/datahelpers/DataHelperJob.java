package atzios.greenhouse.cultivations.datahelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import atzios.greenhouse.cultivations.contents.ContentJob;

/**
 * Created by Ατζιος on 22/12/2014.
 */
public class DataHelperJob {
    private Context context;
    public DataHelperJob(Context context) {
        this.context = context;
    }
    public void create(ContentJob job) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "insert into JOB(JOB_NAME,COMMENTS) values('" + job.getName() + "','" + job.getComments() + "')";
            db.execSQL(query);
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id,boolean deleteWorks) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "delete from JOB where id="+id;
            db.execSQL(query);
            if(deleteWorks) {
                query = "delete from WORK where JOB_ID="+id;
                db.execSQL(query);
            }
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }

    }
    public void update(ContentJob job) {
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "update JOB set JOB_NAME='" + job.getName() + "',COMMENTS='" + job.getComments() + "' "
                    +"where id="+job.getId();
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
    public ArrayList<ContentJob> getAll() {
        //O pinakas
        ArrayList<ContentJob> jobs = new ArrayList<>();
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "select * from job";

            Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst()) {
                do {
                    ContentJob job = new ContentJob();
                    job.setId(cursor.getInt(0));
                    job.setName(cursor.getString(1));
                    job.setComments(cursor.getString(2));
                    jobs.add(job);
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        return jobs;
    }
    public ContentJob get(int id) {
        ContentJob job = new ContentJob();
        try {
            SQLiteDatabase db = new DatabaseOpenHelper(context).getWritableDatabase();
            String query = "select * from job where id="+id;
            Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst()) {
                job.setId(id);
                job.setName(cursor.getString(1));
                job.setComments(cursor.getString(2));
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        return job;
    }

}
