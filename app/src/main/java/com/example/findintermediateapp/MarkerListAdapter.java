package com.example.findintermediateapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;

import static com.example.findintermediateapp.MainActivity.fifthMarker;
import static com.example.findintermediateapp.MainActivity.firstMarker;
import static com.example.findintermediateapp.MainActivity.fourthMarker;
import static com.example.findintermediateapp.MainActivity.secondMarker;
import static com.example.findintermediateapp.MainActivity.sf;
import static com.example.findintermediateapp.MainActivity.thirdMarker;

public class MarkerListAdapter extends RecyclerView.Adapter<MarkerListAdapter.ViewHolder> {

    Context context;
    private ArrayList<MarkerListItem> markerData = null;
    MainActivity activity_main = new MainActivity();
    NaverMap naverMap;

    // 생성자에서 데이터 리스트 객체를 전달받음
    MarkerListAdapter(ArrayList<MarkerListItem> list) {
        markerData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @Override
    public MarkerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View view = inflater.inflate(R.layout.inserted_marker_list_item, parent, false);
        MarkerListAdapter.ViewHolder vh = new MarkerListAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(MarkerListAdapter.ViewHolder holder, int position) {
        MarkerListItem item = markerData.get(position);
        String markerNum = String.valueOf(item.getMarkerNum());
        //holder.tv_markerNum.setText(markerNum);
        holder.tv_markerLocation.setText(item.getMarkerLocation());

        holder.iv_markerDelete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                markerData.remove(position);
                notifyDataSetChanged();
                Log.d("pos", String.valueOf(position));
                SharedPreferences.Editor editor = sf.edit();
                if(position == 0) {
                    firstMarker.setMap(null);
                    Log.d("position00", String.valueOf(position));
                    editor.putString("firstMapX", "0");
                    editor.putString("firstMapY", "0");
                    editor.putInt("count", (sf.getInt("count", 0)-1));
                    editor.commit();
                }
                else if(position == 1) {
                    secondMarker.setMap(null);
                    Log.d("position11", String.valueOf(position));
                    editor.putString("secondMapX", "0");
                    editor.putString("secondMapY", "0");
                    editor.putInt("count", (sf.getInt("count", 0)-1));
                    editor.commit();
                }
                else if(position == 2) {
                    thirdMarker.setMap(null);
                    Log.d("position22", String.valueOf(position));
                    editor.putString("thirdMapX", "0");
                    editor.putString("thirdMapY", "0");
                    editor.putInt("count", (sf.getInt("count", 0)-1));
                    editor.commit();
                }
                else if(position == 3) {
                    fourthMarker.setMap(null);
                    Log.d("position33", String.valueOf(position));
                    editor.putString("fourthMapX", "0");
                    editor.putString("fourthMapY", "0");
                    editor.putInt("count", (sf.getInt("count", 0)-1));
                    editor.commit();
                }
                else if(position == 4) {
                    fifthMarker.setMap(null);
                    Log.d("position44", String.valueOf(position));
                    editor.putString("fifthMapX", "0");
                    editor.putString("fifthMapY", "0");
                    editor.putInt("count", (sf.getInt("count", 0)-1));
                    editor.commit();
                }
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴
    @Override
    public int getItemCount() { return markerData.size(); }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_markerNum;
        TextView tv_markerLocation;
        ImageView iv_markerDelete;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    MarkerListItem item = markerData.get(pos);
                    if(pos != RecyclerView.NO_POSITION) {
                    }
                }
            });
            // 뷰 객체에 대한 참조. (hold strong reference)
            //tv_markerNum = itemView.findViewById(R.id.marker_num);
            tv_markerLocation = itemView.findViewById(R.id.marker_location);
            iv_markerDelete = itemView.findViewById(R.id.marker_delete);
        }
    }

}
