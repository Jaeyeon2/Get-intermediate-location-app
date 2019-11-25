package com.example.findintermediateapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class AddMemo extends AppCompatActivity {
    String addedLocation;
    TextView tv_addedLocation;
    Button btn_addMemo;
    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
    ImageView iv_memoImage1;
    ImageView iv_memoImage2;
    private static final int REQUEST_CODE1 = 0;
    private static final int REQUEST_CODE2 = 1;
    public boolean add_memo_image1 = false;
    public boolean add_memo_image2 = false;
    private Uri filePath;
    public int addCount = 0;
    public static Bitmap[] bitmap_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
        addedLocation = getIntent().getStringExtra("location");
//        tv_addedLocation = findViewById(R.id.add_page_location);
        btn_addMemo = findViewById(R.id.add_memo_button);
//        tv_addedLocation.setText(addedLocation);
        iv_memoImage1 = findViewById(R.id.add_memo_image1);
        iv_memoImage2 = findViewById(R.id.add_memo_image2);

        iv_memoImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE1);
            }
        });
        iv_memoImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE2);
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE1) {
            if(resultCode == RESULT_OK) {
                if(add_memo_image1 == false) {
                    addCount++;
                    bitmap_array = new Bitmap[addCount];
                    add_memo_image1 = true;
                }
                try {
                    filePath = data.getData();
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    bitmap_array[0] = BitmapFactory.decodeStream(in);
                    in.close();
                    iv_memoImage1.setImageBitmap(bitmap_array[0]);
                }catch(Exception e)
                {

                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == REQUEST_CODE2) {

        }
    }
}
