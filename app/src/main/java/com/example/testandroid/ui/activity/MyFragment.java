package com.example.testandroid.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testandroid.R;
import com.example.testandroid.bean.StudentResponse;
import com.example.testandroid.inf.ILoadStudentListener;
import com.example.testandroid.manager.StudentManager;
import com.example.testandroid.ui.adapter.StudentAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

public class MyFragment extends BottomSheetDialogFragment implements ILoadStudentListener {

    private StudentAdapter studentAdapter;
    private SmartRefreshLayout smartRefreshLayout;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getContext();
        return new BottomSheetDialog(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        //获取dialog对象
        Dialog dialog = getDialog();
        //把windowsd的默认背景颜色去掉，不然圆角显示不见
        if (!(dialog instanceof BottomSheetDialog)) {
            return;
        }
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
        Window window = bottomSheetDialog.getWindow();
        if (window == null) {
            return;
        }
        window.findViewById(R.id.design_bottom_sheet).setBackground(new ColorDrawable(Color.TRANSPARENT));
        FrameLayout bottomSheet = bottomSheetDialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet == null) {
            return;
        }

        final BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
        // 初始为展开状态
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        View view = getView();
        if (view == null) {
            return;
        }
        view.findViewById(R.id.re_back_img).setOnClickListener(v -> behavior.setState(BottomSheetBehavior.STATE_HIDDEN));

    }

    /**
     * 弹窗高度，默认为屏幕高度的四分之三
     * 子类可重写该方法返回peekHeight
     *
     * @return height
     */
    protected int getPeekHeight() {
        int peekHeight = getResources().getDisplayMetrics().heightPixels;
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight / 3;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_bottomsheet,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view == null) {
            return;
        }
        smartRefreshLayout = view.findViewById(R.id.smart_refresh_layout);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        studentAdapter =  new StudentAdapter();
        recyclerView.setAdapter(studentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL));

        StudentManager.getInstance().refresh(this);

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> StudentManager.getInstance().refresh(this));
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> StudentManager.getInstance().loadMore(this));
    }

    @Override
    public void onRefreshFail(String reason) {
        Toast.makeText(getContext(),reason,Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getContext(),reason,Toast.LENGTH_SHORT).show();
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