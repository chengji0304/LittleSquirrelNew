package com.panda.littlesquirrel.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseFragment;

/**
 * Created by jinjing on 2019/3/22.
 */

public class BottomFragment extends BaseFragment {
    private static BottomFragment instance;

    public static BottomFragment getInstance(){
        if(instance==null){
            synchronized (BottomFragment.class){
                if(instance==null){
                    instance=new BottomFragment();
                }
            }
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
      return  createView(paramLayoutInflater, R.layout.bottom,paramViewGroup);
    }

    @Override
    public void onViewCreated(View paramView, Bundle paramBundle) {
        super.onViewCreated(paramView, paramBundle);
    }
}
