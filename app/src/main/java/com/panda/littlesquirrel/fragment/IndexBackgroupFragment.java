package com.panda.littlesquirrel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseFragment;

/**
 * Created by jinjing on 2019/3/22.
 */

public class IndexBackgroupFragment extends BaseFragment {
    private static volatile IndexBackgroupFragment  instance;
    public static IndexBackgroupFragment getInstance(){
        if(instance==null){
             synchronized (IndexBackgroupFragment.class){
                 if(instance==null){
                     instance=new IndexBackgroupFragment();
                 }
             }
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        return createView(paramLayoutInflater, R.layout.index_bj,paramViewGroup);

    }

    @Override
    public void onViewCreated(View paramView, Bundle paramBundle) {
        super.onViewCreated(paramView, paramBundle);
    }
}
