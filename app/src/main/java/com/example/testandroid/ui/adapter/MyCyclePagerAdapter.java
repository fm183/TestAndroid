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
import com.wangpeiyuan.cycleviewpager2.adapter.CyclePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MyCyclePagerAdapter extends CyclePagerAdapter<MyCyclePagerAdapter.PagerViewHolder> {

    private List<String> mData;

    public MyCyclePagerAdapter(List<String> data){
        this.mData = new ArrayList<>();
        mData.addAll(data);
    }

    @Override
    public int getRealItemCount() {
        return mData.size();
    }

    @Override
    public void onBindRealViewHolder(@NonNull PagerViewHolder holder, int position) {
        int imgWidth = UtilsSize.dpToPx(holder.itemView.getContext(),240);
        int imgHeight = UtilsSize.dpToPx(holder.itemView.getContext(),370);
        Glide.with(holder.ivPager).load(mData.get(position)).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(UtilsSize.dpToPx(holder.ivPager.getContext(),8),0))
                .override(imgWidth,imgHeight)).into(holder.ivPager);
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