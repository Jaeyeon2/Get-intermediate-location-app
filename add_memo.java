package wap.example.findintermediateapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;


public class add_memo extends AppCompatActivity {
TextView text;
TextView tv_address;
    Intent x;
    Bitmap[] addedBitmap=new Bitmap[100];
    String y;
    String addr;
    public int addcount=1;
    Bitmap[] memo_work=new Bitmap[addcount];
    private static Uri filePath;
    ImageAdapter imageAdapter;
    GridView gridView;
    private static final int REQUEST_CODE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);
    text=findViewById(R.id.min);
    tv_address=findViewById(R.id.jae);
    x=getIntent();
    y=x.getStringExtra("location");
    addr=x.getStringExtra("address");
    text.setText(y);
    tv_address.setText(addr);
    imageAdapter = new ImageAdapter(add_memo.this, this, memo_work);

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
                //db를 만든 후에 작성

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
                    imageAdapter=new ImageAdapter(this,add_memo.this,memo_work);
                    gridView.setAdapter(imageAdapter);
                }catch (Exception e){

                }
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(this,"사진 선택 취소",Toast.LENGTH_LONG).show();
            }
        }
    }


public class ImageAdapter extends BaseAdapter{
        private Context context;

    Activity mActiviy=null;
    Bitmap[] memo_image=null;
    private Integer[] images={R.drawable.dog1,R.drawable.dog2,R.drawable.dog3};

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
                imageView.setLayoutParams(new GridView.LayoutParams(screenWidth/3,screenWidth/3));
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

}
