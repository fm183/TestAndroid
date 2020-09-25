package com.example.testandroid.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testandroid.R;
import com.example.testandroid.api.ILoadStudentListener;
import com.example.testandroid.bean.StudentResponse;
import com.example.testandroid.manager.StudentManager;
import com.example.testandroid.ui.adapter.StudentAdapter;

import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity  implements ILoadStudentListener {

    private static final Logger logger = Logger.getLogger(MainActivity.class.getName());

    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter =  new StudentAdapter();
        recyclerView.setAdapter(studentAdapter);

        StudentManager.getInstance().refresh(this);

    }

    @Override
    public void onRefreshFail(String reason) {
        Toast.makeText(this,reason,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefreshSuccess(List<StudentResponse.DataBean> list) {
        if (studentAdapter != null) {
            studentAdapter.refreshData(list);
        }
    }

    @Override
    public void onLoadFail(String reason) {
        Toast.makeText(this,reason,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadSuccess(List<StudentResponse.DataBean> list) {
        if (studentAdapter != null) {
            studentAdapter.loadData(list);
        }
    }
}
