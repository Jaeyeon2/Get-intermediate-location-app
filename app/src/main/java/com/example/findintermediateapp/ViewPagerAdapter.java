package com.example.findintermediateapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ViewPagerAdapter extends PagerAdapter {

    LayoutInflater inflater;
    Uri[] uri_image;
    Context context;

    public ViewPagerAdapter(LayoutInflater inflater, Uri[] uri) {
        context = inflater.getContext();
        this.inflater = inflater;
        this.uri_image = uri;
    }
    @Override
    public int getCount() {
        return (null != uri_image) ? uri_image.length : 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // TODO Auto-generated method stub
        View view=null;
        //새로운 View 객체를 Layoutinflater를 이용해서 생성
        //만들어질 View의 설계는 res폴더>>layout폴더>>viewpater_childview.xml 레이아웃 파일 사용

        view= inflater.inflate(R.layout.viewpager_childview, null);
        //만들어진 View안에 있는 ImageView 객체 참조
        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.
        MemoImageView img= view.findViewById(R.id.img_viewpager_childimage);

        //ImageView에 현재 position 번째에 해당하는 이미지를 보여주기 위한 작업
        //현재 position에 해당하는 이미지를 setting
        Glide.with(context).load(uri_image[position]).into(img);
        //ViewPager에 만들어 낸 View 추가
        container.addView(view);
        //Image가 세팅된 View를 리턴
        return view;
    }

    //화면에 보이지 않은 View는파괴를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View)object);
    }
    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드
    @Override
    public boolean isViewFromObject(View v, Object obj) {
        // TODO Auto-generated method stub
        return v==obj;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}