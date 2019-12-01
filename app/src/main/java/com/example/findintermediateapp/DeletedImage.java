package com.example.findintermediateapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateTodoMutation;
import com.amazonaws.amplify.generated.graphql.ListTodosQuery;
import com.amazonaws.amplify.generated.graphql.OnCreateTodoSubscription;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.AppSyncSubscriptionCall;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.annotation.Nonnull;

import type.CreateTodoInput;

import static com.example.findintermediateapp.MainActivity.androidId;

public class DeletedImage extends ChangeStateBar {
    TextView tv_location;
    TextView tv_address;
    ImageView iv_exImage;
    Intent intent;
    static Bitmap[] addedBitmap = new Bitmap[100];
    static Uri[] uri_addedUri = new Uri[100];
    String y;
    String addr;
    public int addcount = 1;
    Bitmap[] memo_work = new Bitmap[addcount];
    Uri[] uri_imageArr = new Uri[addcount];
    private static Uri filePath;
    public static Uri[] filePathArray = new Uri[100];
    public  Uri[] addedfilePath = new Uri[addcount];
    ImageAdapter imageAdapter;
    static GridView gridView;
    EditText et_memoContent;
    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
    String convertedBitmap;
    String location_name;
    public static final int KITKAT_VALUE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_image);
        tv_location = findViewById(R.id.deleted_image_location);
        tv_address = findViewById(R.id.deleted_image_address);
        et_memoContent = findViewById(R.id.deleted_image_content);
        intent = getIntent();
        y = intent.getStringExtra("location");
        addr = intent.getStringExtra("address");
        et_memoContent.setText(intent.getStringExtra("content"));
        tv_location.setText(y);
        tv_address.setText(addr);

        String[] strArr_deletedImage = intent.getStringArrayExtra("str_deletedImageArr");
        uri_imageArr = new Uri[strArr_deletedImage.length];
        for(int i = 0; i < strArr_deletedImage.length; i++)
        {
            uri_imageArr[i] = Uri.parse(strArr_deletedImage[i]);
            Log.d("strArr_deletedImage[i]", strArr_deletedImage[i]);
            Log.d("uri_imageArr[i]zzzz", String.valueOf(uri_imageArr[i]));
            uri_addedUri[i] = Uri.parse(strArr_deletedImage[i]);

            if(i == strArr_deletedImage.length -1) {
                imageAdapter = new ImageAdapter(DeletedImage.this, this, uri_imageArr);
                imageAdapter.notifyDataSetChanged();
            }
        }
        addcount = strArr_deletedImage.length;

        Toolbar toolbar = findViewById(R.id.deleted_image_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        gridView = (GridView) findViewById(R.id.deleted_image_gridview);
        gridView.setAdapter(imageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_memo_toolbar_menu, menu);
        return true;
    }

    @Override    //위에 툴바
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tool_menu:
                addMemo();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == KITKAT_VALUE) {
            if (resultCode == RESULT_OK) {
                try {
                    addcount++;
                    filePath = data.getData();
                    Uri imageUri = Uri.parse(String.valueOf(filePath));
                    String imagePath = imageUri.getPath();
                    Log.d("imagePath", imagePath);
                    memo_work = new Bitmap[addcount];
                    uri_imageArr = new Uri[addcount];
                    uri_addedUri[addcount-2] = imageUri;
                    filePathArray[addcount -1] = filePath;
                    Log.d("uri_addedUri[addcount-1", String.valueOf(uri_addedUri[addcount-1]));
                    for (int i = 0; i < addcount; i++) {
                        Log.d("uri_imageArr[i]", String.valueOf(uri_addedUri[i]));
                        if (i == addcount - 1) {
                            // uri_imageArr[i] = firstUri;
                        } else {
                            uri_imageArr[i] = uri_addedUri[i];
                        }
                    }
                    imageAdapter = new ImageAdapter(this, DeletedImage.this, uri_imageArr);
                    gridView.setAdapter(imageAdapter);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }


    public class ImageAdapter extends BaseAdapter {
        private Context context;

        Activity mActiviy = null;
        Uri[] memo_image = null;
        ImageAdapter imageAdapter;

        public ImageAdapter(Activity activity, Context con, Uri[] work) {
            this.context = con;
            this.mActiviy = activity;
            this.memo_image = work;
        }

        ;


        public int getCount() {
            return (null != memo_image) ? memo_image.length : 0;
        }

        public Object getItem(int pos) {
            return (null != memo_image) ? memo_image.length : 0;
        }

        public long getItemId(int pos) {
            return pos;
        }

        public View getView(int pos, View convertView, ViewGroup parent) {

            for(int i = 0; i < memo_image.length; i++) {
                Log.d("memo_imageaaa", String.valueOf(memo_image[i]));
            }
            ImageView imageView;

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;

            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(screenWidth / 3 - 10, screenWidth / 3 - 10));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(3, 3, 3, 3);
            } else {
                imageView = (ImageView) convertView;
            }
            if (pos == memo_image.length - 1) {
                imageView.setImageResource(R.drawable.add_work_no2);
                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        if (Build.VERSION.SDK_INT < 19) {
                            intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");
                            mActiviy.startActivityForResult(intent, KITKAT_VALUE);
                        } else  {
                            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("*/*");
                            startActivityForResult(intent, KITKAT_VALUE);
                        }
                    }
                });
            } else {
                Glide.with(context).load(memo_image[pos]).into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent addedImageIntent = new Intent(context, AddedImage.class);
                        addedImageIntent.putExtra("addedImage_uri", String.valueOf(memo_image[pos]));
                        addedImageIntent.putExtra("addedImage_index", pos);
                        String[] str_imageArr = new String[memo_image.length];
                        for(int i = 0; i < str_imageArr.length; i++)
                        {
                            str_imageArr[i] = String.valueOf(memo_image[i]);
                        }
                        addedImageIntent.putExtra("addedImage_array", str_imageArr);
                        addedImageIntent.putExtra("addedImage_location", tv_location.getText().toString());
                        addedImageIntent.putExtra("addedImage_address", tv_address.getText().toString());
                        addedImageIntent.putExtra("addedImage_content", et_memoContent.getText().toString());
                        addedImageIntent.putExtra("addedImage_requestPage", getIntent().getStringExtra("request_page"));
                        addedImageIntent.putExtra("addedImage_mapx", getIntent().getStringExtra("mapx"));
                        addedImageIntent.putExtra("addedImage_mapy", getIntent().getStringExtra("mapy"));
                        startActivity(addedImageIntent);
                        finish();
                    }
                });
            }
            return imageView;
        }
    }

    public void addMemo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd a hh:mm");
        Date time = new Date();
        String time1 = format.format(time);
        Log.d("time1111", time1);

        location_name = tv_location.getText().toString();
        String location_address = tv_address.getText().toString();
        String memo_content = et_memoContent.getText().toString();
        String location_x = getIntent().getStringExtra("mapx");
        String location_y = getIntent().getStringExtra("mapy");
        Log.d("location_x1", location_x);
        String str_allRegPhoto = "";

        for (int i = 0; i < addcount-1; i++) {
            str_allRegPhoto = str_allRegPhoto + String.valueOf(uri_imageArr[i]) + "|";
            Log.d("uri_imageArrgg", String.valueOf(uri_imageArr[i]));
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.NAME, location_name);
        values.put(FeedReaderContract.FeedEntry.ADDRESS, location_address);
        values.put(FeedReaderContract.FeedEntry.MEMO, memo_content);
        values.put(FeedReaderContract.FeedEntry.PHOTO, str_allRegPhoto);
        Log.d("str_allRegPhoto", str_allRegPhoto);
        values.put(FeedReaderContract.FeedEntry.COORDINATE_X, location_x);
        values.put(FeedReaderContract.FeedEntry.COORDINATE_Y, location_y);
        values.put(FeedReaderContract.FeedEntry.MEMOTIME, time1);
        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

        if(getIntent().getStringExtra("request_page").equals("MainActivity")) {
            Intent mainIntent = new Intent(DeletedImage.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else if(getIntent().getStringExtra("request_page").equals("MemoListPage")){
            Intent memoListIntent = new Intent(DeletedImage.this, MemoListPage.class);
            memoListIntent.putExtra("memo_location", location_name);
            memoListIntent.putExtra("memo_address", location_address);
            startActivity(memoListIntent);
            finish();
        }
    }

    public String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}