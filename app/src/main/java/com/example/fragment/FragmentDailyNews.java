package com.example.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.Const;
import com.example.DailyNewsContentActivity;
import com.example.R;
import com.example.adapter.RecyclerDailyNewsAdapter;
import com.example.adapter.TopStoriesImageAdapter;
import com.example.adapter.interfaces.OnRecyclerViewClickListener;
import com.example.bean.EventBean.EventLoadDailyNews;
import com.example.bean.HistoryStoriesBean;
import com.example.bean.NewsDataBean;
import com.example.bean.OutputStoriesBean;
import com.example.db.NewsListDao;
import com.example.http.OkHttpManager;
import com.example.utils.CommonUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


public class FragmentDailyNews extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static FragmentDailyNews fragment;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view_news_list)
    RecyclerView recyclerViewNewsList;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.tool_bar_layout)
    CollapsingToolbarLayout collapsingToolBarLayout;

    @Bind(R.id.vp_top_imgs_container)
    ViewPager vpTopImgsContainer;
    @Bind(R.id.ll_gray_dot_container)
    LinearLayout llGrayDotContainer;
    @Bind(R.id.iv_red_dot)
    ImageView ivRedDot;
    @Bind(R.id.tv_top_stories_title)
    TextView tvTopStoriesTitle;


    private String title;
    public static final String ARGS_TITLE = "title";

    private List<OutputStoriesBean> mStories;
    private NewsListDao dao;
    private OkHttpManager manager;
    private RecyclerDailyNewsAdapter adapter;
    private String mLastDate;

    private List<OutputStoriesBean> mTopStories;
    private int mDotSpan;


    private static final String TAG = "tag";
    private static final String DATE_MODE = "yyyyMMdd";

    private boolean isLoadingOtherDayDate;
    private TopStoriesImageAdapter topStoriesImageAdapter;


    public static FragmentDailyNews getInstance(String title) {
        if (fragment == null) {
            synchronized (FragmentDailyNews.class) {
                if (fragment == null) {
                    fragment = new FragmentDailyNews();
                    Bundle args = new Bundle();
                    args.putString(ARGS_TITLE, title);
                    fragment.setArguments(args);
                }
            }
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARGS_TITLE);
        }
        dao = NewsListDao.getInstance(mActivity);
        manager = OkHttpManager.getInstance(mActivity);
        mLastDate = getTodayDate();

        mStories = dao.loadStoriesAccordingDate(null);
        mTopStories = dao.loadTopStories();


        isLoadingOtherDayDate = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_news, container, false);
        ButterKnife.bind(this, view);
        initToolBar();
        initRecyclerView();

        initDot();
        initDotSpan();
        initTopStoriesTitle();
        initViewPagerEvent();

        return view;
    }

    private void initViewPagerEvent() {
        topStoriesImageAdapter = new TopStoriesImageAdapter(
                getActivity(), mTopStories);
        vpTopImgsContainer.setAdapter(topStoriesImageAdapter);

        vpTopImgsContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int leftMargin = (int) (mDotSpan * positionOffset + position * mDotSpan);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedDot.getLayoutParams();
                params.leftMargin = leftMargin;
                ivRedDot.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                tvTopStoriesTitle.setText(mTopStories.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    protected void afterActivityCreated(Bundle savedInstanceState) {
        loadStories();
    }


    private void loadStories() {
        setLateDate();
        if (mStories != null && mStories.size() > 0) {
            //判断最开始的日期是否为今天
            //如果不是 判断隔了多少天，如果超过一天
            //加载今天的数据 并且将数据库当中的数据全部清除 然后存储今天的数据
            //只差一天 从服务器加载今天的数据，并显示在最前面
            String lastedDate = mStories.get(0).getDate();
            if (!lastedDate.equals(getTodayDate())) {
                //最新一天不是今天
                //超过一天
                if (spanTodayAndOtherDay(getTodayDate(), lastedDate) > 1) {
                    loadTodayData(true, true);

                } else {
                    //不超过一天
                    loadTodayData(false, true);

                }
            }
        } else {
            //数据库为空
            loadTodayData(false, true);
        }

    }

    private void setLateDate() {
        if (mStories != null && mStories.size() > 0)
            mLastDate = mStories.get(mStories.size() - 1).getDate();
    }

    /**
     * 加载今天的数据
     */
    private void loadTodayData(boolean needRemoveAll, boolean needPost) {
        if (needPost)
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(true);
                }
            });

        String url = Const.URL_LASTED_DAILY_NEWS;
        loadDailyNewsFromServer(url, true, needRemoveAll);
    }


    private void loadOtherDayData() {
        String url = Const.URL_EARLY_DAILY_NEWS + mLastDate;
        Log.e(TAG, url);
        loadDailyNewsFromServer(url, false, false);
    }

    private void loadDailyNewsFromServer(String url, final boolean isToday, final boolean needRemoveAllData) {
        manager.getAsync(url, new OkHttpManager.ResultCallback<String>() {
            @Override
            public void onError(Call call, IOException e) {
                EventBus.getDefault().post(new EventLoadDailyNews(Const.CODE_ERROR, "网络异常"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {

                    if (isToday) {
                        if (needRemoveAllData) {

                            handlerNeedRemoveAllData(result);
                        } else {

                            handlerDailyBeansData(result, true);
                        }

                        EventBus.getDefault().post(new EventLoadDailyNews(Const.CODE_SUCCESS_TODAY, null));

                    } else {
                        handlerDailyBeansData(result, false);
                        EventBus.getDefault().post(new EventLoadDailyNews(Const.CODE_SUCCESS_OTHER_DAY, null));
                    }

                } catch (Exception e) {
                    EventBus.getDefault().post(new EventLoadDailyNews(Const.CODE_ERROR, "数据解析异常"));
                }
            }
        });
    }

    /**
     * 将数据存入数据库当中
     *
     * @param result
     * @param isToday
     * @throws Exception
     */
    private void handlerDailyBeansData(String result, boolean isToday) throws Exception {
        if (isToday) {
            NewsDataBean data = new Gson().fromJson(result, NewsDataBean.class);
            dao.saveNewsData(data);
        } else {
            //没有topStories
            HistoryStoriesBean beans = new Gson().fromJson(result, HistoryStoriesBean.class);
            dao.saveStories(beans.getStories(), beans.getDate());
        }

    }

    /**
     * 先将数据库当中的数据删除，然后将数据存入数据库当中
     *
     * @param result
     * @throws Exception
     */
    private void handlerNeedRemoveAllData(String result) throws Exception {
        NewsDataBean data = new Gson().fromJson(result, NewsDataBean.class);
        dao.removeStoriesAddStories(data);
    }

    private void notifyAddTodayNews() {

        List<OutputStoriesBean> outputStoriesBeen = dao.loadStoriesAccordingDate(getTodayDate());

        if (mStories.containsAll(outputStoriesBeen)) {
            return;
        }

        //去除重复项
        if (mStories != null && mStories.size() > 0) {
            List<OutputStoriesBean> out = copyStories();
            out.retainAll(outputStoriesBeen);
            mStories.removeAll(out);
        }


        mStories.addAll(0, outputStoriesBeen);
        adapter.notifyItemChanged(0);

        setLateDate();

    }

    private List<OutputStoriesBean> copyStories() {
        List<OutputStoriesBean> out = new ArrayList<>();
        for (OutputStoriesBean o : mStories) {
            out.add(o);
        }
        return out;
    }


    private void notifyAddOtherDayNews() {
        int pos = mStories.size();
        mStories.addAll(dao.loadStoriesAccordingDate(beforeLastDate()));
        adapter.notifyItemChanged(pos);
        setLateDate();
    }

    private void notifyAddTopStories() {
        mTopStories.clear();
        mTopStories.addAll(dao.loadTopStories());
        initDot();
        initDotSpan();
        initTopStoriesTitle();
        topStoriesImageAdapter.notifyDataSetChanged();
    }


    /**
     * 得到最后一天的前一天的日期
     *
     * @return
     */
    private String beforeLastDate() {
        String lastDate = mLastDate;

        DateFormat df = new SimpleDateFormat(DATE_MODE, Locale.CHINA);

        Date parse = null;
        try {
            parse = df.parse(lastDate);
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(parse);

            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = CommonUtil.parseDay(calendar.get(Calendar.MONTH));
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));


            return year + month + day;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;

    }


    /**
     * 两个日期中间隔了多少天
     *
     * @param today
     * @param otherDday
     * @return
     */
    private int spanTodayAndOtherDay(String today, String otherDday) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_MODE, Locale.CHINA);
        Date dateToday = null;
        Date other = null;
        try {
            dateToday = format.parse(today);
            other = format.parse(otherDday);
            long time = Math.abs(dateToday.getTime() - other.getTime());
            return accordingTimeGetSpanDay(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据时间算出天数
     *
     * @param time
     * @return
     */
    private int accordingTimeGetSpanDay(long time) {
        return (int) time / 1000 / 60 / 60 / 24;
    }

    private String getTodayDate() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_MODE, Locale.CHINA);
        return format.format(new Date());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventLoadDailyNews(EventLoadDailyNews news) {
        refreshLayout.setRefreshing(false);

        if (news.getEventCode() == Const.CODE_SUCCESS_TODAY) {
            //今天
            notifyAddTodayNews();
            notifyAddTopStories();

        } else if (news.getEventCode() == Const.CODE_SUCCESS_OTHER_DAY) {
            isLoadingOtherDayDate = false;

            notifyAddOtherDayNews();
        } else if (news.getEventCode() == Const.CODE_ERROR) {
            CommonUtil.showToast(news.getEventMsg());
        }
    }


    private void initRecyclerView() {
        adapter = new RecyclerDailyNewsAdapter(getActivity(), mStories);
        recyclerViewNewsList.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewNewsList.setLayoutManager(layoutManager);
        recyclerViewNewsList.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnRecyclerViewClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DailyNewsContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(DailyNewsContentActivity.KEY_ID, mStories.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        refreshLayout.setOnRefreshListener(this);

        recyclerViewNewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = lm.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = lm.getItemCount();

                    if (lastVisibleItem == totalItemCount - 1 && isSlidingToLast && !isLoadingOtherDayDate) {
                        isLoadingOtherDayDate = true;

                        refreshLayout.setRefreshing(true);
                        loadOtherDayData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                isSlidingToLast = dy > 0;
            }
        });


    }


    private void initToolBar() {
        collapsingToolBarLayout.setTitle(title);
        collapsingToolBarLayout.setExpandedTitleColor(Color.parseColor("#00ffffff"));

        toolbar.inflateMenu(R.menu.menu_tool_bar);

        DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.navigation_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onRefresh() {
        loadTodayData(false, false);
    }


    private void initDot() {
        if (mTopStories != null) {

            ivRedDot.setVisibility(View.VISIBLE);

            int leftMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 3, getActivity().getResources().getDisplayMetrics());


            if(llGrayDotContainer.getChildCount() > 0){
                llGrayDotContainer.removeAllViews();
            }

            for (int i = 0; i < mTopStories.size(); i++) {
                ImageView ivDot = new ImageView(getActivity());
                ivDot.setImageResource(R.drawable.shape_gray_dot);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (i > 0) {
                    params.leftMargin = leftMargin;
                }
                llGrayDotContainer.addView(ivDot, params);
            }
        }
    }


    private void initDotSpan() {
        ivRedDot.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ivRedDot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            ivRedDot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }

                        if (llGrayDotContainer.getChildCount() > 1) {
                            mDotSpan = llGrayDotContainer.getChildAt(1).getLeft() - llGrayDotContainer.getChildAt(0).getLeft();
                        }

                    }
                });
    }

    private void initTopStoriesTitle(){
        if(mTopStories.size() > 0){
            tvTopStoriesTitle.setText(mTopStories.get(vpTopImgsContainer.getCurrentItem()).getTitle());
        }
    }
}
