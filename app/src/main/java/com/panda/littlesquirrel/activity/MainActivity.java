package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.adapter.UserSelectReclyViewAdapater;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.entity.SelcetInfo;
import com.panda.littlesquirrel.fragment.ADBannerActivity;
import com.panda.littlesquirrel.fragment.CollectorLoginFragment;
import com.panda.littlesquirrel.fragment.UserSelectFragment;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.panda.littlesquirrel.view.ErrorStatusDialog;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @Bind(R.id.backAndTime)
    public BackAndTimerView backAndTime;
    @Bind(R.id.image_service_tel)
    ImageView imageServiceTel;
    @Bind(R.id.tv_service_tel)
    TextView tvServiceTel;
    @Bind(R.id.image_device_num)
    ImageView imageDeviceNum;
    @Bind(R.id.tv_device_num)
    TextView tvDeviceNum;
    @Bind(R.id.btn_my_recycler)
    public Button btnMyRecycler;
    @Bind(R.id.top)
    FrameLayout top;
    @Bind(R.id.fl_content)
    FrameLayout flContent;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.buttom)
    FrameLayout buttom;
    public CountTimer countTimerView;
    private UserSelectReclyViewAdapater adapter;
    private ArrayList<SelcetInfo> mlist;
    private String isLogin;
    private ErrorStatusDialog errorStatusDialog = new ErrorStatusDialog();
    private CountDownTimer timer;
    FragmentManager fm;
    FragmentTransaction ft;
    Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        // testPage();
    }

    private void init() {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        UserSelectFragment userSelectFragment = UserSelectFragment.getInstance();
        ft.add(R.id.fl_content, userSelectFragment, "fragment1").show(userSelectFragment).commit();
        //初始化CountTimer，设置倒计时为1分钟。
        countTimerView = new CountTimer(60000, 1000, MainActivity.this);


    }


    private void timeStart() {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countTimerView.start();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                countTimerView.start();
                break;
            default:
                countTimerView.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        backAndTime.setVisibility(View.GONE);
        timeStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        countTimerView.cancel();
    }

    @OnClick(R.id.btn_my_recycler)
    public void onViewClicked() {
        countTimerView.cancel();
        showFragment(UserSelectFragment.getInstance(), CollectorLoginFragment.getInstace());
    }


    public void getFindData(String reciveData) {

    }


    public class CountTimer extends CountDownTimer {
        private Context context;

        /**
         * 参数 millisInFuture       倒计时总时间（如60S，120s等）
         * 参数 countDownInterval    渐变时间（每次倒计1s）
         */
        public CountTimer(long millisInFuture, long countDownInterval, Context context) {
            super(millisInFuture, countDownInterval);
            this.context = context;
        }

        // 计时完毕时触发
        @Override
        public void onFinish() {
            //  UIHelper.showMainActivity((Activity) context);
            Intent intent = new Intent(MainActivity.this, ADBannerActivity.class);
            startActivity(intent);

        }

        // 计时过程显示
        @Override
        public void onTick(long millisUntilFinished) {
        }
    }


}
