package com.example.findintermediateapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MemoPage extends ChangeStateBar {

    TextView tv_memoContent;
    TextView tv_memoLocation;
    TextView tv_memoDate;
    TextView tv_toolbarTitle;
    ViewPager vp_imagePager;
    Intent intent;
    String str_allFilePath;
    String[] str_filePath;
    Uri[] uri_filePath;
    Bitmap[] bm_file;
    int memoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_page);
        intent = getIntent();

        tv_memoContent = findViewById(R.id.memo_content);
        tv_memoContent.setText(intent.getStringExtra("memo_content"));
        tv_toolbarTitle = findViewById(R.id.memo_page_title);
        tv_toolbarTitle.setText(intent.getStringExtra("memo_location"));
        str_filePath = intent.getStringArrayExtra("memo_allImages");
        vp_imagePager = findViewById(R.id.memo_pager);
        tv_memoDate = findViewById(R.id.memo_date);
        tv_memoDate.setText(intent.getStringExtra("memo_date"));
        String id = intent.getStringExtra("memo_id");
        memoId = Integer.valueOf(id);
        uri_filePath = new Uri[str_filePath.length];
        bm_file = new Bitmap[str_filePath.length];
        for(int i = 0; i < str_filePath.length; i++) {
            uri_filePath[i] = Uri.parse(str_filePath[i]);
        }
        
        ViewPagerAdapter adapter = new ViewPagerAdapter(getLayoutInflater(), uri_filePath);
        vp_imagePager.setAdapter(adapter);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        Toolbar toolbar = findViewById(R.id.memo_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memo_page_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_deleteMemo:
                  deleteColumn(memoId);
                  Intent memoListIntent = new Intent(MemoPage.this, MemoListPage.class);
                  Intent mainIntent = new Intent(MemoPage.this, MainActivity.class);
                  MainActivity mainActivity = new MainActivity();
                  startActivity(mainIntent);
                  mainActivity.finish();
                memoListIntent.putExtra("memo_location", tv_toolbarTitle.getText().toString());
                memoListIntent.putExtra("memo_address", getIntent().getStringExtra("memo_address"));
                  startActivity(memoListIntent);
                  finish();
                return true;
            case R.id.action_editMemo:
                // MainActivity 새로 고침
                Intent mainIntent2 = new Intent(MemoPage.this, MainActivity.class);
                MainActivity mainActivity2 = new MainActivity();
                startActivity(mainIntent2);
                mainActivity2.finish();

                Intent memoEditIntent = new Intent(MemoPage.this, EditMemo.class);
                memoEditIntent.putExtra("edit_memo_location", tv_toolbarTitle.getText().toString());
                memoEditIntent.putExtra("edit_memo_address", getIntent().getStringExtra("memo_address"));
                memoEditIntent.putExtra("edit_memo_content", tv_memoContent.getText().toString());
                memoEditIntent.putExtra("edit_memo_imageArr", str_filePath);
                startActivity(memoEditIntent);
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean deleteColumn(long id){
        SQLiteDatabase mDB;
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
        mDB = dbHelper.getWritableDatabase();
        return mDB.delete(FeedReaderContract.FeedEntry.TABLE_NAME, "_id="+id, null) > 0;
    }

}