package com.example.findintermediateapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.ViewHolder> {

    Context context;
    private ArrayList<MemoListItem> memoData = null;

    // 생성자에서 데이터 리스트 객체를 전달받음
    MemoListAdapter(ArrayList<MemoListItem> list) { memoData = list; }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @Override
    public MemoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.memo_list_item, parent, false);

        MemoListAdapter.ViewHolder vh = new MemoListAdapter.ViewHolder(view);
        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(MemoListAdapter.ViewHolder holder, int position) {
        MemoListItem item = memoData.get(position);
        Bitmap bitmap = null;
        holder.tv_memoContent.setText(item.getMemoContent());
        holder.tv_memoImageCount.setText(String.valueOf(item.getMemoImageCount()));
        holder.tv_memoTime.setText(item.getMemoDate());
        Log.d("item.getMemoDate", item.getMemoDate());
        Uri imageUri = Uri.parse(item.getMemoFirstImage());
        Glide
                .with(context)
                .load(imageUri)
                .apply(new RequestOptions().circleCrop())
                .into(holder.iv_memoImage);
    }

    // getItemCount() - 전체 데이터 갯수 리턴
    @Override
    public int getItemCount() {
        return (null != memoData) ? memoData.size() : 0;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_memoContent;
        TextView tv_memoImageCount;
        TextView tv_memoTime;
        ImageView iv_memoImage;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    MemoListItem item = memoData.get(pos);
                        Intent memoPageIntent = new Intent(context, MemoPage.class);
                        memoPageIntent.putExtra("memo_location",item.getMemoLocation());
                        memoPageIntent.putExtra("memo_content", item.getMemoContent());
                        memoPageIntent.putExtra("memo_date", item.getMemoDate());
                        if(!item.getMemoAllImage().equals("")) {
                            memoPageIntent.putExtra("memo_allImages", item.getMemoAllImage());
                        }else {
                            memoPageIntent.putExtra("memo_allImages", "noImage");
                        }
                        context.startActivity(memoPageIntent);
                }
            });

            tv_memoContent = itemView.findViewById(R.id.memo_item_content);
            tv_memoImageCount = itemView.findViewById(R.id.memo_item_imageCount);
            iv_memoImage = itemView.findViewById(R.id.memo_item_image);
            tv_memoTime = itemView.findViewById(R.id.memo_item_date);
        }
    }


}
