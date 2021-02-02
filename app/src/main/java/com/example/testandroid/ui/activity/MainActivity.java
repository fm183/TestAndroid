package com.example.testandroid.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testandroid.AdWebActivity;
import com.example.testandroid.R;
import com.example.testandroid.view.DayWithdrawProgressView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DayWithdrawProgressView dayWithdrawProgressView = findViewById(R.id.day_withdraw_progress);
        dayWithdrawProgressView.setProgress(0.5f);

        dayWithdrawProgressView.setOnClickListener(v ->{
            AdWebActivity.launch(this,"https://peeplocation.in/","");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



}
