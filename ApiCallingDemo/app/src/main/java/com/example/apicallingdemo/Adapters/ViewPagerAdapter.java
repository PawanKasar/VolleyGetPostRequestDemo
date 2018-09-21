package com.example.apicallingdemo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.apicallingdemo.R;

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int[] mResources = {
            R.drawable.coc_a,
            R.drawable.coc_b,
            R.drawable.coc_c
    };

    public ViewPagerAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.viewpagerlayout, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_viewpager);
        imageView.setImageResource(mResources[position]);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //container.removeView((LinearLayout) object);
    }
}
