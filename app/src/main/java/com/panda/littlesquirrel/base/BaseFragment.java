package com.panda.littlesquirrel.base;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.activity.MainActivity;


/**
 * Created by jinjing on 2019/3/22.
 */

public class BaseFragment extends Fragment {
    protected MainActivity mainActivity;
    protected View mView;
    public View createView(LayoutInflater paramLayoutInflater, int paramInt, ViewGroup paramViewGroup)
    {
        Logger.i("info--->"+getClass().getSimpleName() + ":onCreateView");
        this.mView = paramLayoutInflater.inflate(paramInt, paramViewGroup, false);
        return this.mView;
    }

    @Override
    public void onActivityCreated(Bundle paramBundle)
    {
        super.onActivityCreated(paramBundle);
        Logger.i("info--->"+getClass().getSimpleName() + ":onActivityCreated");
    }

    public void onAttach(Activity paramActivity)
    {
        super.onAttach(paramActivity);
        this.mainActivity = ((MainActivity)paramActivity);
        Log.i("info", getClass().getSimpleName() + ":onAttach");
    }

    @Override
    public void onAttach(Context paramContext)
    {
        super.onAttach(paramContext);
        this.mainActivity = ((MainActivity)getActivity());
        Log.i("info", getClass().getSimpleName() + ":onAttach_context");
    }

    @Override
    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        Log.i("info", getClass().getSimpleName() + ":onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("info", getClass().getSimpleName() + ":onDestroy");
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        Log.i("info", getClass().getSimpleName() + ":onDestroyView");
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        Log.i("info", getClass().getSimpleName() + ":onDetach");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.i("info", getClass().getSimpleName() + ":onPause");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i("info", getClass().getSimpleName() + ":onResume");
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.i("info", getClass().getSimpleName() + ":onStart");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.i("info", getClass().getSimpleName() + ":onStop");
    }

    @Override
    public void onViewCreated(View paramView, Bundle paramBundle)
    {
        super.onViewCreated(paramView, paramBundle);
        Log.i("info", getClass().getSimpleName() + ":onViewCreated");
    }


    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    public void showFragment(Fragment currentFragment, Fragment fg){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        //如果之前没有添加过
        if(!fg.isAdded()){
            transaction
                    .hide(currentFragment)
                    .add(R.id.fl_content,fg,fg.getClass().getName());  //第三个参数为当前的fragment绑定一个tag，tag为当前绑定fragment的类名
        }else{
            transaction
                    .hide(currentFragment)
                    .show(fg);
        }

        currentFragment = fg;

        transaction.commit();

    }
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }
    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getActivity(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

}
