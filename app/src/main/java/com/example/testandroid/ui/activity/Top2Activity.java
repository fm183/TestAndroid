package com.example.testandroid.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testandroid.R;
import com.example.testandroid.bean.MyViewMode;
import com.example.testandroid.viewmodule.MyViewModule;

public class Top2Activity extends AppCompatActivity {

    private MyViewModule myViewModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top2);

        new MyFragment().show(getSupportFragmentManager(),"dialog");

        myViewModule = MyViewModule.getInstance(this.getViewModelStore());
        myViewModule.getData().observe(this, myViewMode -> {
            Log.d("MyViewModule ", "Top2Activity onCreate: ");
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        myViewModule.getData().postValue(new MyViewMode());
        return super.onTouchEvent(event);
    }
}
