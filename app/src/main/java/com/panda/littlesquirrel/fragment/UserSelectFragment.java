package com.panda.littlesquirrel.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.activity.BigRecoveryActivity;
import com.panda.littlesquirrel.activity.QuickGuideActivity;
import com.panda.littlesquirrel.activity.UserLoginActivity;
import com.panda.littlesquirrel.activity.UserSelectActivity;
import com.panda.littlesquirrel.adapter.UserSelectReclyViewAdapater;
import com.panda.littlesquirrel.base.BaseFragment;
import com.panda.littlesquirrel.entity.SelcetInfo;
import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.utils.Utils;
import com.panda.littlesquirrel.view.ErrorStatusDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jinjing on 2019/3/22.
 */

public class UserSelectFragment extends BaseFragment {
    private static volatile UserSelectFragment instance;
    @Bind(R.id.btn_big_piece)
    Button btnBigPiece;
    @Bind(R.id.btn_guide)
    Button btnGuide;
    @Bind(R.id.tv_line)
    TextView tvLine;
    @Bind(R.id.ry_grabage)
    RecyclerView ryGrabage;
    @Bind(R.id.tip_01)
    TextView tip01;
    @Bind(R.id.img_public_03)
    ImageView imgPublic03;
    @Bind(R.id.tip_02)
    TextView tip02;
    @Bind(R.id.tip_03)
    TextView tip03;
    @Bind(R.id.tip_04)
    TextView tip04;
    @Bind(R.id.tip_05)
    TextView tip05;
    @Bind(R.id.rl_weixin)
    RelativeLayout rlWeixin;

    private UserSelectReclyViewAdapater adapter;
    private ArrayList<SelcetInfo> mlist;
    private String isLogin;
    private CountDownTimer timer;

    private ErrorStatusDialog errorStatusDialog = new ErrorStatusDialog();
    public static UserSelectFragment getInstance() {
        if (instance == null) {
            synchronized (UserSelectFragment.class) {
                if (instance == null) {
                    instance = new UserSelectFragment();
                }
            }
        }
        return instance;
    }

    private ArrayList<SelcetInfo> getData() {
       mlist = new ArrayList<>();
//        SelcetInfo bottle = new SelcetInfo(R.drawable.bottle, "饮料瓶", "", "0.05", "30");
//        mlist.add(bottle);
//        SelcetInfo page = new SelcetInfo(R.drawable.paper, "纸类", "满箱", "0.70", "56.83");
//        mlist.add(page);
//        SelcetInfo book = new SelcetInfo(R.drawable.book, "书籍", "80%", "0.80", "40.83");
//        mlist.add(book);
//        SelcetInfo plastic = new SelcetInfo(R.drawable.plastic, "塑料", "", "0.70", "26.83");
//        mlist.add(plastic);
//        SelcetInfo textile = new SelcetInfo(R.drawable.fabric, "纺织物", "", "0.20", "16.83");
//        mlist.add(textile);
//        SelcetInfo metal = new SelcetInfo(R.drawable.metal, "金属", "满箱", "0.80", "26.83");
//        mlist.add(metal);
//        SelcetInfo glasses = new SelcetInfo(R.drawable.glass, "玻璃", "", "1", "36.57");
//        mlist.add(glasses);
//        SelcetInfo harmgoods = new SelcetInfo(R.drawable.harm, "有害垃圾", "", "1", "6.83");
//        mlist.add(harmgoods);
        return mlist;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, R.layout.user_select_goods, container);
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
         mainActivity.backAndTime.setVisibility(View.GONE);
        mlist = getData();
        ryGrabage.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new UserSelectReclyViewAdapater(getActivity(), mlist);
        ryGrabage.setAdapter(adapter);
        adapter.setOnItemClickListener(new UserSelectReclyViewAdapater.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Logger.e("info" + position);
                if (Utils.hourMinuteBetween(new SimpleDateFormat("HH:mm").format(new Date()), "8:30", "21:00")) {
                    if (!mlist.get(position).getFullstatus().equals("满箱")) {
                        if (StringUtil.isEmpty(isLogin)) {
                            mainActivity.countTimerView.cancel();
                          showFragment(UserSelectFragment.getInstance(),UserLoginFragment.getInstance());
                        } else {

                        }
                    } else {
                        errorStatusDialog.setImage(R.drawable.error);
                        errorStatusDialog.setContent("投递箱暂满,请稍后投递哦");
                        errorStatusDialog.setOnConfirmClickListener(new ErrorStatusDialog.ConfirmCallBack() {
                            @Override
                            public void onConfirm() {
                                errorStatusDialog.dismiss();

                            }
                        });

                        errorStatusDialog.show(getActivity().getFragmentManager(),"full_error");
                        //倒计时
                        if(timer!=null){
                            timer.cancel();
                        }
                        timer = new CountDownTimer(1000 * 6, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
                                if (secondsRemaining > 0) {

                                }
                            }

                            @Override
                            public void onFinish() {
                                errorStatusDialog.dismiss();


                            }
                        }.start();

                    }

                } else {
                    Logger.e("info-->"+"time");

                    errorStatusDialog.setContent("投递时间为8:30-21:00哦~");
                    errorStatusDialog.setImage(R.drawable.error);
                    errorStatusDialog.setOnConfirmClickListener(new ErrorStatusDialog.ConfirmCallBack() {
                        @Override
                        public void onConfirm() {
                            errorStatusDialog.dismiss();



                        }
                    });
                    errorStatusDialog.show(getActivity().getFragmentManager(), "time_error");
                    if(timer!=null){
                        timer.cancel();
                    }
                    timer = new CountDownTimer(1000 * 6, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
                            if (secondsRemaining > 0) {

                            }
                        }

                        @Override
                        public void onFinish() {
                            errorStatusDialog.dismiss();


                        }
                    }.start();
                }
            }
        });
    }

    @OnClick({R.id.btn_big_piece, R.id.btn_guide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_big_piece:
                mainActivity.countTimerView.cancel();
                openActivity(BigRecoveryActivity.class);
                break;

            case R.id.btn_guide:
                mainActivity.countTimerView.cancel();
               showFragment(UserSelectFragment.getInstance(),QuickGuideFragment.getInstace());
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if ( !hidden) {
            mainActivity.backAndTime.setVisibility(View.GONE);

        }
    }

}
