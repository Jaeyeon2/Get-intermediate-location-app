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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

import me.relex.circleindicator.CircleIndicator;

public class MemoPage extends ChangeStateBar {

    TextView tv_memoContent;
    TextView tv_memoLocation;
    TextView tv_memoDate;
    TextView tv_toolbarTitle;
    ViewPager vp_imagePager;
    Intent intent;
    String str_allFilePath;
    String memoX;
    String memoY;
    String requestPage;
    String[] str_filePath;
    Uri[] uri_filePath;
    Bitmap[] bm_file;
    int memoId;
    LinearLayout ll_yesImage;
    LinearLayout ll_memo;


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
        requestPage = intent.getStringExtra("request_page");
        memoId = Integer.valueOf(id);

        if(str_filePath != null) {
            uri_filePath = new Uri[str_filePath.length];
            bm_file = new Bitmap[str_filePath.length];
            for (int i = 0; i < str_filePath.length; i++) {
                uri_filePath[i] = Uri.parse(str_filePath[i]);
            }
        }
        memoX = intent.getStringExtra("memo_x");
        memoY = intent.getStringExtra("memo_y");
        ll_yesImage = findViewById(R.id.yes_image_layout);
        ll_memo = findViewById(R.id.memo_layout);


        
        ViewPagerAdapter adapter = new ViewPagerAdapter(getLayoutInflater(), uri_filePath);
        vp_imagePager.setAdapter(adapter);

        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(vp_imagePager);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        Toolbar toolbar = findViewById(R.id.memo_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        if(getIntent().getStringExtra("memo_allImages").equals("noImage"))
        {
            ll_yesImage.setVisibility(View.INVISIBLE);

            DisplayMetrics metrics1 = getApplicationContext().getResources().getDisplayMetrics();
            int screenWidth = metrics1.widthPixels;
            int screemHeight = metrics1.heightPixels;
            ll_memo.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screemHeight));
        }
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
                  Intent mainIntent = new Intent(MemoPage.this, MainActivity.class);
                  MainActivity mainActivity = new MainActivity();
                  startActivity(mainIntent);
                  mainActivity.finish();
                  if(requestPage.equals("MemoListPage"))
                  {
                      Intent memoListIntent = new Intent(MemoPage.this, MemoListPage.class);
                      memoListIntent.putExtra("memo_location", tv_toolbarTitle.getText().toString());
                      memoListIntent.putExtra("memo_address", getIntent().getStringExtra("memo_address"));
                      startActivity(memoListIntent);
                  } else if(requestPage.equals("MyMemoPage"))
                  {


                  }
                  finish();
                return true;
            case R.id.action_editMemo:
                Intent memoEditIntent = new Intent(MemoPage.this, EditMemo.class);
                memoEditIntent.putExtra("edit_memo_location", tv_toolbarTitle.getText().toString());
                memoEditIntent.putExtra("edit_memo_address", getIntent().getStringExtra("memo_address"));
                Log.d("memo_address", getIntent().getStringExtra("memo_address"));
                memoEditIntent.putExtra("edit_memo_content", tv_memoContent.getText().toString());
                memoEditIntent.putExtra("edit_memo_imageArr", str_filePath);
                memoEditIntent.putExtra("edit_memo_id", intent.getStringExtra("memo_id"));
                String[] strArr_allImage = getIntent().getStringArrayExtra("memo_allImages");
                String str_allImage = "";
                for(int i = 0; i < strArr_allImage.length; i++)
                {
                    str_allImage = str_allImage + strArr_allImage[i] + "|";
                }
                memoEditIntent.putExtra("edit_memo_allImages", str_allImage);
                memoEditIntent.putExtra("edit_memo_date", tv_memoDate.getText().toString());
                memoEditIntent.putExtra("edit_memo_x", memoX);
                memoEditIntent.putExtra("edit_memo_y", memoY);
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
