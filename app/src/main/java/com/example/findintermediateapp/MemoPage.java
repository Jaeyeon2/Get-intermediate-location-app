package com.example.findintermediateapp;

import android.content.Context;
import android.content.Intent;
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
    TextView tv_toolbarTitle;
    ViewPager vp_imagePager;
    Intent intent;
    String str_allFilePath;
    String[] str_filePath;
    Uri[] uri_filePath;
    Bitmap[] bm_file;

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
                return true;
            case R.id.action_editMemo:
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
