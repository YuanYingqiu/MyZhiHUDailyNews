package com.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import com.example.R;

public class MyTintTextView2 extends AppCompatTextView {



    private int mColor;
    private Drawable mDrawable;

    public MyTintTextView2(Context context) {
        this(context, null);
    }

    public MyTintTextView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTintTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.MyTintTextView2);
        mColor = ta.getColor(R.styleable.MyTintTextView2_tint_color,Color.BLUE);
        ta.recycle();

        setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                context.getResources().getDisplayMetrics()), 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                context.getResources().getDisplayMetrics()), 0);


        setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
                context.getResources().getDisplayMetrics()));

        setGravity(Gravity.CENTER_VERTICAL);


        Drawable drawable = getCompoundDrawables()[0];
        if(drawable!=null)
            drawable.setColorFilter(mColor, PorterDuff.Mode.SRC_IN);

    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        if (selected) {
            setBackgroundColor(ColorUtils.setAlphaComponent(mColor, 80));

            setTextAppearance(getContext(), R.style.text_view_base_bond);
            setTextColor(mColor);
        } else {
            setBackgroundColor(Color.WHITE);

            setTextAppearance(getContext(), R.style.text_view_base_normal);
            setTextColor(Color.BLACK);
        }


    }

    public void setColor(int color) {
        this.mColor = Color.parseColor("#"+parseColorToHex(color));
    }


    public void setLeftDrawable(Drawable drawable){
        this.mDrawable = drawable;
        setDrawable();
    }

    private void setDrawable() {
        mDrawable.setBounds(0,0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
        setCompoundDrawables(mDrawable,null,null,null);
        mDrawable.setColorFilter(mColor, PorterDuff.Mode.SRC_IN);

    }


    private String parseColorToHex(int color) {
        String col = Integer.toHexString(color);
        if (col.length() != 6)
            col = parseColor(col);
        return col;
    }

    private String parseColor(String col) {

        char[] chars = col.toCharArray();
        StringBuilder sb = new StringBuilder();


        int size = Math.abs(6 - chars.length);
        for (int i = 0; i < size; i++) {
            sb.append('0');
        }


        for (int i = 0; i < chars.length; i++) {
            sb.append(chars[i]);
        }

        return sb.toString();
    }


}
