package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;
import com.panda.littlesquirrel.utils.SerialPortUtils;
import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.panda.littlesquirrel.view.ErrorStatusDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import android_serialport_api.SerialPort;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.panda.littlesquirrel.R.drawable.timer;

public class CollectorTakeGoodsActivity extends BaseActivity {


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
    @Bind(R.id.tv_line_07)
    TextView tvLine07;
    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.tv_line_08)
    TextView tvLine08;
    @Bind(R.id.tv_tip_07)
    TextView tvTip07;
    @Bind(R.id.tv_full_state)
    TextView tvFullState;
    @Bind(R.id.tv_full_value)
    TextView tvFullValue;
    @Bind(R.id.tv_garbage)
    TextView tvGarbage;
    @Bind(R.id.tv_garbage_value)
    TextView tvGarbageValue;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_price_value)
    TextView tvPriceValue;
    @Bind(R.id.tv_tip_05)
    TextView tvTip05;
    @Bind(R.id.tv_tip_06)
    TextView tvTip06;
    @Bind(R.id.btn_recycler_finished)
    Button btnRecyclerFinished;
    @Bind(R.id.tv_open_tip)
    TextView tvOpenTip;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_collector_take_goods)
    RelativeLayout activityCollectorTakeGoods;
    private Handler handle = new Handler();
    private GarbageParam garbageParam;
    private String balance;
    private int number;
    // public SerialPort serialPort;
    // public Handler mStartHandler;
    // public byte[] mBuffer;
    private boolean isClose = false;
    private ArrayList<Integer> images;
    // public  String reciveData = "";
//    public StringBuilder reciveData;
//    public SerialPortUtils serialPortUtils = new SerialPortUtils("/dev/ttyS4", 9600);
    private ErrorStatusDialog errorStatusDialog = new ErrorStatusDialog();
    private CountDownTimer timer;
    private PreferencesUtil prf;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);
        setContentView(R.layout.activity_collector_take_goods);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        System.loadLibrary("serial_port");
        serialPort = serialPortUtils.openSerialPort();
        mStartHandler = new Handler();
        reciveData = new StringBuilder();
        prf = new PreferencesUtil(this);
        reciveData = new StringBuilder();
        setListener();
        initData();
    }

    @Override
    public void getFindData(final String s) {
        Logger.e("CollectorTakeGoodsActivity--->" + s);
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (s.contains("E26")) {
                    isClose = true;
                }
                reciveData = new StringBuilder();
            }
        }, 1000);


    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        //setListener();
        initBanner();
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
        backAndTime.setVisibility(View.GONE);
        garbageParam = getIntent().getBundleExtra("message").getParcelable("garbage");
        switch (garbageParam.getCategory()) {
            case "饮料瓶":
                number = 6;
                break;
            case "纸类1箱":
                number = 3;
                break;
            case "纸类2箱":
                number = 4;
                break;
            case "书籍":
                number = 5;
                break;
            case "塑料":
                number = 2;
                break;
            case "纺织物1箱":
                number = 0;
                break;
            case "纺织物2箱":
                number = 1;
                break;
            case "金属":
                number = 2;
                break;
            case "玻璃":
                number = 5;
                break;
            case "有害垃圾":
                number = 5;
                break;

        }
        // serialPortUtils.sendSerialPort("androidC54:" + number + ";");

        getBlance();

        tvCategory.setText(garbageParam.getCategory() + "回收物");
        // tvFullValue.setText(StringUtil.isEmpty(garbageParam.getFullState()) ? "未满箱" : garbageParam.getFullState());
        if (!StringUtil.isEmpty(garbageParam.getFullState())) {
            if (Integer.valueOf(garbageParam.getFullState()) >= 80 & Integer.valueOf(garbageParam.getFullState()) < 100) {
                tvFullValue.setTextColor(Color.parseColor("#ff333333"));
                tvFullValue.setText("80%");
            } else if (Integer.valueOf(garbageParam.getFullState()) >= 100) {
                tvFullValue.setTextColor(Color.parseColor("#f6546c"));
                tvFullValue.setText("满箱");
            } else {
                tvFullValue.setTextColor(Color.parseColor("#ff333333"));
                tvFullValue.setText("未满箱");
            }
        }

        //  String quantity = new BigDecimal(garbageParam.getQuantity()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).toString();

        if (garbageParam.getCategory().equals("饮料瓶")) {
            tvGarbageValue.setText(garbageParam.getQuantity() + "个");
        } else {
            tvGarbageValue.setText(StringUtil.getTotalWeight(garbageParam.getQuantity()) + "公斤");
        }


        if (garbageParam.getCategory().equals("玻璃") | garbageParam.getCategory().equals("有害垃圾")) {

            tvPriceValue.setText("0.00元");
        } else {

            tvPriceValue.setText(StringUtil.getTotalPrice(garbageParam.getMoney(), garbageParam.getQuantity(), String.valueOf(number)) + "元");
        }
        btnMyRecycler.setVisibility(View.GONE);

