package com.situ.com.myscrollview;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by xukai on 2017/6/23.
 */

public class Utils {
    private static final String TAG = "xukai.Utils";
    public static int GetScreenWidth(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
    public static int GetScreenHeight(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
    public static int GetScreenDenityDpi(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }
}
