package com.example.testandroid.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testandroid.R;
import com.example.testandroid.bean.StudentResponse;
import com.example.testandroid.inf.OnItemClickListener;
import com.example.testandroid.ui.viewholder.StudentViewHolder;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {

    private List<StudentResponse.DataBean> dataBeanList = new ArrayList<>();
    private OnItemClickListener<StudentResponse.DataBean> onItemClickListener;


    public void refreshData(List<StudentResponse.DataBean> dataBeanList){
        this.dataBeanList.clear();
        if (dataBeanList != null && !dataBeanList.isEmpty()) {
            this.dataBeanList.addAll(dataBeanList);
        }
        notifyDataSetChanged();
    }

    public void loadData(List<StudentResponse.DataBean> dataBeanList){
        if (dataBeanList != null && !dataBeanList.isEmpty()) {
            this.dataBeanList.addAll(dataBeanList);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<StudentResponse.DataBean> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentResponse.DataBean dataBean = this.dataBeanList.get(position);
        holder.bindData(dataBean);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(dataBean,v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.dataBeanList == null ? 0 : this.dataBeanList.size();
    }
}
