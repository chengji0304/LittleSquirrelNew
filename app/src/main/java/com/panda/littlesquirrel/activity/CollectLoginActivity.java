package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.utils.Base64Utils;
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
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectLoginActivity extends BaseActivity {


    private ArrayList<Integer> images;
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
    @Bind(R.id.tv_line_03)
    TextView tvLine03;
    @Bind(R.id.tv_recycler_scan_login)
    TextView tvRecyclerScanLogin;
    @Bind(R.id.tv_line_04)
    TextView tvLine04;
    @Bind(R.id.img_recycler_login_qr_code)
    ImageView imgRecyclerLoginQrCode;
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.btn_recycler_psw_login)
    Button btnRecyclerPswLogin;
    @Bind(R.id.tv_tip_02)
    TextView tvTip02;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_collect_login)
    RelativeLayout activityCollectLogin;

    @Bind(R.id.ll_wait)
    LinearLayout llWait;
    @Bind(R.id.ll_scan)
    LinearLayout llScan;
    private String code;
    private Bitmap bitmap;
    private int requestNumber = 0;
    PreferencesUtil prf;
    private Timer timer = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {  //这个是发送过来的消息
            // 处理从子线程发送过来的消息
            switch (msg.what) {
                case 1:
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    getOrCode();
                    break;
                default:
                    break;
            }

        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);
        setContentView(R.layout.activity_collect_login);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        prf = new PreferencesUtil(this);
        initData();
        //initTimer();
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        initBanner();
        btnMyRecycler.setVisibility(View.GONE);
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
        getOrCode();


    }

    private void getOrCode() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", prf.readPrefs(Constant.DEVICEID));
            addSubscription(Constant.HTTP_URL + "machine/QRCode/recycler", jsonObject.toString(), new CallBack<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(ApiException e) {

                }

                @Override
                public void onSuccess(String s) {
                    Logger.e("s--->" + s);
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if (stateCode.equals("1")) {
                        com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSON.parseObject(jsonObject.getString("result"));
                        String code = object.getString("base64Img");
                        bitmap = Base64Utils.base64ToBitmap(code);
                        imgRecyclerLoginQrCode.setImageBitmap(bitmap);
                        //   prf.writePrefs(Constant.COLLECT_QRCODE,code);
                        llScan.setVisibility(View.VISIBLE);
                        llWait.setVisibility(View.GONE);
                       // SoundPlayUtil.play(11);
                        //initTimer();
                        timer = new Timer();
                        MyTimerTask myTimerTask = new MyTimerTask();//定时器
                        timer.schedule(myTimerTask, 3000, 5000);//每隔5秒

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Logger.e("requestNumber-->" + (requestNumber));
            requestNumber++;
            if (requestNumber >= 50) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                requestNumber = 0;
                //生成新码
                handler.sendEmptyMessage(1);
            }
            //查询条码
            Logger.e("查询中。。。。");
            //barCodeQuery();
            getScanRecycler();


        }
    }

    private void getScanRecycler() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", prf.readPrefs(Constant.DEVICEID));
            addSubscription(Constant.HTTP_URL + "machine/verification/getScanRecycler", jsonObject.toString(), new CallBack<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(ApiException e) {

                }

                @Override
                public void onSuccess(String s) {
                    Logger.e("s--->" + s);
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if (stateCode.equals("1")) {
                        com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSON.parseObject(jsonObject.getString("result"));
                        String mobile = object.getString("account");
                        prf.writePrefs(Constant.COLLECTOR_MOBILE, mobile);
                        openActivity(RecylerSelectActivity.class);
                        finish();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                //openActivity(UserSelectActivity.class);
                backAndTime.stop();
                openActivity(UserSelectActivity.class);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                // openActivity(UserSelectActivity.class);
                backAndTime.stop();
                openActivity(UserSelectActivity.class);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
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

    @OnClick(R.id.btn_recycler_psw_login)
    public void onViewClicked() {
        backAndTime.stop();
        openActivity(CollectTeleActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        if (timer != null) {
            timer.cancel();
        }

        backAndTime.stop();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void getFindData(String reciveData) {

    }


}
