package com.panda.littlesquirrel.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.activity.UserSelectActivity;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.service.UpdateService;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.utils.Utils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ADBannerActivity extends BaseActivity {

    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.activity_adbanner)
    LinearLayout activityAdbanner;
    ArrayList<Integer> images;
    ArrayList<String> imagesUrl;
    private PreferencesUtil prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adbanner);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        prf = new PreferencesUtil(this);
        if (!StringUtil.isEmpty(prf.readPrefs(Constant.DEVICEID))) {
            if (Utils.hourMinuteBetween(new SimpleDateFormat("HH:mm").format(new Date()), "23:30", "23:59")||Utils.hourMinuteBetween(new SimpleDateFormat("HH:mm").format(new Date()), "04:30", "04:59")) {
                /*如果服务正在运行，直接return*/
                if (isServiceRunning("com.panda.littlesquirrel.service.UpdateService")) {
                    Log.e("服务正在运行", "return");
                    // return;
                } else {
                 /*启动服务*/
                    Intent intent = new Intent(this, UpdateService.class);
                    startService(intent);
                }

            } else {
                Logger.e("时间未到");
            }
        }
        //  mStartHandler = new Handler();
        initData();
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        imagesUrl = new ArrayList<>();
        getADPicture();

    }

    private void getADPicture() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", prf.readPrefs(Constant.DEVICEID));

            //  jsonObject.put("deviceID", "100012312");
            jsonObject.put("type", 0);
            addSubscription(Constant.HTTP_URL + "machine/home/images", jsonObject.toString(), new CallBack<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(ApiException e) {
                    initBendi();

                }

                @Override
                public void onSuccess(String s) {
                    // Logger.e("s--->" + s);
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if (stateCode.equals("1")) {
                        com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSON.parseObject(jsonObject.getString("result"));
                        JSONArray arry = com.alibaba.fastjson.JSON.parseArray(object.getString("picture"));
                        for (int i = 0; i < arry.size(); i++) {
                            String a = (String) arry.get(i);
                            imagesUrl.add(a);
                        }
                        Logger.e("size--->" + imagesUrl.size());
                        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                        //设置指示器位置（指示器居右）
                        //  banner.setIndicatorGravity(BannerConfig.RIGHT);
                        //设置图片加载器
                        banner.setImageLoader(new GlideImageLoader());
                        //设置图片集合
                        banner.isAutoPlay(true);
                        banner.setImages(imagesUrl);
                        banner.setViewPagerIsScroll(false);
                        //设置轮播时间
                        banner.setDelayTime(1000 * 60 * 2);
                        //banner设置方法全部调用完毕时最后调用
                        banner.setOnBannerListener(new OnBannerListener() {

                            @Override
                            public void OnBannerClick(int position) {

                                Intent intent = new Intent(ADBannerActivity.this, UserSelectActivity.class);
                                intent.putExtra("type", "1");
                                startActivity(intent);
                                finish();
                            }
                        });
                        banner.start();
                        banner.startAutoPlay();

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBendi() {
        images=new ArrayList<>();
        images.add(R.drawable.banner5);
        images.add(R.drawable.banner4);
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置指示器位置（指示器居右）
        //  banner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.isAutoPlay(true);
        banner.setImages(images);
        banner.setViewPagerIsScroll(false);
        //设置轮播时间
        banner.setDelayTime(1000 * 60 * 2);
        //banner设置方法全部调用完毕时最后调用
        banner.setOnBannerListener(new OnBannerListener() {

            @Override
            public void OnBannerClick(int position) {

                Intent intent = new Intent(ADBannerActivity.this, UserSelectActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                finish();
            }
        });
        banner.start();
        banner.startAutoPlay();
    }


    @Override
    public void getFindData(String reciveData) {

    }

    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.drawable.banner1)//图片加载出来前，显示的图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
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
