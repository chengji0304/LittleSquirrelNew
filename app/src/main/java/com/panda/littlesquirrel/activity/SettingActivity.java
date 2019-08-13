package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;
import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import android_serialport_api.SerialPort;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    // 特殊下标位置
    private static final int PHONE_INDEX_3 = 3;
    private static final int PHONE_INDEX_4 = 4;
    private static final int PHONE_INDEX_8 = 8;
    private static final int PHONE_INDEX_9 = 9;
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
    @Bind(R.id.ll_left)
    LinearLayout llLeft;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    @Bind(R.id.ll_open)
    LinearLayout llOpen;
    @Bind(R.id.ll_close)
    LinearLayout llClose;
    @Bind(R.id.ll_close_elec)
    LinearLayout llCloseElec;
    @Bind(R.id.ll_open_elec)
    LinearLayout llOpenElec;
    @Bind(R.id.ll_open_time)
    LinearLayout llOpenTime;
    @Bind(R.id.ll_jiance)
    LinearLayout llJiance;
    @Bind(R.id.btn_system)
    Button btnSystem;
    @Bind(R.id.btn_login_out)
    Button btnLoginOut;
    @Bind(R.id.ll_mid)
    FrameLayout llMid;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.ll_buttom)
    FrameLayout llButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_setting)
    RelativeLayout activitySetting;
    Intent intent;
    public SerialPort serialPort;
    public byte[] mBuffer;
    public StringBuilder reciveData;
    @Bind(R.id.ll_opentime)
    LinearLayout llOpentime;
    @Bind(R.id.ll_closetime)
    LinearLayout llClosetime;
    @Bind(R.id.ll_boxheight)
    LinearLayout llBoxheight;
    @Bind(R.id.ll_chuanandbox)
    LinearLayout llChuanandbox;
    private Handler handler;
    //public SerialPortUtils serialPortUtils = new SerialPortUtils("/dev/ttyS4", 9600);
    private ArrayList<Integer> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        // System.loadLibrary("serial_port");
        // serialPort = serialPortUtils.openSerialPort();
       // mStartHandler = new Handler();
       // reciveData = new StringBuilder();
        handler = new Handler();
        initData();
    }


    private void initData() {
        sendTimerBoaadCastReceiver(this);
        //setListener();
        initBanner();
        tvDeviceNum.setText("程序版本:"+ StringUtil.getVersionName(this));
        btnMyRecycler.setVisibility(View.GONE);

    }

    /**
     * 串口数据监听
     */
/*
    @Override
    public void setListener() {
        serialPortUtils.setReceiveListener(new SerialPortUtils.ReceiveListener() {
            @Override
            public void dataReceive(byte[] buffer, int size) {

                mBuffer = buffer;
                Log.e("buff", "数据监听：" + new String(mBuffer).trim());
                if (mBuffer.length > 0) {
                    mStartHandler.post(runnable);
                }
            }

            //开线程更新UI
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (mBuffer.length > 0) {
                        reciveData.append(new String(mBuffer).trim());
                        if (reciveData.toString().contains("@")) {
                            Log.e("SerialPortUtils", "数据监听：" + reciveData.toString());
                            String[] re = reciveData.toString().split("@");
                            if (re.length >= 1 && re[0].length() > 1) {
                                getFindData(re[0]);
                            }
                            reciveData = new StringBuilder();
                        }
                    }

                }
//                    } catch (UnsupportedEncodingException e)
//                    {
//                        e.printStackTrace();
//                    }

//                }
            };
        });

    }
**/
    @Override
    public void getFindData(String s) {

        Logger.e("SettingActivity--->" + s);
        if (s.contains("W5")) {
            //getSettingDialog(type);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTimer();
    }

    private void initTimer() {
        backAndTime.setVisibility(View.VISIBLE);
        backAndTime.setTimerVisableStatue(false);
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {

                openActivity(UserSelectActivity.class);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backAndTime != null) {
            backAndTime.stop();
        }
        //  serialPortUtils.closeSerialPort();
    }

    @OnClick({R.id.ll_left, R.id.ll_right, R.id.ll_open, R.id.ll_close, R.id.ll_close_elec, R.id.ll_open_elec, R.id.ll_open_time, R.id.ll_jiance, R.id.btn_system, R.id.btn_login_out, R.id.ll_opentime, R.id.ll_closetime,R.id.ll_boxheight,R.id.ll_chuanandbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_left:
                // serialPortUtils.sendSerialPort("androidC51:;");
                // serialPortUtils.sendSerialPort("androidP611:"+""+";");
                openActivity(LeftBoxSettingActivity.class);
                finish();
                break;
            case R.id.ll_right:
                openActivity(RightBoxSettingActivity.class);
                finish();
                // serialPortUtils.sendSerialPort("androidP601:"+"1"+";");
                // serialPortUtils.sendSerialPort("androidP602:"+""+";");
                break;
            case R.id.ll_open:
                intent = new Intent(SettingActivity.this, SlotSettingActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.ll_close:
                intent = new Intent(SettingActivity.this, SlotSettingActivity.class);
                intent.putExtra("type", "3");
                startActivity(intent);
                break;
            case R.id.ll_open_elec:
                intent = new Intent(SettingActivity.this, SlotSettingActivity.class);
                intent.putExtra("type", "4");
                startActivity(intent);
                break;
            case R.id.ll_close_elec:
                intent = new Intent(SettingActivity.this, SlotSettingActivity.class);
                intent.putExtra("type", "5");
                startActivity(intent);
                break;

            case R.id.ll_open_time:
                intent = new Intent(SettingActivity.this, SlotSettingActivity.class);
                intent.putExtra("type", "6");
                startActivity(intent);
                break;
            case R.id.ll_jiance:

                intent = new Intent(SettingActivity.this, SlotSettingActivity.class);
                intent.putExtra("type", "7");
                startActivity(intent);
                break;
            case R.id.btn_system:
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        serialPortUtils.sendSerialPort("androidC56:" + "1" + ";");
//                    }
//                }, 200);
                break;
            case R.id.btn_login_out:
                intent = new Intent(SettingActivity.this, UserSelectActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_opentime:
                intent = new Intent(SettingActivity.this, SlotSettingActivity.class);
                intent.putExtra("type", "0");
                startActivity(intent);

                break;
            case R.id.ll_closetime:
                intent = new Intent(SettingActivity.this, SlotSettingActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);

                break;
            case R.id.ll_boxheight:
                intent = new Intent(SettingActivity.this, SlotSettingActivity.class);
                intent.putExtra("type", "10");
                startActivity(intent);

                break;
            case R.id.ll_chuanandbox:
                intent = new Intent(SettingActivity.this, SlotSettingActivity.class);
                intent.putExtra("type", "11");
                startActivity(intent);

                break;
        }

        //
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
                    .asBitmap()
                    .skipMemoryCache(true)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                    .transform(transformation)
                    .into(imageView);

        }

    }

}
