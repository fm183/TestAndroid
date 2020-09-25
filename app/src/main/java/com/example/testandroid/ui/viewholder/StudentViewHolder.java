package com.example.testandroid.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testandroid.R;
import com.example.testandroid.bean.StudentResponse;
import com.example.testandroid.utils.DateTimeUtils;

public class StudentViewHolder extends RecyclerView.ViewHolder {

    private TextView tvName;
    private TextView tvTime;

    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        tvTime = itemView.findViewById(R.id.tv_time);
    }

    public void bindData(StudentResponse.DataBean dataBean){
        tvName.setText(dataBean.getName());
        tvTime.setText(DateTimeUtils.millis2MonthAndDay(dataBean.getCreate_time()));
    }

}
