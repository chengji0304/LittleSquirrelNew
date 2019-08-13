package com.panda.littlesquirrel.config;

import android.app.Application;
import android.graphics.Typeface;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.panda.littlesquirrel.utils.CloseBarUtils;
import com.panda.littlesquirrel.utils.SoundPlayUtil;

import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.model.HttpHeaders;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * Created by jinjing on 2019/3/21.
 */

public class MyApplication extends Application {
    private static volatile MyApplication app;
    public static Typeface arial;
    private static MyApplication instance;
    public static Typeface pingFangSc_regular;
    public static Typeface pingFangSc_semibold;
    public static Typeface pingFang_medium;

    public static MyApplication getInstance() {
        if (app == null) {
            synchronized (MyApplication.class) {
                if (app == null) {
                    app = new MyApplication();
                }
            }
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        changePermission();
        SoundPlayUtil.init(this);
        typeFaceInit();
        CloseBarUtils.closeBar();
        //网络请求
        EasyHttp.init(this);
        EasyHttp.getInstance()
                .debug("RxEasyHttp", true)
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .setRetryCount(5)//默认网络不好自动重试3次
                .setRetryDelay(3000)//每次延时500ms重试
                .setCertificates()
                .setRetryIncreaseDelay(3000)//每次延时叠加500ms
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024)//设置缓存大小为50M
                .setCacheVersion(1);//缓存版本为1
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  //（可选）是否显示线程信息。 默认值为true
                .methodCount(2)         // （可选）要显示的方法行数。 默认2
                .methodOffset(7)        // （可选）隐藏内部方法调用到偏移量。 默认5
                //.logStrategy(customLog) //（可选）更改要打印的日志策略。 默认LogCat
                .tag("squirrel")   //（可选）每个日志的全局标记。 默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
//        Logger.addLogAdapter(new AndroidLogAdapter() {
//            @Override public boolean isLoggable(int priority, String tag) {
//                return com.orhanobut.logger.BuildConfig.DEBUG;
//            }
//        });

    }


    private void changePermission() {
        execRootCmdSilent("chmod 777 /dev/watchdog");
        execRootCmdSilent("kill `(ps |grep rk_wtd_test | busybox awk '{print $2}')`");
        File file = new File("/dev/watchdog");
        if (!file.canRead() || !file.canWrite()) {
            try {
                execRootCmdSilent("chmod 666 /dev/watchdog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private void typeFaceInit() {
        pingFangSc_semibold = Typeface.createFromAsset(getAssets(), "fonts/PingFangSC_Semibold.ttf");
        pingFangSc_regular = Typeface.createFromAsset(getAssets(), "fonts/PingFangSC_Regular.ttf");
        pingFang_medium = Typeface.createFromAsset(getAssets(), "fonts/PingFangSC_Medium.ttf");
        arial = Typeface.createFromAsset(getAssets(), "fonts/Arial.ttf");
    }


}
