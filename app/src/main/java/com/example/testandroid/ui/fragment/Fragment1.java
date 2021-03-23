package com.example.testandroid.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testandroid.R;

public class Fragment1 extends BaseFragment {

    public static Fragment1 getInstance(){
        return new Fragment1();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment1;
    }
}
