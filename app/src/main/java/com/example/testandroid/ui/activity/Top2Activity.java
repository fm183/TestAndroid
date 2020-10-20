package com.example.testandroid.ui.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testandroid.R;

public class Top2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top2);
        Log.d("MainActivity", "Top2Activity onCreate: ");

        new MyFragment().show(getSupportFragmentManager(),"dialog");
    }
}
