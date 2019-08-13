package com.panda.littlesquirrel.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jinjing on 2019/3/27.
 */

public class BaseDialogFragment extends DialogFragment {
    public AlertDialog mDialog;
    private int mHeight;
    private int mLayoutResours;
    protected OnCloseDialogListener mOnCloseDialogListener;
    public View mView;
    private int mWidth;
    private int mYBias;

    public static abstract interface OnCloseDialogListener{
        public abstract void onCloseDialog();
    }
    public void setOnCloseDialogListener(OnCloseDialogListener onCloseDialogListener){
        this.mOnCloseDialogListener=onCloseDialogListener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        mView= LayoutInflater.from(getActivity()).inflate(mLayoutResours,null);
        dialog.setView(mView);
        mDialog=dialog.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });

        return mDialog;

    }
    public  void setLayoutResource(int resource){
        mLayoutResours=resource;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // 去掉标题 死恶心死恶心的
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        Window localWindow=getDialog().getWindow();
//        localWindow.setGravity(Gravity.CENTER);


    }
}
