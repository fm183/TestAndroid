package com.example.testandroid.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testandroid.R;
import com.example.testandroid.bean.CountryMobileCode;
import com.example.testandroid.inf.ItemClickListener;
import com.example.testandroid.ui.viewholder.MobileCodeViewHolder;
import com.example.testandroid.utils.CollectionsUtils;

import java.util.ArrayList;
import java.util.List;

public class MobileCodeAdapter extends RecyclerView.Adapter<MobileCodeViewHolder> {

    private final List<CountryMobileCode> countryMobileCodeList;
    private ItemClickListener<CountryMobileCode> itemClickListener;

    public MobileCodeAdapter() {
        countryMobileCodeList = new ArrayList<>();
    }

    public void setItemClickListener(ItemClickListener<CountryMobileCode> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void updateData(List<CountryMobileCode> countryMobileCodeList) {
        this.countryMobileCodeList.clear();
        if (countryMobileCodeList != null && !countryMobileCodeList.isEmpty()) {
            this.countryMobileCodeList.addAll(countryMobileCodeList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MobileCodeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new MobileCodeViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_mobile_code, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MobileCodeViewHolder viewHolder, int position) {
        CountryMobileCode countryMobileCode = CollectionsUtils.getItem(countryMobileCodeList,position);
        if (countryMobileCode == null) {
            return;
        }

        viewHolder.updateData(countryMobileCode, position == getItemCount() - 1);
        viewHolder.itemView.setOnClickListener(v -> {
            if(itemClickListener != null){
                itemClickListener.onItemClick(v,position,countryMobileCode);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryMobileCodeList.size();
    }
}
