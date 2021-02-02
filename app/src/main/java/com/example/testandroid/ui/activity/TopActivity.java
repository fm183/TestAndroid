package com.example.testandroid.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.testandroid.R;
import com.example.testandroid.viewmodule.MyViewModule;

public class TopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        Log.d("MainActivity", "TopActivity onCreate: ");
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "TopActivity onClick: ");
                startActivityForResult(new Intent(TopActivity.this,Top2Activity.class),1);
            }
        });

        MyViewModule myViewModule = MyViewModule.getInstance(this.getViewModelStore());
        myViewModule.getData().observe(this,myMode -> {
            Log.d("MyViewModule ", "TopActivity onCreate: ");
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(getClass().getSimpleName(), "onActivityResult: requestCode="+requestCode+",resultCode="+resultCode);
    }
}
