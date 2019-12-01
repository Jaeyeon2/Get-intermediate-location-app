package com.example.findintermediateapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import static com.example.findintermediateapp.AddMemo.gridView;

public class AddedImage extends ChangeStateBar {

    ImageView iv_addedImage;
    LinearLayout ll_addedImageDelete;
    Uri uri_addedImage;
    Uri[] uri_updatedArr;
    String[] str_updatedArr;
    AddMemo.ImageAdapter imageAdapter;
    private Animation fab_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_image);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        iv_addedImage = findViewById(R.id.added_image);
        ll_addedImageDelete = findViewById(R.id.added_image_delete);
        str_updatedArr = getIntent().getStringArrayExtra("addedImage_array");
        String strImage = getIntent().getStringExtra("addedImage_uri");
        uri_addedImage = Uri.parse(strImage);
        Glide.with(this).load(uri_addedImage).into(iv_addedImage);
        ll_addedImageDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                uri_updatedArr = new Uri[str_updatedArr.length];
                for(int i = 0; i < str_updatedArr.length; i++)
                {
                    uri_updatedArr[i] = Uri.parse(str_updatedArr[i]);
                }

                int index = getIntent().getIntExtra("addedImage_index", 0);
                Uri[] uri_deletedArr = new Uri[uri_updatedArr.length-1];

                for(int i = 0; i < uri_updatedArr.length-1; i++)
                {
                 if(i < index)
                 {
                     uri_deletedArr[i] = uri_updatedArr[i];
                 } else {
                     uri_deletedArr[i] = uri_updatedArr[i+1];
                 }
                }

                String[] str_deletedArr = new String[uri_deletedArr.length];
                for(int i = 0; i < uri_deletedArr.length; i++)
                {
                  str_deletedArr[i] = String.valueOf(uri_deletedArr[i]);
                }

                Intent deletedImageIntent = new Intent(AddedImage.this, DeletedImage.class);
                deletedImageIntent.putExtra("str_deletedImageArr", str_deletedArr);
                deletedImageIntent.putExtra("image_delete", "yes");
                deletedImageIntent.putExtra("location", getIntent().getStringExtra("addedImage_location"));
                deletedImageIntent.putExtra("address", getIntent().getStringExtra("addedImage_address"));
                startActivity(deletedImageIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent deletedImageIntent = new Intent(AddedImage.this, DeletedImage.class);
        deletedImageIntent.putExtra("image_delete", "no");
        deletedImageIntent.putExtra("str_deletedImageArr", str_updatedArr);
        deletedImageIntent.putExtra("location", getIntent().getStringExtra("addedImage_location"));
        deletedImageIntent.putExtra("address", getIntent().getStringExtra("addedImage_address"));
        startActivity(deletedImageIntent);
        overridePendingTransition(0, 0);
        finish();

    }
}
