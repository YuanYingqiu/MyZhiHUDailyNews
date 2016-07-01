package com.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.bean.EventBean.EventLoadNewsContent;
import com.example.bean.NewsContentBean;
import com.example.bean.OutputStoriesBean;
import com.example.db.NewsContentDao;
import com.example.http.OkHttpManager;
import com.example.utils.CommonUtil;
import com.example.view.MyMaterialProgressDrawable;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class DailyNewsContentActivity extends AppCompatActivity {

    public static final String KEY_ID = "key_id";

    private static final String link = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news.css\" />";
    private static final String src = "file:///android_asset/css/";
    @Bind(R.id.wb)
    WebView web;
    @Bind(R.id.iv_progress)
    ImageView ivProgress;
    @Bind(R.id.fl_progress_root)
    FrameLayout flProgressRoot;
    private MyMaterialProgressDrawable mProgressDrawable;
    private NewsContentDao dao;

    private int mType;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_news_content);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        dao = NewsContentDao.getInstance(DailyNewsContentActivity.this);

        initWebView();
        initProgress();
        initData();

    }

    private void initData() {
        OutputStoriesBean mNews = getIntent().getExtras().getParcelable(KEY_ID);
        if (mNews == null) {
            CommonUtil.showToast("打开姿势有待提高");
            return;
        }

        mType = mNews.getType();
        mId = mNews.getId();

        Logger.d("type:"+mType);

        NewsContentBean newsContent = dao.loadNewsContent(mId);

        if (mType == 1) {

            String shareUrl = newsContent.getShare_url();
            if(shareUrl == null || shareUrl.equals("")){
                loadDataFromServer();
            }else {
                showSharedNews(newsContent.getShare_url());
            }

        } else {

            String content = newsContent.getBody();
            if (content == null || content.equals("")) {
                loadDataFromServer();
            } else {
                showNews(content);
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        web.destroy();
        EventBus.getDefault().unregister(this);
    }

    private void loadDataFromServer() {
        String url = Const.URL_DAILY_NEWS_CONTENT + mId;


        startProgress();

        OkHttpManager.getInstance().getAsync(url, new OkHttpManager.ResultCallback<String>() {
            @Override
            public void onError(Call call, IOException e) {
                EventBus.getDefault().post(new EventLoadNewsContent(Const.CODE_ERROR, "网络异常", null));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    NewsContentBean bean = new Gson().fromJson(json, NewsContentBean.class);
                    dao.saveNewsContent(mId, bean);
                    EventBus.getDefault().post(new EventLoadNewsContent(Const.CODE_SUCCESS, null, bean));

                } catch (Exception e) {
                    EventBus.getDefault().post(new EventLoadNewsContent(Const.CODE_FAILED, "服务器连接异常", null));
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadNewsContent(EventLoadNewsContent content) {

        stopProgress();

        if (content.getEventCode() == Const.CODE_SUCCESS) {
            NewsContentBean bean = content.getBean();

            if(mType == 1){
                showSharedNews(bean.getShare_url());
            }else {
                showNews(bean.getBody());
            }
        } else {
            CommonUtil.showToast(content.getEventMsg());
        }
    }


    private void initProgress() {
        mProgressDrawable = new MyMaterialProgressDrawable(
                DailyNewsContentActivity.this, ivProgress);

        mProgressDrawable.setAlpha(255);
        ivProgress.setImageDrawable(mProgressDrawable);
    }

    private void initWebView() {
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }


    private void startProgress() {
        mProgressDrawable.start();
        flProgressRoot.setVisibility(View.VISIBLE);

        ScaleAnimation sa = new ScaleAnimation(
                0f, 1.0f, 0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(200);
        sa.setFillAfter(true);
        flProgressRoot.setAnimation(sa);

    }

    private void stopProgress() {

        ScaleAnimation sa = new ScaleAnimation(
                1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(200);
        sa.setFillAfter(true);
        flProgressRoot.setAnimation(sa);

        flProgressRoot.setVisibility(View.GONE);
        mProgressDrawable.stop();
    }

    private void showNews(String htmlData) {
        if(TextUtils.isEmpty(htmlData)){
            CommonUtil.showToast("出现了未知的错误");
            return;
        }
        if (htmlData.contains("img-place-holder")) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) web.getLayoutParams();
            params.topMargin = (-1) * (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            web.setLayoutParams(params);
        }


        web.loadDataWithBaseURL(src, link + htmlData, "text/html", "utf-8", null);
    }

    private void showSharedNews(String shareUrl){
        web.loadUrl(shareUrl);
    }


}
