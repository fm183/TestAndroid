package com.example.testandroid.ui.activity;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testandroid.R;
import com.example.testandroid.ui.fragment.Fragment1;
import com.example.testandroid.ui.fragment.Fragment2;
import com.example.testandroid.view.LuckyLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //透明导航栏

        TabLayout tabLayout = findViewById(R.id.tab);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setAdapter(new MainFragmentPagerAdapter(this));
        viewPager2.setOffscreenPageLimit(2);
        TabLayoutMediator mTabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(position == 0 ?"title1" : "title2"));
        mTabLayoutMediator.attach();


        List<String> data = new ArrayList<>();
        data.add("1元");
        data.add("礼包");
        data.add("0.5元");
        data.add("???元");
        data.add("400元");
        data.add("礼包");
        data.add("50元");
        data.add("???元");
        LuckyLayout luckyLayout = findViewById(R.id.luck_layout);
        luckyLayout.setLuckyText(data);
    }
    private static class MainFragmentPagerAdapter extends FragmentStateAdapter {

        public MainFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new Fragment1();
            }
            return new Fragment2();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



}
