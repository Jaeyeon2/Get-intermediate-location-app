package wap.example.findintermediateapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import wap.example.findintermediateapp.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class AddMemo extends ChangeStateBar {
    TextView tv_location;
    TextView tv_address;
    ImageView iv_exImage;
    Intent x;
    static Bitmap[] addedBitmap = new Bitmap[100];
    static Uri[] uri_addedUri = new Uri[100];
    String y;
    String addr;
    public int addcount = 1;
    Bitmap[] memo_work = new Bitmap[addcount];
    Uri[] uri_imageArr = new Uri[addcount];
    private static Uri filePath;
    public Uri[] filePathArray = new Uri[100];
    public  Uri[] addedfilePath = new Uri[addcount];
    ImageAdapter imageAdapter;
    static GridView gridView;
    EditText et_memoContent;
    EditText et_myLocation;
    TextView tv_myLocationAddress;
    private static final int REQUEST_CODE = 0;
    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
    MemoImagesDbHelper imagesDbHelper = new MemoImagesDbHelper(this);
    String convertedBitmap;
    String location_name;
    public static final int KITKAT_VALUE = 1002;
    String location;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);


        tv_location = findViewById(R.id.min);
        tv_address = findViewById(R.id.jae);
        et_myLocation = findViewById(R.id.add_memo_myLocation);
        et_memoContent = findViewById(R.id.add_memo_content);
        tv_myLocationAddress = findViewById(R.id.add_memo_address);
        x = getIntent();
        LinearLayout searchView = findViewById(R.id.add_memo_ll);
        LinearLayout locationView = findViewById(R.id.add_memo_my);
        if(x.getStringExtra("location").equals("MyLocation"))
        {
            searchView.setVisibility(INVISIBLE);
            locationView.setVisibility(VISIBLE);
            tv_location.setVisibility(INVISIBLE);
            et_myLocation.setVisibility(VISIBLE);
        } else {
            //LinearLayout.LayoutParams wp = (LinearLayout.LayoutParams)view.getLayoutParams();
            //wp.width = 1000;
            //wp.height = 200;
            //view.setLayoutParams(wp);
            searchView.setVisibility(VISIBLE);
            locationView.setVisibility(INVISIBLE);
            y = x.getStringExtra("location");
            et_myLocation.setVisibility(INVISIBLE);
            tv_location.setVisibility(VISIBLE);
            et_myLocation.setHeight(0);
            tv_location.setText(y);
        }
        addr = x.getStringExtra("address");
        tv_myLocationAddress.setText(addr);
        tv_address.setText(addr);

        imageAdapter = new ImageAdapter(AddMemo.this, this, uri_imageArr);


        Toolbar toolbar = findViewById(R.id.tools);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(imageAdapter);

        /*
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();
    }
    public void runMutation(){
        CreateTodoInput createTodoInput = CreateTodoInput.builder().
                name("Use AppSync").
                description("Realtime and Offline").
                build();
        mAWSAppSyncClient.mutate(CreateTodoMutation.builder().input(createTodoInput).build())
                .enqueue(mutationCallback);
         */
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
                    InputStream in = getContentResolver().openInputStream(filePath);
                    Log.d("filePath", String.valueOf(filePath));
                    Bitmap img;
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                    memo_work = new Bitmap[addcount];
                    uri_imageArr = new Uri[addcount];
                    Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.add_work_no2);
                    //Uri firstUri = getImageUri(getApplicationContext(), bitmap);
                    filePathArray[addcount -1] = filePath;
                    addedBitmap[addcount - 1] = img;
                    uri_addedUri[addcount -1] = imageUri;
                    Log.d("uri_addedUri[addcount-1", String.valueOf(uri_addedUri[addcount-1]));
                    for (int i = 0; i < addcount; i++) {
                        Log.d("uri_imageArr[i]", String.valueOf(uri_addedUri[i]));
                        if (i == addcount - 1) {
                            // uri_imageArr[i] = firstUri;
                        } else {
                            uri_imageArr[i] = uri_addedUri[i + 1];
                        }
                    }
                    imageAdapter = new ImageAdapter(this, AddMemo.this, uri_imageArr);
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

                        if(x.getStringExtra("location").equals("MyLocation"))
                        {
                            location = et_myLocation.getText().toString();
                        } else {
                            location = tv_location.getText().toString();
                        }

                        Intent addedImageIntent = new Intent(context, AddedImage.class);
                        addedImageIntent.putExtra("addedImage_uri", String.valueOf(memo_image[pos]));
                        addedImageIntent.putExtra("addedImage_index", pos);
                        String[] str_imageArr = new String[memo_image.length];
                        for(int i = 0; i < str_imageArr.length; i++)
                        {
                            str_imageArr[i] = String.valueOf(memo_image[i]);
                            Log.d("str_imageArr[i]qqq", str_imageArr[i]);
                        }
                        Log.d("str_imageArr.lengthqqq", String.valueOf(str_imageArr.length));

                        addedImageIntent.putExtra("addedImage_array", str_imageArr);
                        addedImageIntent.putExtra("addedImage_location", location);
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

        if (x.getStringExtra("location").equals("MyLocation")) {
            location = et_myLocation.getText().toString();
        } else {
            location = tv_location.getText().toString();
        }
        String location_address = tv_address.getText().toString();
        String memo_content;
        String str_allRegPhoto = "";

        if(et_memoContent.getText().toString().equals(""))
        {
            memo_content = "noContent";
        } else {
            memo_content = et_memoContent.getText().toString();
        }

        for(int i = 0; i < filePathArray.length; i++)
        {
            Log.d("filePathArray[i]", String.valueOf(filePathArray[i]));
        }

        if(filePathArray[1] == null)
        {
            str_allRegPhoto = "noImage";
        } else {
            for (int i = 0; i < addcount - 1; i++) {
                Log.d("addedBitmap", String.valueOf(addedBitmap[i]));
                str_allRegPhoto = str_allRegPhoto + String.valueOf(filePathArray[i + 1]) + "|";
            }
        }
        if(!et_memoContent.getText().toString().equals("") || filePathArray[1] != null)
        {
            String location_x = getIntent().getStringExtra("mapx");
            String location_y = getIntent().getStringExtra("mapy");
            Log.d("location_x1", location_x);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.NAME, location);
            values.put(FeedReaderContract.FeedEntry.ADDRESS, location_address);
            values.put(FeedReaderContract.FeedEntry.MEMO, memo_content);
            values.put(FeedReaderContract.FeedEntry.PHOTO, str_allRegPhoto);
            Log.d("str_allRegPhoto", str_allRegPhoto);
            values.put(FeedReaderContract.FeedEntry.COORDINATE_X, location_x);
            values.put(FeedReaderContract.FeedEntry.COORDINATE_Y, location_y);
            values.put(FeedReaderContract.FeedEntry.MEMOTIME, time1);
            db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

            if (getIntent().getStringExtra("request_page").equals("MainActivity")) {
                Intent mainIntent = new Intent(AddMemo.this, MainActivity.class);
                mainIntent.putExtra("added_locationX", location_x);
                mainIntent.putExtra("added_locationY", location_y);
                startActivity(mainIntent);
                finish();
            } else if (getIntent().getStringExtra("request_page").equals("MemoListPage")) {
                // MainActivity 새로 고침
                /*
                Intent mainIntent2 = new Intent(AddMemo.this, MainActivity.class);
                MainActivity mainActivity2 = new MainActivity();
                startActivity(mainIntent2);
                mainActivity2.finish();
                 */
                Intent memoListIntent = new Intent(AddMemo.this, MemoListPage.class);
                memoListIntent.putExtra("memo_location", location);
                memoListIntent.putExtra("memo_address", location_address);
                memoListIntent.putExtra("added_locationX", location_x);
                memoListIntent.putExtra("added_locationY", location_y);
                startActivity(memoListIntent);
                finish();
        }
        } else {
            showAlertDialog();
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

    void showAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메모 저장 실패");
        builder.setMessage("내용 또는 사진을 등록해주세요!");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}