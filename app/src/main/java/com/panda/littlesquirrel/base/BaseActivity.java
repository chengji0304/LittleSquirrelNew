package com.panda.littlesquirrel.base;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;

import com.panda.littlesquirrel.activity.CollectLoginActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.utils.NetUtil;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.utils.SerialPortUtils;

import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBack;


import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;

/**
 * Created by jinjing on 2019/3/22.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Disposable mSubscription;
    public SerialPort serialPort;
    //  public Handler mStartHandler;
    public byte[] mBuffer;
    //public  String reciveData = "";
    public StringBuilder reciveData;
    public SerialPortUtils serialPortUtils = new SerialPortUtils("/dev/ttyS4", 9600);
    private final int Time = 8 * 1000;
    private Timer wdtimer = null;
    // public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public Handler mStartHandler;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //  hideNavigationBar();
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);

        reciveData = new StringBuilder();
        setContentView(R.layout.activity_big_recovery);
        //setListener();


    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);

    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtra("message", pBundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void hideNavigationBar() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE;//0x00001000; // SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        try {
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    public void showFragment(Fragment currentFragment, Fragment fg) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //如果之前没有添加过
        if (!fg.isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.fl_content, fg, fg.getClass().getName());  //第三个参数为当前的fragment绑定一个tag，tag为当前绑定fragment的类名
        } else {
            transaction
                    .hide(currentFragment)
                    .show(fg);
        }

        currentFragment = fg;

        transaction.commit();

    }

    /**
     * 通过Action启动Activity
     *
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    public void addSubscription(String url, String json, CallBack<String> callBack) {
        if (NetUtil.checkNet(this)) {
            try {
                mSubscription = EasyHttp.post(url).upJson(json).execute(callBack);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            // setNetwork();
        }

    }

    /**
     * 网络未连接时，调用设置方法
     */
    protected void setNetwork() {
        // TODO Auto-generated method stub..
        //Toast.makeText(this, "wifi is closed!", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("网络提示信息");
        builder.setMessage("网络未连接，如果继续，请先设置网络！");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent = null;
                /**
                 * 判断手机系统的版本！如果API大于10 就是3.0+ 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
                 */
                if (Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(
                            Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        builder.create();
        builder.show();
    }

    /**
     * 串口数据监听
     */


    public void setListener() {
        serialPortUtils.setReceiveListener(new SerialPortUtils.ReceiveListener() {
            @Override
            public void dataReceive(byte[] buffer, int size) {
                mBuffer = buffer;
                Log.e("buff", "数据监听：" + new String(mBuffer).trim());
                // if (new String(mBuffer).trim().endsWith("@")){
                mStartHandler.post(runnable);
                // }


            }

            //开线程更新UI
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    if (mBuffer.length > 0) {

                        reciveData.append(new String(mBuffer).trim());
                        if (reciveData.toString().contains("@")) {
                            // Log.e("SerialPortUtils", "数据监听：" + reciveData);
                            String[] re = reciveData.toString().split("@");
                            if (re.length >= 1 && re[0].length() > 1) {
                                Logger.e("处理--->" + re[0]);
                                getFindData(re[0]);
                            }
                            reciveData = new StringBuilder();
                        }
                    }


                }
//                    } catch (UnsupportedEncodingException e)
//                    {
//                        e.printStackTrace();
//                    }

//                }
            };
        });

    }

//    public void getFindData(String s) {
//       // Logger.e("s-->"+s);
//
//    }


//    public void getFindData(String s) {
//    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        return super.getLayoutInflater();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //  unbind();
        if (mSubscription != null) {
            EasyHttp.cancelSubscription(mSubscription);
        }
        if (mStartHandler != null) {
            mStartHandler.removeCallbacksAndMessages(null);
        }
        if (serialPort != null) {
            serialPortUtils.closeSerialPort();
        }

        if (wdtimer != null) {
            wdtimer.cancel();
        }
        //cancelAlarmManagerBR(this);


    }

    public abstract void getFindData(String reciveData);

    /*
     定时发喂狗广播
     */
    public void sendTimerBoaadCastReceiver(Context mContext) {

        wdtimer = new Timer();
        WDTimerTask myTimerTask = new WDTimerTask();//定时器
        wdtimer.schedule(myTimerTask, 0, 8000);//每隔5秒
//        Intent mIntent = new Intent(Constant.ACTION_WATCHDOG_KICK);//喂狗
//        Logger.e("发送喂狗，全军出击");
//        sendBroadcast(mIntent);
////        // 触发服务的起始时间 这里是// 5秒后发送广播，只发送一次
//
//        PendingIntent pendIntent = PendingIntent.getBroadcast(mContext, 0,
//                mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // 进行闹铃注册
//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        manager.setInexactRepeating(
//                AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime(), Time, pendIntent);


    }

    private class WDTimerTask extends TimerTask {

        @Override
        public void run() {
            Intent mIntent = new Intent(Constant.ACTION_WATCHDOG_KICK);//喂狗
            Logger.e("发送喂狗，全军出击");
            sendBroadcast(mIntent);

        }
    }
    /*
    private void cancelAlarmManagerBR(Context mContext) {
      //  String ACTION_NAME = "android.intent.action.alarm.timer";// 广播名称

        Intent mIntent = new Intent(Constant.ACTION_WATCHDOG_KICK);

        PendingIntent pendIntent = PendingIntent.getBroadcast(mContext, 0,
                mIntent, 0);
        // 与上面的intent匹配（filterEquals(intent)）的闹钟会被取消

        // 进行闹铃取消
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.cancel(pendIntent);



    }
     */

    private long mPressedTime = 0;

    @Override
    public void onBackPressed() {
        //  mcLog.writeLog(MCLog.WARN,"BackPressed");
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        } else {//退出程序
            this.finish();
        }
    }
}
