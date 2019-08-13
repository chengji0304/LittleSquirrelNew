package com.panda.littlesquirrel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseFragment;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.panda.littlesquirrel.view.DigitalKeyboard;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jinjing on 2019/3/25.
 */

public class CollectorTeleFragment extends BaseFragment {
    private static volatile CollectorTeleFragment instance;
    @Bind(R.id.tv_operate_tip)
    TextView tvOperateTip;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.tv_select)
    TextView tvSelect;
    @Bind(R.id.tv_pay)
    TextView tvPay;
    @Bind(R.id.tv_open)
    TextView tvOpen;
    @Bind(R.id.tv_close)
    TextView tvClose;
    @Bind(R.id.tv_num_01)
    TextView tvNum01;
    @Bind(R.id.tv_line_02)
    TextView tvLine02;
    @Bind(R.id.tv_line_05)
    TextView tvLine05;
    @Bind(R.id.tv_tip_03)
    TextView tvTip03;
    @Bind(R.id.tv_line_06)
    TextView tvLine06;
    @Bind(R.id.ed_account)
    EditText edAccount;
    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.ll_etAcc)
    RelativeLayout llEtAcc;
    @Bind(R.id.ed_password)
    EditText edPassword;
    @Bind(R.id.tv_password)
    TextView tvPassword;
    @Bind(R.id.ll_etPwd)
    RelativeLayout llEtPwd;
    @Bind(R.id.recycler_digital_keyboard)
    DigitalKeyboard recyclerDigitalKeyboard;

    public static CollectorTeleFragment getInstance() {
        if (instance == null) {
            synchronized (CollectorTeleFragment.class) {
                if (instance == null) {
                    instance = new CollectorTeleFragment();
                }
            }
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = createView(paramLayoutInflater, R.layout.recycler_tele_login, paramViewGroup);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View paramView, Bundle paramBundle) {
        super.onViewCreated(paramView, paramBundle);

        initData();
        initKeyBoard();
    }

    private void initData() {
        mainActivity.btnMyRecycler.setVisibility(View.GONE);
    }

    private void initKeyBoard() {

        initTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivity.backAndTime.stop();
        ButterKnife.unbind(this);

    }

    private void initTimer() {
        this.mainActivity.backAndTime.setTimer(30);
        this.mainActivity.backAndTime.setBackVisableStatue(true);
        this.mainActivity.backAndTime.setVisableStatue(Boolean.valueOf(true));
        this.mainActivity.backAndTime.start();
        this.mainActivity.backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {

            }
        });

        this.mainActivity.backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {

            }
        });
    }
}
