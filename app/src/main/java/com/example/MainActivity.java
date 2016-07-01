package com.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.bean.EventBean.EventLoadThemes;
import com.example.bean.ThemesItemBean;
import com.example.bean.ThemesItemOutputBean;
import com.example.db.NewsThemeDao;
import com.example.fragment.FragmentDailyNews;
import com.example.fragment.FragmentThemesDailyNews;
import com.example.http.OkHttpManager;
import com.example.interfaces.OnToggleListener;
import com.example.utils.CommonUtil;
import com.example.view.MyTintTextView2;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnToggleListener {

    @Bind(R.id.ll_theme)
    LinearLayout llTheme;
    @Bind(R.id.navigation_layout)
    DrawerLayout navigationLayout;
    @Bind(R.id.mttv_home)
    MyTintTextView2 mttvHome;

    private int mCurrentItemPosition = 0;
    private static final String STATE_CURRENT_ITEM_POSITION = "state_current_item_position";

    private List<MyTintTextView2> mThemesItems = new ArrayList<>();

    private FragmentManager manager;
    private List<ThemesItemOutputBean> mThemes;
    private NewsThemeDao dao;

    private int[] mIcons;
    private boolean isHomeSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //默认为true
        isHomeSelected = true;
        mCurrentItemPosition = -1;

        if (savedInstanceState != null) {

            savedInstanceState.putParcelable("android:support:fragments",null);

            mCurrentItemPosition = savedInstanceState.getInt(STATE_CURRENT_ITEM_POSITION);
            isHomeSelected = savedInstanceState.getBoolean(STATE_HOME_SELECTED);
        }

        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

        //fragment初始化
        manager = getSupportFragmentManager();

        initIcons();
        initData();
        initEvent();

    }

    private void initIcons() {
        mIcons = new int[]{

                R.mipmap.ic_bookmark_icon, R.mipmap.ic_bottomtabbar_discover, R.mipmap.ic_drawer_chat,
                R.mipmap.ic_drawer_roundtable, R.mipmap.ic_eye_white_24dp, R.mipmap.ic_notifications,
                R.mipmap.ic_job,R.mipmap.ic_copy, R.mipmap.ic_flag,
                R.mipmap.ic_intro,R.mipmap.ic_draft_more, R.mipmap.ic_create_read,
                R.mipmap.ic_bottomtabbar_more

        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        dao = NewsThemeDao.getInstace(MainActivity.this);
        mThemes = dao.loadThemes();

        if (mThemes != null && mThemes.size() > 0) {
            proCreateThemesViews();
        } else {
            loadThemesFromServer();
        }
    }

    private void loadThemesFromServer() {
        OkHttpManager manager = OkHttpManager.getInstance();
        manager.getAsync(Const.URL_THEME_ITEM, new OkHttpManager.ResultCallback<String>() {
            @Override
            public void onError(Call call, IOException e) {
                EventBus.getDefault().post(new EventLoadThemes(Const.CODE_ERROR, Const.EVENT_MSG_INTENET_ERROR));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (TextUtils.isEmpty(json)) {
                    EventBus.getDefault().post(new EventLoadThemes(Const.CODE_ERROR, Const.EVENT_MSG_SERVICE_ERROR));
                    return;
                }
                ThemesItemBean newsThemeBean = new Gson().fromJson(json, ThemesItemBean.class);
                dao.saveThemes(newsThemeBean.getOthers());
                EventBus.getDefault().post(new EventLoadThemes(Const.CODE_SUCCESS, null));
            }
        });
    }

    private void proCreateThemesViews() {

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 46, getResources().getDisplayMetrics()));


        for (int i = 0; i < mThemes.size(); i++) {
            createThemesViews(params, mThemes.get(i), i);
        }


        if(!mttvHome.isSelected()){
            if(mCurrentItemPosition != -1){
                mThemesItems.get(mCurrentItemPosition).setSelected(true);

            }

        }

    }


    private void createThemesViews(LinearLayoutCompat.LayoutParams params, ThemesItemOutputBean bean, final int pos) {
        MyTintTextView2 tv = new MyTintTextView2(MainActivity.this);
        tv.setText(bean.getName());
        tv.setColor(bean.getColor());
        tv.setLeftDrawable(ContextCompat.getDrawable(MainActivity.this, mIcons[pos]));
        llTheme.addView(tv, params);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNavigationItem(pos);
            }
        });
        mThemesItems.add(tv);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadThemeEvent(EventLoadThemes themes) {
        if (themes.getEventCode() == Const.CODE_SUCCESS) {
            mThemes.clear();
            mThemes.addAll(dao.loadThemes());
            proCreateThemesViews();
        } else {
            CommonUtil.showToast(themes.getEventMsg());
        }
    }


    private static final String STATE_HOME_SELECTED = "state_is_home_selected";
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CURRENT_ITEM_POSITION, mCurrentItemPosition);
        outState.putBoolean(STATE_HOME_SELECTED,isHomeSelected);
        super.onSaveInstanceState(outState);
    }

    private void initEvent() {

        mttvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHomeSelected();
            }
        });


        if(isHomeSelected){
            setHomeSelected();
        }
    }

    private void setHomeSelected(){
        isHomeSelected = true;
        mttvHome.setSelected(true);

        if(mCurrentItemPosition!=-1){
            if(mThemesItems.size() >0 )
                mThemesItems.get(mCurrentItemPosition).setSelected(false);
        }

        mCurrentItemPosition = -1;

        setHomeFragment();
        toggle();
    }


    private void setHomeFragment(){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, FragmentDailyNews.getInstance("首页"));
        transaction.commit();

    }


    private void selectedNavigationItem(int position) {

        if (mCurrentItemPosition == position)
            return;

        mttvHome.setSelected(false);
        isHomeSelected = false;

        if(mCurrentItemPosition != -1){
            mThemesItems.get(mCurrentItemPosition).setSelected(false);
        }

        mCurrentItemPosition = position;
        mThemesItems.get(mCurrentItemPosition).setSelected(true);
        toggle();

        replaceFragment(position);

    }

    private void replaceFragment(int position) {

        FragmentThemesDailyNews themesDailyNews = FragmentThemesDailyNews.newInstance(mThemes.get(position));

        manager.beginTransaction()
                .replace(R.id.fragment_container, themesDailyNews)
                .commit();



        themesDailyNews.setOnFragmentBackListener(new FragmentThemesDailyNews.OnFragmentBackListener() {
            @Override
            public void onFragmentBack() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mCurrentItemPosition != -1){
            setHomeSelected();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void toggle() {
        if (navigationLayout.isDrawerOpen(GravityCompat.START))
            navigationLayout.closeDrawer(GravityCompat.START);
    }


}
