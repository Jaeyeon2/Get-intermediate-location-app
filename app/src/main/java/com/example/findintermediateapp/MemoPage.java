package com.example.findintermediateapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MemoPage extends AppCompatActivity {

    TextView tv_memoContent;
    TextView tv_memoLocation;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_page);
        intent = getIntent();

        tv_memoContent = findViewById(R.id.memo_content);
        tv_memoLocation = findViewById(R.id.memo_location);
        tv_memoContent.setText(intent.getStringExtra("memo_content"));
        tv_memoLocation.setText(intent.getStringExtra("memo_location"));
    }
}
