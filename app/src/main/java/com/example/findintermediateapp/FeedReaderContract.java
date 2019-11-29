package com.example.findintermediateapp;

import android.provider.BaseColumns;

public class FeedReaderContract {
    private FeedReaderContract() {}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "location_memo";
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String MEMO = "memo";
        public static final String PHOTO = "photo";
        public static final String MEMOTIME = "memotime";
        public static final String COORDINATE_X = "coordinate_x";
        public static final String COORDINATE_Y = "coordinate_y";
        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FeedEntry.TABLE_NAME + " ("  +
                FeedEntry._ID + " INTEGER PRIMARY KEY," +
                FeedEntry.NAME + " TEXT," +
                FeedEntry.ADDRESS + " TEXT," +
                FeedEntry.MEMO + " TEXT," +
                FeedEntry.PHOTO + " TEXT," +
                FeedEntry.MEMOTIME + " TEXT,"+
                FeedEntry.COORDINATE_X + " TEXT," +
                FeedEntry.COORDINATE_Y + " TEXT)" ;
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }
}
