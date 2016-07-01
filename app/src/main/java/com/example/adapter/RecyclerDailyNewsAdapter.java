package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.R;
import com.example.adapter.interfaces.OnRecyclerViewClickListener;
import com.example.bean.OutputStoriesBean;
import com.example.bean.TopStoriesBean;
import com.example.utils.DateUtil;
import com.example.utils.MyImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;


public class RecyclerDailyNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OutputStoriesBean> mDailyNews;
    private LayoutInflater mInflater;
    private MyImageLoader mLoader;
    private static final int ITEM_NORMAL = 1;
    private static final int ITEM_WITH_DATE = 0;
    private SimpleDateFormat mFormat;

    public RecyclerDailyNewsAdapter(Context context, List<OutputStoriesBean> dailyNews) {
        this.mDailyNews = dailyNews;
        mLoader = MyImageLoader.getInstance(context);
        mInflater = LayoutInflater.from(context);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_WITH_DATE)
            return new DailyNewsWithDateViewHolder(mInflater.inflate(R.layout.item_recycler_daily_news_with_date, parent, false),mListener);
        else
            return new DailyNewsViewHolder(mInflater.inflate(R.layout.item_recycler_daily_news, parent, false),mListener);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        OutputStoriesBean bean = mDailyNews.get(position);

        if (bean == null)
            return;

        if (holder instanceof DailyNewsWithDateViewHolder) {
            bindWithDateViewHolder((DailyNewsWithDateViewHolder) holder, bean);
        } else {
            bindNormalViewHolder((DailyNewsViewHolder) holder, bean);
        }

    }

    private void bindWithDateViewHolder(DailyNewsWithDateViewHolder holder, OutputStoriesBean bean) {
        if(DateUtil.getTodayDate("yyyyMMdd").equals(bean.getDate())){
            holder.tvDate.setText("今日热闻");
        }else {
            String date = DateUtil.accordingDateGetDateAndWeek(bean.getDate(),"yyyyMMdd","MM月dd日");
            holder.tvDate.setText(date);
        }

        holder.tvTitle.setText(bean.getTitle());

        mLoader.with(holder.ivImg,bean.getImage());
    }

    private void bindNormalViewHolder(DailyNewsViewHolder holder, OutputStoriesBean bean) {
        holder.tvTitle.setText(bean.getTitle());
        mLoader.with(holder.ivImg,bean.getImage());
    }




    @Override
    public int getItemCount() {
        return mDailyNews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return ITEM_WITH_DATE;
        String currentDate = mDailyNews.get(position).getDate();
        boolean isDifferent = !currentDate.equals(mDailyNews.get(position - 1).getDate());
        return isDifferent ? ITEM_WITH_DATE : ITEM_NORMAL;
    }

    private OnRecyclerViewClickListener mListener;

    public void setOnRecyclerViewClickListener(OnRecyclerViewClickListener listener) {
        this.mListener = listener;
    }


    class DailyNewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivImg;
        RelativeLayout rl;
        protected OnRecyclerViewClickListener mListener;
        public DailyNewsViewHolder(final View itemView, OnRecyclerViewClickListener listener) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl_item);
            this.mListener = listener;

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null)
                        mListener.onItemClick(rl,getLayoutPosition());
                }
            });

        }
    }

    class DailyNewsWithDateViewHolder extends DailyNewsViewHolder {
        TextView tvDate;

        public DailyNewsWithDateViewHolder(View itemView,OnRecyclerViewClickListener listener) {
            super(itemView,listener);
            tvDate = (TextView) itemView.findViewById(R.id.tv_news_list_date);
        }
    }
}

