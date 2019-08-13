package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.ForbiddenSysKeyBoardUtils;
import com.panda.littlesquirrel.utils.ScreenUtil;

import com.panda.littlesquirrel.view.BackAndTimerView;
import com.panda.littlesquirrel.view.DigitalKeyboard;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingLoginActivity extends BaseActivity {

    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.image_service_tel)
    ImageView imageServiceTel;
    @Bind(R.id.tv_service_tel)
    TextView tvServiceTel;
    @Bind(R.id.image_device_num)
    ImageView imageDeviceNum;
    @Bind(R.id.tv_device_num)
    TextView tvDeviceNum;
    @Bind(R.id.ll_image)
    LinearLayout llImage;
    @Bind(R.id.btn_my_recycler)
    Button btnMyRecycler;
    @Bind(R.id.ll_top)
    FrameLayout llTop;
    @Bind(R.id.tv_line_05)
    TextView tvLine05;
    @Bind(R.id.tv_tip_03)
    TextView tvTip03;
    @Bind(R.id.tv_line_06)
    TextView tvLine06;
    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.ed_account)
    EditText edAccount;
    @Bind(R.id.ll_etAcc)
    LinearLayout llEtAcc;
    @Bind(R.id.tv_password)
    TextView tvPassword;
    @Bind(R.id.ed_password)
    EditText edPassword;
    @Bind(R.id.ll_etPwd)
    LinearLayout llEtPwd;
    @Bind(R.id.recycler_digital_keyboard)
    DigitalKeyboard recyclerDigitalKeyboard;
    @Bind(R.id.ll_mid)
    FrameLayout llMid;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.ll_buttom)
    FrameLayout llButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_setting_login)
    RelativeLayout activitySettingLogin;
    private static final int PHONE_INDEX_3 = 3;
    private static final int PHONE_INDEX_4 = 4;
    private static final int PHONE_INDEX_8 = 8;
    private static final int PHONE_INDEX_9 = 9;
    private ArrayList<Integer> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_login);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        initData();

    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        initBanner();
        btnMyRecycler.setVisibility(View.GONE);
        ForbiddenSysKeyBoardUtils.bannedSysKeyBoard(SettingLoginActivity.this, edAccount);
        ForbiddenSysKeyBoardUtils.bannedSysKeyBoard(SettingLoginActivity.this, edPassword);
//        edAccount.post(new Runnable() {
//            @Override
//            public void run() {
//                edAccount.getText().clear();
//                edAccount.requestFocus();
//
//            }
//        });
//        edPassword.post(new Runnable() {
//            @Override
//            public void run() {
//                edPassword.getText().clear();
//            }
//        });
        edAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != PHONE_INDEX_3 && i != PHONE_INDEX_8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == PHONE_INDEX_4 || sb.length() == PHONE_INDEX_9) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    edAccount.setText(sb.toString());
                    edAccount.setSelection(index);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        edAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recyclerDigitalKeyboard.setEditText(edAccount);
                    //SoundPlayUtils.enablePaly = false;
                }//   Logger.e("edAcount1--->"+edAccount.getText().length());

            }

        });
        edPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recyclerDigitalKeyboard.setEditText(edPassword);
                   // SoundPlayUtils.enablePaly = false;
                }

            }
        });

        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = recyclerDigitalKeyboard.getEditText();

                // Logger.e("s--->"+s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerDigitalKeyboard.setOnConfirmListener(new DigitalKeyboard.OnConfirmListener() {
            @Override
            public void onConfirm() {
                if(edAccount.getText().toString().trim().replaceAll(" ","").equals("13577777777")&edPassword.getText().toString().trim().equals("666666")){
                    openActivity(SettingActivity.class);
                    finish();
                }else {
                    edAccount.setText("");
                    edPassword.setText("");
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTimer();
    }

    private void initTimer() {
        backAndTime.setTimer(280);
        backAndTime.setBackVisableStatue(true);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.start();
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                openActivity(UserSelectActivity.class);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                openActivity(UserSelectActivity.class);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }


    @Override
    public void getFindData(String reciveData) {
       // Logger.e("reciveData--->" + reciveData);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private void initBanner() {
        images = new ArrayList<>();
        images.add(R.drawable.banner_setting);
//        images.add(R.drawable.banner222);
//        images.add(R.drawable.banner333);
        //设置banner样式(显示圆形指示器)
        bannerButtom.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置指示器位置（指示器居右）
        bannerButtom.setIndicatorGravity(BannerConfig.RIGHT);
        //设置图片加载器
        bannerButtom.setImageLoader(new GlideImageLoader());
        //设置图片集合
        bannerButtom.setImages(images);
        bannerButtom.isAutoPlay(true);
        //设置轮播时间
        bannerButtom.setDelayTime(4000);
        //banner设置方法全部调用完毕时最后调用
        bannerButtom.setOnBannerListener(new OnBannerListener() {

            @Override
            public void OnBannerClick(int position) {
                Logger.e("position" + position);

            }
        });
        bannerButtom.start();
    }

    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(final Context context, Object path, final ImageView imageView) {
            CornerTransform transformation = new CornerTransform(context, ScreenUtil.dip2px(context, 30));
            //只是绘制左上角和右上角圆角
            transformation.setExceptCorner(false, false, false, false);
            Glide.with(context)
                    .load(path)
                  //  .asBitmap()
                    .skipMemoryCache(true)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                  //  .transform(transformation)
                    .into(imageView);

        }

    }
}
