package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuickGuideActivity extends BaseActivity {

    @Bind(R.id.image_service_tel)
    ImageView imageServiceTel;
    @Bind(R.id.image_device_num)
    ImageView imageDeviceNum;
    @Bind(R.id.tv_service_tel)
    TextView tvServiceTel;
    @Bind(R.id.tv_device_num)
    TextView tvDeviceNum;
    @Bind(R.id.btn_my_recycler)
    Button btnMyRecycler;
    @Bind(R.id.tv_guide_tip)
    TextView tvGuideTip;
    @Bind(R.id.tv_guide_title_bg)
    TextView tvGuideTitleBg;
    @Bind(R.id.tv_tip_1)
    TextView tvTip1;
    @Bind(R.id.img_tip_1)
    ImageView imgTip1;
    @Bind(R.id.tv_tip_2)
    TextView tvTip2;
    @Bind(R.id.img_tip_2)
    ImageView imgTip2;
    @Bind(R.id.tv_tip_3)
    TextView tvTip3;
    @Bind(R.id.img_tip_3)
    ImageView imgTip3;
    @Bind(R.id.tv_tip_4)
    TextView tvTip4;
    @Bind(R.id.img_tip_4)
    ImageView imgTip4;
    @Bind(R.id.tv_tip_5)
    TextView tvTip5;
    @Bind(R.id.img_tip_5)
    ImageView imgTip5;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_quick_guide)
    RelativeLayout activityQuickGuide;
    private PreferencesUtil prf;
    private ArrayList<Integer> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this,getApplication(),360);
        setContentView(R.layout.activity_quick_guide);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        prf=new PreferencesUtil(this);
        initData();
        initTimer();
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        initBanner();
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
        btnMyRecycler.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backAndTime.start();

    }

    private void initTimer() {
        backAndTime.setTimer(280);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.setBackVisableStatue(true);
        // backAndTime.start();
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                backAndTime.stop();


                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                //   overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                backAndTime.stop();

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backAndTime.stop();
    }


    @Override
    public void getFindData(String reciveData) {

    }

    private void initBanner() {
        images = new ArrayList<>();
        images.add(R.drawable.banner111);
        images.add(R.drawable.banner222);
        images.add(R.drawable.banner333);
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
        bannerButtom.setDelayTime(1000*30);
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
                    .asBitmap()
                    .skipMemoryCache(true)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                    .transform(transformation)
                    .into(imageView);

        }

    }
}
