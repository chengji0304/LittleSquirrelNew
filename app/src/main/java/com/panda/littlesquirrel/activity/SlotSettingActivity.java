package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.ForbiddenSysKeyBoardUtils;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;
import com.panda.littlesquirrel.utils.SerialPortUtils;

import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.utils.Utils;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.panda.littlesquirrel.view.DigitalKeyboard;
import com.panda.littlesquirrel.view.SettingRightDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import android_serialport_api.SerialPort;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SlotSettingActivity extends BaseActivity {

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
    @Bind(R.id.tv_set_tv)
    TextView tvSetTv;
    @Bind(R.id.ed_set)
    EditText edSet;
    @Bind(R.id.tv_set_type)
    TextView tvSetType;
    @Bind(R.id.recycler_digital_keyboard)
    DigitalKeyboard recyclerDigitalKeyboard;
    @Bind(R.id.tv_set_result)
    TextView tvSetResult;
    @Bind(R.id.ll_mid)
    FrameLayout llMid;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.ll_buttom)
    FrameLayout llButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    private CountDownTimer timer;
    private SettingRightDialog srdialog = new SettingRightDialog();
    private String type;
    // public SerialPort serialPort;
    // public byte[] mBuffer;
    // public  String reciveData = "";
    // public StringBuilder reciveData;
    private Handler handler;
    private ArrayList<Integer> images;
    // public SerialPortUtils serialPortUtils = new SerialPortUtils("/dev/ttyS4", 9600);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);
        setContentView(R.layout.activity_slot_setting);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        System.loadLibrary("serial_port");
        serialPort = serialPortUtils.openSerialPort();
        mStartHandler = new Handler();
        handler = new Handler();
        reciveData = new StringBuilder();
        setListener();
        initData();
    }

    @Override
    public void getFindData(String s) {

        Logger.e("SlotSettingActivity--->" + s);
        if (s.contains("W5")) {

            getSettingDialog(type);
        }
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        initBanner();
        // setListener();
//        backAndTime.setVisibility(View.GONE);
        backAndTime.setBackVisableStatue(true);
        backAndTime.setTimerVisableStatue(false);
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btnMyRecycler.setVisibility(View.GONE);
        ForbiddenSysKeyBoardUtils.bannedSysKeyBoard(SlotSettingActivity.this, edSet);
        type = getIntent().getStringExtra("type");
        switch (type) {
            case "0"://开时
                tvSetTv.setText("开门时间:");
                edSet.setHint("请输入开门时间");
                tvSetType.setText("开门时间范围: 30-100");
                break;
            case "1"://关时
                tvSetTv.setText("关门时间:");
                edSet.setHint("请输入关门时间");
                tvSetType.setText("关门时间范围: 30-100");
                break;
            case "2"://开占
                tvSetTv.setText("开门占空比:");
                edSet.setHint("请输入开门占空比");
                tvSetType.setText("占空比范围: 0-99");
                break;
            case "3"://关占
                tvSetTv.setText("关门占空比:");
                edSet.setHint("请输入关门占空比");
                tvSetType.setText("占空比范围: 0-99");
                break;
            case "4"://开限
                tvSetTv.setText("开门电流限值:");
                edSet.setHint("请输入开门电流限值");
                tvSetType.setText("电流限值范围: 0-99");
                break;
            case "5"://关限
                tvSetTv.setText("关门电流限值:");
                edSet.setHint("请输入关门电流限值");
                tvSetType.setText("电流限值范围: 0-99");
                break;
            case "6"://开时

                tvSetTv.setText("开门保持时间:");
                edSet.setHint("请输开门保持时间");
                tvSetType.setText("开门保持时间范围: 0-255秒");
                break;
            case "7"://检测
                tvSetTv.setText("主板检测周期:");
                edSet.setHint("请输入主板检测时间");
                tvSetType.setText("主板检测周期时间范围: 10-100秒");
                break;
            case "10":
                tvSetTv.setText("内桶深度:");
                edSet.setHint("请输入内桶深度");
                tvSetType.setText("内桶深度范围: 0-100");
                break;
            case "11":
                tvSetTv.setText("传感器到桶底距离:");
                edSet.setHint("请输入传感器到桶底距离");
                tvSetType.setText("传感器到桶底距离范围: 0-240");
                break;
        }

        edSet.post(new Runnable() {
            @Override
            public void run() {
                edSet.getText().clear();
                edSet.requestFocus();

            }
        });

        recyclerDigitalKeyboard.setEditText(edSet);
        recyclerDigitalKeyboard.setOnConfirmListener(new DigitalKeyboard.OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (timer != null) {
                    timer.cancel();
                }
                if (backAndTime != null) {
                    backAndTime.stop();
                }
                String number = edSet.getText().toString().trim();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < 6; i++) {
                    stringBuilder.append(String.valueOf(i)).append("-").append(number).append(",");
                }
                final String string = stringBuilder.toString().substring(0, stringBuilder.toString().lastIndexOf(","));
                switch (type) {
                    case "0":
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidP144:" + string + ";");
                            }
                        }, 2000);


                        break;
                    case "1":
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidP145:" + string + ";");
                            }
                        }, 2000);


                        break;
                    case "2":
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidP139:" + string + ";");
                            }
                        }, 2000);


                        break;
                    case "3":
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidP141:" + string + ";");
                            }
                        }, 2000);

                        break;
                    case "4":
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidP140:" + string + ";");
                            }
                        }, 2000);

                        break;
                    case "5":
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidP142:" + string + ";");
                            }
                        }, 2000);

                        break;
                    case "6":
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidP143:" + string + ";");
                            }
                        }, 2000);

                        break;
                    case "7":
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidP206:" + edSet.getText().toString().trim() + ";");
                            }
                        }, 2000);

                        break;
                    case "10":
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidP135:" + string + ";");
                            }
                        }, 2000);
                        break;
                    case "11":
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidP136:" + string + ";");
                            }
                        }, 2000);
                        break;
                }
                /*
                srdialog.setContent("开箱占空比:" + edSet.getText().toString().trim());
                srdialog.setOnConfirmClickListener(new SettingRightDialog.ConfirmCallBack() {
                    @Override
                    public void onConfirm() {
                        srdialog.dismiss();
                        finish();
                    }
                });
                srdialog.setOnCloseClickListener(new SettingRightDialog.CloseCallBack() {
                    @Override
                    public void onClose() {
                        srdialog.dismiss();
                        tvSetResult.setVisibility(View.VISIBLE);
                        tvSetResult.setText("已设置开箱占空比:" + edSet.getText().toString().trim());
                        edSet.setText("");
                    }
                });
                srdialog.show(getFragmentManager(), "setting_dialog");
                */


            }
        });
    }

    private void getSettingDialog(final String type) {

        srdialog.setTitle("投口设置成功");
        switch (type) {
            case "0":
                srdialog.setContent("开门时间:" + edSet.getText().toString().trim() + "秒");
                break;
            case "1":
                srdialog.setContent("开门时间:" + edSet.getText().toString().trim() + "秒");
                break;
            case "2":
                srdialog.setContent("开门占空比:" + edSet.getText().toString().trim());
                break;
            case "3":
                srdialog.setContent("关门占空比:" + edSet.getText().toString().trim());
                break;
            case "4":
                srdialog.setContent("开门电流限值:" + edSet.getText().toString().trim());
                break;
            case "5":
                srdialog.setContent("关门电流限值:" + edSet.getText().toString().trim());
                break;
            case "6":
                srdialog.setContent("开门保持时间:" + edSet.getText().toString().trim() + "秒");
                break;
            case "7":
                srdialog.setContent("检测周期时间:" + edSet.getText().toString().trim() + "秒");
                break;
            case "10":
                srdialog.setContent("内桶深度:" + edSet.getText().toString().trim());
                break;
            case "11":
                srdialog.setContent("传感器到桶底距离:" + edSet.getText().toString().trim());
                break;

        }


        srdialog.setOnConfirmClickListener(new SettingRightDialog.ConfirmCallBack() {
            @Override
            public void onConfirm() {
                srdialog.dismiss();
                finish();
            }
        });
        srdialog.setOnCloseClickListener(new SettingRightDialog.CloseCallBack() {
            @Override
            public void onClose() {
                srdialog.dismiss();
                tvSetResult.setVisibility(View.VISIBLE);
                switch (type) {
                    case "0":
                        tvSetResult.setText("开门时间:" + edSet.getText().toString().trim() + "秒");
                        break;
                    case "1":
                        tvSetResult.setText("开门时间:" + edSet.getText().toString().trim() + "秒");
                        break;
                    case "2":
                        tvSetResult.setText("开门占空比:" + edSet.getText().toString().trim());
                        break;
                    case "3":
                        tvSetResult.setText("关门占空比:" + edSet.getText().toString().trim());
                        break;
                    case "4":
                        tvSetResult.setText("开门电流限值:" + edSet.getText().toString().trim());
                        break;
                    case "5":
                        tvSetResult.setText("关门电流限值:" + edSet.getText().toString().trim());
                        break;
                    case "6":
                        tvSetResult.setText("开门保持时间:" + edSet.getText().toString().trim() + "秒");
                        break;
                    case "7":
                        tvSetResult.setText("检测周期时间:" + edSet.getText().toString().trim() + "秒");
                        break;
                    case "10":
                        tvSetResult.setText("内桶深度:" + edSet.getText().toString().trim());
                        break;
                    case "11":
                        tvSetResult.setText("传感器到桶底距离:" + edSet.getText().toString().trim());
                        break;

                }
                // tvSetResult.setText("已设置开箱占空比:" + edSet.getText().toString().trim());
                edSet.setText("");
            }
        });
        srdialog.show(getFragmentManager(), "setting_dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        serialPort = serialPortUtils.openSerialPort();
    }

    private void initTimer() {
        backAndTime.setVisibility(View.VISIBLE);
        backAndTime.setTimerVisableStatue(false);
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mStartHandler != null) {
            mStartHandler.removeCallbacksAndMessages(null);
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (serialPort != null) {
            serialPortUtils.closeSerialPort();
        }
//        if(srdialog!=null){
//            srdialog.dismiss();
//
//        }
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
                   // .asBitmap()
                    .skipMemoryCache(true)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                   // .transform(transformation)
                    .into(imageView);

        }

    }
}
