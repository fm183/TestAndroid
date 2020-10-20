package com.example.testandroid.inf;

import android.view.View;

public interface OnItemClickListener<T> {
    void onItemClick(T t, View view);
}
