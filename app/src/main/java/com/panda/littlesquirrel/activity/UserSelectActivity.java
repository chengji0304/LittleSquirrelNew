package com.panda.littlesquirrel.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.adapter.UserSelectReclyViewAdapater;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.entity.BoxBean;
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.entity.HandShakeRecord;
import com.panda.littlesquirrel.entity.PriceInfo;
import com.panda.littlesquirrel.entity.SelcetInfo;
import com.panda.littlesquirrel.fragment.ADBannerActivity;
import com.panda.littlesquirrel.service.PollDevStatusService;
import com.panda.littlesquirrel.service.UpdateService;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.GlideRoundImage;
import com.panda.littlesquirrel.utils.MD5Util;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.SIMUtils;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;
import com.panda.littlesquirrel.utils.SerialPortUtils;
import com.panda.littlesquirrel.utils.SoundPlayUtil;

import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.utils.Utils;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.panda.littlesquirrel.view.ErrorStatusDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android_serialport_api.SerialPort;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.init;

/**
 * 首页
 *
 * @author pandaKing
 * @version V1.0
 * @header BAKit.h
 * BABaseProject
 * @brief BAKit
 * @copyright Copyright © 2016年 博爱. All rights reserved.
 */

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？
public class UserSelectActivity extends BaseActivity {

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
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_home)
    RelativeLayout activityHome;
    @Bind(R.id.ll_image)
    LinearLayout llImage;
    @Bind(R.id.ll_wait)
    LinearLayout llWait;
    @Bind(R.id.ll_user_select)
    LinearLayout llUserSelect;
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
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
    private UserSelectReclyViewAdapater adapter;
    private PreferencesUtil prf;
    private ArrayList<SelcetInfo> mlist;
    private String isLogin;
    private CountTimer countTimerView;
    private ErrorStatusDialog errorStatusDialog = new ErrorStatusDialog();
    private CountDownTimer timer;
    private long exitTime = 0;
    private int count = 0;
    private ArrayList<PriceInfo> mWastePrice;
    // String str = "S8:0;S59:431055193939415005D5FF39-1;S60:1-0,2-0,3-0;S61:0-0,1-10,2-110,3-30,4-40,5-88,6-60;S62:0-100,1-234,2-600,3-1200,4-3600,5-600,6-100;@";
    private ArrayList<PriceInfo> priceList;
    ArrayList<String> imagesUrl;
    private String sound;
    private Handler handler;
    private String deviceID;
    private LocationClient mlc;
    private ArrayList<GarbageParam> prfList;
    private ArrayList<Integer> images;
    private double lati;
    private double longa;
    private String totalCount;
    private String tempter;
    private String fullState;
    private String signKey;
    private String bottlePrice;
    private String bookPrice;
    private String paperPrice;
    private String metalPrice;
    private String plasticPrice;
    private String fabricPrice;
    //private final int Time = 5 * 1000;

    // public UpdateReceiver mUpdateReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);
        setContentView(R.layout.activity_user_select);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        //registerMessageReceiver();
        DogWatch();
        prf = new PreferencesUtil(this);
        Logger.e("deviceid--->" + prf.readPrefs(Constant.DEVICEID));
        System.loadLibrary("serial_port");
        serialPort = serialPortUtils.openSerialPort();
        mStartHandler = new Handler();
        reciveData = new StringBuilder();
        sound = getIntent().getStringExtra("type");
        handler = new Handler();
        isLogin = prf.readPrefs(Constant.LOGIN_STATUS);
        initPollDev();
        initData();
        setListener();
        Logger.e("lati---》" + prf.readPrefs(Constant.LATI));
        if (!StringUtil.isEmpty(prf.readPrefs(Constant.LATI))) {
            Logger.e("deid---》" + prf.readPrefs(Constant.DEVICEID));
            if (!StringUtil.isEmpty(prf.readPrefs(Constant.DEVICEID))) {
                tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
                getCategoryMsg();//获取饮料瓶总数


            } else {
                getDeviceID();

                // Logger.e("lati---》"+prf.readPrefs(Constant.LATI));
                // setListener();
            }
        } else {
            initLocation();
            //  initData();
            // setListener();
        }

        // initLocation();

        //  initLocation();
        // setListener();
    }
    private void initPollDev(){
        if (isServiceRunning("com.panda.littlesquirrel.service.PollDevStatusService")) {
            Log.e("服务正在运行", "return");
            // return;
        } else {
                 /*启动服务*/
            Intent intent = new Intent(UserSelectActivity.this,PollDevStatusService.class);
            startService(intent);
        }

    }

    /*
     看门狗。。。。
     */
    private void DogWatch() {

        Intent intent = new Intent(Constant.ACTION_WATCHDOG_INIT);
        sendBroadcast(intent);

        //  handler.postDelayed()
    }

    /*
     百度定位
     */
    private void initLocation() {
        mlc = new LocationClient(getApplicationContext());
        mlc.registerLocationListener(new LocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setIsNeedAddress(true);// 位置，一定要设置，否则后面得不到
        option.setScanSpan(3000);//设置发起定位请求的间隔时间为5000ms
        option.disableCache(true);//禁止启用缓存定位
        option.setPriority(LocationClientOption.NetWorkFirst); //设置gps优先
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");
        // 可选，默认gcj02，设置返回的定位结果坐标系
        mlc.setLocOption(option);
        //setLocationOption(); //定义setLocationOption()方法
        //启动定位客户端
        mlc.start();
    }

    public class LocationListener implements BDLocationListener {

        //接受位置信息
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            //经纬度
            if (location != null) {
                lati = location.getLatitude();
                longa = location.getLongitude();
                mlc.stop();
                Logger.e("info--->" + lati + "   " + longa);
                prf.writePrefs(Constant.LATI, String.valueOf(lati));
                prf.writePrefs(Constant.LONGA, String.valueOf(longa));
                if (!StringUtil.isEmpty(prf.readPrefs(Constant.DEVICEID))) {
                    getCategoryMsg();//获取饮料瓶总数
                    tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
                } else {
                    getDeviceID();
                }
            } else {

            }

        }


    }

    private void getCategoryMsg() {
        try {
            JSONObject jsonObject = new JSONObject();
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
                    if (stateCode.equals("1")) {

                        com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSON.parseObject(jsonObject.getString("result"));
                        if (StringUtil.isEmpty(object.getString("countOrWeight"))) {
                            totalCount = "0";
                        } else {
                            totalCount = object.getString("countOrWeight");
                            if (totalCount.contains(".")) {
                                totalCount = totalCount.substring(0, totalCount.indexOf("."));
                            }
                        }
                        // totalCount=totalCount.substring(0,totalCount.indexOf("."));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serialPortUtils.sendSerialPort("androidC56:1;");
                            }
                        }, 1000);
                    } else {

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getFindData(String s) {
        if (s.startsWith("S59") & s.endsWith(";")) {
            s = s.replaceAll("--", "-");
            try {
                Map<String, String> info = StringUtil.getInfo(s);
                org.json.JSONObject jsonObject = new org.json.JSONObject();
                Iterator<Map.Entry<String, String>> entries = info.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, String> entry = entries.next();
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
                com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSON.parseObject(jsonObject.toString());
                String s62 = object.getString("S62");
                String s61 = object.getString("S61");
                String s8 = object.getString("S8");
                String[] str62 = s62.split(",");
                String[] str61 = s61.split(",");
                String[] str8 = s8.split(",");
                tempter = str8[0].split("-")[1];
                weight0 = str62[0].split("-")[1];
                weight1 = str62[1].split("-")[1];
                weight2 = str62[2].split("-")[1];
                weight3 = str62[3].split("-")[1];
                weight4 = str62[4].split("-")[1];
                weight5 = str62[5].split("-")[1];
                capacity0 = str61[0].split("-")[1];
                capacity1 = str61[1].split("-")[1];
                capacity2 = str61[2].split("-")[1];
                capacity3 = str61[3].split("-")[1];
                capacity4 = str61[4].split("-")[1];
                capacity5 = str61[5].split("-")[1];
                getGarbagePrice();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (s.contains("E")) {

        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //获取设备所有信息
                    serialPortUtils.sendSerialPort("androidC56:1;");
                }
            }, 1000);
        }
    }

    /*
      握手周期
     */
    private void handshake() {
        try {
            HandShakeRecord hsrecord = new HandShakeRecord();
            hsrecord.setDeviceID(prf.readPrefs(Constant.DEVICEID));
            //hsrecord.setIccid("89860434231880305258");
            hsrecord.setIccid(SIMUtils.getICCID(this));
            BoxBean bean = null;
            for (int i = 0; i < mlist.size(); i++) {
                bean = new BoxBean();
                bean.setCategory(Integer.valueOf(mlist.get(i).getTyep()));
                bean.setQuantity(Integer.valueOf(mlist.get(i).getFullstatus()));
                // Logger.e("getFullstatus" + i + "" + mlist.get(i).getFullstatus());
                bean.setTemperature(Double.valueOf(tempter));
                bean.setWeight(Double.valueOf(StringUtil.getTotalWeight(mlist.get(i).getQuantity())));
                bean.setCanNum(Integer.valueOf(mlist.get(i).getCanNum()));
                hsrecord.getParam().add(bean);
            }
            String jsonString = com.alibaba.fastjson.JSON.toJSONString(hsrecord);
            // Logger.e("json--->" + jsonString);
            addSubscription(Constant.HTTP_URL + "machine/setting/handshake", jsonString, new CallBack<String>() {
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

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        initBanner();//底部banner


        if (!StringUtil.isEmpty(isLogin)) {
            btnMyRecycler.setVisibility(View.GONE);
        }
//        if (!StringUtil.isEmpty(prf.readPrefs(Constant.DEVICEID))) {
//
//            tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    serialPortUtils.sendSerialPort("androidC56:1;");
//                }
//            }, 2000);
//
//        }
//        if (!StringUtil.isEmpty(prf.readPrefs(Constant.DEVICEID))) {
//            setListener();
//            tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
//        } else {
//            getDeviceID();
//        }
        countTimerView = new CountTimer(1000 * 2 * 60, 1000, this);//待机时间
        mWastePrice = new ArrayList<>();
    }

    /*
      设备编号
     */
    private void getDeviceID() {
        try {
            // longa=117.352735;
            // lati=34.153902;
            final JSONObject josnObject = new JSONObject();
            josnObject.put("iccid", SIMUtils.getICCID(this));
            josnObject.put("longitude", longa);
            josnObject.put("latitude", lati);
//            josnObject.put("iccid", "89860434231880305258");
//            josnObject.put("longitude", longa);
//            josnObject.put("latitude", lati);
            addSubscription(Constant.HTTP_URL + "machine/setting/factoryMode", josnObject.toString(), new CallBack<String>() {
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
                        deviceID = object.getString("deviceID");
                        signKey = object.getString("signKey");
                        Logger.e("signKey--->" + signKey);
                        tvDeviceNum.setText("设备编号:" + deviceID);
                        prf.writePrefs(Constant.DEVICEID, deviceID);
                        prf.writePrefs(Constant.SIGNKEY, signKey);
                        // initData();
                        getCategoryMsg();
//
                    }

                }
            });
        } catch (Exception e) {
            Logger.e("e--->" + e.getMessage());

        }
    }

    /*
       种类价格
     */
    private void getGarbagePrice() {
        try {
            JSONObject json = new JSONObject();
            if (StringUtil.isEmpty(prf.readPrefs(Constant.DEVICEID))) {
                json.put("deviceID", prf.readPrefs(Constant.DEVICEID));
            } else {
                json.put("deviceID", prf.readPrefs(Constant.DEVICEID));
            }

            addSubscription(Constant.HTTP_URL + "machine/home/price", json.toString(), new CallBack<String>() {
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
                    // Logger.e("s-->" + s);
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if (stateCode.equals("1")) {
                        com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSON.parseObject(jsonObject.getString("result"));
                        JSONArray array = obj.getJSONArray("wastePrice");
                        PriceInfo info = null;
                        for (int i = 0; i < array.size(); i++) {
                            info = new PriceInfo();
                            com.alibaba.fastjson.JSONObject job = array.getJSONObject(i);
                            info.setType(job.getString("category"));
                            info.setCollectorPrice(job.getString("collectorPrice"));
                            info.setUserPrice(job.getString("userPrice"));
                            mWastePrice.add(info);
                        }
                    }
                    prf.setPriceList(Constant.PRICE_LIST, mWastePrice);
                    mlist = new ArrayList<>();
                    for (int i = 0; i < mWastePrice.size(); i++) {

                        if (mWastePrice.get(i).getType().equals("3")) {
                            bookPrice = mWastePrice.get(i).getUserPrice();
                        }
                        if (mWastePrice.get(i).getType().equals("2")) {
                            paperPrice = mWastePrice.get(i).getUserPrice();
                        }

                        if (mWastePrice.get(i).getType().equals("1")) {
                            bottlePrice = mWastePrice.get(i).getUserPrice();
                        }

                        if (mWastePrice.get(i).getType().equals("5")) {
                            fabricPrice = mWastePrice.get(i).getUserPrice();
                        }

                        if (mWastePrice.get(i).getType().equals("4")) {
                            plasticPrice = mWastePrice.get(i).getUserPrice();
                        }
                        if (mWastePrice.get(i).getType().equals("6")) {
                            metalPrice = mWastePrice.get(i).getUserPrice();
                        }


                    }
                    SelcetInfo bottle = new SelcetInfo(R.drawable.book, "3", "书籍", capacity5, bookPrice, weight5, "5");
                    mlist.add(bottle);
                    SelcetInfo page = new SelcetInfo(R.drawable.paper, "2", "纸类1箱", capacity3, paperPrice, weight3, "3");
                    mlist.add(page);
                    SelcetInfo book = new SelcetInfo(R.drawable.paper, "2", "纸类2箱", capacity4, paperPrice, weight4, "4");
                    mlist.add(book);
                    SelcetInfo plastic = new SelcetInfo(R.drawable.bottle, "1", "饮料瓶", "10", bottlePrice, totalCount, "6");
                    mlist.add(plastic);
                    SelcetInfo textile = new SelcetInfo(R.drawable.metal, "6", "金属", capacity2, metalPrice, weight2, "2");
                    mlist.add(textile);
                    SelcetInfo metal = new SelcetInfo(R.drawable.fabric, "5", "纺织物1箱", capacity0, fabricPrice, weight0, "0");
                    mlist.add(metal);
                    SelcetInfo glasses = new SelcetInfo(R.drawable.fabric, "5", "纺织物2箱", capacity1, fabricPrice, weight1, "1");
                    mlist.add(glasses);
                    SelcetInfo harmgoods = new SelcetInfo(R.drawable.plastic, "4", "塑料", capacity2, plasticPrice, weight2, "2");
                    mlist.add(harmgoods);
                    //握手周期
                    handshake();
                    llWait.setVisibility(View.GONE);
                    llUserSelect.setVisibility(View.VISIBLE);
                    ryGrabage.setLayoutManager(new GridLayoutManager(UserSelectActivity.this, 4));
                    adapter = new UserSelectReclyViewAdapater(UserSelectActivity.this, mlist);
                    ryGrabage.setAdapter(adapter);
                    adapter.setOnItemClickListener(new UserSelectReclyViewAdapater.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            if (Utils.hourMinuteBetween(new SimpleDateFormat("HH:mm").format(new Date()), "5:00", "23:59")) {

                                if (mlist.get(position).getTypeName().equals("饮料瓶")) {
                                    if (Integer.valueOf(mlist.get(position).getQuantity()) < 250) {
                                        if (StringUtil.isEmpty(isLogin)) {
                                            prf.writePrefs(Constant.GARBAGE_TYPE, mlist.get(position).getTypeName());
                                            openActivity(UserLoginActivity.class);
                                            finish();
                                        } else {
                                            prf.writePrefs(Constant.GARBAGE_TYPE, mlist.get(position).getTypeName());
                                            openActivity(UserTypeSelectActivity.class);
                                            finish();
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
                                        errorStatusDialog.show(getFragmentManager(), "full_error");
                                        //倒计时
                                        if (timer != null) {
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
                                    String quantity = String.valueOf(Double.valueOf(mlist.get(position).getQuantity()) * 10);
                                    BigDecimal bigDecimal = new BigDecimal(quantity);
                                    BigDecimal bc = new BigDecimal("1000");
                                    if (Integer.valueOf(mlist.get(position).getFullstatus()) < 100 &
                                            Double.valueOf(bigDecimal.divide(bc, 2, BigDecimal.ROUND_HALF_UP).toString()) < 40.00) {
                                        if (StringUtil.isEmpty(isLogin)) {
                                            prf.writePrefs(Constant.GARBAGE_TYPE, mlist.get(position).getTypeName());
                                            openActivity(UserLoginActivity.class);
                                            finish();
                                        } else {
                                            prf.writePrefs(Constant.GARBAGE_TYPE, mlist.get(position).getTypeName());
                                            openActivity(UserTypeSelectActivity.class);
                                            finish();
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

                                        errorStatusDialog.show(getFragmentManager(), "full_error");
                                        //倒计时
                                        if (timer != null) {
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


                            } else {
                                errorStatusDialog.setContent("投递时间为5:00-24:00哦~");
                                errorStatusDialog.setImage(R.drawable.error);
                                errorStatusDialog.setOnConfirmClickListener(new ErrorStatusDialog.ConfirmCallBack() {
                                    @Override
                                    public void onConfirm() {
                                        errorStatusDialog.dismiss();
                                    }
                                });
                                errorStatusDialog.show(getFragmentManager(), "time_error");
                                if (timer != null) {
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
      不触碰跳转到待机画面
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                countTimerView.start();
                break;
            default:
                countTimerView.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backAndTime != null) {
            backAndTime.stop();
        }
        if (mlc != null) {
            mlc.stop();
        }

        if (mStartHandler != null) {
            mStartHandler.removeCallbacksAndMessages(null);
        }
        if (serialPort != null) {
            serialPortUtils.closeSerialPort();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        if (countTimerView != null) {
            countTimerView.cancel();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        countTimerView.cancel();
    }

    public class CountTimer extends CountDownTimer {
        private Context context;

        /**
         * 参数 millisInFuture       倒计时总时间（如60S，120s等）
         * 参数 countDownInterval    渐变时间（每次倒计1s）
         */
        public CountTimer(long millisInFuture, long countDownInterval, Context context) {
            super(millisInFuture, countDownInterval);
            this.context = context;
        }

        // 计时完毕时触发
        @Override
        public void onFinish() {
            //  UIHelper.showMainActivity((Activity) context);
            Intent intent = new Intent(UserSelectActivity.this, ADBannerActivity.class);
            startActivity(intent);
            finish();

        }

        // 计时过程显示
        @Override
        public void onTick(long millisUntilFinished) {

        }

    }


    private void initTimer() {
        backAndTime.setTimer(80);
        backAndTime.setVisibility(View.VISIBLE);
        backAndTime.setBackVisableStatue(false);
        backAndTime.start();
        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                backAndTime.stop();
                Intent intent = new Intent(UserSelectActivity.this, DeliverListFinishActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!StringUtil.isEmpty(sound)) {
            SoundPlayUtil.play(10);
        }
        isLogin = prf.readPrefs(Constant.LOGIN_STATUS);
        if (!StringUtil.isEmpty(isLogin)) {
            if (countTimerView != null) {
                countTimerView.cancel();
            }

            btnBigPiece.setVisibility(View.INVISIBLE);
            btnGuide.setVisibility(View.INVISIBLE);
            btnMyRecycler.setVisibility(View.GONE);
            initTimer();

        } else {
            btnBigPiece.setVisibility(View.VISIBLE);
            btnGuide.setVisibility(View.VISIBLE);
            btnMyRecycler.setVisibility(View.VISIBLE);
            backAndTime.setVisibility(View.GONE);
            ivLogo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if ((System.currentTimeMillis() - exitTime) > 1000) {
                                exitTime = System.currentTimeMillis();
                            } else {
                                exitTime = 0;
                                openActivity(SettingLoginActivity.class);
                                finish();
                            }

                            break;
                    }
                    return false;
                }
            });
            timeStart();
        }


    }

    private void timeStart() {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countTimerView.start();
            }
        });
    }

    @OnClick({R.id.btn_my_recycler, R.id.btn_big_piece, R.id.btn_guide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_my_recycler:
//                if(countTimerView!=null){
//                    countTimerView.cancel();
//                }
                openActivity(CollectLoginActivity.class);
                finish();
                break;
            case R.id.btn_big_piece:
                openActivity(BigRecoveryActivity.class);
                break;
            case R.id.btn_guide:
                openActivity(QuickGuideActivity.class);
                break;

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
        bannerButtom.setDelayTime(1000 * 30);
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

    /**
     * 判断服务是否运行
     */
    private boolean isServiceRunning(final String className) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }

}
