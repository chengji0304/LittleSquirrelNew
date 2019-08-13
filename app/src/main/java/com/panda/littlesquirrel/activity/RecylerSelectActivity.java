package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.adapter.CollectSelectReclyViewAdapater;
import com.panda.littlesquirrel.adapter.UserSelectReclyViewAdapater;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.entity.PriceInfo;
import com.panda.littlesquirrel.entity.SelcetInfo;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import android_serialport_api.SerialPort;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecylerSelectActivity extends BaseActivity {


    ArrayList<Integer> images;
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
    @Bind(R.id.rv_item_gara)
    RecyclerView rvItemGara;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_recyler_select)
    RelativeLayout activityRecylerSelect;
    private GarbageParam garbageParam;
    private ArrayList<SelcetInfo> mlist;
    private PreferencesUtil prf;
    private CollectSelectReclyViewAdapater adapter;
    private ArrayList<PriceInfo> priceList;
    private String weight0;
    private String weight1;
    private String weight2;
    private String weight3;
    private String weight4;
    private String weight5;
    private String weight6;
    private String capacity0;
    private String capacity1;
    private String capacity2;
    private String capacity3;
    private String capacity4;
    private String capacity5;
    private String capacity6;
    String str = "S8:0;S59:431055193939415005D5FF39-1;S60:1-0,2-0,3-0;S61:0-0,1-10,2-110,3-30,4-40,5-88,6-60;S62:0-100,1-234,2-620,3-1200,4-3600,5-620,6-100;@";
    //public SerialPort serialPort;
//    public byte[] mBuffer;
//    public  String reciveData = "";
    private String category;
    public SerialPort serialPort;
    public Handler handler;
    private String total;
    private String bottlePrice;
    private String bookPrice;
    private String paperPrice;
    private String metalPrice;
    private String plasticPrice;
    private String fabricPrice;
//    public Handler mStartHandler;
//    public byte[] mBuffer;
//    //public  String reciveData = "";
//    public StringBuilder reciveData;
//    public SerialPortUtils serialPortUtils = new SerialPortUtils("/dev/ttyS4", 9600);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);
        setContentView(R.layout.activity_recyler_select);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        System.loadLibrary("serial_port");
        serialPort = serialPortUtils.openSerialPort();
        mStartHandler = new Handler();
        reciveData = new StringBuilder();
        handler = new Handler();
        Logger.e("mhanld-->" + mStartHandler);
        Logger.e("recive-->" + reciveData);
        prf = new PreferencesUtil(this);
        priceList = prf.getPriceList(Constant.PRICE_LIST);
//        mStartHandler = new Handler();
//        reciveData = new StringBuilder();
        setListener();
        initData();
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        initBanner();
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
        btnMyRecycler.setVisibility(View.GONE);
        getCategoryMsg();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                serialPortUtils.sendSerialPort("androidC56:1;");
