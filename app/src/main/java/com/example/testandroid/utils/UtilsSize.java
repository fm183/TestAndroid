package com.example.testandroid.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class UtilsSize {
    public static int dpToPx(Context context, float fDp) {
        if (null == context)
            return 0;

        return Math.round(fDp * context.getResources().getDisplayMetrics().density);
    }

    public static int pxToDp(Context context, float fPx) {
        if (null == context)
            return 0;

        return Math.round(fPx / context.getResources().getDisplayMetrics().density);
    }

    public static int getDPI(Context context) {
        if (null == context)
            return 0;

        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static int getScreenWidth(Context context) {
        if (null == context)
            return 0;

        try {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            if (null != dm) {
                return dm.widthPixels;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int getScreenHeight(Context context) {
        if (null == context)
            return 0;

        try {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            if (null != dm) {
                return dm.heightPixels;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int getRealScreenHeight(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                DisplayMetrics outMetrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
                int widthPixels = outMetrics.widthPixels;
                return outMetrics.heightPixels;
            }
        } catch (Exception e) {

        }
        return 0;
    }
}
