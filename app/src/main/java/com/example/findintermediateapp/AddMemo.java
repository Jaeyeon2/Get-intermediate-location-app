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
import android.os.AsyncTask;
import android.os.Build;
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

import javax.annotation.Nonnull;

import type.CreateTodoInput;

import static com.example.findintermediateapp.MainActivity.androidId;

public class AddMemo extends ChangeStateBar {
    TextView tv_location;
    TextView tv_address;
    Intent x;
    static Bitmap[] addedBitmap = new Bitmap[100];
    String y;
    String addr;
    public int addcount = 1;
    Bitmap[] memo_work = new Bitmap[addcount];
    private static Uri filePath;
    public static Uri[] filePathArray = new Uri[100];
    public  Uri[] addedfilePath = new Uri[addcount];
    ImageAdapter imageAdapter;
    GridView gridView;
    EditText et_memoContent;
    private static final int REQUEST_CODE = 0;
    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
    MemoImagesDbHelper imagesDbHelper = new MemoImagesDbHelper(this);
    private AWSAppSyncClient mAWSAppSyncClient;
    private AppSyncSubscriptionCall subscriptionWatcher;
    DynamoDBMapper dynamoDBMapper;
    String convertedBitmap;
    String location_name;
    public static final int KITKAT_VALUE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        AWSMobileClient.getInstance().initialize(this).execute();
        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();

        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();

        tv_location = findViewById(R.id.min);
        tv_address = findViewById(R.id.jae);
        et_memoContent = findViewById(R.id.add_memo_content);
        x = getIntent();
        y = x.getStringExtra("location");
        addr = x.getStringExtra("address");
        tv_location.setText(y);
        tv_address.setText(addr);
        imageAdapter = new ImageAdapter(AddMemo.this, this, memo_work);

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

    private GraphQLCall.Callback<CreateTodoMutation.Data> mutationCallback = new GraphQLCall.Callback<CreateTodoMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateTodoMutation.Data> response) {
            Log.i("Results", "Added Todo");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("Error", e.toString());
        }
    };

    public void runQuery() {
        mAWSAppSyncClient.query(ListTodosQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(todosCallback);
    }

    private GraphQLCall.Callback<ListTodosQuery.Data> todosCallback = new GraphQLCall.Callback<ListTodosQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListTodosQuery.Data> response) {
            Log.i("Results", response.data().listTodos().items().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };

    private void subscribe() {
        OnCreateTodoSubscription subscription = OnCreateTodoSubscription.builder().build();
        subscriptionWatcher = mAWSAppSyncClient.subscribe(subscription);
        subscriptionWatcher.execute(subCallback);
    }

    private AppSyncSubscriptionCall.Callback subCallback = new AppSyncSubscriptionCall.Callback() {
        @Override
        public void onResponse(@Nonnull Response response) {
            Log.i("Response", response.data().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("Error", e.toString());
        }

        @Override
        public void onCompleted() {
            Log.i("Completed", "Subscription completed");
        }
    };

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
                    final int takeFlags = data.getFlags() &
                    (Intent.FLAG_GRANT_READ_URI_PERMISSION|
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    addcount++;
                    filePath = data.getData();
                    InputStream in = getContentResolver().openInputStream(filePath);
                    Log.d("filePath", String.valueOf(filePath));
                    Bitmap img;
                    img = BitmapFactory.decodeStream(in);
                    in.close();
                    memo_work = new Bitmap[addcount];
                    Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.add_work_no2);
                    filePathArray[addcount -1 ] = filePath;
                    addedBitmap[addcount - 1] = img;
                    for (int i = 0; i < addcount; i++) {
                        Log.d("addedBitmap", String.valueOf(addedBitmap[i]));
                        if (i == addcount - 1) {
                            memo_work[i] = bitmap;
                        } else {
                            memo_work[i] = addedBitmap[i + 1];
                        }
                    }
                    imageAdapter = new ImageAdapter(this, AddMemo.this, memo_work);
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
        Bitmap[] memo_image = null;

        public ImageAdapter(Activity activity, Context con, Bitmap[] work) {
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
            ImageView imageView;

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;

            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(screenWidth / 3 - 10, screenWidth / 3 - 10));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(10, 10, 10, 10);
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
                imageView.setImageBitmap(memo_image[pos]);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
            return imageView;
        }
    }

    public void addMemo() {

        location_name = tv_location.getText().toString();
        String location_address = tv_address.getText().toString();
        String memo_content = et_memoContent.getText().toString();
        String location_x = getIntent().getStringExtra("mapx");
        String location_y = getIntent().getStringExtra("mapy");
        String str_allRegPhoto = "";
        SQLiteDatabase imageDb = imagesDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        SQLiteStatement p = imageDb.compileStatement("insert into memo_image_table(name, photo) values(?, ?)");
        for (int i = 0; i < addcount; i++) {
            Log.d("addedBitmap", String.valueOf(addedBitmap[i]));
        }
        if (addcount == 2) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            addedBitmap[1].compress(Bitmap.CompressFormat.JPEG, 100, stream);
            convertedBitmap = getBase64String(addedBitmap[1]);
            cv.put("name", location_name);
            cv.put("photo",String.valueOf(filePathArray[1]));
            Log.d("filePathArray[1]",String.valueOf(filePathArray[1]));
            imageDb.insert("memo_image_table", null, cv);
        } else if (addcount > 2) {
            for (int i = 0; i < addcount - 1; i++) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                addedBitmap[i + 1].compress(Bitmap.CompressFormat.PNG, 0, stream);
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

        Intent mainIntent = new Intent(AddMemo.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    public void createMemoImage() {
        final MemoDO memoItem = new MemoDO();
        memoItem.setAndroidId(androidId);
        memoItem.setImage(convertedBitmap);
        memoItem.setLocation(location_name);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(memoItem);
            }
        }).start();
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
//JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("image", "ss");
                jsonObject.accumulate("androidId", androidId);
                jsonObject.accumulate("location", location_name);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
//URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
//연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송


                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

//서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
//버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

//서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("error"))
            {

            }
            else if(result.equals("addMemoSuccess"))
            {

            }
        }
    }
}
