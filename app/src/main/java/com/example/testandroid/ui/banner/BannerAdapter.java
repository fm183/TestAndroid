package com.example.testandroid.ui.banner;

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

/**
 * Created by zzf on 2018/7/29.
 */
 
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private final List<String> mData;
    private boolean mIsInfiniteLoop;

    public BannerAdapter(List<String> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        mData = data;
    }

    public void setIsInfiniteLoop(boolean isInfiniteLoop) {
        this.mIsInfiniteLoop = isInfiniteLoop;
    }

    private void loadImage(ImageView imageView, String url, int imgWidth, int imgHeight){
        Glide.with(imageView).load(url).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(UtilsSize.dpToPx(imageView.getContext(),8),0))
        .override(imgWidth,imgHeight)).into(imageView);
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewpager,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        int imgWidth = UtilsSize.dpToPx(holder.itemView.getContext(),240);
        int imgHeight = UtilsSize.dpToPx(holder.itemView.getContext(),370);

        loadImage(holder.ivIcon,mData.get(getRealPosition(position)),imgWidth,imgHeight);
    }

    @Override
    public int getItemCount() {
        return mIsInfiniteLoop ? Integer.MAX_VALUE : mData.size();
    }

    public int getRealCount() {
        return mData.size();
    }

    public int getRealPosition(int position) {
        return position % mData.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{

        ImageView ivIcon;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
        }
    }

}