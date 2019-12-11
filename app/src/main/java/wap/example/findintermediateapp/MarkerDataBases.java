package wap.example.findintermediateapp;
import android.provider.BaseColumns;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;

public final class MarkerDataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String LOCATIONNAME = "locationname";
        public static final String LOCATIONADDRESS = "locationaddress";
        public static final String MEMO = "memo";
        public static final String PHOTO = "photo";
        public static final String MAPX = "mapx";
        public static final String MAPY = "mapy";
        public static final String COUNT = "count";
        public static final String _TABLENAME0 = "locationtable2";
        public static final String _CREATE0 = "create table if not exists " + _TABLENAME0 + "("
                +_ID +" integer primary key autoincrement, "
                + LOCATIONNAME + " text not null , "
                + LOCATIONADDRESS + " text not null , "
                + MEMO + " text not null , "
                + PHOTO + " text not null , "
                + MAPX + " text not null , "
                + MAPY + " text not null , "
                + COUNT + "integer not null);";
    }
}

