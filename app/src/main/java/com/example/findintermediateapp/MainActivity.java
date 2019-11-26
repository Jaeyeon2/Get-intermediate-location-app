package com.example.findintermediateapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.gigamole.library.ShadowLayout;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ZoomControlView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.findintermediateapp.MemoImagesDatabase.MemoImages.TABLE_NAME;

public class MainActivity extends ChangeStateBar implements OnMapReadyCallback {
    private MapView mapView;
    private Cursor mCursor;
    public static Marker firstMarker;
    public static Marker secondMarker;
    public static Marker thirdMarker;
    public static Marker fourthMarker;
    public static Marker fifthMarker;
    public static Marker firstMarkerImage;
    public static Marker addMemoMarker;
    public static String[] photoArr;
//    public static Marker savedMarker;
    public static String androidId;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    View et_inputLocation;
    LinearLayout ll_input;
    LocationButtonView myLocation_btn;
    String user_location = "false";
    String location_mapx;
    String location_mapy;
    Intent intent;
    double user_mapX;
    double user_mapY;
    int locationNum;
    int markerCount;
    public static SharedPreferences sf;
    int dataCount = 0;
    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
    MemoImagesDbHelper imagesDbHelper = new MemoImagesDbHelper(this);
    TextView tv_markerLocation;
    Cursor cursor;
    SQLiteDatabase db;
    public String regPhoto;


    LinearLayout ll_markerDelete;
    RecyclerView markerRecyclerView = null;
    MarkerListAdapter markerListAdapter = null;
    ArrayList<MarkerListItem> markerList = new ArrayList<MarkerListItem>();

    FrameLayout.LayoutParams params;
    ImageView img;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    RelativeLayout rl_clickedLocation;
    DynamoDBMapper dynamoDBMapper;
    DynamoDBMapperConfig config;
    String tempName;
    public Bitmap decodedBitmap;
    String allMemoImages = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("device androidId", androidId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("kafivuks0k"));
        myLocation_btn = findViewById(R.id.myLocation_btn);
        et_inputLocation = findViewById(R.id.input_location);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();
        rl_clickedLocation = findViewById(R.id.clicked_location_name);
        ll_markerDelete = findViewById(R.id.marker_delete_layout);
        tv_markerLocation = findViewById(R.id.marker_location);
        addMemoMarker = new Marker();
//        savedMarker = new Marker();
        img = findViewById(R.id.marker_image);
        params = (FrameLayout.LayoutParams)img.getLayoutParams();

        Bitmap bitmap_ex = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        String bitmap_ste = getBase64String(bitmap_ex);
        Log.d("bitmap_str", bitmap_ste);

