package com.example.testandroid.api;

import com.example.testandroid.bean.StudentResponse;
import com.example.testandroid.constant.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StudentService {

    @GET(Constant.LIST_STUDENT)
    Call<StudentResponse> loadList(@Query("page_no") int page_no,@Query("page_size") int page_size);

}
