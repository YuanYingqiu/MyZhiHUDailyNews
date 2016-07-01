package com.example.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bean.NewsContentBean;

public class NewsContentDao {
    private static NewsContentDao dao;
    private final SQLiteDatabase db;

    private NewsContentDao(Context context){
        NewsContentOpenHelper helper = new NewsContentOpenHelper(
                context,"NewContent.db",null,1);
        db = helper.getWritableDatabase();
    }

    public static NewsContentDao getInstance(Context context){
        if(dao == null){
            synchronized (NewsContentDao.class){
                if(dao == null)
                    dao = new NewsContentDao(context);
            }
        }
        return dao;
    }

    public void saveNewsContent(String id, NewsContentBean bean){
        ContentValues values = new ContentValues();
        values.put(NewsContentOpenHelper.COLUMN_News_ID,id);
        values.put(NewsContentOpenHelper.COLUMN_BODY,bean.getBody());
        values.put(NewsContentOpenHelper.COLUMN_SHARE_URL,bean.getShare_url());
        db.insert(NewsContentOpenHelper.TABLE_NAME,null,values);
    }

    public NewsContentBean loadNewsContent(String id){
        Cursor cursor = db.query(
                NewsContentOpenHelper.TABLE_NAME,null,NewsContentOpenHelper.COLUMN_News_ID+" = ?",new String[]{id},null,null,null);
        NewsContentBean bean = new NewsContentBean();
        if(cursor.moveToNext()){
            bean.setBody(cursor.getString(cursor.getColumnIndex(NewsContentOpenHelper.COLUMN_BODY)));
            bean.setShare_url(cursor.getString(cursor.getColumnIndex(NewsContentOpenHelper.COLUMN_SHARE_URL)));
        }
        cursor.close();
        return bean;
    }


}