//            }
//        }, 1000);

        //initBanner();
        //  mlist = getData();
        // getFindData(str);


    }
    private void getCategoryMsg() {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            jsonObject.put("category", 1);
            jsonObject.put("deviceId", prf.readPrefs(Constant.DEVICEID));
            addSubscription(Constant.HTTP_URL + "machine/recycling/getCategoryMsg", jsonObject.toString(), new CallBack<String>() {
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
                    //{"stateCode":1,"errorMessage":"处理成功","result":{"price":0.00,"countOrWeight":0.0,"category":"饮料瓶","canFullStatus":0}}
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if(stateCode.equals("1")){
                        com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSON.parseObject(jsonObject.getString("result"));
                        total= object.getString("countOrWeight");
                        if (StringUtil.isEmpty(object.getString("countOrWeight"))) {
                            total = "0";
                        } else {
                            total = object.getString("countOrWeight");
                            if(total.contains(".")){
                                total=total.substring(0,total.indexOf(".")) ;
                            }
                        }
                      //  total=total.substring(0,total.indexOf("."));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidC56:1;");
                            }
                        }, 1000);
                    }else{

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

//    private void initBanner() {
//        images = new ArrayList<>();
//        images.add(R.drawable.buttombanner2);
//        images.add(R.drawable.buttombanner3);
//        images.add(R.drawable.bottom_adv_banner);
//        //设置banner样式(显示圆形指示器)
//        bannerButtom.setBannerStyle(BannerConfig.NOT_INDICATOR);
//        //设置指示器位置（指示器居右）
//        bannerButtom.setIndicatorGravity(BannerConfig.RIGHT);
//        //设置图片加载器
//        bannerButtom.setImageLoader(new GlideImageLoader());
//        //设置图片集合
//        bannerButtom.setImages(images);
//        bannerButtom.isAutoPlay(true);
//        //设置轮播时间
//        bannerButtom.setDelayTime(4000);
//        //banner设置方法全部调用完毕时最后调用
//        bannerButtom.setOnBannerListener(new OnBannerListener() {
//
//            @Override
//            public void OnBannerClick(int position) {
//                Logger.e("position" + position);
//
//            }
//        });
//        bannerButtom.start();
//    }
//
//    class GlideImageLoader extends ImageLoader {
//        @Override
//        public void displayImage(Context context, Object path, ImageView imageView) {
//            Glide.with(context)
//                    .load(path)
//                    .fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                    .into(imageView);
//
//        }
//
//    }

    private void initTimer() {
        backAndTime.setTimer(280);
        backAndTime.setBackVisableStatue(true);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.start();
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                clearStatus();
//                openActivity(UserSelectActivity.class);
//                finish();
            }
        });

        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                clearStatus();
