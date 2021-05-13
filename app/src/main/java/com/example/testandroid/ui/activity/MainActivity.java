package com.example.testandroid.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testandroid.R;
import com.example.testandroid.ui.banner.BannerAdapter;
import com.example.testandroid.view.banner.BannerView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String[] urlList = new String[]{
            "http://callshowstatic.jidiandian.cn/upload/20191106/9966b4e978fa452e967d5b50aaed55a6.jpg?imageMogr2/thumbnail/!350x540r/gravity/Center/crop/350x540",
            "http://callshowstatic.jidiandian.cn/upload/20201022/d13d935c4ab7433bbe5c927480859dcd.jpg?imageMogr2/thumbnail/!350x540r/gravity/Center/crop/350x540",
            "http://callshowstatic.jidiandian.cn/upload/20191015/17c0839365be4c18b65870b980b05198.jpg?imageMogr2/thumbnail/!350x540r/gravity/Center/crop/350x540",
            "http://callshowstatic.jidiandian.cn/upload/20201022/f4bbe646827945299136fdcd3fdda7a6.jpg?imageMogr2/thumbnail/!350x540r/gravity/Center/crop/350x540",
            "http://callshowstatic.jidiandian.cn/upload/20200929/5a1519fde52649039d3c3fe1c7f34902.jpg?imageMogr2/thumbnail/!350x540r/gravity/Center/crop/350x540",
            "http://callshowstatic.jidiandian.cn/upload/20190911/9a2ceafa69cc48838d9a2e46bb6bd6ad.gif?imageMogr2/thumbnail/!350x540r/gravity/Center/crop/350x540"
    };

    private BannerView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banner = findViewById(R.id.banner);
        banner.setAdapter(new BannerAdapter(Arrays.asList(urlList)));
        banner.setVisibility(View.VISIBLE);

//        CycleViewPager2 cycleViewPager2 = findViewById(R.id.banner2);
//        new CycleViewPager2Helper(cycleViewPager2)
//                .setAdapter(new MyCyclePagerAdapter(Arrays.asList(urlList)))
//                .setMultiplePagerScaleInTransformer(
//                        UtilsSize.dpToPx(this,20),
//                        UtilsSize.dpToPx(this,20),
//                        0.1f
//                )
//                .setAutoTurning(3000L)
//                .build();
//
//        Banner banner = findViewById(R.id.banner3);
//        banner.setAdapter(new ImageAdapter(Arrays.asList(urlList)));
//                //设置左右页面露出来的宽度及item与item之间的宽度
//        banner.setPageMargin((int) (UtilsSize.getScreenWidth(this) * 0.2f), UtilsSize.dpToPx(this, 10));
////内置ScaleInTransformer，设置切换缩放动画
//        banner.getViewPager2().setPageTransformer(new ScaleInTransformer(0.8f));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("test_state",true);
        Log.d(MainActivity.class.getSimpleName(), "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean testState = savedInstanceState.getBoolean("test_state");
        Log.d(MainActivity.class.getSimpleName(), "onRestoreInstanceState: testState="+testState);
    }
}
