package com.panda.littlesquirrel.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeliverListActivity extends BaseActivity {

    @Bind(R.id.image_service_tel)
    ImageView imageServiceTel;
    @Bind(R.id.tv_service_tel)
    TextView tvServiceTel;
    @Bind(R.id.image_device_num)
    ImageView imageDeviceNum;
    @Bind(R.id.tv_device_num)
    TextView tvDeviceNum;
    @Bind(R.id.btn_my_recycler)
    Button btnMyRecycler;
    @Bind(R.id.ll_top)
    FrameLayout llTop;
    @Bind(R.id.rv_deliver)
    RecyclerView rvDeliver;
    @Bind(R.id.tv_tip_02)
    TextView tvTip02;
    @Bind(R.id.tv_money_value)
    TextView tvMoneyValue;
    @Bind(R.id.tv_tip_03)
    TextView tvTip03;
    @Bind(R.id.tv_encourage_value)
    TextView tvEncourageValue;
    @Bind(R.id.btn_over)
    Button btnOver;
    @Bind(R.id.btn_again)
    Button btnAgain;
    @Bind(R.id.ll_mid)
    FrameLayout llMid;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.ll_buttom)
    FrameLayout llButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_deliver)
    RelativeLayout activityDeliver;
    private ArrayList<GarbageParam> mlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this,getApplication(),360);
        setContentView(R.layout.activity_deliver);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        initData();
        initTimer();
    }

    private void initTimer() {
        backAndTime.setTimer(280);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.setBackVisableStatue(false);
        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                backAndTime.stop();
                openActivity(DeliverSuccessActivity.class);

            }
        });
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        btnMyRecycler.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        backAndTime.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backAndTime.stop();
    }


    @Override
    public void getFindData(String reciveData) {

    }

    @OnClick({R.id.btn_over, R.id.btn_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_over:
                openActivity(DeliverSuccessActivity.class);
                finish();
                break;
            case R.id.btn_again:
                openActivity(UserSelectActivity.class);
                finish();
                break;
        }
    }
}
