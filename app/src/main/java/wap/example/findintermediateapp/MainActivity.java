package wap.example.findintermediateapp;

import wap.example.findintermediateapp.R;
import com.facebook.ads.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.NativeActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.ads.AudienceNetworkAds;
import com.gigamole.library.ShadowLayout;
import com.naver.maps.geometry.LatLng;
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
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends ChangeStateBar implements OnMapReadyCallback {
    private String TAG = NativeActivity.class.getSimpleName();
    private NativeAd nativeAd;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;

    private MapView mapView;
    private Cursor mCursor;
    NaverMap naverMap;
    Marker myLocationMarker;
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
    Bitmap tempBitmap = null;
    View et_inputLocation;
    LinearLayout ll_input;
    String user_location = "false";
    String location_mapx;
    String location_mapy;
    Intent intent;
    String address;
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
    public int display_x;
    public int display_y;


    LinearLayout ll_markerDelete;
    RecyclerView markerRecyclerView = null;
    MarkerListAdapter markerListAdapter = null;
    ArrayList<MarkerListItem> markerList = new ArrayList<MarkerListItem>();

    FrameLayout.LayoutParams params;
    ImageView img;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    RelativeLayout rl_clickedLocation;
    String tempName;
    public Bitmap decodedBitmap;
    String allMemoImages = "";
    String[] memoName;
    String[] memoAdress;
    String[] memoMemo;
    String[] memoPhoto;
    String[] memoMemotime;
    String[] memoX;
    String[] memoY;
    String[] memoId;
    int memoCount = 0;

    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    int display_width;
    int display_height;
    DisplayMetrics displayMetrics;
    double screenInches;

//    LocationManager mLM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // db에서 데이터 삭제
        //SQLiteDatabase db1 = dbHelper.getWritableDatabase();
        //db1.delete(FeedReaderContract.FeedEntry.TABLE_NAME, null, null);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);



        AudienceNetworkAds.initialize(this);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        double y = Math.pow(screenHeight /displayMetrics.xdpi, 2);
        double x = Math.pow(screenWidth /displayMetrics.xdpi, 2);
        screenInches = Math.sqrt(x + y);
        screenInches = (double) Math.round(screenInches *10)/10;
        Log.d(TAG, "screenWidth : " + screenWidth + ", screenHeight : " + screenHeight + ", screenInches" + screenInches);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name, address, memo, photo, memotime, coordinate_x, coordinate_y, _id from location_memo" , null);
        memoCount = cursor.getCount();
        memoName = new String[memoCount];
        memoAdress = new String[memoCount];
        memoMemo = new String[memoCount];
        memoPhoto = new String[memoCount];
        memoMemotime = new String[memoCount];
        memoX = new String[memoCount];
        memoY = new String[memoCount];
        memoId = new String[memoCount];
        myLocationMarker = new Marker();

        int index = 0;
        while(cursor.moveToNext())
        {
         memoName[index] = cursor.getString(0);
         memoAdress[index] = cursor.getString(1);
         memoMemo[index] = cursor.getString(2);
         memoMemotime[index] = cursor.getString(4);
         memoPhoto[index] = cursor.getString(3);
         memoX[index] = cursor.getString(5);
         memoY[index] = cursor.getString(6);
         memoId[index] = cursor.getString(7);
         index++;
        }

        for(int i = 0; i < memoCount; i++)
        {
            Log.d("memoName[index]", memoName[i]);
        }

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("device androidId", androidId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("kafivuks0k"));

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
                myLocationMarker.setMap(null);
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

        final ShadowLayout location_shadowLayout = (ShadowLayout)findViewById(R.id.myLocation_shadow);
        location_shadowLayout.setIsShadowed(true);
        location_shadowLayout.setShadowAngle(5);
        location_shadowLayout.setShadowRadius(20);
        location_shadowLayout.setShadowDistance(0);
        location_shadowLayout.setShadowColor(colorValue);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        mapView.getMapAsync(naverMap -> {

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
            addMemoIntent.putExtra("request_page", "MainActivity");
            startActivity(addMemoIntent);
            return true;
        });
        et_inputLocation.setOnClickListener(clickListener);
        intent = getIntent();
        if(intent.getStringExtra("location_mapx") != null)
        {
            Log.d("location_mapx111", intent.getStringExtra("location_mapx"));

        }

    }

    public void myLocationOnClick(View view) {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck!= PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this,"현재위 치 메모기능을 사용하기 위해 위치 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                Toast.makeText(this,"현재 위치 메모기능을 사용하기 위해 위치 권한이 필요합니다.",Toast.LENGTH_LONG).show();

            }
        }
        gpsTracker = new GpsTracker(MainActivity.this);

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        Log.d("latitudezzz", String.valueOf(latitude));
        address = getCurrentAddress(latitude, longitude);
        String[] str_address = address.split(" ");
        address = "";
        for(int i = 1; i < str_address.length; i++)
        {
            address = address + str_address[i] + " " ;

            if(i == str_address.length-1)
            {
                if(address.equals("미발견 "))
                {
                    if (!checkLocationServicesStatus()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("위치 서비스 비활성화");
                        builder.setMessage("현재위치에 메모를 추가하기 위해서는 위치 권한 설정이 필요합니다.\n"
                                + "위치 설정을 수정하실래요?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent
                                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
                            }
                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        builder.create().show();
                    }else {
                        Log.d("address111", address);
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("주소 미발견");
                        builder.setMessage("현재 위치를 불러오는데 실패하였습니다. ");
                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();

                        checkRunTimePermission();
                    }
                } else {
                    Log.d("address11", address);
                    showAlertDialog(latitude, longitude);
                }
            }
        }

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
                Intent myMemoIntent = new Intent(MainActivity.this, MyMemo.class);

                myMemoIntent.putExtra("memoName",memoName);
                myMemoIntent.putExtra("memoAdress",memoAdress);
                myMemoIntent.putExtra("memoPhoto",memoPhoto);
                myMemoIntent.putExtra("memoContent", memoMemo);
                myMemoIntent.putExtra("memoMemotime",memoMemotime);
                myMemoIntent.putExtra("memoX",memoX);
                myMemoIntent.putExtra("memoY",memoY);
                myMemoIntent.putExtra("memoId", memoId);
                myMemoIntent.putExtra("memoCount",memoCount);
                startActivity(myMemoIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
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
        if(getIntent().getStringExtra("added_locationX") != null)
        {
            Double x = Double.valueOf(getIntent().getStringExtra("added_locationX"));
            Double y = Double.valueOf(getIntent().getStringExtra("added_locationY"));
            GeoTransPoint oKA = new GeoTransPoint(x, y);
            GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);
            Double added_x = oGeo.getY();
            Double added_y = oGeo.getX();

            CameraPosition earlyCameraPosition = new CameraPosition(new LatLng(added_x, added_y), 12);
            CameraUpdate earlyCameraUpdate = CameraUpdate.toCameraPosition(earlyCameraPosition);
            naverMap.moveCamera(earlyCameraUpdate);
        } else {

            CameraPosition earlyCameraPosition = new CameraPosition(new LatLng(36.456943, 127.829456), 6);
            CameraUpdate earlyCameraUpdate = CameraUpdate.toCameraPosition(earlyCameraPosition);
            naverMap.moveCamera(earlyCameraUpdate);
        }
        int cursorCount = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name, address, memo, photo, memotime, coordinate_x, coordinate_y from location_memo" , null);
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
                Log.d("tempPhoto22", tempPhoto);
                    String tempDate = cursor.getString(4);
                    String tempX = String.valueOf(cursor.getString(5));
                    String tempY = String.valueOf(cursor.getString(6));
                Bitmap bitmap2 = null;
                    Uri imageUri = null;
                    boolean existingPhoto = false;
                    if(tempPhoto.equals("noImage")) {
                        for(int i = 0; i < memoName.length; i++)
                        {
                            if(memoName[i].equals(tempName))
                            {
                                if(!memoPhoto[i].equals("noImage"))
                                {
                                    existingPhoto = true;
                                }
                            }

                            if(i == memoName.length-1)
                            {
                                if(existingPhoto == false)
                                {
                                    Resources resources = getApplicationContext().getResources();
                                    imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(R.drawable.memo5) + '/' + resources.getResourceTypeName(R.drawable.memo5) + '/' + resources.getResourceEntryName(R.drawable.memo5));
                                }
                            }
                        }
                 //       bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.add_work_no2);
                    }
                else {
                    String[] eachPhoto = tempPhoto.split("\\|");
                    imageUri = Uri.parse(eachPhoto[0]);
                    Log.d("imageUrizzz", String.valueOf(imageUri));
                    Log.d("eachPhoto[0]zzz", eachPhoto[0]);
                }
                Glide.with(getApplicationContext()).asBitmap().load(imageUri).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Bitmap bitmap2 = resource;
                        double lat_x;
                        double lng_y;
                        if (tempX.contains(".")) {
                            lat_x = Double.valueOf(tempX);
                            lng_y = Double.valueOf(tempY);
                        } else {
                        Double x = Double.valueOf(tempX);
                        Double y = Double.valueOf(tempY);
                        GeoTransPoint oKA = new GeoTransPoint(x, y);
                        GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, oKA);
                        lat_x = oGeo.getY();
                        lng_y = oGeo.getX();
                    }
                        Marker savedMarker;
                        savedMarker = new Marker();

                        savedMarker.setPosition(new LatLng(lat_x, lng_y));
                        ImageView iv_marker = new ImageView(MainActivity.this);

                        Bitmap marker_size;
                        Bitmap photo;
                        Canvas canvas;

                        if(screenInches < 5.8) {
                            marker_size = BitmapFactory.decodeResource(getResources(), R.drawable.location_marker);
                            marker_size = Bitmap.createScaledBitmap(marker_size, 225, 225, true);
                            bitmap2 = Bitmap.createScaledBitmap(bitmap2, 150, 150, true);
                            photo = getCroppedBitmap(bitmap2);
                            canvas = new Canvas(marker_size);
                            canvas.drawBitmap(photo, 38, 18, null);

                        } else {
                            marker_size = BitmapFactory.decodeResource(getResources(), R.drawable.location_marker);
                            marker_size = Bitmap.createScaledBitmap(marker_size, 165, 165, true);
                            bitmap2 = Bitmap.createScaledBitmap(bitmap2, 113, 113, true);
                            photo = getCroppedBitmap(bitmap2);
                            canvas = new Canvas(marker_size);
                            canvas.drawBitmap(photo, 27, 11, null);

                        }

                        iv_marker.setImageBitmap(marker_size);
                        savedMarker.setIcon(OverlayImage.fromView(iv_marker));
                        savedMarker.setMap(naverMap);


                        savedMarker.setOnClickListener(overlay -> {
                            Intent memoListIntent = new Intent(MainActivity.this, MemoListPage.class);
                            memoListIntent.putExtra("memo_location", memo_location);
                            memoListIntent.putExtra("added_locationX", String.valueOf(tempX));
                            memoListIntent.putExtra("added_locationY", String.valueOf(tempY));
                            memoListIntent.putExtra("memo_address", tempAddress);
                            startActivity(memoListIntent);
                            return true;
                        });

                    }
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

                    CameraPosition existingCameraPosition = new CameraPosition(new LatLng(user_mapX, user_mapY), 12);
                    CameraUpdate existingCamemraUpdate = CameraUpdate.toCameraPosition(existingCameraPosition);
                    naverMap.moveCamera(existingCamemraUpdate);
                    existingLocation = true;
                    break;
                }
            }
            if(existingLocation == false && data_count == cursorCount){
                Log.d("해당위치에 존재하는 메모가 업습니다.", "1");
//                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(user_mapX, user_mapY), 5);
//                naverMap.moveCamera(cameraUpdate);

                CameraPosition addCameraPosition = new CameraPosition(new LatLng(user_mapX, user_mapY), 12);
                CameraUpdate addCamemraUpdate = CameraUpdate.toCameraPosition(addCameraPosition);
                naverMap.moveCamera(addCamemraUpdate);

                ImageView iv_addMemoMarker = new ImageView(this);
                addMemoMarker.setPosition(new LatLng(user_mapX, user_mapY));
                Bitmap smallMarker = BitmapFactory.decodeResource(getResources(), R.drawable.location_plus_marker8);
                if(screenInches < 5.8)
                {
                    smallMarker = Bitmap.createScaledBitmap(smallMarker, 225, 225, true);
                } else {
                    smallMarker = Bitmap.createScaledBitmap(smallMarker, 165, 165, true);
                }
                iv_addMemoMarker.setImageBitmap(smallMarker);
                addMemoMarker.setIcon(OverlayImage.fromView(iv_addMemoMarker));
                addMemoMarker.setMap(naverMap);
                tv_markerLocation.setText(getIntent().getStringExtra("location_name"));
                rl_clickedLocation.setVisibility(VISIBLE);
            }

            if(cursor.getCount() == 0)
            {
                CameraPosition addCameraPosition = new CameraPosition(new LatLng(user_mapX, user_mapY), 12);
                CameraUpdate addCamemraUpdate = CameraUpdate.toCameraPosition(addCameraPosition);
                naverMap.moveCamera(addCamemraUpdate);

                ImageView iv_addMemoMarker = new ImageView(this);
                addMemoMarker.setPosition(new LatLng(user_mapX, user_mapY));
                Bitmap smallMarker = BitmapFactory.decodeResource(getResources(), R.drawable.location_plus_marker8);
                if(screenInches < 5.8)
                {
                    smallMarker = Bitmap.createScaledBitmap(smallMarker, 225, 225, true);
                } else {
                    smallMarker = Bitmap.createScaledBitmap(smallMarker, 165, 165, true);
                }
                iv_addMemoMarker.setImageBitmap(smallMarker);
                addMemoMarker.setIcon(OverlayImage.fromView(iv_addMemoMarker));
                addMemoMarker.setMap(naverMap);
                tv_markerLocation.setText(getIntent().getStringExtra("location_name"));
                rl_clickedLocation.setVisibility(VISIBLE);
            }
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
            loadNativeAd();
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

    void showAlertDialog(Double latitude, Double longitude)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(address);
        builder.setMessage("현재 위치에 메모를 추가 하시겠습니까?");
        builder.setPositiveButton("예",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                addMemoMarker.setMap(null);
                myLocationMarker.setMap(null);

                CameraPosition addMemoCameraPosition = new CameraPosition(new LatLng(latitude, longitude), 12);
                CameraUpdate addMemoCameraUpdate = CameraUpdate.toCameraPosition(addMemoCameraPosition);
                naverMap.moveCamera(addMemoCameraUpdate);
                boolean existingLocation = false;

                for(int i = 0; i < memoX.length; i++) {
                    Log.d("memoXsss", memoX[i]);
                    if(latitude.equals(memoX[i]))
                    {
                     existingLocation = true;
                    }
                    if(i == memoX.length-1)
                    {
                        if(existingLocation==true)
                        {

                        } else {
                            ImageView iv_myLocation = new ImageView(MainActivity.this);
                            Bitmap myLocationBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_plus_marker8);
                            if(screenInches < 5.8)
                            {
                                myLocationBitmap = Bitmap.createScaledBitmap(myLocationBitmap, 225, 225, true);
                            } else {
                                myLocationBitmap = Bitmap.createScaledBitmap(myLocationBitmap, 165, 165, true);
                            }
                            iv_myLocation.setImageBitmap(myLocationBitmap);
                            myLocationMarker.setPosition(new LatLng(latitude, longitude));
                            myLocationMarker.setIcon(OverlayImage.fromView(iv_myLocation));
                            myLocationMarker.setMap(naverMap);

                            myLocationMarker.setOnClickListener(overlay -> {
                                Intent addMemoIntent = new Intent(MainActivity.this, AddMemo.class);
                                addMemoIntent.putExtra("location", "MyLocation");
                                addMemoIntent.putExtra("address", address);
                                addMemoIntent.putExtra("mapx", String.valueOf(latitude));
                                addMemoIntent.putExtra("mapy", String.valueOf(longitude));
                                addMemoIntent.putExtra("request_page", "MainActivity");
                                startActivity(addMemoIntent);
                                return true;
                            });

                            rl_clickedLocation.setVisibility(VISIBLE);
                            tv_markerLocation.setText(address);
                        }
                    }
                }

                if(memoCount == 0)
                {
                    ImageView iv_myLocation = new ImageView(MainActivity.this);
                    Bitmap myLocationBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_plus_marker8);
                    if(screenInches < 5.8)
                    {
                        myLocationBitmap = Bitmap.createScaledBitmap(myLocationBitmap, 225, 225, true);
                    } else {
                        myLocationBitmap = Bitmap.createScaledBitmap(myLocationBitmap, 165, 165, true);
                    }
                    iv_myLocation.setImageBitmap(myLocationBitmap);
                    myLocationMarker.setPosition(new LatLng(latitude, longitude));
                    myLocationMarker.setIcon(OverlayImage.fromView(iv_myLocation));
                    myLocationMarker.setMap(naverMap);

                    myLocationMarker.setOnClickListener(overlay -> {
                        Intent addMemoIntent = new Intent(MainActivity.this, AddMemo.class);
                        addMemoIntent.putExtra("location", "MyLocation");
                        addMemoIntent.putExtra("address", address);
                        addMemoIntent.putExtra("mapx", String.valueOf(latitude));
                        addMemoIntent.putExtra("mapy", String.valueOf(longitude));
                        addMemoIntent.putExtra("request_page", "MainActivity");
                        startActivity(addMemoIntent);
                        return true;
                    });

                    rl_clickedLocation.setVisibility(VISIBLE);
                    tv_markerLocation.setText(address);
                }
            }
        });
        builder.setNegativeButton("아니요",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {
                //위치 값을 가져올 수 있음
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission(){
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeAd = new NativeAd(this, "577938649687815");

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }

                inflateAd(nativeAd);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                nativeAd.downloadMedia();
                // Inflate Native Ad into Container
                inflateAd(nativeAd);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });

        // Request an ad
        //nativeAd.loadAd();
        nativeAd.loadAd(NativeAdBase.MediaCacheFlag.NONE);
    }

    private void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(MainActivity.this, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        /*
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);

         */
    }


}
