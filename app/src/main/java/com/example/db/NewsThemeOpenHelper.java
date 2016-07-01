package com.example.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsThemeOpenHelper extends SQLiteOpenHelper {
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_THEME_ID = "theme_id";
    public static final String COLUMN_THEME_NAME = "theme_name";
    public static final String COLUMN_THEME_COLOR = "theme_color";
    public static final String COLUMN_THEME_DESC = "theme_desc";
    public static final String COLUMN_THEME_THUMBNAIL = "theme_thumbnail";


    public static final String TABLE_THEME = "themes";

    private static final String CREATE_TABLE = "create table "+TABLE_THEME+"("
            +COLUMN_ID+" integer primary key autoincrement,"
            +COLUMN_THEME_ID+" integer UNIQUE,"
            +COLUMN_THEME_NAME +" text,"
            +COLUMN_THEME_COLOR+" integer,"
            +COLUMN_THEME_DESC+" text,"
            +COLUMN_THEME_THUMBNAIL+" text)";

    public NewsThemeOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
