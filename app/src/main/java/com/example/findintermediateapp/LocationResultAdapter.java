package com.example.findintermediateapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LocationResultAdapter extends RecyclerView.Adapter<LocationResultAdapter.ViewHolder> {

    Context context;
    private ArrayList<LocationResultItem> ResultData = null;

    // 생성자에서 데이터 리스트 객체를 전달받음
    LocationResultAdapter(ArrayList<LocationResultItem> list)
    {
        ResultData = list;
    }
    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public LocationResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.location_result_list_item, parent, false);
        LocationResultAdapter.ViewHolder vh = new LocationResultAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(LocationResultAdapter.ViewHolder holder, int position) {

        LocationResultItem item = ResultData.get(position);
        holder.tv_resultName.setText(item.getResultName());
        holder.tv_resultLocation.setText(item.getResultLocation());

    }

    // getItemCount() - 전체 데이터 갯수 리턴
    @Override
    public int getItemCount() {
        return ResultData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_resultName;
        TextView tv_resultLocation;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    LocationResultItem item = ResultData.get(pos);
                    if(pos != RecyclerView.NO_POSITION) {

                        Intent mainIntent = new Intent(context, MainActivity.class);

                        item.setResultNumberIncre();

                        mainIntent.putExtra("resultNumber", String.valueOf(item.getResultNumber()));
                        mainIntent.putExtra("user_location", "true");
                        mainIntent.putExtra("location_mapx", item.getResultMapx());
                        mainIntent.putExtra("location_mapy", item.getResultMapy());



                        Log.d("location_mapx", item.getResultMapx());
                        Log.d("location_mapy", item.getResultMapy());

                        context.startActivity(mainIntent);
                     }
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            tv_resultName = itemView.findViewById(R.id.location_name);
            tv_resultLocation = itemView.findViewById(R.id.location_address);

        }
    }
}
