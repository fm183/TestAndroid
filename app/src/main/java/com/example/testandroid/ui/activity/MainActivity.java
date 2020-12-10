package com.example.testandroid.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.testandroid.R;
import com.example.testandroid.bean.CountryMobileCode;
import com.example.testandroid.inf.ILoadStudentListener;
import com.example.testandroid.bean.StudentResponse;
import com.example.testandroid.inf.OnItemClickListener;
import com.example.testandroid.manager.MobileCodeManager;
import com.example.testandroid.manager.StudentManager;
import com.example.testandroid.ui.adapter.MobileCodeAdapter;
import com.example.testandroid.ui.adapter.StudentAdapter;
import com.example.testandroid.view.VersificationCodeLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity  implements ILoadStudentListener {

    private static final Logger logger = Logger.getLogger(MainActivity.class.getName());

    private StudentAdapter studentAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private MobileCodeAdapter mobileCodeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*smartRefreshLayout = findViewById(R.id.smart_refresh_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter =  new StudentAdapter();
        recyclerView.setAdapter(studentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        StudentManager.getInstance().refresh(this);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> StudentManager.getInstance().refresh(MainActivity.this));
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> StudentManager.getInstance().loadMore(MainActivity.this));

        studentAdapter.setOnItemClickListener((dataBean, view) -> new MyFragment().show(getSupportFragmentManager(),"dialog"));*/

        RecyclerView recyclerViewCode = findViewById(R.id.recycler_view_code);
        mobileCodeAdapter = new MobileCodeAdapter();
        recyclerViewCode.setAdapter(mobileCodeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewCode.setLayoutManager(linearLayoutManager);

        MobileCodeManager.getInstance().addListener((countryMobileCodeList, countryMobileIndexList) -> mobileCodeAdapter.updateData(countryMobileCodeList));
        MobileCodeManager.getInstance().loadMobileCode();

       VersificationCodeLayout versificationCodeLayout =  findViewById(R.id.vcl);
       versificationCodeLayout.setOnInputListener(new VersificationCodeLayout.OnInputListener() {
           @Override
           public void onSuccess(String code) {
               Log.d(MainActivity.class.getSimpleName(), "onSuccess: code="+code);
           }

           @Override
           public void onInput() {
               Log.d(MainActivity.class.getSimpleName(), "onInput:");

           }
       });

       ImageView circle = findViewById(R.id.iv_circle);

        Glide.with(this)
                .load("http://honghua.ilikemanga.com/res/83f043d06036426bbc01397c568637a1.jpg?1920x1920")//图片路径，可以是网络路径
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(circle);
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
