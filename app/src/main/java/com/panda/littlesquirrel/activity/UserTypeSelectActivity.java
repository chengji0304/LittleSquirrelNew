package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.panda.littlesquirrel.adapter.GarbageTypeReclyViewAdapter;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.entity.Garbage;
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.entity.PriceInfo;
import com.panda.littlesquirrel.entity.SelcetInfo;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;
import com.panda.littlesquirrel.utils.SerialPortUtils;
import com.panda.littlesquirrel.utils.SoundPlayUtil;

import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.youth.banner.Banner;


import com.alibaba.fastjson.JSONArray;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

import android_serialport_api.SerialPort;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserTypeSelectActivity extends BaseActivity {

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
    @Bind(R.id.ll_top)
    FrameLayout llTop;
    @Bind(R.id.tv_opening_tip)
    TextView tvOpeningTip;
    @Bind(R.id.tv_line_01)
    TextView tvLine01;
    @Bind(R.id.tv_tip_02)
    TextView tvTip02;
    @Bind(R.id.tv_line_02)
    TextView tvLine02;
    @Bind(R.id.imgs_show)
    RelativeLayout imgsShow;
    @Bind(R.id.btn_open)
    Button btnOpen;
    @Bind(R.id.tv_60g)
    TextView tv60g;
    @Bind(R.id.normal)
    LinearLayout normal;
    @Bind(R.id.img_bottle_deliver_state)
    ImageView imgBottleDeliverState;
    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.tv_tip_05)
    TextView tvTip05;
    @Bind(R.id.btn_finish)
    Button btnFinish;
    @Bind(R.id.deliver_bottle)
    LinearLayout deliverBottle;
    @Bind(R.id.ll_mid)
    FrameLayout llMid;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.ll_buttom)
    FrameLayout llButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_user_type_select)
    RelativeLayout activityUserTypeSelect;
    @Bind(R.id.ry_grabage_type)
    RecyclerView ryGrabageType;
    //   private SelcetInfo info;
    private ArrayList<Garbage> mlist;
    private GarbageTypeReclyViewAdapter adapter;
    private Handler handle = new Handler();
    private PreferencesUtil prf;
    private ArrayList<GarbageParam> prfList;
    private String number;
    private ArrayList<PriceInfo> priceList;
    private String userPrice;
    private Handler handler;
    private ArrayList<Integer> images;
    private String category;
    private String quatity;
    private String openStatus;
    private String closeStatus;
    private CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_select);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        System.loadLibrary("serial_port");
        serialPort = serialPortUtils.openSerialPort();
        mStartHandler = new Handler();
        reciveData = new StringBuilder();
        handle = new Handler();
        prf = new PreferencesUtil(this);
        prfList = prf.getDataList(Constant.DELIVER_LIST);
        priceList = prf.getPriceList(Constant.PRICE_LIST);

        initData();
    }

    @Override
    public void getFindData(final String s) {
        Logger.e("UserTypeSelectActivity--->" + s);
        if(!StringUtil.isEmpty(s)){
            if (s.startsWith("E27:")||s.contains("27")) {
                openStatus="1";
                btnOpen.setEnabled(true);
                backAndTime.stop();
                btnOpen.setText("投递完成");
                btnOpen.setTextColor(Color.parseColor("#FFFFFF"));
                openBoxNotify();
            } else if (s.startsWith("E28:")) {
                closeStatus="1";
                if (number.equals("6")) {

                } else {
                    btnOpen.setEnabled(false);
                    btnOpen.setText("正在称重");
                    SoundPlayUtil.play(18);

                }
            } else if (s.contains("E21:")) {
                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //确认提醒
                        serialPortUtils.sendSerialPort("androidC50:0;");
                        Map<String, String> info = StringUtil.getInfo(s);
                        Logger.d(info);
                        if (info.size() == 1) {
                            Iterator<Map.Entry<String, String>> entries = info.entrySet().iterator();
                            while (entries.hasNext()) {
                                Map.Entry<String, String> entry = entries.next();
                                String[] value = entry.getValue().split("-");
                                if (category.equals("7")) {
                                    //  Logger.e("1");
                                    quatity=String.valueOf(Integer.valueOf(value[1]));
                                    prfList.add(new GarbageParam(category, "",quatity, "1"));
                                } else if (category.equals("8")) {
                                    //Logger.e("1");
                                    quatity=String.valueOf(Integer.valueOf(value[1]));
                                    prfList.add(new GarbageParam(category, "", quatity, "1"));

                                } else if (category.equals("1")) {
                                    // Logger.e("1");
                                    quatity=value[1];
                                    prfList.add(new GarbageParam(category, "", quatity, userPrice));

                                } else {
                                    quatity=String.valueOf(Integer.valueOf(value[1]));
                                    prfList.add(new GarbageParam(category, "",quatity, userPrice));

//                                if (Integer.valueOf(value[1]) < 6) {
//                                    prfList.add(new GarbageParam(category, "", String.valueOf(Integer.valueOf(value[1])), "0.00"));
//                                } else {
//                                    Logger.e("1");
//                                    prfList.add(new GarbageParam(category, "", String.valueOf(Integer.valueOf(value[1])), userPrice));
//                                }

                                }

                            }
                        }
                        Logger.e("prfListsize--->" + prfList.size());
                        Logger.d(JSONArray.toJSONString(prfList));
                        Map<String, GarbageParam> map = new HashMap<String, GarbageParam>();
                        for (GarbageParam garbageParam : prfList) {
                            String key = garbageParam.getCategory();
                            if (map.containsKey(key)) {
                                GarbageParam gar = map.get(key);
                                String money = gar.getMoney();
                                String quantity = gar.getQuantity();
                                // String totalMoney = StringUtil.getTotal(money, garbageParam.getMoney());
                                String totalQuantity = StringUtil.getTotal(quantity, garbageParam.getQuantity());
                                garbageParam.setMoney(money);
                                garbageParam.setQuantity(totalQuantity);

                            }
                            map.put(key, garbageParam);
                        }
                        prfList.clear();
                        prfList.addAll(map.values());
                        Logger.e("-----------------------");
                        Logger.d(JSONArray.toJSONString(prfList));
                        prf.setDataList(Constant.DELIVER_LIST, prfList);
                        if (prfList.size() == 1) {
                            openActivity(DeliverFinishActivity.class);
                            finish();
                        } else {
                            openActivity(DeliverListFinishActivity.class);
                            finish();
                        }


                    }
                }, 2000);


            }
            reciveData = new StringBuilder();
        }else{
//            handle.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    serialPortUtils.sendSerialPort("androidC51:" + number + ";");
//                }
//            },4000);
        }


    }

    /*
     开箱提醒
     */
    private void openBoxNotify() {
        try {
            JSONObject jsonObjcet = new JSONObject();
            jsonObjcet.put("deviceId", prf.readPrefs(Constant.DEVICEID));
            jsonObjcet.put("openBoxStatus", 1);
            jsonObjcet.put("phoneNum", prf.readPrefs(Constant.USER_MOBILE));
            addSubscription(Constant.HTTP_URL + "machine/delivery/openBoxNotify", jsonObjcet.toString(), new CallBack<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(ApiException e) {
                    backAndTime.setTimer(backAndTime.getCurrentTime());
                    backAndTime.start();
                }

                @Override
                public void onSuccess(String s) {
                    Logger.e("s-->" + s);
                    backAndTime.setTimer(backAndTime.getCurrentTime());
                    backAndTime.start();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        setListener();
        initBanner();
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
        mlist = new ArrayList<>();
        btnMyRecycler.setVisibility(View.GONE);
        Logger.e("type--->" + prf.readPrefs(Constant.GARBAGE_TYPE));
        switch (prf.readPrefs(Constant.GARBAGE_TYPE)) {
            case "饮料瓶":
                category = "1";
                number = "6";
             //   userPrice = priceList.get(0).getUserPrice();
             //   userPrice=priceList
                for(int i =0;i<priceList.size();i++){
                if(priceList.get(i).getType().equals(category)){
                    userPrice = priceList.get(i).getUserPrice();
                }
            }
                Logger.e("userPrice--->"+userPrice);
                mlist.add(new Garbage(R.drawable.bottle_fruit, "果汁瓶"));
                mlist.add(new Garbage(R.drawable.bottle_coco, "可乐瓶"));
                mlist.add(new Garbage(R.drawable.bottle_milk, "牛奶瓶"));
                mlist.add(new Garbage(R.drawable.bottle_can, "果汁瓶"));
                btnOpen.setText("请开始投递");
                tvOpeningTip.setText("请在投递口灯光闪烁时投递");
                tv60g.setVisibility(View.GONE);
                break;
            case "纸类1箱":
                category = "2";
                number = "3";
               //userPrice = priceList.get(1).getUserPrice();
                for(int i =0;i<priceList.size();i++){
                    if(priceList.get(i).getType().equals(category)){
                        userPrice = priceList.get(i).getUserPrice();
                    }
                }
                Logger.e("userPrice--->"+userPrice);
                mlist.add(new Garbage(R.drawable.paper_carton, "纸盒"));
                mlist.add(new Garbage(R.drawable.paper_bag, "包装纸袋"));
                mlist.add(new Garbage(R.drawable.paper_news, "废报纸"));
                mlist.add(new Garbage(R.drawable.paper_card, "明信片"));
                break;
            case "纸类2箱":
                category = "2";
                number = "4";
                //userPrice = priceList.get(1).getUserPrice();
                for(int i =0;i<priceList.size();i++){
                    if(priceList.get(i).getType().equals(category)){
                        userPrice = priceList.get(i).getUserPrice();
                    }
                }
                Logger.e("userPrice--->"+userPrice);
                mlist.add(new Garbage(R.drawable.paper_carton, "纸盒"));
                mlist.add(new Garbage(R.drawable.paper_bag, "包装纸袋"));
                mlist.add(new Garbage(R.drawable.paper_news, "废报纸"));
                mlist.add(new Garbage(R.drawable.paper_card, "明信片"));
                break;
            case "书籍":
                category = "3";
                number = "5";
               // userPrice = priceList.get(2).getUserPrice();
                for(int i =0;i<priceList.size();i++){
                    if(priceList.get(i).getType().equals(category)){
                        userPrice = priceList.get(i).getUserPrice();
                    }
                }
                Logger.e("userPrice--->"+userPrice);
                mlist.add(new Garbage(R.drawable.book_textbook, "课本/书籍"));
                mlist.add(new Garbage(R.drawable.book_journal, "杂志/书刊"));
                mlist.add(new Garbage(R.drawable.book_pictorial, "画报/画册"));
                mlist.add(new Garbage(R.drawable.book_office, "办公废纸"));
                break;
            case "塑料":
                category = "4";
                number = "2";
               // userPrice = priceList.get(3).getUserPrice();
                for(int i =0;i<priceList.size();i++){
                    if(priceList.get(i).getType().equals(category)){
                        userPrice = priceList.get(i).getUserPrice();
                    }
                }
                Logger.e("userPrice--->"+userPrice);
                mlist.add(new Garbage(R.drawable.plastic_shell, "塑料外壳"));
                mlist.add(new Garbage(R.drawable.plastic_container, "收纳盒"));
                mlist.add(new Garbage(R.drawable.plastic_basin, "塑料桶/盆"));
                mlist.add(new Garbage(R.drawable.plastic_pipe, "塑料水管"));
                break;
            case "纺织物1箱":
                category = "5";
                number = "0";
                for(int i =0;i<priceList.size();i++){
                    if(priceList.get(i).getType().equals(category)){
                        userPrice = priceList.get(i).getUserPrice();
                    }
                }
             //   userPrice = priceList.get(4).getUserPrice();
                Logger.e("userPrice--->"+userPrice);
                mlist.add(new Garbage(R.drawable.fabric_clothes, "衣服"));
                mlist.add(new Garbage(R.drawable.fabric_scarf, "围巾"));
                mlist.add(new Garbage(R.drawable.fabric_towel, "毛巾"));
                mlist.add(new Garbage(R.drawable.fabric_bolster, "抱枕/靠枕"));
                break;
            case "纺织物2箱":
                category = "5";
                number = "1";
                for(int i =0;i<priceList.size();i++){
                    if(priceList.get(i).getType().equals(category)){
                        userPrice = priceList.get(i).getUserPrice();
                    }
                }
               // userPrice = priceList.get(4).getUserPrice();
                Logger.e("userPrice--->"+userPrice);
                mlist.add(new Garbage(R.drawable.fabric_clothes, "衣服"));
                mlist.add(new Garbage(R.drawable.fabric_scarf, "围巾"));
                mlist.add(new Garbage(R.drawable.fabric_towel, "毛巾"));
                mlist.add(new Garbage(R.drawable.fabric_bolster, "抱枕/靠枕"));
                break;
            case "金属":
                category = "6";
                number = "2";
                for(int i =0;i<priceList.size();i++){
                    if(priceList.get(i).getType().equals(category)){
                        userPrice = priceList.get(i).getUserPrice();
                    }
                }
               // userPrice = priceList.get(5).getUserPrice();
                Logger.e("userPrice--->"+userPrice);
                mlist.add(new Garbage(R.drawable.metal_wire, "电线"));
                mlist.add(new Garbage(R.drawable.metal_frame, "铁质置物架"));
                mlist.add(new Garbage(R.drawable.metal_plates, "金属餐具"));
                mlist.add(new Garbage(R.drawable.metal_pan, "平底锅"));
                break;
            case "玻璃":

                number = "5";
                userPrice = "1";

                Logger.e("userPrice--->"+userPrice);
                mlist.add(new Garbage(R.drawable.glass_cup, "酒杯"));
                mlist.add(new Garbage(R.drawable.glass_winebottle, "酒瓶"));
                mlist.add(new Garbage(R.drawable.glass_glasses, "玻璃碎片"));
                mlist.add(new Garbage(R.drawable.glass_plates, "玻璃餐具"));
                break;
            case "有害垃圾":

                number = "5";
                userPrice = "1";
                mlist.add(new Garbage(R.drawable.harm_drug, "过期药品"));
                mlist.add(new Garbage(R.drawable.harm_temper, "温度计"));
                mlist.add(new Garbage(R.drawable.harm_disinfector, "消毒剂"));
                mlist.add(new Garbage(R.drawable.harm_cell, "废电池"));
                break;

        }
        ryGrabageType.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new GarbageTypeReclyViewAdapter(this, mlist);
        ryGrabageType.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (prf.readPrefs(Constant.GARBAGE_TYPE)) {
            case "饮料瓶":
                SoundPlayUtil.play(17);
                break;
            default:
                SoundPlayUtil.play(13);
                break;
        }
        initTimer();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                serialPortUtils.sendSerialPort("androidC51:" + number + ";");
            }
        }, 500);


        timer = new CountDownTimer(1000 * 10, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
              if(StringUtil.isEmpty(openStatus)){
//                  btnOpen.setText("打开箱门");
//                  btnOpen.setEnabled(true);
                  //
                  serialPortUtils.sendSerialPort("androidC51:" + number + ";");
              }

            }
        }.start();

    }

    private void initTimer() {
        backAndTime.setTimer(150);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.setBackVisableStatue(false);
        backAndTime.start();
        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                backAndTime.stop();
                if(StringUtil.isEmpty(quatity)){
                    quatity="-1";
                    prfList.add(new GarbageParam(category, "",quatity, userPrice));
                    Logger.e("prfListsize--->" + prfList.size());
                    Logger.d(JSONArray.toJSONString(prfList));
                    Map<String, GarbageParam> map = new HashMap<String, GarbageParam>();
                    for (GarbageParam garbageParam : prfList) {
                        String key = garbageParam.getCategory();
                        if (map.containsKey(key)) {
                            GarbageParam gar = map.get(key);
                            String money = gar.getMoney();
                            String quantity = gar.getQuantity();
                            // String totalMoney = StringUtil.getTotal(money, garbageParam.getMoney());
                            String totalQuantity = StringUtil.getTotal(quantity, garbageParam.getQuantity());
                            garbageParam.setMoney(money);
                            garbageParam.setQuantity(totalQuantity);

                        }
                        map.put(key, garbageParam);
                    }
                    prfList.clear();
                    prfList.addAll(map.values());
                    Logger.e("-----------------------");
                    Logger.d(JSONArray.toJSONString(prfList));
                    prf.setDataList(Constant.DELIVER_LIST, prfList);
                    if (prfList.size() == 1) {
                        openActivity(DeliverFinishActivity.class);
                        finish();
                    } else {
                        openActivity(DeliverListFinishActivity.class);
                        finish();
                    }

                }else{
                    if (prfList.size() == 1) {
                        openActivity(DeliverFinishActivity.class);
                        finish();
                    } else {
                        openActivity(DeliverListFinishActivity.class);
                        finish();
                    }
                }


            }
        });
    }

    @OnClick({R.id.btn_open, R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                if(btnOpen.getText().toString().trim().equals("打开箱门")){
                    btnOpen.setText("正在开箱…");
                    btnOpen.setEnabled(false);
                    handle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            serialPortUtils.sendSerialPort("androidC51:" + number + ";");
                        }
                    }, 500);
                    timer = new CountDownTimer(1000 * 10, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            if(StringUtil.isEmpty(openStatus)){
                                //
                                serialPortUtils.sendSerialPort("androidC51:" + number + ";");
                            }

                        }
                    }.start();

                }else {
                    if (number.equals("6")) {
                        btnOpen.setEnabled(false);
                        btnOpen.setText("正在计数");
                        btnOpen.setTextColor(Color.parseColor("#999999"));
                        handle.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //关闭投口
                                serialPortUtils.sendSerialPort("androidC55:" + number + ";");
                            }
                        }, 500);

                        timer = new CountDownTimer(1000 * 14, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                if(StringUtil.isEmpty(closeStatus)){
                                    btnOpen.setText("投递完成");
                                    btnOpen.setEnabled(true);
                                }

                            }
                        }.start();
                    } else {
                        btnOpen.setEnabled(false);
                        SoundPlayUtil.play(19);
                        btnOpen.setText("正在关门");
                        btnOpen.setTextColor(Color.parseColor("#999999"));
                        handle.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //关闭投口
                                serialPortUtils.sendSerialPort("androidC55:" + number + ";");
                            }
                        }, 500);

                        timer = new CountDownTimer(1000 * 14, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                if(StringUtil.isEmpty(closeStatus)){
                                    btnOpen.setText("投递完成");
                                    btnOpen.setEnabled(true);
                                }

                            }
                        }.start();

                    }
                }


                break;
            case R.id.btn_finish:


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
        if (handle != null) {
            handle.removeCallbacksAndMessages(null);
        }
        serialPortUtils.closeSerialPort();
        if (reciveData != null) {
            reciveData = null;
        }

        if(timer!=null){
            timer.cancel();
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
