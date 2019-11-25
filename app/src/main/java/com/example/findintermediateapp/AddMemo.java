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
import android.net.Uri;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddMemo extends ChangeStateBar {
    TextView tv_location;
    TextView tv_address;
    Intent x;
    static Bitmap[] addedBitmap=new Bitmap[100];
    String y;
    String addr;
    public int addcount=1;
    Bitmap[] memo_work=new Bitmap[addcount];
    private static Uri filePath;
    ImageAdapter imageAdapter;
    GridView gridView;
    EditText et_memoContent;
    private static final int REQUEST_CODE=0;
    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
    MemoImagesDbHelper imagesDbHelper = new MemoImagesDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
        tv_location=findViewById(R.id.min);
        tv_address=findViewById(R.id.jae);
        et_memoContent=findViewById(R.id.add_memo_content);
        x=getIntent();
        y=x.getStringExtra("location");
        addr=x.getStringExtra("address");
        tv_location.setText(y);
        tv_address.setText(addr);
        imageAdapter = new ImageAdapter(AddMemo.this, this, memo_work);

        Toolbar toolbar =findViewById(R.id.tools);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        gridView=(GridView)findViewById(R.id.gridview);
        gridView.setAdapter(imageAdapter);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_memo_toolbar_menu,menu);
        return true;
    }

    @Override    //위에 툴바
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.tool_menu:
                addMemo();
                Intent mainIntent = new Intent(AddMemo.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode == REQUEST_CODE){
            if(resultCode==RESULT_OK){
                try{
                    addcount++;
                    filePath=data.getData();
                    InputStream in =getContentResolver().openInputStream(filePath);
                    Bitmap img;
                    img= BitmapFactory.decodeStream(in);
                    in.close();
                    memo_work=new Bitmap[addcount];
                    Bitmap bitmap=BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.add_work_no2);
                    addedBitmap[addcount-1]=img;
                    for(int i=0;i<addcount;i++){
                        Log.d("addedBitmap",String.valueOf(addedBitmap[i]));
                        if(i==addcount-1){
                            memo_work[i]=bitmap;
                        }
                        else{
                            memo_work[i]=addedBitmap[i+1];
                        }
                    }
                    imageAdapter=new ImageAdapter(this, AddMemo.this,memo_work);
                    gridView.setAdapter(imageAdapter);
                }catch (Exception e){

                }
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(this,"사진 선택 취소",Toast.LENGTH_LONG).show();
            }
        }
    }


    public class ImageAdapter extends BaseAdapter {
        private Context context;

        Activity mActiviy=null;
        Bitmap[] memo_image=null;
        public ImageAdapter(Activity activity, Context con, Bitmap[] work){
            this.context=con;
            this.mActiviy=activity;
            this.memo_image=work;
        };


        public int getCount(){
            return (null !=memo_image)? memo_image.length: 0;
        }
        public Object getItem(int pos){
            return (null!=memo_image)?memo_image.length:0;
        }
        public long getItemId(int pos){
            return pos;
        }
        public View getView(int pos, View convertView, ViewGroup parent){
            ImageView imageView;

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int screenWidth=metrics.widthPixels;

            if(convertView==null){
                imageView=new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(screenWidth/3-10,screenWidth/3-10));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(10,10,10,10);
            }
            else{
                imageView=(ImageView)convertView;
            }
            if(pos==memo_image.length-1)
            {
                imageView.setImageResource(R.drawable.add_work_no2);
                imageView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view){
                        Intent intent=new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        mActiviy.startActivityForResult(intent,REQUEST_CODE);
                    }
                });
            }
            else{
                imageView.setImageBitmap(memo_image[pos]);
                imageView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){

                    }
                });
            }
            return imageView;
        }
    }

    public void addMemo() {

        String location_name = tv_location.getText().toString();
        String location_address = tv_address.getText().toString();
        String memo_content = et_memoContent.getText().toString();
        String location_x = getIntent().getStringExtra("mapx");
        String location_y = getIntent().getStringExtra("mapy");
        String str_allRegPhoto = "";
        SQLiteDatabase imageDb = imagesDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SQLiteStatement p = imageDb.compileStatement("insert into memo_image_table(name, photo) values(?, ?)");
        for(int i = 0; i < addcount; i++)
        {
            Log.d("addedBitmap", String.valueOf(addedBitmap[i]));
        }
        if(addcount == 2)
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            addedBitmap[1].compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] data = stream.toByteArray();
            //cv.put("name", location_name);
            //cv.put("photo", data);
            //imageDb.insert("memo_image_table", null, cv);
            p.bindString(1, location_name);
            p.bindBlob(2, data);
            p.execute();
        }else if(addcount > 2) {
            for (int i = 0; i < addcount - 1; i++) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                addedBitmap[i + 1].compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] data = stream.toByteArray();
                cv.put("name", location_name);
                cv.put("photo", data);
                imageDb.insert("memo_image_table", null, cv);
            }
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.NAME, location_name);
        values.put(FeedReaderContract.FeedEntry.ADDRESS, location_address);
        values.put(FeedReaderContract.FeedEntry.MEMO, memo_content);
        values.put(FeedReaderContract.FeedEntry.COORDINATE_X, location_x);
        values.put(FeedReaderContract.FeedEntry.COORDINATE_Y, location_y);
        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

        Cursor addedCursor;
        dbHelper.onOpen(db);
        addedCursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        while(addedCursor.moveToNext()){
            String tempName = addedCursor.getString(addedCursor.getColumnIndex("name"));

            Log.d("tempName111", tempName);
        }
    }

    public String getBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }
}
