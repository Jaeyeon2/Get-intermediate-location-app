package com.example.findintermediateapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MemoPage extends ChangeStateBar {

    TextView tv_memoContent;
    TextView tv_memoLocation;
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
        tv_memoLocation = findViewById(R.id.memo_location);
        tv_memoContent.setText(intent.getStringExtra("memo_content"));
        tv_memoLocation.setText(intent.getStringExtra("memo_location"));
        str_allFilePath = intent.getStringExtra("memo_allImages");
        vp_imagePager = findViewById(R.id.memo_pager);
        str_filePath = str_allFilePath.split("\\|");
        uri_filePath = new Uri[str_filePath.length];
        bm_file = new Bitmap[str_filePath.length];

        for(int i = 0; i < str_filePath.length; i++) {
            uri_filePath[i] = Uri.parse(str_filePath[i]);
            try {
                bm_file[i] = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_filePath[i]);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        ViewPagerAdapter adapter = new ViewPagerAdapter(getLayoutInflater(), bm_file);
        vp_imagePager.setAdapter(adapter);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
    }
}