        ll_markerDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemoMarker.setPosition(new LatLng(0, 0));
                addMemoMarker.setMap(null);
                rl_clickedLocation.setVisibility(INVISIBLE);
            }
        });

        int colorValue = Color.parseColor("#a3a3a3");
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_aa);

        final ShadowLayout shadowLayout = (ShadowLayout)findViewById(R.id.toolbar_shadow);
        shadowLayout.setIsShadowed(true);
        shadowLayout.setShadowAngle(5);
        shadowLayout.setShadowRadius(25);
        shadowLayout.setShadowDistance(0);
        shadowLayout.setShadowColor(colorValue);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        mapView.getMapAsync(naverMap -> {
            myLocation_btn.setMap(naverMap);
            myLocation_btn.setOnClickListener(overlay -> {
                Toast.makeText(this, "마커 1 클릭", Toast.LENGTH_SHORT).show();
            });

        });

        et_inputLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                            Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                            startActivity(searchIntent);
                        }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            Intent searchIntent2 = new Intent(MainActivity.this, SearchActivity.class);
                            startActivity(searchIntent2);
            }
        };

        addMemoMarker.setOnClickListener(overlay -> {
            Intent addMemoIntent = new Intent(MainActivity.this, AddMemo.class);
            addMemoIntent.putExtra("location", getIntent().getStringExtra("location_name"));
            addMemoIntent.putExtra("address", getIntent().getStringExtra("location_address"));
            addMemoIntent.putExtra("mapx", getIntent().getStringExtra("location_mapx"));
            addMemoIntent.putExtra("mapy", getIntent().getStringExtra("location_mapy"));
            startActivity(addMemoIntent);
            return true;
        });
        et_inputLocation.setOnClickListener(clickListener);
        intent = getIntent();

        if(intent.getStringExtra("location_mapx") != null)
        {
            Log.d("location_mapx111", intent.getStringExtra("location_mapx"));
        }

        /*
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.NAME, "부경대");
        values.put(FeedReaderContract.FeedEntry.ADDRESS, "부산광역시 남구 대연동");
        values.put(FeedReaderContract.FeedEntry.MEMO, "내 대학교");
        values.put(FeedReaderContract.FeedEntry.PHOTO, "ss");
        values.put(FeedReaderContract.FeedEntry.COORDINATE_X, "35.134067");
        values.put(FeedReaderContract.FeedEntry.COORDINATE_Y, "129.103179");

        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    */
        /*

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.NAME, "울산대학교");
        values.put(FeedReaderContract.FeedEntry.ADDRESS, "93 대학로 무거동 남구 울산광역시");
        values.put(FeedReaderContract.FeedEntry.MEMO, "울산대울산대");
        values.put(FeedReaderContract.FeedEntry.PHOTO, "");
        values.put(FeedReaderContract.FeedEntry.COORDINATE_X, "35.543895");
        values.put(FeedReaderContract.FeedEntry.COORDINATE_Y, "129.256328");
        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
         */

       // db에서 데이터 삭제
