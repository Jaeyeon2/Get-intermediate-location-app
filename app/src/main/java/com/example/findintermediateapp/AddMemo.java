package com.example.findintermediateapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddMemo extends AppCompatActivity {
    String addedLocation;
    TextView tv_addedLocation;
    Button btn_addMemo;
    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
        addedLocation = getIntent().getStringExtra("location");
        tv_addedLocation = findViewById(R.id.add_page_location);
        btn_addMemo = findViewById(R.id.add_memo_button);
        tv_addedLocation.setText(addedLocation);

        btn_addMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.NAME, "울산대학교");
                values.put(FeedReaderContract.FeedEntry.ADDRESS, "93 대학로 무거동 남구 울산광역시");
                values.put(FeedReaderContract.FeedEntry.MEMO, "울산대울산대");
                values.put(FeedReaderContract.FeedEntry.PHOTO, "ss");
                values.put(FeedReaderContract.FeedEntry.COORDINATE_X, "35.543895");
                values.put(FeedReaderContract.FeedEntry.COORDINATE_Y, "129.256328");

                long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

                Intent mainIntent = new Intent(AddMemo.this, MainActivity.class);
                startActivity(mainIntent);


            }
        });
    }
}
