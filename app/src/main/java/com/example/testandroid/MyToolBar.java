package com.example.testandroid;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyToolBar extends FrameLayout {

    private final TextView tvTitle;
    private final TextView tvRight;
    private final ImageView ivBack;
    private final ImageView ivRight;
    private OnRightClickListener onRightClickListener;

    public MyToolBar(@NonNull Context context) {
        this(context,null);
    }

    public MyToolBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyToolBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.common_text_title_bar, this);

        tvTitle = findViewById(R.id.tv_title);
        tvRight = findViewById(R.id.tv_right);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyToolBar);
        String title = array.getString(R.styleable.MyToolBar_my_tool_bar_title);
        tvTitle.setText(title);

        int titleColor = array.getColor(R.styleable.MyToolBar_my_tool_bar_title_color, Color.WHITE);
        tvTitle.setTextColor(titleColor);

        String rightText = array.getString(R.styleable.MyToolBar_my_tool_bar_right_text);
        tvRight.setText(rightText);

        int rightColor = array.getColor(R.styleable.MyToolBar_my_tool_bar_right_color, Color.TRANSPARENT);
        tvRight.setTextColor(rightColor);

        boolean isShowBack = array.getBoolean(R.styleable.MyToolBar_my_tool_bar_back_show,true);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setVisibility(isShowBack ? VISIBLE : GONE);

        ivRight = findViewById(R.id.iv_right);
        int resourceId = array.getResourceId(R.styleable.MyToolBar_my_tool_bar_right_icon,0);
        if (resourceId != 0) {
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageResource(resourceId);
            ivRight.setOnClickListener(v -> {
                if (onRightClickListener != null) {
                    onRightClickListener.onRightClick();
                }
            });
        }

        tvRight.setVisibility(VISIBLE);

        array.recycle();

        ivBack.setOnClickListener(v -> {
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        });

        tvRight.setOnClickListener(v -> {
            if (onRightClickListener != null) {
                onRightClickListener.onRightClick();
            }
        });
    }

    public void setTitle(CharSequence title){
        tvTitle.setText(title);
    }

    public void setRightVisibility(int visibility){
        tvRight.setVisibility(visibility);
    }

    public void setRightText(CharSequence text){
        tvRight.setText(text);
    }

    public void setRightIcon(int resourceId){
        ivRight.setImageResource(resourceId);
    }

    public void setBackVisibility(int visibility){
        ivBack.setVisibility(visibility);
    }

    public boolean isBackVisibility(){
        return ivBack.getVisibility() == VISIBLE;
    }

    public void setOnRightClickListener(OnRightClickListener onRightClickListener) {
        this.onRightClickListener = onRightClickListener;
    }

    public interface OnRightClickListener{
        void onRightClick();
    }

}
