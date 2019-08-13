package com.panda.littlesquirrel.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.panda.littlesquirrel.R;

/**
 * Created by jinjing on 2019/4/4.
 */

public class ErrorStatusDialog extends DialogFragment {

    private TextView tvContent;
    private Button btnConfirm;
    private ImageView ivImage;
    private ConfirmCallBack mconfirmCallBack;
    private boolean onTouchOutside = false;
    private String content;
    private int  imageUrl;
    /**
     * 确定点击事件
     *
     * @param confirmCallBack
     * @return
     */
    public ErrorStatusDialog setOnConfirmClickListener(ConfirmCallBack confirmCallBack) {
        this.mconfirmCallBack = confirmCallBack;
        return this;
    }

    /**
     * 取消回调
     */
    public interface ConfirmCallBack {

        void onConfirm();

    }

    /**
     * 设置内容
     *
     * @param content
     * @return
     */
    public ErrorStatusDialog setContent(String content) {
        this.content = content;
        return this;
    }
    public ErrorStatusDialog setImage(int image) {
        this.imageUrl = image;
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
        View hintView = inflater.inflate(R.layout.box_full_tip_dialog, null);
        initView(hintView);
        return hintView;
    }

    private void initView(View hintView) {
        tvContent=hintView.findViewById(R.id.tv_tip);
        ivImage=hintView.findViewById(R.id.img_error);
        btnConfirm=hintView.findViewById(R.id.btn_ok);
        tvContent.setText(content);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mconfirmCallBack.onConfirm();
            }
        });

    }

}
