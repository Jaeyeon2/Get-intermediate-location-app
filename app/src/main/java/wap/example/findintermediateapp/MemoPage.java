package wap.example.findintermediateapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.bottomappbar.BottomAppBarTopEdgeTreatment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naver.maps.map.overlay.Marker;

import wap.example.findintermediateapp.R;

import me.relex.circleindicator.CircleIndicator;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MemoPage extends ChangeStateBar {

    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);

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
    TextView tv_noImageDate;

    String[] memoName;
    String[] memoAdress;
    String[] memoMemo;
    String[] memoPhoto;
    String[] memoMemotime;
    String[] memoX2;
    String[] memoY2;
    String[] memoId2;
    int memoCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_page);
        intent = getIntent();

        tv_memoContent = findViewById(R.id.memo_content);
        tv_memoContent.setText(intent.getStringExtra("memo_content"));
        if(intent.getStringExtra("memo_content").equals("noContent")) {
            tv_memoContent.setVisibility(INVISIBLE);
        }
        tv_toolbarTitle = findViewById(R.id.memo_page_title);
        tv_toolbarTitle.setText(intent.getStringExtra("memo_location"));
        str_filePath = intent.getStringArrayExtra("memo_allImages");
        vp_imagePager = findViewById(R.id.memo_pager);
        tv_memoDate = findViewById(R.id.memo_date);
        tv_noImageDate = findViewById(R.id.noImage_memo_date);
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
        Log.d("intent.getStrgExtra(memo_x)", String.valueOf(intent.getStringExtra("memo_x")));
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

