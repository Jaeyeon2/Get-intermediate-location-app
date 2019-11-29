package com.example.findintermediateapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MemoListPage extends ChangeStateBar {

    public RecyclerView memoListRecyclerView;
    public MemoListAdapter memoListAdapter;
    public String memoLocation;
    public String memoAddress;
    public String memoContent;
    public String memoFirstImage;
    public String[] memoEachImage;
    public String memoAllImage;
    public int memoImageCount;

    TextView tv_memoListLocation;
    TextView tv_memoListAddress;
    ArrayList<MemoListItem> memoData = new ArrayList<MemoListItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list_page);
        memoLocation = getIntent().getStringExtra("memo_location");
        memoAddress = getIntent().getStringExtra("memo_address");
        tv_memoListLocation = findViewById(R.id.memo_list_location);
        tv_memoListAddress = findViewById(R.id.memo_list_address);
        tv_memoListLocation.setText(memoLocation);
        tv_memoListAddress.setText(memoAddress);

        memoListRecyclerView = findViewById(R.id.memo_list_recyclerview);
        memoListAdapter = new MemoListAdapter(memoData);
        memoListRecyclerView.setAdapter(memoListAdapter);
        memoListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select name, address, memo, photo from location_memo", null);
        while(cursor.moveToNext()) {
           if(cursor.getString(0).equals(memoLocation) && cursor.getString(1).equals(memoAddress))
           {
               memoContent = cursor.getString(2);
               memoAllImage = cursor.getString(3);
               memoEachImage = memoAllImage.split("\\|");
               memoImageCount = memoEachImage.length;

               setMemoList(memoContent, memoEachImage[0], memoEachImage, memoImageCount);
               memoListAdapter.notifyDataSetChanged();
           }
        }

        Toolbar toolbar = findViewById(R.id.memo_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

    }

    public void setMemoList(String content, String firstImage, String[] eachImage, int imageCount)
    {
        MemoListItem item = new MemoListItem();
        item.setMemoContent(content);
        item.setMemoFirstImage(firstImage);
        item.setMemoImageCount(imageCount);
        item.setMemoAllImage(eachImage);

        memoData.add(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memo_list_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
