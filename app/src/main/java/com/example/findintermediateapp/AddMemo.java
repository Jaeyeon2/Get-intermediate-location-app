package com.example.findintermediateapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AddMemo extends AppCompatActivity {
    String addedLocation;
    TextView tv_addedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        addedLocation = getIntent().getStringExtra("location");
        tv_addedLocation = findViewById(R.id.add_page_location);

        tv_addedLocation.setText(addedLocation);

    }
}
