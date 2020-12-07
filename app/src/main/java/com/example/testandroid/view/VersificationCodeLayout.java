package com.example.testandroid.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testandroid.R;

import java.util.ArrayList;
import java.util.List;

public class VersificationCodeLayout extends FrameLayout {

    private final TextView tvCode1;
    private final TextView tvCode2;
    private final TextView tvCode3;
    private final TextView tvCode4;
    private final InputMethodManager imm;
    private final EditText editText;
    private final List<String> codes = new ArrayList<>();

    public VersificationCodeLayout(@NonNull Context context) {
        this(context,null);
    }

    public VersificationCodeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VersificationCodeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_versification_code, this, true);

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        tvCode1 = findViewById(R.id.tv_code1);
        tvCode2 = findViewById(R.id.tv_code2);
        tvCode3 = findViewById(R.id.tv_code3);
        tvCode4 = findViewById(R.id.tv_code4);
        editText = findViewById(R.id.et_code);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && s.length()>0) {
                    editText.setText("");
                    if(codes.size() < 4){
                        codes.add(s.toString());
                        showCode();
                    }
                }
            }
        });

        // 监听验证码删除按键
        editText.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && codes.size()>0) {
                codes.remove(codes.size()-1);
                showCode();
                return true;
            }
            return false;
        });
    }

    /**
     * 显示输入的验证码
     */
    private void showCode(){
        String code1 = "";
        String code2 = "";
        String code3 = "";
        String code4 = "";
        if(codes.size()>=1){
            code1 = codes.get(0);
        }
        if(codes.size()>=2){
            code2 = codes.get(1);
        }
        if(codes.size()>=3){
            code3 = codes.get(2);
        }
        if(codes.size()>=4){
            code4 = codes.get(3);
        }
        tvCode1.setText(code1);
        tvCode2.setText(code2);
        tvCode3.setText(code3);
        tvCode4.setText(code4);

        callBack();//回调
    }


    /**
     * 回调
     */
    private void callBack(){
        if(onInputListener==null){
            return;
        }
        if(codes.size()==4){
            onInputListener.onSuccess(getPhoneCode());
        }else{
            onInputListener.onInput();
        }
    }

    //定义回调
    public interface OnInputListener{
        void onSuccess(String code);
        void onInput();
    }
    private OnInputListener onInputListener;
    public void setOnInputListener(OnInputListener onInputListener){
        this.onInputListener = onInputListener;
    }

    /**
     * 显示键盘
     */
    public void showSoftInput(){
        //显示软键盘
        if(imm!=null && editText!=null) {
            editText.postDelayed(() -> imm.showSoftInput(editText, 0),200);
        }
    }

    /**
     * 获得手机号验证码
     * @return 验证码
     */
    public String getPhoneCode(){
        StringBuilder sb = new StringBuilder();
        for (String code : codes) {
            sb.append(code);
        }
        return sb.toString();
    }


}
