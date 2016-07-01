package com.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.R;


public class MyTestView extends AppCompatTextView {


    public MyTestView(Context context) {
        this(context, null);
    }

    public MyTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyTestView);
        int color = ta.getColor(R.styleable.MyTestView_drawable_test_tint_color, Color.WHITE);
        ta.recycle();

        Drawable drawable = getCompoundDrawables()[0];
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);


    }
}
