package com.example.testandroid.view;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LuckItemDecoration extends RecyclerView.ItemDecoration{

    private int space;
    public LuckItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,@NonNull View view, RecyclerView parent,@NonNull RecyclerView.State state) {
        int count = parent.getChildCount();
        int position = parent.getChildAdapterPosition(view);
        Log.d(LuckItemDecoration.class.getSimpleName(), "getItemOffsets: count="+count+",position="+position);

        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        outRect.top = space;
    }
}
