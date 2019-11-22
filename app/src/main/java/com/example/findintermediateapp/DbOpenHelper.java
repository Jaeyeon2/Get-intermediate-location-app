package com.example.findintermediateapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {
    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    public long insertColumn(String name, String address, String memo, String photo, String x, String y, Integer count)
    {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.LOCATIONNAME, name);
        values.put(DataBases.CreateDB.LOCATIONADDRESS, address);
        values.put(DataBases.CreateDB.MEMO, memo);
        values.put(DataBases.CreateDB.PHOTO, photo);
        values.put(DataBases.CreateDB.MAPX, x);
        values.put(DataBases.CreateDB.MAPY, y);
        values.put(DataBases.CreateDB.COUNT, count);

        return mDB.insert(DataBases.CreateDB._TABLENAME0, null, values);
    }

    public Cursor selectColumns() {
        return mDB.query(DataBases.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }

    public boolean updateColumn(long id, String name, String address, String memo, String photo, String x, String y, Integer count)
    {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.LOCATIONNAME, name);
        values.put(DataBases.CreateDB.LOCATIONADDRESS, address);
        values.put(DataBases.CreateDB.MEMO, memo);
        values.put(DataBases.CreateDB.PHOTO, photo);
        values.put(DataBases.CreateDB.MAPX, x);
        values.put(DataBases.CreateDB.MAPY, y);
        values.put(DataBases.CreateDB.COUNT, count);

        return mDB.update(DataBases.CreateDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateDB._TABLENAME0, null, null);
    }

    public boolean deleteColumn(long id) {
        return mDB.delete(DataBases.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }
}