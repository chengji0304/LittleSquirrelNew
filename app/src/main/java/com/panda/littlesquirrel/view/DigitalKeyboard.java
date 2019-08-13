package com.panda.littlesquirrel.view;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.utils.SoundPlayUtil;


/**
 * Created by jinjing on 2019/3/22.
 */

public class DigitalKeyboard extends RelativeLayout implements View.OnClickListener {
    public EditText mEditText;
    private OnConfirmListener mConfirm;
    // private SoundPool mSoundPool;


    public static abstract interface OnConfirmListener {
        public abstract void onConfirm();
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.mConfirm = onConfirmListener;
    }

    public DigitalKeyboard(Context context) {
        super(context);

        mEditText = new EditText(context);
        inintView();
        // initData(context);

    }


    public DigitalKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);

        mEditText = new EditText(context);
        inintView();
        //  initData(context);

    }


    private void inintView() {
        View loaclView = inflate(getContext(), R.layout.recycler_digital_keyboard, this);
        loaclView.findViewById(R.id.btn_num_01).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_num_0).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_num_1).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_num_2).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_num_3).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_num_4).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_num_5).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_num_6).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_num_7).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_num_8).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_num_9).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_del).setOnClickListener(this);
        loaclView.findViewById(R.id.btn_ok).setOnClickListener(this);


    }

    public void setEditText(EditText paramEditText) {
        this.mEditText = paramEditText;

    }
    public  void release(){
        //SoundPlayUtils.release();
    }
    public String getEditText() {
        return mEditText.getText().toString();
    }

    @Override
    public void onClick(View v) {
        if (mEditText != null) {
            switch (v.getId()) {
                case R.id.btn_num_01:
                    break;
                case R.id.btn_num_0:
                    if (getEditText().length() <= 12) {
                        SoundPlayUtil.play(0);

                        mEditText.getText().insert(mEditText.getSelectionStart(), "0");
                    } else {

                    }


                    break;
                case R.id.btn_num_1:
                    if (getEditText().length() <= 12) {
                        SoundPlayUtil.play(1);
                        //SoundPlayUtils.StartMusic(1);
                        mEditText.getText().insert(mEditText.getSelectionStart(), "1");
                        Logger.e("lenth---->" + getEditText().length());
                    } else {

                    }


                    break;
                case R.id.btn_num_2:
                    if (getEditText().length() <= 12) {
                        SoundPlayUtil.play(2);
                        //SoundPlayUtils.StartMusic(2);
                        mEditText.getText().insert(mEditText.getSelectionStart(), "2");
                    }


                    break;
                case R.id.btn_num_3:
                    if (getEditText().length() <= 12) {
                        SoundPlayUtil.play(3);
                        // SoundPlayUtils.StartMusic(3);
                        mEditText.getText().insert(mEditText.getSelectionStart(), "3");
                    }


                    break;
                case R.id.btn_num_4:
                    if (getEditText().length() <= 12) {
                        SoundPlayUtil.play(4);
                        // SoundPlayUtils.StartMusic(4);
                        mEditText.getText().insert(mEditText.getSelectionStart(), "4");
                    }


                    break;
                case R.id.btn_num_5:
                    if (getEditText().length() <= 12) {
                        // SoundPlayUtils.StartMusic(5);
                        SoundPlayUtil.play(5);
                        mEditText.getText().insert(mEditText.getSelectionStart(), "5");
                    }


                    break;
                case R.id.btn_num_6:
                    if (getEditText().length() <= 12) {
                        SoundPlayUtil.play(6);
                        // SoundPlayUtils.StartMusic(6);
                        mEditText.getText().insert(mEditText.getSelectionStart(), "6");
                    }


                    break;
                case R.id.btn_num_7:
                    if (getEditText().length() <= 12) {
                        SoundPlayUtil.play(7);
                        // SoundPlayUtils.StartMusic(7);
                        mEditText.getText().insert(mEditText.getSelectionStart(), "7");
                    }


                    break;
                case R.id.btn_num_8:
                    if (getEditText().length() <= 12) {
                        SoundPlayUtil.play(8);
                        // SoundPlayUtils.StartMusic(8);
                        mEditText.getText().insert(mEditText.getSelectionStart(), "8");
                    }


                    break;
                case R.id.btn_num_9:
                    if (getEditText().length() <= 12) {
                        SoundPlayUtil.play(9);
                        // SoundPlayUtils.StartMusic(9);
                        mEditText.getText().insert(mEditText.getSelectionStart(), "9");
                    }


                    break;
                case R.id.btn_del:
                    if (getEditText().length() > 0) {
                        mEditText.getText().delete(mEditText.getSelectionStart() - 1, mEditText.getSelectionStart());
                    }

                    break;
                case R.id.btn_ok:

                    if (mConfirm != null) {

                        this.mConfirm.onConfirm();
                    }
                    break;

            }
        }

    }


}
