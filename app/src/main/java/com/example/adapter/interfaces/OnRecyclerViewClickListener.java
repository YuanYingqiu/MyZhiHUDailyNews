package com.example.adapter.interfaces;

import android.view.View;


public interface OnRecyclerViewClickListener {
    void onItemClick(View view,int position);
    void onItemLongClick(View view, int position);
}
