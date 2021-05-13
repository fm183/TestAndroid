package com.example.testandroid.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testandroid.R;
import com.example.testandroid.utils.UtilsSize;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

//或者使用其他三方框架，都是支持的，如：BRVAH
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.PagerViewHolder>  {
    private List<String> mData;

    public ImageAdapter(List<String> data){
        this.mData = new ArrayList<>();
        mData.addAll(data);
    }

    @Override
    public void onBindViewHolder(@NonNull PagerViewHolder holder, int position) {
        int imgWidth = UtilsSize.dpToPx(holder.itemView.getContext(),240);
        int imgHeight = UtilsSize.dpToPx(holder.itemView.getContext(),370);
        Glide.with(holder.ivPager).load(mData.get(position)).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(UtilsSize.dpToPx(holder.ivPager.getContext(),8),0))
                .override(imgWidth,imgHeight)).into(holder.ivPager);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewpager, parent, false));
    }

    static class PagerViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPager;

        public PagerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPager = itemView.findViewById(R.id.iv_icon);
        }
    }





}