package com.example.testandroid.inf;

import android.view.View;

public interface ItemClickListener<T> {
    void onItemClick(View view, int position, T data);
}