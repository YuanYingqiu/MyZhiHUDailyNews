package com.example.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bean.OutputStoriesBean;
import com.example.utils.MyImageLoader;

import java.util.ArrayList;
import java.util.List;

public class TopStoriesImageAdapter extends PagerAdapter {

    private List<OutputStoriesBean> mStories;
    private List<ImageView> mNewsImgs;
    private MyImageLoader mImageLoader;
    private Context mContext;

    public TopStoriesImageAdapter(Context context, List<OutputStoriesBean> stories) {
        this.mStories = stories;
        this.mContext = context;
        mNewsImgs = new ArrayList<>();
        mImageLoader = MyImageLoader.getInstance(context);
        initImageView();
    }

    private void initImageView(){
        for (int i = 0 ; i < mStories.size() ; i++){
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mNewsImgs.add(iv);
        }

    }

    @Override
    public int getCount() {
        return mStories.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView ivNews = mNewsImgs.get(position);

        mImageLoader.with(ivNews,mStories.get(position).getImage());

        container.addView(ivNews);
        return ivNews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void notifyDataSetChanged() {
        initImageView();
        super.notifyDataSetChanged();

    }
}
