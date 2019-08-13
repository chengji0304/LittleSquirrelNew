package com.panda.littlesquirrel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseFragment;
import com.panda.littlesquirrel.view.BackAndTimerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jinjing on 2019/4/8.
 */

public class UserLoginFragment extends BaseFragment {
    private static volatile UserLoginFragment instance;
    @Bind(R.id.tv_line_09)
    TextView tvLine09;
    @Bind(R.id.tv_tip_09)
    TextView tvTip09;
    @Bind(R.id.tv_line_10)
    TextView tvLine10;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.btn_switch_tel)
    Button btnSwitchTel;
    @Bind(R.id.tv_tip_12)
    TextView tvTip12;
    @Bind(R.id.img_user_qrcode)
    ImageView imgUserQrcode;

    @Bind(R.id.tv_tip_11)
    TextView tvTip11;
    @Bind(R.id.rl_sao)
    RelativeLayout rlSao;

    public static UserLoginFragment getInstance() {
        if (instance == null) {
            synchronized (UserSelectFragment.class) {
                if (instance == null) {
                    instance = new UserLoginFragment();
                }
            }
        }
        return instance;
    }
    @Override
    public void onResume() {
        super.onResume();
        initTimer();
    }

    private void initTimer() {
        this.mainActivity.backAndTime.setVisibility(View.VISIBLE);
        this.mainActivity.backAndTime.setTimer(60);
        this.mainActivity.backAndTime.setBackVisableStatue(true);
        this.mainActivity.backAndTime.setVisableStatue(Boolean.valueOf(true));
        this.mainActivity.backAndTime.start();
        this.mainActivity.backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                mainActivity.backAndTime.stop();
                mainActivity.backAndTime.setVisibility(View.GONE);
                mainActivity.btnMyRecycler.setVisibility(View.VISIBLE);
                showFragment(UserLoginFragment.getInstance(),UserSelectFragment.getInstance());
            }
        });

        this.mainActivity.backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                mainActivity.backAndTime.stop();
                mainActivity.backAndTime.setVisibility(View.GONE);
                mainActivity.btnMyRecycler.setVisibility(View.VISIBLE);
                showFragment(UserLoginFragment.getInstance(),UserSelectFragment.getInstance());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, R.layout.user_scan_login, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onViewCreated(View paramView, Bundle paramBundle) {
        super.onViewCreated(paramView, paramBundle);
        initData();
    }

    private void initData() {
        mainActivity.btnMyRecycler.setVisibility(View.GONE);
    }



    @OnClick(R.id.btn_switch_tel)
    public void onViewClicked() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if ( !hidden) {
            mainActivity.btnMyRecycler.setVisibility(View.GONE);
            this.mainActivity.backAndTime.setVisibility(View.VISIBLE);
            this.mainActivity.backAndTime.setTimer(60);
            this.mainActivity.backAndTime.setBackVisableStatue(true);
            this.mainActivity.backAndTime.setVisableStatue(Boolean.valueOf(true));
            this.mainActivity.backAndTime.start();
        }
    }
}
