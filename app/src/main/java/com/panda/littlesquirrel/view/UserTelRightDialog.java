package com.panda.littlesquirrel.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseDialogFragment;
import com.panda.littlesquirrel.entity.UserInfoCache;

/**
 * Created by jinjing on 2019/3/28.
 */

public class UserTelRightDialog extends DialogFragment {
    private TextView tvContent;
    private Button btnConfirm;
    private Button btnClose;

    private  ConfirmCallBack mconfirmCallBack;
    private  CloseCallBack mcloseCallBack;
    private boolean onTouchOutside = false;
    private String content;


    /**
     * 设置内容
     *
     * @param content
     * @return
     */
    public UserTelRightDialog setContent(String content) {
        this.content = content;
        return this;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 设置背景透明
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // 去掉标题 死恶心死恶心的
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        // set cancel on touch outside
        getDialog().setCanceledOnTouchOutside(onTouchOutside);
        View hintView = inflater.inflate(R.layout.user_tel_right_dialog, null);
        initView(hintView);
        return hintView;
    }

    private void initView(View hintView) {
        tvContent=hintView.findViewById(R.id.tv_tel_num);
        btnConfirm=hintView.findViewById(R.id.btn_ok);
        btnClose=hintView.findViewById(R.id.btn_close);

        tvContent.setText(content.substring(0,3)+" "+content.substring(3,7)+" "+content.substring(7,11));
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mconfirmCallBack.onConfirm();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcloseCallBack.onClose();
            }
        });
    }

    /**
     * 确定点击事件
     *
     * @param confirmCallBack
     * @return
     */
    public UserTelRightDialog setOnConfirmClickListener(ConfirmCallBack confirmCallBack) {
        this.mconfirmCallBack = confirmCallBack;
        return this;
    }

    /**
     * 取消点击事件
     *
     * @param closeCallBack
     * @return
     */
    public UserTelRightDialog setOnCloseClickListener(CloseCallBack closeCallBack) {
        this.mcloseCallBack = closeCallBack;
        return this;
    }
    /**
     * 取消回调
     */
    public interface ConfirmCallBack {

        void onConfirm();

    }

    /**
     * 取消回调
     */
    public interface CloseCallBack {

        void onClose();

    }



}
