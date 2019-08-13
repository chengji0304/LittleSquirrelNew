package com.panda.littlesquirrel.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.entity.PriceInfo;

import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.StringUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by diaohaixiang on 2019/8/12.
 */

public class PollDevStatusService extends Service {
    private PreferencesUtil prf;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prf = new PreferencesUtil(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("PollDevStatusService", "time：" + new Date().
                        toString());
                sendPollDevStatus();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 5 * 60 * 1000; // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /*
           发送设备信息
         */
    private void sendPollDevStatus() {
        try {
            JSONObject json = new JSONObject();
            if (!StringUtil.isEmpty(prf.readPrefs(Constant.DEVICEID))) {
                json.put("deviceID", prf.readPrefs(Constant.DEVICEID));
                Log.e("PollDevStatusService", "deviceID：" + prf.readPrefs(Constant.DEVICEID));
                EasyHttp.post(Constant.HTTP_URL + "machine/setting/machineStatus").upJson(json.toString()).execute(new CallBack<String>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        Log.e("PollDevStatusService", "error" + e.toString());
                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.e("PollDevStatusService", "success" + s.toString());
                        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}