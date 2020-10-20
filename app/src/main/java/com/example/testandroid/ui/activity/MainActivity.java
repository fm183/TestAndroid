package com.example.testandroid.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testandroid.R;
import com.example.testandroid.inf.ILoadStudentListener;
import com.example.testandroid.bean.StudentResponse;
import com.example.testandroid.inf.OnItemClickListener;
import com.example.testandroid.manager.StudentManager;
import com.example.testandroid.ui.adapter.StudentAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity  implements ILoadStudentListener {

    private static final Logger logger = Logger.getLogger(MainActivity.class.getName());

    private StudentAdapter studentAdapter;
    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smartRefreshLayout = findViewById(R.id.smart_refresh_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter =  new StudentAdapter();
        recyclerView.setAdapter(studentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        StudentManager.getInstance().refresh(this);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> StudentManager.getInstance().refresh(MainActivity.this));
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> StudentManager.getInstance().loadMore(MainActivity.this));

        studentAdapter.setOnItemClickListener((dataBean, view) -> new MyFragment().show(getSupportFragmentManager(),"dialog"));
    }

    @Override
    public void onRefreshFail(String reason) {
        Toast.makeText(this,reason,Toast.LENGTH_SHORT).show();
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void onRefreshSuccess(List<StudentResponse.DataBean> list) {
        if (studentAdapter != null) {
            studentAdapter.refreshData(list);
        }
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void onLoadFail(String reason) {
        Toast.makeText(this,reason,Toast.LENGTH_SHORT).show();
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onLoadSuccess(List<StudentResponse.DataBean> list) {
        if (studentAdapter != null) {
            studentAdapter.loadData(list);
        }
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishLoadMore();
        }
    }
}
