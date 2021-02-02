package com.example.testandroid.viewmodule;

import androidx.annotation.NonNull;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.example.testandroid.bean.MyViewMode;

public class MyViewModule extends ViewModel {

    private static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory() {
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            MyViewModule viewModel = new MyViewModule();
            return (T) viewModel;
        }
    };

    @NonNull
    public static MyViewModule getInstance(ViewModelStore viewModelStore) {
        ViewModelProvider viewModelProvider = new ViewModelProvider(viewModelStore, FACTORY);
        return (MyViewModule)viewModelProvider.get(MyViewModule.class);
    }

    private final MutableLiveData<MyViewMode> data;

    private MyViewModule(){
        data = new MutableLiveData<>();
    }

    public MutableLiveData<MyViewMode> getData() {
        return data;
    }

}
