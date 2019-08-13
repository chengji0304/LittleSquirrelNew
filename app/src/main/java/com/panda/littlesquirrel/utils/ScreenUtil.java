package com.panda.littlesquirrel.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by YuShuangPing on 2018/10/19.
 */
public class ScreenUtil {
    private static int WIDTH=0;
    //获取屏幕尺寸的宽
    public static int getScreenWidth(Context context){
        if (WIDTH==0){
            DisplayMetrics dm=new DisplayMetrics();
            WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            //把屏幕尺寸信息赋值给DisplayMetrics dm
            wm.getDefaultDisplay().getMetrics(dm);
            WIDTH=dm.widthPixels;
        }
        return WIDTH;
    }
//    public static int dip2px(Context context, int size){
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,size,
//                context.getResources().getDisplayMetrics());
//    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
