package com.example.testandroid.ui.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testandroid.R;
import com.example.testandroid.bean.CountryMobileCode;


public class MobileCodeViewHolder extends RecyclerView.ViewHolder {

    private final TextView tvName;
    private final View viewLine;
    private final View viewBottom;
    private final TextView tvTitle;

    public MobileCodeViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        viewLine = itemView.findViewById(R.id.view_line);
        tvTitle = itemView.findViewById(R.id.tv_title);
        viewBottom = itemView.findViewById(R.id.view_bottom);
    }

    public void updateData(CountryMobileCode countryMobileCode, boolean isLastPosition){
        if (countryMobileCode == null || tvName == null || viewLine == null || tvTitle == null) {
            return;
        }
        boolean isTitle = countryMobileCode.isTitleType();
        tvTitle.setText(countryMobileCode.getName());
        tvName.setText(String.format("%s%s", countryMobileCode.getName(), countryMobileCode.getPhone_code()));
        viewLine.setVisibility(isLastPosition || isTitle ? View.GONE : View.VISIBLE);
        tvTitle.setVisibility(isTitle ? View.VISIBLE : View.GONE);
        tvName.setVisibility(isTitle ? View.GONE : View.VISIBLE);
        viewBottom.setVisibility(isLastPosition ? View.VISIBLE : View.GONE);
        itemView.setBackgroundColor(isTitle ? ContextCompat.getColor(itemView.getContext(),R.color.item_mobile_code_index_bg_color) : Color.TRANSPARENT);
    }


}
