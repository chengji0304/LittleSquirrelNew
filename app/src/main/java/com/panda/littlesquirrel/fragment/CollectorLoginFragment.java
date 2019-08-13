package com.panda.littlesquirrel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.activity.MainActivity;
import com.panda.littlesquirrel.base.BaseFragment;
import com.panda.littlesquirrel.view.BackAndTimerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jinjing on 2019/3/25.
 */

public class CollectorLoginFragment extends BaseFragment {
    private static volatile CollectorLoginFragment instance;

    public static CollectorLoginFragment getInstace() {
        if (instance == null) {
            synchronized (CollectorLoginFragment.class) {
                if (instance == null) {
                    instance = new CollectorLoginFragment();
                }
            }
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, R.layout.recycler_scan_login, container);

        return view;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity.btnMyRecycler.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        initTimer();
    }

    private void initTimer() {
        this.mainActivity.backAndTime.setVisibility(View.VISIBLE);
        this.mainActivity.backAndTime.setTimer(20);
        this.mainActivity.backAndTime.setBackVisableStatue(true);
        this.mainActivity.backAndTime.setVisableStatue(Boolean.valueOf(true));
        this.mainActivity.backAndTime.start();
        this.mainActivity.backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                mainActivity.backAndTime.stop();
                mainActivity.backAndTime.setVisibility(View.GONE);
                mainActivity.btnMyRecycler.setVisibility(View.VISIBLE);
             showFragment(CollectorLoginFragment.getInstace(),UserSelectFragment.getInstance());
            }
        });

        this.mainActivity.backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                mainActivity.backAndTime.stop();
                mainActivity.backAndTime.setVisibility(View.GONE);
                mainActivity.btnMyRecycler.setVisibility(View.VISIBLE);
                showFragment(CollectorLoginFragment.getInstace(),UserSelectFragment.getInstance());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mainActivity.backAndTime.stop();
    }

    @OnClick(R.id.btn_recycler_psw_login)
    public void onViewClicked() {
        showFragment(CollectorLoginFragment.getInstace(),CollectorTeleFragment.getInstance());


    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if ( !hidden) {
            mainActivity.btnMyRecycler.setVisibility(View.GONE);
            this.mainActivity.backAndTime.setVisibility(View.VISIBLE);
            this.mainActivity.backAndTime.setTimer(20);
            this.mainActivity.backAndTime.setBackVisableStatue(true);
            this.mainActivity.backAndTime.setVisableStatue(Boolean.valueOf(true));
            this.mainActivity.backAndTime.start();
        }
    }

}
