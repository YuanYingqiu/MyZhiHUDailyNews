package com.example.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.http.OkHttp3Downloader;
import com.example.http.OkHttpManager;
import com.squareup.picasso.Picasso;


public class MyImageLoader {
    private Picasso picasso;
    private MyImageLoader(Context context){
        picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(OkHttpManager.client))
                .build();
    }
    private static MyImageLoader loader;
    public static MyImageLoader getInstance(Context context){
        if(loader == null){
            synchronized (MyImageLoader.class){
                if(loader == null)
                    loader = new MyImageLoader(context);
            }
        }
        return loader;
    }
    public void with(ImageView view,String url) {

        picasso.load(url).into(view);
    }

    public static void loadImage(Context context,ImageView view,String url){
        Picasso.with(context).load(url).into(view);
    }
}
