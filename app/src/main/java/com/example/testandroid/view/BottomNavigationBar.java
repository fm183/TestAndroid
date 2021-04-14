package com.example.testandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.testandroid.R;

public class BottomNavigationBar extends LinearLayout implements View.OnClickListener {

    private Paint paint;
    private Path path;
    private float width;
    private int currentPosition = 0;
    private onBottomNavClickListener listener;

//    private String[] tabText = {"打卡", "发现", "消息", "我的"};
    //未选中icon
    private int[] normalIcon = {R.drawable.icon_home, R.drawable.icon_new, R.drawable.icon_person, R.drawable.icon_person};
    //选中时icon
    private int[] selectIcon = {R.drawable.icon_home_lan, R.drawable.icon_new_lan, R.drawable.icon_person_lan, R.drawable.icon_person_lan};

    private ImageView img1, img2, imgCenter, img3, img4;

    private ViewPager viewPager;

    public BottomNavigationBar(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_navigator, this);

        img1 = view.findViewById(R.id.first);
        img2 = view.findViewById(R.id.second);
        imgCenter = view.findViewById(R.id.centerIcon);
        img3 = view.findViewById(R.id.third);
        img4 = view.findViewById(R.id.forth);
        setWillNotDraw(false);

        //2、通过Resources获取
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        imgCenter.setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setShadowLayer(30,0,20,Color.BLACK);
        path.moveTo(0, dip2px(28));

        path.lineTo(0, dip2px(28));
        path.quadTo(0, dip2px(28), width / 8  - dip2px(25), dip2px(18));
        path.quadTo(width / 8, -45, width / 8  + dip2px(25), dip2px(18));
        path.quadTo(width / 8  + dip2px(30), dip2px(28), width - dip2px(150), dip2px(28));
        path.lineTo(width, dip2px(28));
        path.lineTo(width, dip2px(71));
        path.lineTo(0, dip2px(71));
        path.close();
        canvas.drawPath(path, paint);
        super.onDraw(canvas);
    }

    /**
     * 根据屏幕的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setUpWithViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first:
                if (currentPosition == 0) {
                    break;
                }
                setUnSelect(currentPosition);
                currentPosition = 0;
                viewPager.setCurrentItem(currentPosition,true);
                img1.setImageResource(selectIcon[currentPosition]);
                break;
            case R.id.second:
                if (currentPosition == 1) {
                    break;
                }
                setUnSelect(currentPosition);
                currentPosition = 1;
                viewPager.setCurrentItem(currentPosition,true);
                img2.setImageResource(selectIcon[currentPosition]);
                break;
            case R.id.third:
                if (currentPosition == 2) {
                    break;
                }
                setUnSelect(currentPosition);
                currentPosition = 2;
                viewPager.setCurrentItem(currentPosition,true);
                img3.setImageResource(selectIcon[currentPosition]);
                break;
            case R.id.forth:
                if (currentPosition == 3) {
                    break;
                }
                setUnSelect(currentPosition);
                currentPosition = 3;
                viewPager.setCurrentItem(currentPosition,true);
                img4.setImageResource(selectIcon[currentPosition]);
                break;
            case R.id.centerIcon:
                if (listener != null) {
                    listener.onCenterIconClick();
                }
                break;
        }
    }

    private void setUnSelect(int position) {
        switch (position) {
            case 0:
                img1.setImageResource(normalIcon[0]);
                break;
            case 1:
                img2.setImageResource(normalIcon[1]);
                break;
            case 2:
                img3.setImageResource(normalIcon[2]);
                break;
            case 3:
                img4.setImageResource(normalIcon[3]);
                break;
        }
    }

    public interface onBottomNavClickListener {
        void onCenterIconClick();
    }

    public void setOnListener(onBottomNavClickListener listener){
        this.listener = listener;
    }
}