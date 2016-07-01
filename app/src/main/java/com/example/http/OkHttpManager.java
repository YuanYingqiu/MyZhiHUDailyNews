package com.example.http;

import android.content.Context;

import com.example.App;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttpManager {
    private static OkHttpManager manager;
    private Context context;
    private OkHttpManager(){}
    private OkHttpManager(Context context) {
        this.context = context;
    }

    public synchronized static OkHttpManager getInstance(Context context) {
        if (manager == null) {
            synchronized (OkHttpManager.class) {
                manager = new OkHttpManager();
            }
        }
        return manager;
    }

    public synchronized static OkHttpManager getInstance() {
        if (manager == null) {
            synchronized (OkHttpManager.class) {
                manager = new OkHttpManager();
            }
        }
        return manager;
    }


    public static OkHttpClient client = new OkHttpClient();

    public void getAsync(String url, final ResultCallback<String> callback) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }


    public interface ResultCallback<String> {
        void onError(Call call, IOException e);

        void onResponse(Call call, Response response) throws IOException;


    }


}
