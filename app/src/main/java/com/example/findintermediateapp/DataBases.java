package com.example.findintermediateapp;

import android.provider.BaseColumns;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String MAPX = "mapx";
        public static final String MAPY = "mapy";
        public static final String COUNT = "count";
        public static final String _TABLENAME0 = "locationtable";
        public static final String _CREATE0 = "create table if not exists " + _TABLENAME0 + "("
                + MAPX + " text not null , "
                + MAPY + " text not null , "
                + COUNT + "integer not null);";
    }
}

