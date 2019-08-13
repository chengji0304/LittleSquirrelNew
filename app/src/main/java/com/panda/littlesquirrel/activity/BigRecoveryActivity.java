package com.panda.littlesquirrel.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.view.BackAndTimerView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BigRecoveryActivity extends BaseActivity {
    @Bind(R.id.image_service_tel)
    ImageView imageServiceTel;
    @Bind(R.id.image_device_num)
    ImageView imageDeviceNum;
    @Bind(R.id.tv_service_tel)
    TextView tvServiceTel;
    @Bind(R.id.tv_device_num)
    TextView tvDeviceNum;
    @Bind(R.id.btn_my_recycler)
    Button btnMyRecycler;
    @Bind(R.id.img_banner)
    ImageView imgBanner;
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.img_routine)
    ImageView imgRoutine;
    @Bind(R.id.activity_big_recovery)
    RelativeLayout activityBigRecovery;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
   private  PreferencesUtil prf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this,getApplication(),360);
        setContentView(R.layout.activity_big_recovery);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        prf=new PreferencesUtil(this);
        initData();
        initTimer();
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        btnMyRecycler.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        backAndTime.start();
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backAndTime.stop();


    }

    @Override
    public void getFindData(String reciveData) {

    }

    private void initTimer() {
        backAndTime.setTimer(280);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.setBackVisableStatue(true);
        //backAndTime.start();
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                backAndTime.stop();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                backAndTime.stop();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }


}
