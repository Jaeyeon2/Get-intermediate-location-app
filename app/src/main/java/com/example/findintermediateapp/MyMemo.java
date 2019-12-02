package com.example.findintermediateapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

public class MyMemo extends AppCompatActivity {

    RecyclerView recyclerView;
    MyMemoAdapter adapter;
    GridLayoutManager layoutManager;

    ArrayList<Item> list = new ArrayList<Item>();

    Uri[] uri ;
    String[] mName;
    String[] mAdress;
    String[] mPhoto;
    String[] mMemoTime;
    String[] mMemox;
    String[] mMemoy;
    int mCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_memo);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
       mCount = getIntent().getIntExtra("memoCount", 0);

       mName = new String[mCount];
       mAdress = new String[mCount];
       mPhoto = new String[mCount];
       mMemoTime = new String[mCount];
       mMemox = new String[mCount];
       mMemoy = new String[mCount];


        mName = getIntent().getStringArrayExtra("memoName");
        mAdress = getIntent().getStringArrayExtra("memoAdress");
        mPhoto = getIntent().getStringArrayExtra("memoPhoto");
        mMemoTime = getIntent().getStringArrayExtra("memoMemoTime");
        mMemox = getIntent().getStringArrayExtra("memoX");
        mMemoy = getIntent().getStringArrayExtra("memoY");

        String[] first_img=new String[mCount];


        for(int i =0; mPhoto.length>i; i++){
            String[]mString = mPhoto[i].split("\\|");
            first_img[i]=mString[0];
        }

        for(int i = 0; mPhoto.length>i; i++){
            Item item = new Item(first_img[i]);
            list.add(item);
        }

        recyclerView = (RecyclerView)findViewById(R.id.myMemo_recyclerView);
        adapter = new MyMemoAdapter(getApplicationContext(), list);

       recyclerView.setLayoutManager(layoutManager);
       recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}