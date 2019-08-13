package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.adapter.DeliverReclyViewAdapter;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.entity.RecordBean;
import com.panda.littlesquirrel.entity.SaveDeliveryRecord;
import com.panda.littlesquirrel.utils.BitmapUtil;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.Encrypt;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;
import com.panda.littlesquirrel.utils.SoundPlayUtil;
import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeliverListFinishActivity extends BaseActivity {

    @Bind(R.id.image_service_tel)
    ImageView imageServiceTel;
    @Bind(R.id.tv_service_tel)
    TextView tvServiceTel;
    @Bind(R.id.image_device_num)
    ImageView imageDeviceNum;
    @Bind(R.id.tv_device_num)
    TextView tvDeviceNum;
    @Bind(R.id.btn_my_recycler)
    Button btnMyRecycler;
    @Bind(R.id.ll_top)
    FrameLayout llTop;
    @Bind(R.id.rv_deliver)
    RecyclerView rvDeliver;
    @Bind(R.id.tv_tip_02)
    TextView tvTip02;
    @Bind(R.id.tv_money_value)
    TextView tvMoneyValue;
    @Bind(R.id.tv_tip_03)
    TextView tvTip03;
    @Bind(R.id.tv_encourage_value)
    TextView tvEncourageValue;
    @Bind(R.id.btn_over)
    Button btnOver;
    @Bind(R.id.btn_again)
    Button btnAgain;
    @Bind(R.id.ll_mid)
    FrameLayout llMid;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.ll_buttom)
    FrameLayout llButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_deliver)
    RelativeLayout activityDeliver;
    @Bind(R.id.ll_jifen)
    RelativeLayout llJifen;
    @Bind(R.id.ll_money)
    RelativeLayout llMoney;
    // private ArrayList<GarbageParam> mlist;
    private DeliverReclyViewAdapter adapter;
    private Double money = 0.0;
    private int jifen = 0;
    private String totalMoney = "";
    private String totalJifen = "";
    private String category;
    private PreferencesUtil prf;
    private ArrayList<GarbageParam> prfList;
    private ArrayList<Integer> images;
    private String sign;
    private String key;
    private String weight;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);
        setContentView(R.layout.activity_deliver_list_finish);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        prf = new PreferencesUtil(this);
        prfList = prf.getDataList(Constant.DELIVER_LIST);
        key = prf.readPrefs(Constant.SIGNKEY);
        initData();
        initTimer();
    }

    private void initTimer() {
        backAndTime.setTimer(280);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.setBackVisableStatue(false);
        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                backAndTime.stop();
                btnOver.setEnabled(false);
                saveRecord();

            }
        });
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        initBanner();
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));

        Logger.d(JSONArray.toJSONString(prfList));
        for (GarbageParam garbageParam : prfList) {
            if (garbageParam.getCategory().equals("7")) {
                jifen += Integer.valueOf(garbageParam.getMoney());
            } else if (garbageParam.getCategory().equals("8")) {
                jifen += Integer.valueOf(garbageParam.getMoney());
            } else {
                if (garbageParam.getCategory().equals("1")) {
                    money += Float.valueOf(StringUtil.getTotalPrice(garbageParam.getMoney(), garbageParam.getQuantity(), "6"));
                } else {
                    money += Float.valueOf(StringUtil.getTotalPrice(garbageParam.getMoney(), garbageParam.getQuantity(), "1"));
                }

            }

        }
        Logger.e("money--->" + new DecimalFormat("#0.00").format(money));
        Logger.e("jifen--->" + String.valueOf(jifen));
        btnMyRecycler.setVisibility(View.GONE);
        rvDeliver.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeliverReclyViewAdapter(this, prfList);
        rvDeliver.setAdapter(adapter);
        totalMoney = new DecimalFormat("#0.00").format(money);
        totalJifen = String.valueOf(jifen);
        if (totalMoney.equals("0.00")) {
            llMoney.setVisibility(View.GONE);
            totalMoney = "";
        } else {
            llMoney.setVisibility(View.VISIBLE);
            tvMoneyValue.setText(new DecimalFormat("#0.00").format(money) + "环保金");
        }

        if (totalJifen.equals("0")) {
            llJifen.setVisibility(View.GONE);
            totalJifen = "";
        } else {
            llJifen.setVisibility(View.VISIBLE);
            tvEncourageValue.setText(totalJifen + "积分");

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        backAndTime.start();
        SoundPlayUtil.play(14);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backAndTime.stop();
    }


    @Override
    public void getFindData(String reciveData) {

    }

    @OnClick({R.id.btn_over, R.id.btn_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_over:
                // screenshot();
//                Intent intent = new Intent(DeliverListFinishActivity.this, DeliverSuccessActivity.class);
//                intent.putExtra("money",totalMoney);
//                intent.putExtra("jifen",totalJifen);
//                startActivity(intent);
//                finish();
               // btnOver.setEnabled(false);
                if (prfList.size() > 0) {
                    btnOver.setEnabled(false);
                    backAndTime.stop();
                    saveRecord();
                } else {
                    openActivity(DeliverSuccessActivity.class);
                    finish();
                }

                break;
            case R.id.btn_again:
//                Intent again = new Intent(DeliverListFinishActivity.this, UserSelectActivity.class);
//                again.putExtra("status","2");
//                startActivity(again);
                openActivity(UserSelectActivity.class);
                finish();
                break;
        }
    }

    private void screenshot() {
        // 获取屏幕
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        bmp = dView.getDrawingCache();
        if (bmp != null) {
            // 图片文件路径
            try {
                String filePath = Constant.SHOOTSCREEN + File.separator + prf.readPrefs(Constant.USER_MOBILE) + "-" + StringUtil.getSerialNumber() + ".png";
                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                File handIdsmall = new File(BitmapUtil.getSmallDir(), file.getName());
                BitmapUtil.compressImage(bmp, handIdsmall);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    public void saveRecord() {
        try {
            String serialNum = StringUtil.getSerialNumber();
            SaveDeliveryRecord record = new SaveDeliveryRecord();
            record.setDeviceID(prf.readPrefs(Constant.DEVICEID));
            record.setSerialNum(serialNum);
            record.setAccount(prf.readPrefs(Constant.USER_MOBILE));
            RecordBean bean = null;
            for (int i = 0; i < prfList.size(); i++) {
                bean = new RecordBean();
//                switch (prfList.get(i).getCategory()) {
//
//                    case "饮料瓶":
//                        category = "1";
//                        break;
//                    case "纸类1箱":
//                    case "纸类2箱":
//                        category = "2";
//                        break;
//                    case "书籍":
//                        category = "3";
//                        break;
//                    case "塑料":
//                        category = "4";
//                        break;
//                    case "纺织物1箱":
//                    case "纺织物2箱":
//                        category = "5";
//                        break;
//                    case "金属":
//                        category = "6";
//                        break;
//                    case "玻璃":
//                        category = "7";
//                        break;
//                    case "有害垃圾":
//                        category = "8";
//                        break;
//                }
                bean.setCategory(Integer.valueOf(prfList.get(i).getCategory()));
                if (prfList.get(i).getCategory().equals("1")) {
                    bean.setCount(Integer.valueOf(prfList.get(i).getQuantity()));
                    bean.setWeight(0);
                } else {
                    weight = StringUtil.getTotalWeight(prfList.get(i).getQuantity());
                    if (weight.substring(weight.length() - 2, weight.length()).equals("00")) {
                        Logger.e("1");
                        bean.setWeight(Double.valueOf(weight.substring(0, weight.indexOf("."))));
                    } else if (weight.substring(weight.length() - 1, weight.length()).equals("0")) {
                        Logger.e("2");
                        bean.setWeight(Double.valueOf(weight.substring(0, weight.length() - 1)));
                    } else {
                        Logger.e("3");
                        bean.setWeight(Double.valueOf(StringUtil.getTotalWeight(prfList.get(i).getQuantity())));
                    }
                    bean.setCount(0);
                    // bean.setWeight(Double.valueOf(StringUtil.getTotalWeight(prfList.get(i).getQuantity())));
                }

                record.getRecord().add(bean);
            }
            if (prfList.get(0).getCategory().equals("1")) {
                sign = prf.readPrefs(Constant.USER_MOBILE) + prf.readPrefs(Constant.DEVICEID) + prfList.get(0).getCategory() + prfList.get(0).getQuantity() + "0" + key;
            } else {

                if (prfList.get(0).getQuantity().equals("0")) {
                    sign = prf.readPrefs(Constant.USER_MOBILE) + prf.readPrefs(Constant.DEVICEID) + prfList.get(0).getCategory() + "0" + "0" + key;
                } else {
                    weight = StringUtil.getTotalWeight(prfList.get(0).getQuantity());
                    Logger.e("weight--->" + StringUtil.getTotalWeight(prfList.get(0).getQuantity()));
                    if (weight.substring(weight.length() - 2, weight.length()).equals("00")) {
                        Logger.e("11");
                        //  bean.setWeight(Double.valueOf(weight.substring(0,weight.indexOf("."))));
                        sign = prf.readPrefs(Constant.USER_MOBILE) + prf.readPrefs(Constant.DEVICEID) + prfList.get(0).getCategory() + "0" + Integer.valueOf(weight.substring(0, weight.indexOf("."))) + key;

                    } else if (weight.substring(weight.length() - 1, weight.length()).equals("0")) {
                        Logger.e("22");
                        //  bean.setWeight(Double.valueOf(weight.substring(0,weight.length()-1)));
                        sign = prf.readPrefs(Constant.USER_MOBILE) + prf.readPrefs(Constant.DEVICEID) + prfList.get(0).getCategory() + "0" + Double.valueOf(weight.substring(0, weight.length() - 1)) + key;
                    } else {
                        Logger.e("33");
                        sign = prf.readPrefs(Constant.USER_MOBILE) + prf.readPrefs(Constant.DEVICEID) + prfList.get(0).getCategory() + "0" + StringUtil.getTotalWeight(prfList.get(0).getQuantity()) + key;
                    }

                    // Logger.e("weight--->"+StringUtil.getTotalWeight(prfList.get(0).getQuantity()));
                    // sign=prf.readPrefs(Constant.USER_MOBILE)+prf.readPrefs(Constant.DEVICEID)+prfList.get(0).getCategory()+"0"+StringUtil.getTotalWeight(prfList.get(0).getQuantity())+key;

                }
            }
            Logger.e("sign--->" + sign);
            record.setSignedKey(Encrypt.SHA512(sign));
            String jsonString = com.alibaba.fastjson.JSON.toJSONString(record);
            Logger.e("json--->" + jsonString);
            addSubscription(Constant.HTTP_URL + "machine/delivery/saveRecord", jsonString, new CallBack<String>() {
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
                    Logger.e("s--->" + s);
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if (stateCode.equals("1")) {
                        Intent intent = new Intent(DeliverListFinishActivity.this, DeliverSuccessActivity.class);
                        intent.putExtra("money", totalMoney);
                        intent.putExtra("jifen", totalJifen);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        Intent intent = new Intent(DeliverListFinishActivity.this, DeliverSuccessActivity.class);
                        intent.putExtra("money", totalMoney);
                        intent.putExtra("jifen", totalJifen);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
}
