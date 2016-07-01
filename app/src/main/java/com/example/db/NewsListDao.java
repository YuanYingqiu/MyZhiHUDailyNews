package com.example.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bean.NewsDataBean;
import com.example.bean.OutputStoriesBean;
import com.example.bean.StoriesBean;
import com.example.bean.TopStoriesBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class NewsListDao {
    private static final String DB_NAME = "NewsList.db";
    private SQLiteDatabase db;

    private NewsListDao(Context context) {
        NewsListOpenHelper openHelper = new NewsListOpenHelper(context, DB_NAME, null, 1);
        db = openHelper.getWritableDatabase();
    }

    private static NewsListDao dao;

    public static NewsListDao getInstance(Context context) {
        if (dao == null) {
            synchronized (NewsListDao.class) {
                if (dao == null)
                    dao = new NewsListDao(context);
            }
        }
        return dao;
    }

    /**
     *
     * @param bean 最新日期的url下所有的news，包括TopStories
     */
    public void saveNewsData(NewsDataBean bean) {
        if (bean != null) {
            List<StoriesBean> stories = bean.getStories();
            List<TopStoriesBean> topStories = bean.getTop_stories();
            String date = bean.getDate();
            saveStories(stories, date);
            removeTopStoriesAddTopStories(topStories,date);
        }
    }


    public List<OutputStoriesBean> loadStoriesAccordingDate(String date) {
        return loadNews(NewsListOpenHelper.TABLE_NAME_STORIES, date);
    }


    /**
     * 加载topStories
     *
     * @return
     */
    public List<OutputStoriesBean> loadTopStories() {
        return loadNews(NewsListOpenHelper.TABLE_NAME_TOP_STORIES, null);
    }

    private List<OutputStoriesBean> loadNews(String tableName, String date) {
        List<OutputStoriesBean> beans = new ArrayList<>();
        Cursor cursor;
        if (date == null)
            cursor = db.query(tableName, null, null, null, null, null, null);
        else
            cursor = db.query(tableName, null, NewsListOpenHelper.COLUMN_DATE + " = ?", new String[]{date}, null, null, null);
            while (cursor.moveToNext()) {
                OutputStoriesBean bean = new OutputStoriesBean();
                bean.setImage(cursor.getString(cursor.getColumnIndex(NewsListOpenHelper.COLUMN_IMG_URL)));
                bean.setId(cursor.getString(cursor.getColumnIndex(NewsListOpenHelper.COLUMN_STORIES_ID)));
                bean.setTitle(cursor.getString(cursor.getColumnIndex(NewsListOpenHelper.COLUMN_TILE)));
                bean.setMultipic(cursor.getInt(cursor.getColumnIndex(NewsListOpenHelper.COLUMN_IS_MULTI_IMG)) == 1);
                bean.setDate(cursor.getString(cursor.getColumnIndex(NewsListOpenHelper.COLUMN_DATE)));
                bean.setType(cursor.getInt(cursor.getColumnIndex(NewsListOpenHelper.COLUMN_TYPE)));
                beans.add(bean);
            }

        cursor.close();
        Collections.sort(beans,new DateCompare());

        return beans;
    }

    public void saveStories(List<StoriesBean> beans, String date) {
        ContentValues values;
        for (StoriesBean story : beans) {
            values = new ContentValues();
            values.put(NewsListOpenHelper.COLUMN_STORIES_ID, story.getId());
            values.put(NewsListOpenHelper.COLUMN_DATE, date);
            values.put(NewsListOpenHelper.COLUMN_IS_MULTI_IMG, story.isMultipic() ? 1 : 0);
            values.put(NewsListOpenHelper.COLUMN_TILE, story.getTitle());
            values.put(NewsListOpenHelper.COLUMN_IMG_URL, story.getImages().get(0));
            values.put(NewsListOpenHelper.COLUMN_TYPE,story.getType());
            db.insert(NewsListOpenHelper.TABLE_NAME_STORIES,null,values);
        }
    }


    /**
     * 先将所有的 topStories 删除掉，再存入新的
     * @param topStories
     * @param date
     */
    private void removeTopStoriesAddTopStories(List<TopStoriesBean> topStories, String date){
        try {
            db.beginTransaction();
            db.delete(NewsListOpenHelper.TABLE_NAME_TOP_STORIES,null,null);
            saveTopStories(topStories, date);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    private void saveTopStories(List<TopStoriesBean> beans, String date) {
        for (TopStoriesBean story : beans) {
            ContentValues values = new ContentValues();
            values.put(NewsListOpenHelper.COLUMN_STORIES_ID, story.getId());
            values.put(NewsListOpenHelper.COLUMN_DATE, date);
            values.put(NewsListOpenHelper.COLUMN_IS_MULTI_IMG, story.isMultipic() ? 1 : 0);
            values.put(NewsListOpenHelper.COLUMN_TILE, story.getTitle());
            values.put(NewsListOpenHelper.COLUMN_IMG_URL, story.getImage());
            values.put(NewsListOpenHelper.COLUMN_TYPE,story.getType());

            db.insert(NewsListOpenHelper.TABLE_NAME_TOP_STORIES, null, values);
        }
    }


    public void removeStoriesAddStories(NewsDataBean bean) {
        try {
            db.beginTransaction();
            db.delete(NewsListOpenHelper.TABLE_NAME_STORIES, null, null);
            saveNewsData(bean);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }


    }


    private class DateCompare implements Comparator<OutputStoriesBean>{

        @Override
        public int compare(OutputStoriesBean o1, OutputStoriesBean o2) {
            return o2.getDate().compareTo(o1.getDate());
        }
    }
}
