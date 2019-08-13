package com.panda.littlesquirrel.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jinjing on 2019/3/27.
 */

public class UerTimerDialog extends BaseDialogFragment implements View.OnClickListener {
    private static volatile UerTimerDialog instance;
    TextView mTvTimetip;
    private Thread mThread;


    public static UerTimerDialog getInstance() {
        if (instance == null) {
            synchronized (UerTimerDialog.class) {
                instance = new UerTimerDialog();
            }
        }
        return instance;
    }

    @Override
    public void onClick(View v) {
        instance.dismiss();
        mThread.interrupt();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setLayoutResource(R.layout.user_deliver_time_dialog);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {

        this.mDialog.findViewById(R.id.btn_ok).setOnClickListener(this);
        mTvTimetip=(TextView) this.mView.findViewById(R.id.tv_time_tip);
        mTvTimetip.setText("投递时间为8:30-21:00哦~");
        mThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(5000L);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.cancel();
                            mDialog.dismiss();
                            if(mOnCloseDialogListener!=null){
                                mOnCloseDialogListener.onCloseDialog();
                            }
                        }
                    });
                }catch (Exception e){
                    e.getMessage();
                }

            }
        });
        mThread.start();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThread.interrupt();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
