package com.example.testandroid.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.testandroid.R;

import java.util.Random;

public class LuckyDrawConstraintView extends ConstraintLayout {

    private static final int ANIM_DELAYED = 2000;
    private static final int NORMAL_STATUS = 0;
    private static final int NO_CHANGE_STATUS = 1;

    private final View[] lotteryArray;
    private int luckyIndex = -1;
    private int luckStatus = NORMAL_STATUS;//当前可以抽奖状态
    private int mLastIndex = -1;
    private ValueAnimator mAnimator = null;

    public LuckyDrawConstraintView(@NonNull Context context) {
        this(context,null);
    }

    public LuckyDrawConstraintView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LuckyDrawConstraintView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_luck_draw, this);
        lotteryArray = new View[]{
                findViewById(R.id.include_lottery0),
                findViewById(R.id.include_lottery1),
                findViewById(R.id.include_lottery2),
                findViewById(R.id.include_lottery3),
                findViewById(R.id.include_lottery4),
                findViewById(R.id.include_lottery5),
                findViewById(R.id.include_lottery6),
                findViewById(R.id.include_lottery7)};

        updateSelectedBg();
        findViewById(R.id.include_button).setOnClickListener(v -> startLuck());
    }


    public void setNoChange(){
        luckStatus = NO_CHANGE_STATUS;
    }

    private void startLuck(){
        if (luckStatus == NO_CHANGE_STATUS) {
            Toast.makeText(getContext(),"没有机会了",Toast.LENGTH_SHORT).show();
            return;
        }
        Random random = new Random();
        luckyIndex = random.nextInt(8);
        startAnim();
    }

    private void startAnim(){
        if (mAnimator != null && mAnimator.isRunning()) {
            return;
        }
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(ANIM_DELAYED);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(mUpdateListener);
        mAnimator.addListener(mAdapterListener);
        int count = 24 + luckyIndex;
        Log.d(getClass().getSimpleName(), "startAnim: luckyIndex="+luckyIndex+",count="+count+",mLastIndex="+mLastIndex);
        mAnimator.setIntValues(mLastIndex + 1,count);
        mLastIndex = -1;
        mAnimator.start();
    }

    private void updateSelectedBg(){
        for (int i = 0;i < lotteryArray.length;i ++){
            View view = lotteryArray[i];
            view.setBackgroundResource(i == mLastIndex ? R.drawable.bg_lottery_selected : R.drawable.bg_lottery);
        }
    }


    private final ValueAnimator.AnimatorUpdateListener mUpdateListener = animation -> {
        int index = (int) animation.getAnimatedValue();
        mLastIndex = index % 8;
        updateSelectedBg();
    };

    private final AnimatorListenerAdapter mAdapterListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mLastIndex = luckyIndex;
            updateSelectedBg();
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }
}
