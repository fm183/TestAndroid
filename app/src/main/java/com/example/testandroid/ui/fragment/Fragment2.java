package com.example.testandroid.ui.fragment;

import com.example.testandroid.R;

public class Fragment2 extends BaseFragment {

    public static Fragment2 getInstance(){
        return new Fragment2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment2;
    }
}