//                openActivity(UserSelectActivity.class);
//                finish();
            }
        });
    }

    private void clearStatus() {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            jsonObject.put("deviceID", prf.readPrefs(Constant.DEVICEID));
            jsonObject.put("teleNum", prf.readPrefs(Constant.COLLECTOR_MOBILE));
            addSubscription(Constant.HTTP_URL + "machine/recycling/clearStatus", jsonObject.toString(), new CallBack<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(ApiException e) {
                    clearStatus();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backAndTime.stop();
        if (serialPort != null) {
            serialPortUtils.closeSerialPort();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (mStartHandler != null) {
            mStartHandler.removeCallbacksAndMessages(null);
        }

    }

    @Override
    public void getFindData(String reciveData) {
        Logger.e("RecylerSelectActivity--->" + reciveData);
        if (reciveData.startsWith("S")) {
            if (reciveData.endsWith(";")) {
                reciveData = reciveData.replaceAll("--", "-");
                try {
                    Map<String, String> info = StringUtil.getInfo(reciveData);
                    org.json.JSONObject jsonObject = new org.json.JSONObject();
                    Iterator<Map.Entry<String, String>> entries = info.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<String, String> entry = entries.next();
                        jsonObject.put(entry.getKey(), entry.getValue());
                    }
                    JSONObject object = com.alibaba.fastjson.JSON.parseObject(jsonObject.toString());
                    String s62 = object.getString("S62");
                    String s61 = object.getString("S61");
                    Log.e("s62", s62);
                    Log.e("s61", s61);
                    String[] str62 = s62.split(",");
                    String[] str61 = s61.split(",");
                    weight0 = str62[0].split("-")[1];
                    Log.e("0-->", weight0);
                    weight1 = str62[1].split("-")[1];
                    Log.e("1-->", weight1);
                    weight2 = str62[2].split("-")[1];
                    Log.e("2-->", weight2);
                    weight3 = str62[3].split("-")[1];
                    Log.e("3-->", weight3);
                    weight4 = str62[4].split("-")[1];
                    Log.e("4-->", weight4);
                    weight5 = str62[5].split("-")[1];
                    Log.e("5-->", weight5);
                    capacity0 = str61[0].split("-")[1];
                    capacity1 = str61[1].split("-")[1];
                    capacity2 = str61[2].split("-")[1];
                    capacity3 = str61[3].split("-")[1];
                    capacity4 = str61[4].split("-")[1];
                    capacity5 = str61[5].split("-")[1];
//                  capacity6 = str61[6].split("-")[1];
                    mlist = new ArrayList<>();
                    for (int i = 0; i < priceList.size(); i++) {
                        if (priceList.get(i).getType().equals("3")) {
                            bookPrice=priceList.get(i).getUserPrice();
                        }
                        if (priceList.get(i).getType().equals("2")) {
                            paperPrice=priceList.get(i).getUserPrice();
                        }

                        if (priceList.get(i).getType().equals("1")) {
                            bottlePrice=priceList.get(i).getUserPrice();
                        }

                        if (priceList.get(i).getType().equals("5")) {
                            fabricPrice=priceList.get(i).getUserPrice();
                        }

                        if (priceList.get(i).getType().equals("4")) {
                            plasticPrice=priceList.get(i).getUserPrice();
                        }
                        if (priceList.get(i).getType().equals("6")) {
                            metalPrice=priceList.get(i).getUserPrice();
                        }

                    }
                    SelcetInfo bottle = new SelcetInfo(R.drawable.book, "3", "书籍", capacity5, bookPrice, weight5,"5");
                    mlist.add(bottle);
                    SelcetInfo page = new SelcetInfo(R.drawable.paper, "2", "纸类1箱", capacity3, paperPrice, weight3,"3");
                    mlist.add(page);
                    SelcetInfo book = new SelcetInfo(R.drawable.paper, "2", "纸类2箱", capacity4, paperPrice, weight4,"4");
                    mlist.add(book);
                    SelcetInfo plastic = new SelcetInfo(R.drawable.bottle, "1", "饮料瓶", "30", bottlePrice, total,"6");
                    mlist.add(plastic);
                    SelcetInfo textile = new SelcetInfo(R.drawable.metal, "6", "金属", capacity2, metalPrice, weight2,"2");
                    mlist.add(textile);
                    SelcetInfo metal = new SelcetInfo(R.drawable.fabric, "5", "纺织物1箱", capacity0, fabricPrice, weight0,"0");
                    mlist.add(metal);
                    SelcetInfo glasses = new SelcetInfo(R.drawable.fabric, "5", "纺织物2箱", capacity1, fabricPrice, weight1,"1");
                    mlist.add(glasses);
                    SelcetInfo harmgoods = new SelcetInfo(R.drawable.plastic, "4", "塑料", capacity2, plasticPrice, weight2,"2");
                    mlist.add(harmgoods);
                    rvItemGara.setLayoutManager(new GridLayoutManager(this, 4));
                    adapter = new CollectSelectReclyViewAdapater(this, mlist);
                    rvItemGara.setAdapter(adapter);
                    adapter.setOnItemClickListener(new UserSelectReclyViewAdapater.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            SelcetInfo info = mlist.get(position);
                            Logger.e("weight--->" + info.getQuantity());
                            switch (info.getTypeName()) {
                                case "饮料瓶":
                                    category = "1";
                                    break;
                                case "纸类1箱":
                                case "纸类2箱":
                                    category = "2";
                                    break;
                                case "书籍":
                                    category = "3";
                                    break;
                                case "塑料":
                                    category = "4";
                                    break;
                                case "纺织物1箱":
                                case "纺织物2箱":
                                    category = "5";
                                    break;
                                case "金属":
                                    category = "6";
                                    break;
                                case "玻璃":
                                    category = "7";
                                    break;
                                case "有害垃圾":
                                    category = "7";
                                    break;
                            }
                            String money = StringUtil.getTotalPrice(info.getPerprice(), info.getQuantity(), category);
                            Logger.e("money--->" + money);
                            switch (info.getTypeName()) {
                                case "玻璃":
                                case "有害垃圾":
                                    garbageParam = new GarbageParam(info.getTypeName(), info.getFullstatus(), info.getQuantity(), "");
                                    break;
                                default:
                                    garbageParam = new GarbageParam(info.getTypeName(), info.getFullstatus(), info.getQuantity(), info.getPerprice());
                                    break;
                            }
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("garbage", garbageParam);
                            openActivity(CollectorPayActivity.class, bundle);
                            finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        serialPortUtils.sendSerialPort("androidC56:1;");
                    }
                }, 1000);
            }

        } else {

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
