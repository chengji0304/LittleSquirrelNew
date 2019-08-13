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
import com.panda.littlesquirrel.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jinjing on 2019/3/22.
 */

public class TopFragment extends BaseFragment {
    private static volatile TopFragment instance;
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


    public static TopFragment getInstance() {
        if (instance == null) {
            synchronized (TopFragment.class) {
                if (instance == null) {
                    instance = new TopFragment();
                }
            }
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, R.layout.top, container);
        ButterKnife.bind(this, view);
        return view;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_my_recycler)
    public void onViewClicked() {
        //this.mainActivity.getFragmentManager().beginTransaction().replace(R.id.mid, CollectorLoginFragment.getInstace()).commit();


    }
}
