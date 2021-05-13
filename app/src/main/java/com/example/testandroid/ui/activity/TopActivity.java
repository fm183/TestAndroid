package com.example.testandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testandroid.R;
import com.example.testandroid.viewmodule.MyViewModule;

public class TopActivity extends AppCompatActivity {

    private static final String TAG = "==========activity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        int size = 0;
        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()){
                Log.d(TAG, "onCreate: key="+key+",value="+savedInstanceState.get(key));
            }
        }
        Log.d(TAG, "onCreate: size="+size);
        MyViewModule myViewModule = MyViewModule.getInstance(this.getViewModelStore());
        myViewModule.getData().observe(this,myMode -> Log.d("MyViewModule ", "TopActivity onCreate: "));

        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,new BlankFragment())
                .commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        long time = System.currentTimeMillis();
        outState.putLong("test_time",time);
        Log.d(TAG, "onSaveInstanceState: time="+time);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        long time = savedInstanceState.getLong("test_time");
        Log.d(TAG, "onRestoreInstanceState: time="+time);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(getClass().getSimpleName(), "onActivityResult: requestCode="+requestCode+",resultCode="+resultCode);
    }
}
