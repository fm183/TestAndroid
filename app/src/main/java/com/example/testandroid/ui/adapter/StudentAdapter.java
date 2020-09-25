package com.example.testandroid.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testandroid.R;
import com.example.testandroid.bean.StudentResponse;
import com.example.testandroid.ui.viewholder.StudentViewHolder;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {

    private List<StudentResponse.DataBean> dataBeanList = new ArrayList<>();

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

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bindData(this.dataBeanList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.dataBeanList == null ? 0 : this.dataBeanList.size();
    }
}
