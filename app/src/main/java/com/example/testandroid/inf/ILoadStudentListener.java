package com.example.testandroid.inf;

import com.example.testandroid.bean.StudentResponse;

import java.util.List;

public interface ILoadStudentListener {

    void onRefreshFail(String reason);

    void onRefreshSuccess(List<StudentResponse.DataBean> list);

    void onLoadFail(String reason);

    void onLoadSuccess(List<StudentResponse.DataBean> list);
}
