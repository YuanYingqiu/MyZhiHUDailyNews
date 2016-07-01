package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.R;
import com.example.adapter.interfaces.OnRecyclerViewClickListener;
import com.example.bean.OutputStoriesBean;
import com.example.utils.MyImageLoader;

import java.util.List;


public class RecyclerThemeDailyNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OutputStoriesBean> mDailyNews;
    private LayoutInflater mInflater;
    private MyImageLoader mLoader;
    private static final int TYPE_WITH_IMG = 0;
    private static final int TYPE_NO_IMG = 1;

    public RecyclerThemeDailyNewsAdapter(Context context, List<OutputStoriesBean> dailyNews) {
        this.mDailyNews = dailyNews;
        mLoader = MyImageLoader.getInstance(context);
        mInflater = LayoutInflater.from(context);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_WITH_IMG)
            return new DailyNewsViewHolder(mInflater.inflate(R.layout.item_recycler_daily_news,parent,false));
        else
            return new DailyNewsNoImgViewHolder(mInflater.inflate(R.layout.item_recycler_daily_news_no_img_view,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        OutputStoriesBean bean = mDailyNews.get(position);

        if(bean == null)
            return;

        if(holder instanceof DailyNewsViewHolder){
            DailyNewsViewHolder hod = (DailyNewsViewHolder) holder;
            hod.tvTitle.setText(bean.getTitle());
            mLoader.with(hod.ivImg,bean.getImage());
        }else {
            DailyNewsNoImgViewHolder hod = (DailyNewsNoImgViewHolder) holder;
            hod.tvTitle.setText(bean.getTitle());
        }

        if(mListener!=null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(holder.itemView,holder.getLayoutPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.onItemLongClick(holder.itemView,holder.getLayoutPosition());
                    return false;
                }
            });
        }



    }


    @Override
    public int getItemViewType(int position) {
        if(mDailyNews.get(position).getImage() == null || mDailyNews.get(position).getImage().equals("") )
            return TYPE_NO_IMG;
        return TYPE_WITH_IMG;
    }

    @Override
    public int getItemCount() {
        return mDailyNews.size();
    }


    private OnRecyclerViewClickListener mListener;

    public void setOnRecyclerViewClickListener(OnRecyclerViewClickListener listener) {
        this.mListener = listener;
    }


    class DailyNewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivImg;

        public DailyNewsViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
        }
    }


    class DailyNewsNoImgViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        public DailyNewsNoImgViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title_no_img);
        }
    }


}

