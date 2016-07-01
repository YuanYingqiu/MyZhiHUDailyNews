package com.example.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bean.ThemesItemOutputBean;

import java.util.ArrayList;
import java.util.List;

public class NewsThemeDao {
    private static final String DB_NAME_THEME = "Theme.db";
    private static final int VERSION = 1;
    private final SQLiteDatabase db;


    private NewsThemeDao(Context context){
        NewsThemeOpenHelper helper = new NewsThemeOpenHelper(context,DB_NAME_THEME,null,VERSION);
        db = helper.getWritableDatabase();
    }

    private static NewsThemeDao dao;
    public static NewsThemeDao getInstace(Context context){
        if(dao == null){
            synchronized (NewsThemeDao.class){
                if(dao == null){
                    dao = new NewsThemeDao(context);
                }
            }
        }
        return dao;
    }


    public void saveThemes(List<ThemesItemOutputBean> beans){
        if(beans!=null && beans.size()>0){

            for (ThemesItemOutputBean bean : beans){

                ContentValues values = new ContentValues();
                values.put(NewsThemeOpenHelper.COLUMN_THEME_ID,bean.getId());
                values.put(NewsThemeOpenHelper.COLUMN_THEME_NAME,bean.getName());
                values.put(NewsThemeOpenHelper.COLUMN_THEME_COLOR,bean.getColor());
                values.put(NewsThemeOpenHelper.COLUMN_THEME_DESC,bean.getDescription());
                values.put(NewsThemeOpenHelper.COLUMN_THEME_THUMBNAIL,bean.getThumbnail());
                db.insert(NewsThemeOpenHelper.TABLE_THEME,null,values);
            }
        }
    }


    public List<ThemesItemOutputBean> loadThemes(){
        List<ThemesItemOutputBean> beans = new ArrayList<ThemesItemOutputBean>();
        Cursor cursor = db.query(NewsThemeOpenHelper.TABLE_THEME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            ThemesItemOutputBean bean = new ThemesItemOutputBean();
            bean.setId(cursor.getInt(cursor.getColumnIndex(NewsThemeOpenHelper.COLUMN_THEME_ID)));
            bean.setColor(cursor.getInt(cursor.getColumnIndex(NewsThemeOpenHelper.COLUMN_THEME_COLOR)));
            bean.setName(cursor.getString(cursor.getColumnIndex(NewsThemeOpenHelper.COLUMN_THEME_NAME)));
            bean.setDescription(cursor.getString(cursor.getColumnIndex(NewsThemeOpenHelper.COLUMN_THEME_DESC)));
            bean.setThumbnail(cursor.getString(cursor.getColumnIndex(NewsThemeOpenHelper.COLUMN_THEME_THUMBNAIL)));
            beans.add(bean);
        }
        cursor.close();
        return beans;
    }

}
