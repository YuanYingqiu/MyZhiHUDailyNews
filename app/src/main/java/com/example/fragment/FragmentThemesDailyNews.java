package com.example.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Const;
import com.example.DailyNewsContentActivity;
import com.example.R;
import com.example.adapter.RecyclerThemeDailyNewsAdapter;
import com.example.adapter.interfaces.OnRecyclerViewClickListener;
import com.example.bean.EventBean.EventLoadThemeDailyNews;
import com.example.bean.OutputStoriesBean;
import com.example.bean.StoriesBean;
import com.example.bean.ThemeNewsBean;
import com.example.bean.ThemesItemOutputBean;
import com.example.http.OkHttpManager;
import com.example.utils.CommonUtil;
import com.example.utils.MyImageLoader;
import com.google.gson.Gson;

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


public class FragmentThemesDailyNews extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.iv_theme)
    AppCompatImageView ivTheme;

    @Bind(R.id.theme_toolbar)
    Toolbar themeToolbar;

    @Bind(R.id.theme_recycler_view_news_list)
    RecyclerView themeRecyclerViewNewsList;

    @Bind(R.id.theme_refresh_layout)
    SwipeRefreshLayout themeRefreshLayout;

    @Bind(R.id.theme_tool_bar_layout)
    CollapsingToolbarLayout themeToolBarLayout;

    private static final String ARG_PARAM1 = "args_param1";
    private OnFragmentBackListener mListener;
    private ThemesItemOutputBean mThemeBean;
    private List<OutputStoriesBean> mNewsList;
    private RecyclerThemeDailyNewsAdapter adapter;



    public FragmentThemesDailyNews() {
        // Required empty public constructor
    }


    public static FragmentThemesDailyNews newInstance(ThemesItemOutputBean param1) {
        FragmentThemesDailyNews fragment = new FragmentThemesDailyNews();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThemeBean = getArguments().getParcelable(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_themes_news, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }


    @Override
    protected void afterActivityCreated(Bundle savedInstanceState) {
        mNewsList = new ArrayList<>();

        initToolBar();
        initRecyclerView();

        themeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //如果是用ButterKnife的注解找到到refresh_layout
                //当横竖屏切换时，这句话会空指针
                themeRefreshLayout.setRefreshing(true);
            }
        });


        loadThemeDailyNewsFromServer();

    }

    private void initToolBar() {
        themeToolBarLayout.setTitle(mThemeBean.getName());
        themeToolBarLayout.setExpandedTitleColor(Color.parseColor("#00ffffff"));

        themeToolbar.inflateMenu(R.menu.menu_tool_bar);

        DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.navigation_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, drawer, themeToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();
    }

    private void initRecyclerView() {
        adapter = new RecyclerThemeDailyNewsAdapter(getActivity(), mNewsList);
        themeRecyclerViewNewsList.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        themeRecyclerViewNewsList.setLayoutManager(layoutManager);
        themeRecyclerViewNewsList.setItemAnimator(new DefaultItemAnimator());


        adapter.setOnRecyclerViewClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DailyNewsContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(DailyNewsContentActivity.KEY_ID, mNewsList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        themeRefreshLayout.setOnRefreshListener(this);
    }

    private void loadThemeDailyNewsFromServer() {

        String url = Const.URL_THEME_DAILY_NEWS + mThemeBean.getId();
        OkHttpManager.getInstance(getActivity()).getAsync(url, new OkHttpManager.ResultCallback<String>() {
            @Override
            public void onError(Call call, IOException e) {
                EventBus.getDefault().post(new EventLoadThemeDailyNews(Const.CODE_ERROR, "网络连接异常"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("tag", json);
                try {

                    ThemeNewsBean bean = new Gson().fromJson(json, ThemeNewsBean.class);
                    handlerStories(bean);
                    EventBus.getDefault().post(bean);

                } catch (Exception e) {
                    EventBus.getDefault().post(new EventLoadThemeDailyNews(Const.CODE_ERROR, "服务器异常"));
                }
            }

            private void handlerStories(ThemeNewsBean bean) {
                List<StoriesBean> stories = bean.getStories();
                mNewsList.clear();
                for (StoriesBean storiesBean : stories) {
                    OutputStoriesBean output = new OutputStoriesBean();
                    output.setId(storiesBean.getId());
                    output.setTitle(storiesBean.getTitle());
                    output.setType(storiesBean.getType());

                    if (storiesBean.getImages() != null && storiesBean.getImages().size() > 0)
                        output.setImage(storiesBean.getImages().get(0));
                    else
                        output.setImage(null);
                    mNewsList.add(output);
                }
            }
        });
    }


    /**
     * 网络加载完接收刷新UI的事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadNewsErrorEvent(EventLoadThemeDailyNews code) {
        themeRefreshLayout.setRefreshing(false);
        CommonUtil.showToast(code.getEventMsg());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadNewsSuccessEvent(ThemeNewsBean bean) {

        themeRefreshLayout.setRefreshing(false);
        MyImageLoader.loadImage(getActivity(), ivTheme, bean.getBackground());
        adapter.notifyItemChanged(0);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null)
            mListener.onFragmentBack();
    }

    @Override
    public void onRefresh() {
        loadThemeDailyNewsFromServer();
    }

    public interface OnFragmentBackListener {
        void onFragmentBack();
    }


    public void setOnFragmentBackListener(OnFragmentBackListener listener) {
        this.mListener = listener;
    }


}
