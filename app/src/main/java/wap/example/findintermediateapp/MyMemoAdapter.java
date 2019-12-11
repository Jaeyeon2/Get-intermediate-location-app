package wap.example.findintermediateapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import wap.example.findintermediateapp.R;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;

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
        Log.d("list.get(position).image", list.get(position).image);
        String str_cuttingContent;
        if(list.get(position).image.equals("noImage"))
        {
            holder.image.setVisibility(INVISIBLE);
            if(list.get(position).content.length() > 28)
            {
                str_cuttingContent = list.get(position).content.substring(0,26);
                str_cuttingContent = str_cuttingContent + " ···";
                holder.content.setText(str_cuttingContent);
            } else {
                holder.content.setText(list.get(position).content);
            }

        }
        holder.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions().centerCrop())
                .into(holder.image);

        //ImageClickListener imageClickListener = new ImageClickListener(context, uri, list.get(position).allImage);
        //holder.image.setOnClickListener(imageClickListener);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] allImage = list.get(position).allImage.split("\\|");
                Intent memoPageIntent = new Intent(context, MemoPage.class);
                memoPageIntent.putExtra("memo_location",list.get(position).location);
                memoPageIntent.putExtra("memo_address", list.get(position).address);
                memoPageIntent.putExtra("memo_content", list.get(position).content);
                memoPageIntent.putExtra("memo_date", list.get(position).date);
                memoPageIntent.putExtra("memo_id", list.get(position).id);
                memoPageIntent.putExtra("memo_x", list.get(position).x);
                memoPageIntent.putExtra("memo_y", list.get(position).y);
                memoPageIntent.putExtra("request_page", "MyMemoPage");

                Log.d("list.get(position).allImage", list.get(position).allImage);

                if(list.get(position).image.equals("noImage")) {
                    memoPageIntent.putExtra("memo_allImages", "noImage");
                    memoPageIntent.putExtra("memo_image", "no");
                }else {
                    memoPageIntent.putExtra("memo_allImages", allImage);
                    memoPageIntent.putExtra("memo_image", "yes");
                }
                context.startActivity(memoPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        holder.content.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String[] allImage = list.get(position).allImage.split("\\|");
                Intent memoPageIntent = new Intent(context, MemoPage.class);
                memoPageIntent.putExtra("memo_location",list.get(position).location);
                memoPageIntent.putExtra("memo_address", list.get(position).address);
                memoPageIntent.putExtra("memo_content", list.get(position).content);
                memoPageIntent.putExtra("memo_date", list.get(position).date);
                memoPageIntent.putExtra("memo_id", list.get(position).id);
                memoPageIntent.putExtra("memo_x", list.get(position).x);
                memoPageIntent.putExtra("memo_y", list.get(position).y);
                memoPageIntent.putExtra("request_page", "MyMemoPage");

                if(list.get(position).image.equals("noImage")) {
                    memoPageIntent.putExtra("memo_allImages", "noImage");
                    memoPageIntent.putExtra("memo_image", "no");
                }else {
                    memoPageIntent.putExtra("memo_allImages", allImage);
                    memoPageIntent.putExtra("memo_image", "yes");
                }
                context.startActivity(memoPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
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
        TextView content;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recylcerview_row_image);
            content = itemView.findViewById(R.id.recylcerview_row_text);
        }
    }

}
