package com.example.testandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.testandroid.utils.UtilsSize;


public class NumberResultView extends View {

    private static final String TAG = NumberResultView.class.getSimpleName();

    private int mWidth;
    private int mHeight;
    private TextPaint mTextPaint;
    private Paint mPaint;
    private String text = "1";
    private final Rect rect = new Rect();
    private Paint.FontMetricsInt mFontMetrics;
    private int mLineWidth;
    private float mRadius;

    /**
     * baseline在背景显示框的y坐标位置
     */
    private float mBaseLineInRect;

    public NumberResultView(Context context) {
        this(context,null);
    }

    public NumberResultView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(UtilsSize.dpToPx(context,28));
        mFontMetrics = mTextPaint.getFontMetricsInt();

        mLineWidth = UtilsSize.dpToPx(context,1);
        mRadius = UtilsSize.dpToPx(context,4) ;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineWidth);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = w;
        mHeight = h;
        mBaseLineInRect = (mHeight - (mFontMetrics.bottom-mFontMetrics.top)) / 2f + Math.abs(mFontMetrics.top);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mTextPaint.getTextBounds(text,0,text.length(),rect);
        int x = (mWidth >> 1) -  rect.centerX();
        canvas.drawText(text,x,mBaseLineInRect,mTextPaint);

        float y = mHeight - mLineWidth / 2f;
        canvas.drawLine(0,y,mWidth,y,mPaint);
    }
}
