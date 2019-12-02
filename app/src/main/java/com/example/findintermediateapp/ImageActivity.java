package com.example.findintermediateapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.os.Bundle;

import com.bumptech.glide.Glide;

public class ImageActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymemo_imgview);

        ImageView imageView = (ImageView)findViewById(R.id.Mymemo_img);
        setImage(imageView);
    }
    private  void setImage(ImageView imageView){
        Intent receivedIntent = getIntent();
        String imageID = receivedIntent.getStringExtra("imageID");
        Uri uri = Uri.parse(imageID);
        Glide.with(getApplicationContext()).load(uri).into(imageView);
    }
}
