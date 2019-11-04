package com.example.findintermediateapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gigamole.library.ShadowLayout;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ZoomControlView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends ChangeStateBar implements OnMapReadyCallback {
    private MapView mapView;
    View et_inputLocation;
    LinearLayout ll_input;
    LocationButtonView myLocation_btn;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        int colorValue = Color.parseColor("#cccccc");

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_aa);

        final ShadowLayout shadowLayout = (ShadowLayout)findViewById(R.id.toolbar_shadow);
        shadowLayout.setIsShadowed(true);
        shadowLayout.setShadowAngle(5);
        shadowLayout.setShadowRadius(50);
        shadowLayout.setShadowDistance(-5);
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
                if(!hasFocus){

                }else{
                    Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(searchIntent);
                }
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.input_location:
                        Intent searchIntent2 = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(searchIntent2);
                        break;
                }
            }
        };

        et_inputLocation.setOnClickListener(clickListener);
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

        myLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "마커 1 클릭", Toast.LENGTH_SHORT).show();
            }
        });
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

    public String getNaverSearch(String keyword) {

        String clientID = "xSSmW1XTfHB1VKB6JMMH";
        String clientSecret = "BPOCBux60g";
        StringBuffer sb = new StringBuffer();
        try {
            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/local.xml?query=" + text + "&display=10" + "&start=1";

            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", clientID);
            conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            String tag;
            //inputStream으로부터 xml값 받기
            xpp.setInput(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기

                        if (tag.equals("item")) ; //첫번째 검색 결과
                        else if (tag.equals("title")) {
                            sb.append("제목 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("description")) {
                            sb.append("내용 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            return e.toString();
        }
        return sb.toString();
    }

}
