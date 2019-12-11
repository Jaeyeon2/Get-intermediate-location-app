package wap.example.findintermediateapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import wap.example.findintermediateapp.R;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;

public class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.ViewHolder> {

    Activity memoListActivity;
    Context context;
    private ArrayList<MemoListItem> memoData = null;

    // 생성자에서 데이터 리스트 객체를 전달받음
    MemoListAdapter(Activity activity, ArrayList<MemoListItem> list) {
        this.memoListActivity = activity;
        memoData = list;
    }

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
        String str_cuttingContent;
        if(item.getMemoContent().equals("noContent"))
        {
            holder.tv_memoContent.setVisibility(INVISIBLE);
        } else {
            if(item.getMemoContent().length() > 39)
            {
                str_cuttingContent = item.getMemoContent().substring(0,37);
                str_cuttingContent = str_cuttingContent + " ···";
                holder.tv_memoContent.setText(str_cuttingContent);

            } else {
                holder.tv_memoContent.setText(item.getMemoContent());
            }
        }

        holder.tv_memoImageCount.setText(String.valueOf(item.getMemoImageCount()));
        holder.tv_memoTime.setText(item.getMemoDate());
        Log.d("item.getMemoDate", item.getMemoDate());
        Uri imageUri;
        if (item.getMemoFirstImage().equals("noImage")) {
            Resources resources = context.getResources();
            imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(R.drawable.no_image) + '/' + resources.getResourceTypeName(R.drawable.no_image) + '/' + resources.getResourceEntryName(R.drawable.no_image));
            Glide
                    .with(context)
                    .load(imageUri)
                    .apply(new RequestOptions().circleCrop())
                    .into(holder.iv_memoImage);

        } else {
            imageUri = Uri.parse(item.getMemoFirstImage());
            Glide
                    .with(context)
                    .load(imageUri)
                    .apply(new RequestOptions().circleCrop())
                    .into(holder.iv_memoImage);
        }
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
                        memoPageIntent.putExtra("memo_address", item.getMemoAddress());
                        memoPageIntent.putExtra("memo_content", item.getMemoContent());
                        memoPageIntent.putExtra("memo_date", item.getMemoDate());
                        memoPageIntent.putExtra("memo_id", String.valueOf(item.getMemoId()));
                        memoPageIntent.putExtra("memo_x", item.getMemoX());
                        memoPageIntent.putExtra("memo_y", item.getMemoY());
                        memoPageIntent.putExtra("request_page", "MemoListPage");
                        Log.d("memo_id", String.valueOf(item.getMemoId()));

                        if(!item.getMemoFirstImage().equals("noImage")) {
                            memoPageIntent.putExtra("memo_allImages", item.getMemoAllImage());
                            memoPageIntent.putExtra("memo_image","yes");
                        }else {
                            memoPageIntent.putExtra("memo_allImages", "noImage");
                            memoPageIntent.putExtra("memo_image", "no");
                        }
                        context.startActivity(memoPageIntent);
                        memoListActivity.finish();
                }
            });

            tv_memoContent = itemView.findViewById(R.id.memo_item_content);
            tv_memoImageCount = itemView.findViewById(R.id.memo_item_imageCount);
            iv_memoImage = itemView.findViewById(R.id.memo_item_image);
            tv_memoTime = itemView.findViewById(R.id.memo_item_date);
        }
    }


}
