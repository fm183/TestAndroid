package com.example.testandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.testandroid.R;
import com.example.testandroid.utils.UtilsSize;

public class DayWithdrawProgressView extends View {

    private final int[] mColors = new int[2];//进度条颜色（渐变色的2个点）

    private final RectF mRectF = new RectF();
    private final RectF mRectFProgress = new RectF();
    private Paint mPaint;
    private Paint mBasePaint;
    private TextPaint mTextPaint;
    private final Rect rect = new Rect();
    private Paint.FontMetricsInt mFontMetrics;
    /**
     * baseline在背景显示框的y坐标位置
     */
    private float mBaseLineInRect;

    private int mRadio;
    private int mWidth;
    private String percent = "100%";

    public DayWithdrawProgressView(Context context) {
        this(context,null);
    }

    public DayWithdrawProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DayWithdrawProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public DayWithdrawProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(0,0,w,h);
        mRectFProgress.set(0,0,0,h);
        mWidth = w;
        mBaseLineInRect = (h - (mFontMetrics.bottom-mFontMetrics.top)) / 2f + Math.abs(mFontMetrics.top);
    }

    private void init(Context context){
        mRadio = UtilsSize.dpToPx(context,5);

        mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBasePaint.setColor(Color.BLACK);
        mBasePaint.setStrokeCap(Paint.Cap.ROUND);
        mBasePaint.setStrokeWidth(UtilsSize.dpToPx(context,5));

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(UtilsSize.dpToPx(context,5));
        mColors[0] = ContextCompat.getColor(context, R.color.withdraw_alert_btn_start_color);
        mColors[1] = ContextCompat.getColor(context,R.color.withdraw_alert_btn_end_color);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(UtilsSize.dpToPx(context,14));

        mFontMetrics = mTextPaint.getFontMetricsInt();
    }

    public void setProgress(float progress){
        post(() -> {
            float progressWidth = progress * mWidth;
            Log.d(DayWithdrawProgressView.class.getSimpleName(), "setProgress: progressWidth="+progressWidth);
            mRectFProgress.right = progressWidth;
            LinearGradient linearGradient = new LinearGradient(0, 0, progressWidth,0,mColors, null, TileMode.CLAMP);
            mPaint.setShader(linearGradient);
            postInvalidate();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(mRectF,mRadio,mRadio,mBasePaint);
        canvas.drawRoundRect(mRectFProgress,mRadio,mRadio,mPaint);


        mTextPaint.getTextBounds(percent,0,percent.length(),rect);
        int x = (mWidth >> 1) -  rect.centerX();
        canvas.drawText(percent,x,mBaseLineInRect,mTextPaint);
    }
}