//        handle.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 1000);
        /*
        garbageParam = getIntent().getBundleExtra("message").getParcelable("garbage");
        tvCategory.setText(garbageParam.getCategory() + "回收物");
        tvFullValue.setText(StringUtil.isEmpty(garbageParam.getFullState())?"未满箱":garbageParam.getFullState());
        if (garbageParam.getFullState().equals("满箱")) {
            tvFullValue.setTextColor(Color.parseColor("#f6546c"));
        } else {
            tvFullValue.setTextColor(Color.parseColor("#ff333333"));
        }
        if (garbageParam.getCategory().equals("饮料瓶")) {
            tvGarbageValue.setText(garbageParam.getQuantity() + "个");
        } else {
            tvGarbageValue.setText(garbageParam.getQuantity() + "公斤");
        }
        tvPriceValue.setText(StringUtil.isEmpty(garbageParam.getMoney())?"0.0元":garbageParam.getMoney() + "元");
        btnMyRecycler.setVisibility(View.GONE);
        */
    }

    private void getBlance() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", prf.readPrefs(Constant.COLLECTOR_MOBILE));
            addSubscription(Constant.HTTP_URL + "machine/recycling/getRecyclingBalance", jsonObject.toString(), new CallBack<String>() {
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
                    //{"stateCode":1,"errorMessage":"处理成功","result":{"balance":500.00}}
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if (stateCode.equals("1")) {
                        com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSON.parseObject(jsonObject.getString("result"));
                        balance = obj.getString("balance");
                        tvTip06.setText(balance + "元");

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        tvOpenTip.setText("小松鼠正在努力开箱");
////        btnRecyclerFinished.setEnabled(false);
//        btnRecyclerFinished.setText("开箱取货");
//        btnRecyclerFinished.setTextColor(Color.parseColor("#FFFFFF"));

        /*
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvTip07.setText(garbageParam.getCategory() + "取货完成");
                tvOpenTip.setText("箱门已经打开请进行回收");
                btnRecyclerFinished.setEnabled(true);
                btnRecyclerFinished.setText("回收完成");
                btnRecyclerFinished.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }, 3000);
        */
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                serialPortUtils.sendSerialPort("androidC54:" + number + ";");
                btnRecyclerFinished.setEnabled(true);
                btnRecyclerFinished.setTextColor(Color.parseColor("#FFFFFF"));
                btnRecyclerFinished.setText("回收完成");
                tvOpenTip.setText("箱门已开请进行回收");
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // initTimer();


    }

    private void initTimer() {
        backAndTime.setTimer(60);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.setBackVisableStatue(false);
        backAndTime.start();
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                Bundle bundle = new Bundle();
                bundle.putParcelable("garbage", garbageParam);
                openActivity(CollectorTakeFinishActivity.class, bundle);
                finish();
            }
        });
    }

    @OnClick(R.id.btn_recycler_finished)
    public void onViewClicked() {
        // backAndTime.stop();
        // Logger.e("close--->"+isClose);
        if (isClose == true) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("garbage", garbageParam);
            openActivity(CollectorTakeFinishActivity.class, bundle);
            finish();
        } else {
            errorStatusDialog.setContent("请关闭箱门");
            errorStatusDialog.setImage(R.drawable.error);
            errorStatusDialog.setOnConfirmClickListener(new ErrorStatusDialog.ConfirmCallBack() {
                @Override
                public void onConfirm() {
                    backAndTime.stop();


                    errorStatusDialog.dismiss();

                }
            });
            errorStatusDialog.show(getFragmentManager(), "error_dialog");
            timer = new CountDownTimer(1000 * 10, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
                    if (secondsRemaining > 0) {

                    }
                }

                @Override
                public void onFinish() {

                    if (errorStatusDialog != null) {
                        errorStatusDialog.dismiss();
                    }

                    timer.cancel();
                    backAndTime.setTimer(backAndTime.getCurrentTime());
                    backAndTime.start();

                }
            }.start();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backAndTime.stop();
        serialPortUtils.closeSerialPort();
        if (mStartHandler != null) {
            mStartHandler.removeCallbacksAndMessages(null);
        }
        if (reciveData != null) {
            reciveData = null;
        }
        isClose = false;
        if (handle != null) {
            handle.removeCallbacksAndMessages(null);
        }
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
