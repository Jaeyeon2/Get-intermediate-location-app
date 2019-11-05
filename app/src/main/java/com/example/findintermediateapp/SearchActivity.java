package com.example.findintermediateapp;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.parser.Tag;
import org.jsoup.select.Evaluator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class SearchActivity extends ChangeStateBar {

    TextView tv_searchResult;
    TextView tv_searchResult2;
    EditText et_searchInput;
    String[] title;
    String[] roadAddress;
    int k = 0;
    String str;
    String keyword;
    String searchInput;
    InputMethodManager imm;
    RecyclerView locationRecyclerView = null;
    LocationResultAdapter locationAdapter = null;
    ArrayList<LocationResultItem> resultList = new ArrayList<LocationResultItem>();
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        et_searchInput = findViewById(R.id.searchInput);
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        locationRecyclerView = findViewById(R.id.result_list);
        locationAdapter = new LocationResultAdapter(resultList);
        locationRecyclerView.setAdapter(locationAdapter);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.custom_divider));

        locationRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void addLocation(String name, String location, String mapx, String mapy) {

        LocationResultItem result = new LocationResultItem();
        result.setResultName(name);
        result.setResultLocation(location);
        result.setResultMapx(mapx);
        result.setResultMapy(mapy);

        resultList.add(result);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_search:
                resultList.clear();
                searchNaver(et_searchInput.getText().toString());
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void searchNaver(final String searchObject) { // 검색어 = searchObject로 ;
        final String clientId = "xSSmW1XTfHB1VKB6JMMH";//애플리케이션 클라이언트 아이디값";
        final String clientSecret = "BPOCBux60g";//애플리케이션 클라이언트 시크릿값";
        final int display = 30; // 보여지는 검색결과의 수

        // 네트워크 연결은 Thread 생성 필요
        new Thread() {

            @Override
            public void run() {
                try {
                    String text = URLEncoder.encode(searchObject, "UTF-8");
                    String apiURL = "https://openapi.naver.com/v1/search/local?query=" + text + "&display=" + display + "&"; // json 결과
                    // Json 형태로 결과값을 받아옴.
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    con.connect();
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }

                    StringBuilder searchResult = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        searchResult.append(inputLine + "\n");
                    }
                    br.close();
                    con.disconnect();

                    String data = searchResult.toString();
                    String[] array;
                    array = data.split("\"");
                    String[] title = new String[display];
                    String[] link = new String[display];
                    String[] category = new String[display];
                    String[] description = new String[display];
                    String[] telephone = new String[display];
                    String[] address = new String[display];
                    String[] roadAddress = new String[display];
                    String[] mapx = new String[display];
                    String[] mapy = new String[display];
                    int k = 0;

                    for (int i = 0; i < array.length; i++) {
                        if (array[i].equals("title")) {
                            title[k] = array[i + 2];
                            title[k] = title[k].replace("<b>", "");
                            title[k] = title[k].replace("</b>", "");
                        }
                        if (array[i].equals("link"))
                            link[k] = array[i + 2];
                        if (array[i].equals("category"))
                            category[k] = array[i + 2];
                        if (array[i].equals("description"))
                            description[k] = array[i + 2];
                        if (array[i].equals("telephone"))
                            telephone[k] = array[i + 2];
                        if (array[i].equals("address"))
                            address[k] = array[i + 2];
                        if(array[i].equals("roadAddress"))
                            roadAddress[k] = array[i + 2];
                        if (array[i].equals("mapx"))
                            mapx[k] = array[i + 2];
                        if (array[i].equals("mapy")) {
                            mapy[k] = array[i + 2];
                            k++;
                        }
                    }
                    Log.d("title잘나오니: ",title[0] + title[1] + title[2]);
                    Log.d("k잘나오니: ", String.valueOf(k));
                    Log.d("address잘나오니:",address[0] + address[1] + address[2]);
                    Log.d("roadAddress잘나오니:",roadAddress[0] + roadAddress[1] + roadAddress[2]);
                    for(int i = 0; i < k; i++)
                    {
                        addLocation(title[i], roadAddress[i], mapx[i], mapy[i]);
                        if(i == (k-1))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    locationAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }

                    // title[0], link[0], bloggername[0] 등 인덱스 값에 맞게 검색결과를 변수화하였다.
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();

    }
}