/*
       SQLiteDatabase db = dbHelper.getWritableDatabase();
       db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, null, null);
       SQLiteDatabase imageDb = imagesDbHelper.getWritableDatabase();
       imageDb.delete(TABLE_NAME, null, null);
 */
    }

    public void myLocationOnClick(View view) {
        Toast.makeText(this, "마커 1 클릭", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_search:
                return true;
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(false);
        uiSettings.setZoomControlEnabled(false);
        uiSettings.setIndoorLevelPickerEnabled(false);
        uiSettings.setLocationButtonEnabled(false);

        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.None);
        NaverMapOptions options = new NaverMapOptions()
                .locationButtonEnabled(false);

        // 초기 카메라위치 조정
        CameraPosition earlyCameraPosition = new CameraPosition(new LatLng(36.456943, 127.829456), 6);
        CameraUpdate earlyCameraUpdate = CameraUpdate.toCameraPosition(earlyCameraPosition);
        naverMap.moveCamera(earlyCameraUpdate);
        myLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "마커 1 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        int cursorCount = 0;
        SQLiteDatabase imageDb = imagesDbHelper.getWritableDatabase();
        Cursor imageCursor = imageDb.rawQuery("select name, photo from memo_image_table", null);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name, address, memo, photo, coordinate_x, coordinate_y from location_memo" , null);
        Log.d("cursor.getCount()", String.valueOf(cursor.getCount()));
                while(cursor.moveToNext()){
                    cursorCount++;
                    Log.d("cursor.moveToNext()실행", String.valueOf(cursorCount));
                    tempName = cursor.getString(0);
                Log.d("tempNameasd", tempName);
                String memo_location = cursor.getString(0);
                String tempAddress = cursor.getString(1);
                String tempMemo = cursor.getString(2);
                String tempPhoto = cursor.getString(3);
                    Bitmap bitmap2 = null;
                    if(tempPhoto.equals(""))
                {
                    bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.add_work_no2);
                }
                else {
                    String[] eachPhoto = tempPhoto.split("\\|");
                    Uri imageUri = Uri.parse(eachPhoto[0]);

                    try {
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                    String tempX = cursor.getString(4);
                String tempY = cursor.getString(5);
                Double x = Double.valueOf(tempX);
                Double y = Double.valueOf(tempY);
                    GeoTransPoint oKA = new GeoTransPoint(x, y);
                    GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);
                    double lat_x = oGeo.getY();
                    double lng_y = oGeo.getX();

                    Marker savedMarker;
                    savedMarker = new Marker();

                    savedMarker.setPosition(new LatLng(lat_x, lng_y));
                    ImageView iv_marker = new ImageView(this);

                    Bitmap marker_size = BitmapFactory.decodeResource(getResources(), R.drawable.location_marker);
                    marker_size = Bitmap.createScaledBitmap(marker_size, 225, 225, true);

                    bitmap2 = Bitmap.createScaledBitmap(bitmap2, 150, 150, true);
                    Bitmap photo = getCroppedBitmap(bitmap2);

                    Canvas canvas = new Canvas(marker_size);
                    canvas.drawBitmap(photo, 38, 18, null);
                    iv_marker.setImageBitmap(marker_size);

                    savedMarker.setIcon(OverlayImage.fromView(iv_marker));
                    savedMarker.setMap(naverMap);


                    savedMarker.setOnClickListener(overlay -> {

                        Intent memoPageIntent = new Intent(MainActivity.this, MemoPage.class);
                        memoPageIntent.putExtra("memo_location",memo_location);
                        memoPageIntent.putExtra("memo_content", tempMemo);
                        if(!tempPhoto.equals("")) {
                            memoPageIntent.putExtra("memo_allImages", tempPhoto);
                        }else {
                            memoPageIntent.putExtra("memo_allImages", "noImage");
                        }
                        startActivity(memoPageIntent);
                        return true;
                    });
        }

        if(intent.getStringExtra("user_location") == null)
        {
            sf = getSharedPreferences("sFile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sf.edit();
            editor.putInt("count", 0);
            markerCount = sf.getInt("count", 0);
            editor.commit();
            Log.d("위치 선택전 markerCount", String.valueOf(markerCount));

        }
        else{
            markerCount = sf.getInt("count", 0);
            Log.d("위치 선택후 markerCount", String.valueOf(markerCount));

            // 카텍좌표계 -> 경위도 좌표계 변환
            GeoTransPoint oKA = new GeoTransPoint(Double.valueOf(intent.getStringExtra("location_mapx")), Double.valueOf(intent.getStringExtra("location_mapy")));
            GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);
            double lat = oGeo.getY();
            double lng = oGeo.getX();

            user_mapX = lat;
            user_mapY = lng;

            int data_count = 0;

            Cursor cursor2;
            cursor2 = db.query(
                    FeedReaderContract.FeedEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            boolean existingLocation = false;

            while(cursor2.moveToNext()) {
                data_count = data_count + 1;
                Log.d("data_count", String.valueOf(data_count));
                String tempName = cursor2.getString(cursor2.getColumnIndex("name"));
                Double tempX = Double.valueOf(cursor2.getString(cursor2.getColumnIndex("coordinate_x")));
                Double tempY = Double.valueOf(cursor2.getString(cursor2.getColumnIndex("coordinate_y")));
                if(tempName.equals(intent.getStringExtra("location_name")))
                {
                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(tempX, tempY));
                    naverMap.moveCamera(cameraUpdate);
                    existingLocation = true;
                    break;
                }
            }
            if(existingLocation == false && data_count == cursorCount){
                Log.d("해당위치에 존재하는 메모가 업습니다.", "1");
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(user_mapX, user_mapY));
                naverMap.moveCamera(cameraUpdate);
                ImageView iv_addMemoMarker = new ImageView(this);
                addMemoMarker.setPosition(new LatLng(user_mapX, user_mapY));
                Bitmap smallMarker = BitmapFactory.decodeResource(getResources(), R.drawable.location_plus_marker4);
                smallMarker = Bitmap.createScaledBitmap(smallMarker, 200, 200, true);
                iv_addMemoMarker.setImageBitmap(smallMarker);
                addMemoMarker.setIcon(OverlayImage.fromView(iv_addMemoMarker));
                addMemoMarker.setMap(naverMap);
                tv_markerLocation.setText(getIntent().getStringExtra("location_name"));
                rl_clickedLocation.setVisibility(VISIBLE);
            }

            if(cursor.getCount() == 0)
            {
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(user_mapX, user_mapY));
                naverMap.moveCamera(cameraUpdate);
                ImageView iv_addMemoMarker = new ImageView(this);
                addMemoMarker.setPosition(new LatLng(user_mapX, user_mapY));
                Bitmap smallMarker = BitmapFactory.decodeResource(getResources(), R.drawable.location_plus_marker4);
                smallMarker = Bitmap.createScaledBitmap(smallMarker, 200, 200, true);
                iv_addMemoMarker.setImageBitmap(smallMarker);
                addMemoMarker.setIcon(OverlayImage.fromView(iv_addMemoMarker));
                addMemoMarker.setMap(naverMap);
                tv_markerLocation.setText(getIntent().getStringExtra("location_name"));
                rl_clickedLocation.setVisibility(VISIBLE);
            }

            /*
            if(markerCount == 1) {
                CameraUpdate cameraUpdate1 = CameraUpdate.scrollTo(new LatLng(lat, lng));
                naverMap.moveCamera(cameraUpdate1);
                // 첫번째 마커 찍기
                firstMarker = new Marker();
                firstMarkerImage = new Marker();
                firstMarker.setZIndex(1);
                firstMarkerImage.setZIndex(0);
                addMarkerList(markerCount, intent.getStringExtra("location_name"));
                markerListAdapter.notifyDataSetChanged();

                firstMarker.setPosition(new LatLng(user_mapX, user_mapY));
                firstMarkerImage.setPosition(new LatLng(user_mapX+0.000005, user_mapY));

                editor.putString("markerName1", intent.getStringExtra("location_name"));
                editor.putInt("markerNum1", markerCount);
                editor.putString("firstMapX", String.valueOf(user_mapX));
                editor.putString("firstMapY", String.valueOf(user_mapY));
                editor.commit();

                ImageView iv_marker = new ImageView(this);
                CircleImageView iv_image = new CircleImageView(this);
                //img = findViewById(R.id.marker_image);

                Bitmap size = BitmapFactory.decodeResource(getResources(), R.drawable.location_marker);
                size = Bitmap.createScaledBitmap(size, 225, 225, true);
                iv_marker.setImageBitmap(size);

                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.image);
                image = Bitmap.createScaledBitmap(image, 150, 150, true);
                iv_image.setImageBitmap(image);
                Bitmap photo = getCroppedBitmap(image);

                Canvas canvas = new Canvas(size);
                canvas.drawBitmap(photo, 38, 18, null);



                params.width = metrics.widthPixels / 10;
                params.height= metrics.widthPixels / 10;
                img.setLayoutParams(params);

                img.setImageResource(R.drawable.image);
                img.setScaleType(ImageView.ScaleType.FIT_XY);


                firstMarker.setIcon(OverlayImage.fromView(iv_marker));
//                firstMarkerImage.setIcon(OverlayImage.fromView(iv_image));
                firstMarker.setMap(naverMap);
//                firstMarkerImage.setMap(naverMap);
              }
              else if(markerCount == 2) {
                  Log.d("markerCount2", String.valueOf(markerCount));
                double firstMapX = Double.valueOf(sf.getString("firstMapX", "0"));
                double firstMapY = Double.valueOf(sf.getString("firstMapY", "0"));
                addMarkerList(sf.getInt("marKerNum1", 1), sf.getString("markerName1", "0"));
                addMarkerList(markerCount, intent.getStringExtra("location_name"));
                Log.d("sf.getInt(\"marKerNum1\", 0)", String.valueOf(sf.getInt("marKerNum1", 0)));
                markerListAdapter.notifyDataSetChanged();
                editor.putString("markerName2", intent.getStringExtra("location_name"));
                editor.putInt("markerNum2", markerCount);
                editor.putString("secondMapX", String.valueOf(user_mapX));
                editor.putString("secondMapY", String.valueOf(user_mapY));
                editor.commit();
                Log.d("firstMapX", sf.getString("firstMapX", "0"));
                firstMarker = new Marker();
                firstMarker.setPosition(new LatLng(firstMapX, firstMapY));
                firstMarker.setMap(naverMap);

                CameraUpdate cameraUpdate2 = CameraUpdate.scrollTo(new LatLng(lat, lng));
                naverMap.moveCamera(cameraUpdate2);
                // 두번째 마커 찍기
                secondMarker = new Marker();
                secondMarker.setPosition(new LatLng(user_mapX, user_mapY));
                secondMarker.setMap(naverMap);
            }
             else if(markerCount == 3) {
                double firstMapX = Double.valueOf(sf.getString("firstMapX", "0"));
                double firstMapY = Double.valueOf(sf.getString("firstMapY", "0"));
                double secondMapX = Double.valueOf(sf.getString("secondMapX", "0"));
                double secondMapY = Double.valueOf(sf.getString("secondMapY", "0"));
                addMarkerList(sf.getInt("markerNum1",1), sf.getString("markerName1", "0"));
                addMarkerList(sf.getInt("markerNum2", 2), sf.getString("markerName2", "0"));
                addMarkerList(markerCount, intent.getStringExtra("location_name"));
                markerListAdapter.notifyDataSetChanged();

                editor.putString("thirdMapX", String.valueOf(user_mapX));
                editor.putString("thirdMapY", String.valueOf(user_mapY));
                editor.putInt("markerNum3", markerCount);
                editor.putString("markerName3", intent.getStringExtra("location_name"));
                editor.commit();

                Log.d("firstMapX", sf.getString("firstMapX", "0"));
                firstMarker = new Marker();
                firstMarker.setPosition(new LatLng(firstMapX, firstMapY));
                firstMarker.setMap(naverMap);
                secondMarker = new Marker();
                secondMarker.setPosition(new LatLng(secondMapX, secondMapY));
                secondMarker.setMap(naverMap);

                CameraUpdate cameraUpdate3 = CameraUpdate.scrollTo(new LatLng(lat, lng));
                naverMap.moveCamera(cameraUpdate3);
                // 세번째 마커 찍기
                thirdMarker = new Marker();
                thirdMarker.setPosition(new LatLng(user_mapX, user_mapY));
                thirdMarker.setMap(naverMap);
            }
             else if(markerCount == 4) {
                double firstMapX = Double.valueOf(sf.getString("firstMapX", "0"));
                double firstMapY = Double.valueOf(sf.getString("firstMapY", "0"));
                double secondMapX = Double.valueOf(sf.getString("secondMapX", "0"));
                double secondMapY = Double.valueOf(sf.getString("secondMapY", "0"));
                double thirdMapX = Double.valueOf(sf.getString("thirdMapX", "0"));
                double thirdMapY = Double.valueOf(sf.getString("thirdMapY", "0"));
                addMarkerList(sf.getInt("markerNum1",1), sf.getString("markerName1", "0"));
                addMarkerList(sf.getInt("markerNum2", 2), sf.getString("markerName2", "0"));
                addMarkerList(sf.getInt("markerNum3", 3), sf.getString("markerName3", "0"));
                addMarkerList(markerCount, intent.getStringExtra("location_name"));
                markerListAdapter.notifyDataSetChanged();
                editor.putString("fourthMapX", String.valueOf(user_mapX));
                editor.putString("fourthMapY", String.valueOf(user_mapY));
                editor.putInt("markerNum4", markerCount);
                editor.putString("markerName4", intent.getStringExtra("location_name"));
                editor.commit();
                Log.d("firstMapX", sf.getString("firstMapX", "0"));
                firstMarker = new Marker();
                firstMarker.setPosition(new LatLng(firstMapX, firstMapY));
                firstMarker.setMap(naverMap);
                secondMarker = new Marker();
                secondMarker.setPosition(new LatLng(secondMapX, secondMapY));
                secondMarker.setMap(naverMap);
                thirdMarker = new Marker();
                thirdMarker.setPosition(new LatLng(thirdMapX, thirdMapY));
                thirdMarker.setMap(naverMap);

                CameraUpdate cameraUpdate4 = CameraUpdate.scrollTo(new LatLng(lat, lng));
                naverMap.moveCamera(cameraUpdate4);
                // 네번째 마커 찍기
                fourthMarker = new Marker();
                fourthMarker.setPosition(new LatLng(user_mapX, user_mapY));
                fourthMarker.setMap(naverMap);
            }
             else if(markerCount == 5)
             {
                 double firstMapX = Double.valueOf(sf.getString("firstMapX", "0"));
                 double firstMapY = Double.valueOf(sf.getString("firstMapY", "0"));
                 double secondMapX = Double.valueOf(sf.getString("secondMapX", "0"));
                 double secondMapY = Double.valueOf(sf.getString("secondMapY", "0"));
                 double thirdMapX = Double.valueOf(sf.getString("thirdMapX", "0"));
                 double thirdMapY = Double.valueOf(sf.getString("thirdMapY", "0"));
                 double fourthMapX = Double.valueOf(sf.getString("fourthMapX", "0"));
                 double fourthMapY = Double.valueOf(sf.getString("fourthMapY", "0"));
                 addMarkerList(sf.getInt("markerNum1",1), sf.getString("markerName1", "0"));
                 addMarkerList(sf.getInt("markerNum2", 2), sf.getString("markerName2", "0"));
                 addMarkerList(sf.getInt("markerNum3", 3), sf.getString("markerName3", "0"));
                 addMarkerList(sf.getInt("markerNum4", 4), sf.getString("markerName4", "0"));
                 addMarkerList(markerCount, intent.getStringExtra("location_name"));
                 markerListAdapter.notifyDataSetChanged();
                 editor.putString("fifthMapX", String.valueOf(user_mapX));
                 editor.putString("fifthMapY", String.valueOf(user_mapY));
                 editor.putInt("markerNum5", markerCount);
                 editor.putString("markerName5", intent.getStringExtra("location_name"));
                 editor.commit();
                 Log.d("firstMapX", sf.getString("firstMapX", "0"));
                 firstMarker = new Marker();
                 firstMarker.setPosition(new LatLng(firstMapX, firstMapY));
                 firstMarker.setMap(naverMap);
                 secondMarker = new Marker();
                 secondMarker.setPosition(new LatLng(secondMapX, secondMapY));
                 secondMarker.setMap(naverMap);
                 thirdMarker = new Marker();
                 thirdMarker.setPosition(new LatLng(thirdMapX, thirdMapY));
                 thirdMarker.setMap(naverMap);
                 fourthMarker = new Marker();
                 fourthMarker.setPosition(new LatLng(fourthMapX, fourthMapY));
                 fourthMarker.setMap(naverMap);

                 CameraUpdate cameraUpdate5 = CameraUpdate.scrollTo(new LatLng(lat, lng));
                 naverMap.moveCamera(cameraUpdate5);
                 // 다섯번째 마커 찍기
                 fifthMarker = new Marker();
                 fifthMarker.setPosition(new LatLng(user_mapX, user_mapY));
                 fifthMarker.setMap(naverMap);
             }
             */
        }
    }

    public void addMarkerList(int num, String location)
    {
        MarkerListItem item = new MarkerListItem();
        item.setMarkerNum(num);
        item.setMarkerLocation(location);

        markerList.add(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            markerCount = 0;
            SharedPreferences.Editor editor = sf.edit();
            editor.clear();
            editor.commit();
           ActivityCompat.finishAffinity(this);
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public String getBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    /*
    public void readMemoImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                MemoDO memoItem = dynamoDBMapper.load(
                        MemoDO.class,
                        androidId,
                        tempName
                        );

                Log.d("Memo Item : ", memoItem.toString());
                regPhoto = memoItem.getImage();
                byte[] decodedByteArray = Base64.decode(regPhoto, Base64.NO_WRAP);
                decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            }
        }).start();
    }

     */
}
