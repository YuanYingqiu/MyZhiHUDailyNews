package com.example.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NewsListOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME_STORIES = "daily_news_list";
    public static final String TABLE_NAME_TOP_STORIES = "top_stories";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_STORIES_ID = "stories_id";
    public static final String COLUMN_IMG_URL = "imgUrl";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TILE = "title";
    public static final String COLUMN_IS_MULTI_IMG = "multi_img";
    public static final String COLUMN_TYPE = "type";

    private static final String SQL_STORIES = "create table " + TABLE_NAME_STORIES + "("
            + COLUMN_ID+" integer primary key autoincrement,"
            + COLUMN_STORIES_ID + " char(10) UNIQUE,"
            + COLUMN_DATE + " text,"
            + COLUMN_IMG_URL + " text,"
            + COLUMN_TILE + " text,"
            + COLUMN_TYPE +" integer,"
            + COLUMN_IS_MULTI_IMG + " integer)";

    private static final String SQL_TOP_STORIES = "create table " + TABLE_NAME_TOP_STORIES + "("
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_STORIES_ID + " text,"
            + COLUMN_DATE + " text,"
            + COLUMN_IMG_URL + " text,"
            + COLUMN_TILE + " text,"
            + COLUMN_TYPE +" integer,"
            + COLUMN_IS_MULTI_IMG + " integer)";

    public NewsListOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_STORIES);
        db.execSQL(SQL_TOP_STORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
