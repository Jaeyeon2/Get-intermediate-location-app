package com.example.findintermediateapp;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MyMemoAdapter extends RecyclerView.Adapter<MyMemoAdapter.MyViewHolder>{

    Context context;
    ArrayList<Item> list;


    public MyMemoAdapter(Context context, ArrayList<Item> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
     //   holder.image.setImageResource(list.get(position).image);
        Uri uri = Uri.parse(list.get(position).image);
        holder.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions().centerCrop())
                .into(holder.image);

        ImageClickListener imageClickListener = new ImageClickListener(context, uri);
        holder.image.setOnClickListener(imageClickListener);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        view.setLayoutParams(new GridView.LayoutParams(screenWidth/3 -10, screenWidth / 3 -10 ));

        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recylcerview_row_image);

        }
    }

}
