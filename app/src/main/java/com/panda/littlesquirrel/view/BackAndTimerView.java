package com.panda.littlesquirrel.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.panda.littlesquirrel.R;

/**
 * Created by jinjing on 2019/3/22.
 */

public class BackAndTimerView extends RelativeLayout implements View.OnClickListener {
    private Button mBack;
    private OnBackListener mBackListener;
    private CountDownTimer mCountDownTimer;
    private int mSecondTime;
    private Button mTimer;
    private OnTimerFinishListener mTimerFinishListener;

    public static abstract interface OnBackListener{
        public abstract void onBack();
    }

    public static abstract  interface OnTimerFinishListener{
        public abstract void onTimerFinish();
    }
    public BackAndTimerView(Context context) {
        super(context);
        initView();
    }

    public BackAndTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View localView= inflate(getContext(), R.layout.back_and_timer,this);
        this.mBack=findViewById(R.id.btn_back);
        this.mTimer=findViewById(R.id.btn_timer);
        this.mBack.setOnClickListener(this);
    }
    public int getCurrentTime(){
        return Integer.valueOf(mTimer.getText().toString().substring(0,mTimer.getText().toString().length()-1));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
             if(mBackListener!=null){
                 if(mCountDownTimer!=null){
                     mCountDownTimer.cancel();
                 }

                 mBackListener.onBack();
             }

                break;
            case R.id.btn_timer:

                break;
        }
    }
    /*
     点击有效
     */
    public void setBackEnable(boolean paraBoolean){
        if(paraBoolean){
            this.mBack.setEnabled(true);

        }else {
            this.mBack.setEnabled(false);

        }

    }
    /*
     可见状态
     */
    public void setBackVisableStatue(Boolean paraBoolean){
        if(paraBoolean.booleanValue()){
            this.mBack.setVisibility(VISIBLE);


        }else {
            this.mBack.setVisibility(GONE);


        }
    }

    public void setTimerVisableStatue(Boolean paraBoolean){
        if(paraBoolean.booleanValue()){
            this.mTimer.setVisibility(VISIBLE);


        }else {
            this.mTimer.setVisibility(GONE);


        }
    }
    /*
     可见状态
     */
    public void setVisableStatue(Boolean paraBoolean){
        if(paraBoolean.booleanValue()){
            this.mBack.setVisibility(VISIBLE);
            this.mTimer.setVisibility(VISIBLE);

        }else {
            this.mBack.setVisibility(GONE);
            this.mTimer.setVisibility(GONE);

        }
    }

    public void setOnBackListener(OnBackListener onBackListener){
        this.mBackListener=onBackListener;

    }

    public void setOnTimerFinishListener(OnTimerFinishListener onTimerFinishListener){
          this.mTimerFinishListener=onTimerFinishListener;
    }

    public void setTimer(int time){
        this.mSecondTime=time;
        if(this.mCountDownTimer!=null){
            this.mCountDownTimer.cancel();

        }
        mCountDownTimer=new MyCountDownTime((1050+1000*this.mSecondTime),1000L);
    }

    private class MyCountDownTime extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
           if((secondsRemaining==0)&&(mTimerFinishListener!=null)){
               mTimerFinishListener.onTimerFinish();
           }else{
               mTimer.setText(secondsRemaining+"秒");
           }
        }

        @Override
        public void onFinish() {

        }
    }

    public void start(){
        if(mCountDownTimer!=null){
           mCountDownTimer.start();
        }
    }
    public void stop(){
        if(mCountDownTimer!=null){
            mCountDownTimer.cancel();
        }
    }
}
