package com.example.testandroid.manager;

import com.example.testandroid.api.ILoadStudentListener;
import com.example.testandroid.api.StudentService;
import com.example.testandroid.bean.StudentResponse;
import com.example.testandroid.utils.LoggingInterceptors;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentManager {

    private static final Logger logger = Logger.getLogger(StudentManager.class.getName());
    private static final int PAGE_SIZE = 10;

    private static StudentManager instance;
    private int pageNo = 1;

    public static StudentManager getInstance(){
        if (instance == null) {
            synchronized (StudentManager.class){
                if (instance == null) {
                    instance = new StudentManager();
                }
            }
        }
        return instance;
    }

    public void refresh(ILoadStudentListener iLoadStudentListener){
        pageNo = 1;
        load(iLoadStudentListener,false);
    }

    public void loadMore(ILoadStudentListener iLoadStudentListener){
        pageNo ++;
        load(iLoadStudentListener,true);
    }

    private void load(ILoadStudentListener iLoadStudentListener,boolean load) {
        StudentService service = LoggingInterceptors.getInstance().getRetrofit().create(StudentService.class);
        service.loadList(pageNo,PAGE_SIZE).enqueue(new Callback<StudentResponse>() {
            @Override
            public void onResponse(@NotNull Call<StudentResponse> call, @NotNull Response<StudentResponse> response) {
                StudentResponse studentResponse = response.body();
                if (studentResponse == null) {
                    logger.info("onResponse studentResponse == null");
                    if (iLoadStudentListener != null) {
                        if (load) {
                            iLoadStudentListener.onLoadFail("数据为空");
                        }else {
                            iLoadStudentListener.onRefreshFail("数据为空");
                        }
                    }
                    return;
                }
                List<StudentResponse.DataBean> dataBeanList = studentResponse.getData();
                logger.info("onResponse code="+studentResponse.getCode()+",msg="+studentResponse.getMsg()+",size="+(dataBeanList == null ? 0 : dataBeanList.size()));
                if (iLoadStudentListener != null) {
                    if (load) {
                        iLoadStudentListener.onLoadSuccess(dataBeanList);
                    }else {
                        iLoadStudentListener.onRefreshSuccess(dataBeanList);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<StudentResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                if (iLoadStudentListener != null) {
                    if (load) {
                        iLoadStudentListener.onLoadFail("获取数据失败");
                    }else {
                        iLoadStudentListener.onRefreshFail("获取数据失败");
                    }
                }
            }
        });
    }

}
