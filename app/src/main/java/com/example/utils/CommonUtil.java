package com.example.utils;

import android.graphics.Color;
import android.widget.Toast;

import com.example.App;


public class CommonUtil {




    private static final String[] days = new String[]{
            "01","02","03","04","05","06","07","08","09","10","11","12"
    };

    public static void showToast(String msg){
        Toast.makeText(App.getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public static String parseDay(int dayOfMonth){
        return days[dayOfMonth];
    }
}
