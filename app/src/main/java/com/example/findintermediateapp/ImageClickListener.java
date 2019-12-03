package com.example.findintermediateapp;

import android.content.Context;
import android.net.Uri;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.view.View;

public class ImageClickListener implements OnClickListener{
    Context context;
    Uri imageID;
    String allImage;

    public ImageClickListener(Context context, Uri imageID, String images){
        this.context = context;
        this.imageID = imageID;
        this.allImage = images;

    }
    String[] all_image = allImage.split("\\|");

    public void onClick(View v){
    }
}