//        Log.d("memo_image", getIntent().getStringExtra("memo_image"));
//        Log.d("request_page", getIntent().getStringExtra("request_page"));


        if(getIntent().getStringExtra("memo_image").equals("no"))
        {
            Log.d("aaaaaaa111", "s");
            ll_yesImage.setVisibility(INVISIBLE);
            DisplayMetrics metrics1 = getApplicationContext().getResources().getDisplayMetrics();
            int screenWidth = metrics1.widthPixels;
            int screemHeight = metrics1.heightPixels;
            tv_memoDate.setVisibility(INVISIBLE);
            tv_noImageDate.setVisibility(VISIBLE);

            tv_memoDate.setText(intent.getStringExtra("memo_date"));
            tv_noImageDate.setText(intent.getStringExtra("memo_date"));
            //tv_noImageDate.setGravity(Gravity.BOTTOM);
            ll_memo.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screemHeight));
        }
        if(getIntent().getStringExtra("request_page").equals("MyMemoPage_noimage"))
        {

            Log.d("aaaaaaa111", "s");
            ll_yesImage.setVisibility(INVISIBLE);
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

                      SQLiteDatabase db = dbHelper.getWritableDatabase();
                      Cursor cursor = db.rawQuery("select name, address, memo, photo, memotime, coordinate_x, coordinate_y, _id from location_memo" , null);
                      memoCount = cursor.getCount();
                      memoName = new String[memoCount];
                      memoAdress = new String[memoCount];
                      memoMemo = new String[memoCount];
                      memoPhoto = new String[memoCount];
                      memoMemotime = new String[memoCount];
                      memoX2 = new String[memoCount];
                      memoY2 = new String[memoCount];
                      memoId2 = new String[memoCount];

                      int index = 0;
                      while(cursor.moveToNext())
                      {
                          memoName[index] = cursor.getString(0);
                          memoAdress[index] = cursor.getString(1);
                          memoMemo[index] = cursor.getString(2);
                          memoMemotime[index] = cursor.getString(4);
                          memoPhoto[index] = cursor.getString(3);
                          memoX2[index] = cursor.getString(5);
                          memoY2[index] = cursor.getString(6);
                          memoId2[index] = cursor.getString(7);
                          index++;
                      }
                      Intent myMemoIntent = new Intent(MemoPage.this, MyMemo.class);
                      myMemoIntent.putExtra("memoName",memoName);
                      myMemoIntent.putExtra("memoAdress",memoAdress);
                      myMemoIntent.putExtra("memoPhoto",memoPhoto);
                      myMemoIntent.putExtra("memoContent", memoMemo);
                      myMemoIntent.putExtra("memoMemotime",memoMemotime);
                      myMemoIntent.putExtra("memoX",memoX2);
                      myMemoIntent.putExtra("memoY",memoY2);
                      myMemoIntent.putExtra("memoId", memoId2);
                      myMemoIntent.putExtra("memoCount",memoCount);
                      startActivity(myMemoIntent);
                  }
                  finish();
                return true;
            case R.id.action_editMemo:
                Intent memoEditIntent = new Intent(MemoPage.this, EditMemo.class);
                memoEditIntent.putExtra("edit_memo_location", tv_toolbarTitle.getText().toString());
                memoEditIntent.putExtra("edit_memo_address", getIntent().getStringExtra("memo_address"));
                Log.d("memo_address", getIntent().getStringExtra("memo_address"));
                String image_exis = getIntent().getStringExtra("memo_image");
                memoEditIntent.putExtra("edit_memo_content", tv_memoContent.getText().toString());
                memoEditIntent.putExtra("edit_memo_request", requestPage);
                String[] strArr_allImage;
                String str_allImage = "";

                if(image_exis.equals("no"))
                {
                    memoEditIntent.putExtra("edit_memo_id", intent.getStringExtra("memo_id"));
                   memoEditIntent.putExtra("memo_image", "noImage");
                    memoEditIntent.putExtra("edit_memo_allImages", "noImage");
                } else if(image_exis.equals("yes"))
                {
                    memoEditIntent.putExtra("memo_image", "yesImage");
                    memoEditIntent.putExtra("edit_memo_imageArr", str_filePath);
                    memoEditIntent.putExtra("edit_memo_id", intent.getStringExtra("memo_id"));
                    strArr_allImage = getIntent().getStringArrayExtra("memo_allImages");


                    if(!str_allImage.equals("no"))
                    {
                        for (int i = 0; i < strArr_allImage.length; i++) {
                            str_allImage = str_allImage + strArr_allImage[i] + "|";
                        }
                    }
                    memoEditIntent.putExtra("edit_memo_allImages", str_allImage);
                }
                memoEditIntent.putExtra("edit_memo_date", tv_memoDate.getText().toString());
                memoEditIntent.putExtra("edit_memo_x", memoX);
                memoEditIntent.putExtra("edit_memo_y", memoY);
                startActivity(memoEditIntent);
                return true;
            case android.R.id.home:
                Log.d("requestPage22", requestPage);
                if(requestPage.equals("MemoListPage"))
                {
                    Intent memoListIntent = new Intent(MemoPage.this, MemoListPage.class);
                    memoListIntent.putExtra("memo_location", tv_toolbarTitle.getText().toString());
                    memoListIntent.putExtra("added_locationX", memoX);
                    Log.d("added_locationXzzz", String.valueOf(memoX));
                    memoListIntent.putExtra("added_locationY", memoY);
                    memoListIntent.putExtra("memo_address", getIntent().getStringExtra("memo_address"));
                    startActivity(memoListIntent);
                } else if(requestPage.equals("MyMemoPage")) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("select name, address, memo, photo, memotime, coordinate_x, coordinate_y, _id from location_memo" , null);
                    memoCount = cursor.getCount();
                    memoName = new String[memoCount];
                    memoAdress = new String[memoCount];
                    memoMemo = new String[memoCount];
                    memoPhoto = new String[memoCount];
                    memoMemotime = new String[memoCount];
                    memoX2 = new String[memoCount];
                    memoY2 = new String[memoCount];
                    memoId2 = new String[memoCount];

                    int index = 0;
                    while(cursor.moveToNext())
                    {
                        memoName[index] = cursor.getString(0);
                        memoAdress[index] = cursor.getString(1);
                        memoMemo[index] = cursor.getString(2);
                        memoMemotime[index] = cursor.getString(4);
                        memoPhoto[index] = cursor.getString(3);
                        memoX2[index] = cursor.getString(5);
                        memoY2[index] = cursor.getString(6);
                        memoId2[index] = cursor.getString(7);
                        index++;
                    }
                    Intent myMemoIntent = new Intent(MemoPage.this, MyMemo.class);
                    myMemoIntent.putExtra("memoName",memoName);
                    myMemoIntent.putExtra("memoAdress",memoAdress);
                    myMemoIntent.putExtra("memoPhoto",memoPhoto);
                    myMemoIntent.putExtra("memoContent", memoMemo);
                    myMemoIntent.putExtra("memoMemotime",memoMemotime);
                    myMemoIntent.putExtra("memoX",memoX2);
                    myMemoIntent.putExtra("memoY",memoY2);
                    myMemoIntent.putExtra("memoId", memoId2);
                    myMemoIntent.putExtra("memoCount",memoCount);
                    startActivity(myMemoIntent);
                }
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
