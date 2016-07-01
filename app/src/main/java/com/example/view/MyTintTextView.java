package com.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.example.R;

public class MyTintTextView extends LinearLayoutCompat {

    private String mText;
    private LayoutInflater mInflayter;
    private Drawable mDrawable;
    private int color;
    private AppCompatTextView textView;

    public MyTintTextView(Context context) {
        this(context, null);
    }

    public MyTintTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTintTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyTintTextView);
        mDrawable = ta.getDrawable(R.styleable.MyTintTextView_drawable_left);
        color = ta.getColor(R.styleable.MyTintTextView_drawable_tint_color, 0xFFFFFF);
        mText = ta.getString(R.styleable.MyTintTextView_text);
        ta.recycle();
        mInflayter = LayoutInflater.from(context);
        initView();

    }

    private void initView() {
        View view = mInflayter.inflate(R.layout.custom_layout_my_tint_text_view, this);
        textView = (AppCompatTextView) view.findViewById(R.id.tv_custom_desc);
        AppCompatImageView imageView  = (AppCompatImageView) view.findViewById(R.id.iv_custom_tint);
        textView.setText(mText);
        imageView.setImageDrawable(mDrawable);
        imageView.setColorFilter(color);




    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        if(selected){
            textView.setTextColor(color);
            textView.setTextAppearance(getContext(), R.style.text_view_base_bond);
            setBackgroundColor(ColorUtils.setAlphaComponent(color,100));//0-255的透明度
        }else {
            textView.setTextColor(Color.BLACK);
            textView.setTextAppearance(getContext(), R.style.text_view_base_normal);
            setBackgroundColor(Color.WHITE);
        }
    }



}
