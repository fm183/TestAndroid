package com.example.testandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.testandroid.utils.UtilsSize;

public class NumberLockView extends View {

    private static final String[] numbers = new String[]{"7","8","9","4","5","6","1","2","3","","0",""};
    private TextPaint textPaint;
    private int mWidth;
    private int mHeight;
    private Rect rect = new Rect();

    public NumberLockView(Context context) {
        this(context,null);
    }

    public NumberLockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberLockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(UtilsSize.dpToPx(context,28));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawText(numbers[0],0,UtilsSize.dpToPx(getContext(),50),textPaint);
        canvas.drawText(numbers[1],mWidth / 3f,UtilsSize.dpToPx(getContext(),50),textPaint);
        canvas.drawText(numbers[2],mWidth / 2f,UtilsSize.dpToPx(getContext(),50),textPaint);
    }
}
