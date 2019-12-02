package com.example.findintermediateapp;

import android.content.Context;
import android.net.Uri;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.view.View;

public class ImageClickListener implements OnClickListener{
    Context context;
    Uri imageID;

    public ImageClickListener(Context context, Uri imageID){
        this.context = context;
        this.imageID = imageID;

    }

    public void onClick(View v){
        Intent intent= new Intent(context,ImageActivity.class);
        intent.putExtra("imageID", imageID.toString());
        context.startActivity(intent);
    }
}
