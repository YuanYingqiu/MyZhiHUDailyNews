package com.example.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsContentOpenHelper extends SQLiteOpenHelper {
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_News_ID = "news_id";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_SHARE_URL = "share_url";

    public static final String TABLE_NAME = "news_content";

    private static final String CREATE_TABLE = "create table "+TABLE_NAME+"("
            +COLUMN_ID+" integer primary key autoincrement,"
            +COLUMN_News_ID+" text,"
            +COLUMN_SHARE_URL+" text,"
            +COLUMN_BODY+" text)";


    public NewsContentOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
