package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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

public class CollectorTakeFinishActivity extends BaseActivity {


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
    @Bind(R.id.tv_balance)
    TextView tvBalace;
    @Bind(R.id.btn_again_recycle)
    Button btnAgainRecycle;
    @Bind(R.id.btn_back_index)
    Button btnBackIndex;
    @Bind(R.id.tv_tip_08)
    TextView tvTip08;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;

    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_collector_take_finish)
    RelativeLayout activityCollectorTakeFinish;
    private GarbageParam garbageParam;
    private PreferencesUtil prf;
    private ArrayList<GarbageParam> prfList;

    public SerialPort serialPort;
    public Handler mStartHandler;
    public byte[] mBuffer;
    // public  String reciveData = "";
    public StringBuilder reciveData;
    public SerialPortUtils serialPortUtils = new SerialPortUtils("/dev/ttyS4", 9600);
    private String category;
    private ArrayList<Integer> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);
        setContentView(R.layout.activity_collector_take_finish);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

//        System.loadLibrary("serial_port");
//        serialPort = serialPortUtils.openSerialPort();
//        mStartHandler = new Handler();

        prf = new PreferencesUtil(this);
        reciveData = new StringBuilder();
        prfList = prf.getDataList(Constant.DELIVER_LIST);
        Logger.e("prfList--->" + prfList.size());
        setListener();
        initData();
    }
    @Override
    public void getFindData(final String s) {
        Logger.e("recive--->" + s);

        if (s.startsWith("E28:")) {

        } else if (s.startsWith("E21:")) {

        }
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        //setListener();
        initBanner();
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
        backAndTime.setVisibility(View.GONE);
        getBlance();
        garbageParam = getIntent().getBundleExtra("message").getParcelable("garbage");
        switch (garbageParam.getCategory()) {
            case "饮料瓶":
                category = "6";
                break;
            case "纸类1箱":
            case "纸类2箱":
                category = "3";
                break;
            case "书籍":
                category = "4";
                break;
            case "塑料":
                category = "1";
                break;
            case "纺织物1箱":
            case "纺织物2箱":
                category = "0";
                break;
            case "金属":
                category = "2";
                break;
            case "玻璃":
                category = "5";
                break;
            case "有害垃圾":
                category = "5";
                break;
        }
        tvCategory.setText(garbageParam.getCategory() + "回收物");

        //    String quantity = new BigDecimal(garbageParam.getQuantity()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).toString();

        if (garbageParam.getCategory().equals("饮料瓶")) {
            tvGarbageValue.setText(garbageParam.getQuantity() + "个");
        } else {
            tvGarbageValue.setText(StringUtil.getTotalWeight(garbageParam.getQuantity()) + "公斤");
        }


        if (garbageParam.getCategory().equals("玻璃") | garbageParam.getCategory().equals("有害垃圾")) {
            //  tvHarmandglass.setVisibility(View.VISIBLE);
            tvPriceValue.setText("0.00元");
        } else {
            // tvHarmandglass.setVisibility(View.GONE);
            tvPriceValue.setText(StringUtil.getTotalPrice(garbageParam.getMoney(), garbageParam.getQuantity(), category) + "元");
        }

        btnMyRecycler.setVisibility(View.GONE);
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
                        String balance = obj.getString("balance");
                        tvBalace.setText(balance + "元");

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initTimer();
    }

    private void initTimer() {
        backAndTime.setTimer(280);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.setBackVisableStatue(false);
        backAndTime.start();
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                prf.clearPrefs();
                clearStatus();

            }
        });
        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                prf.deletPrefs(Constant.LOGIN_STATUS);
//                openActivity(UserSelectActivity.class);
//                finish();
                clearStatus();
            }
        });
    }

    private void clearStatus() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", prf.readPrefs(Constant.DEVICEID));
            jsonObject.put("teleNum",prf.readPrefs(Constant.COLLECTOR_MOBILE));
            addSubscription(Constant.HTTP_URL + "machine/recycling/clearStatus", jsonObject.toString(), new CallBack<String>() {
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
                        openActivity(UserSelectActivity.class);
                        finish();
                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_again_recycle, R.id.btn_back_index})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_again_recycle:
                openActivity(RecylerSelectActivity.class);
                finish();
                break;
            case R.id.btn_back_index:
                prf.deletPrefs(Constant.LOGIN_STATUS);
                //openActivity(UserSelectActivity.class);
                // finish();
                clearStatus();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backAndTime.stop();
        if (mStartHandler != null) {
            mStartHandler.removeCallbacksAndMessages(null);
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
