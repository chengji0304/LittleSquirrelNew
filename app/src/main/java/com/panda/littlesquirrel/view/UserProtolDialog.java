package com.panda.littlesquirrel.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseDialogFragment;
import com.panda.littlesquirrel.config.MyApplication;
import com.panda.littlesquirrel.utils.TextJustification;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jinjing on 2019/3/28.
 */

public class UserProtolDialog extends DialogFragment  {
    private static volatile UserProtolDialog instance;
    private Thread mThread;
    private JustifyTextView mTvProtocaol;
    private boolean onTouchOutside = false;
    private Button button;
    private ConfirmCallBack mconfirmCallBack;
    private CloseCallBack mcloseCallBack;
    /*
    public static UserProtolDialog getInstance() {
        if (instance == null) {
            synchronized (UerTimerDialog.class) {
                if (instance == null) {
                    instance = new UserProtolDialog();
                }
            }
        }
        return instance;
    }
    */

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        setLayoutResource(R.layout.user_protocol_dialog);
//        return super.onCreateDialog(savedInstanceState);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 设置背景透明
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // 去掉标题 死恶心死恶心的
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        // set cancel on touch outside
        getDialog().setCanceledOnTouchOutside(onTouchOutside);
        View hintView = inflater.inflate(R.layout.user_protocol_dialog, null);
        initView(hintView);
        return hintView;
    }

    private void initView(View hintView) {
        mTvProtocaol = hintView.findViewById(R.id.tv_protocol);
        setProtoText();
        button = hintView.findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcloseCallBack.onClose();
            }
        });
    }

//    @Override
//    public void onResume() {
//
//
//        //mTvProtocaol=mDialog.findViewById(R.id.tv_protocol);
//        setProtoText();
//        mThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(20000L);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mDialog.cancel();
//                            mDialog.dismiss();
//                            if (mOnCloseDialogListener != null) {
//                                mOnCloseDialogListener.onCloseDialog();
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        mThread.start();
//        super.onResume();
//
//
//    }

    private void setProtoText() {
        InputStream inputStream = null;
        try {
            inputStream = getActivity().getAssets().open("user_protocol.txt");
            String str = IOUtils.toString(inputStream, "utf-8");
            inputStream.close();
            mTvProtocaol.setText(str);
            mTvProtocaol.setTypeface(MyApplication.pingFang_medium);
            //  TextJustification.justify(mTvProtocaol,mTvProtocaol.getWidth());
            // mTvProtocaol.setMovementMethod(ScrollingMovementMethod.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//    @Override
//    public void onClick(View v) {
//
//        if (mOnCloseDialogListener != null) {
//            mOnCloseDialogListener.onCloseDialog();
//        }
//        mDialog.dismiss();
//        mThread.interrupt();
//    }

    /**
     * 确定点击事件
     *
     * @param confirmCallBack
     * @return
     */
    public UserProtolDialog setOnConfirmClickListener(ConfirmCallBack confirmCallBack) {
        this.mconfirmCallBack = confirmCallBack;
        return this;
    }

    /**
     * 取消点击事件
     *
     * @param closeCallBack
     * @return
     */
    public UserProtolDialog setOnCloseClickListener(CloseCallBack closeCallBack) {
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
