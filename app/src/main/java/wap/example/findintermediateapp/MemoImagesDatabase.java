package wap.example.findintermediateapp;


import android.provider.BaseColumns;

public class MemoImagesDatabase {
    private MemoImagesDatabase() {}

    public static class MemoImages implements BaseColumns {
        public static final String TABLE_NAME = "memo_image_table";
        public static final String NAME = "name";
        public static final String PHOTO = "photo";
        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " ("  +
                _ID + " INTEGER PRIMARY KEY," +
                NAME + " TEXT," +
                PHOTO + " TEXT)";

        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}