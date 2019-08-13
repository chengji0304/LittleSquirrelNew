package com.panda.littlesquirrel.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jinjing on 2019/4/4.
 */

public class MyDialog extends AlertDialog {
    //    style引用style样式
    public MyDialog(Context context, int width, int height, View layout, int style) {

        super(context, style);

        setContentView(layout);

        Window window = getWindow();

        WindowManager.LayoutParams params = window.getAttributes();

       // params.gravity = Gravity.CENTER;

        window.setAttributes(params);
    }
}

