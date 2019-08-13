package com.panda.littlesquirrel.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.utils.MD5Util;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.StringUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import static android.R.attr.path;
import static android.content.ContentValues.TAG;

/**
 * Created by jinjing on 2019/5/27.
 */

public class UpdateReceiver extends BroadcastReceiver {
    private PreferencesUtil prf;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constant.ACTION_INTENT_RECEIVER)) {
            Logger.e("更新---->" + "定时开启");
            Logger.e("version--->" + StringUtil.getVersionName(context));
            prf = new PreferencesUtil(context);
            Logger.e("info--->" + prf.readPrefs(Constant.DEVICEID));
            upDateApk(prf.readPrefs(Constant.DEVICEID), StringUtil.getVersionName(context));
            Intent i = new Intent(context, UpdateService.class);
            context.startService(i);
        }
    }

    private void upDateApk(String deviceID, String version) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", deviceID);
            jsonObject.put("appVersion", version);
            EasyHttp.post(Constant.HTTP_URL1+"versionUpdate")
                    .writeTimeOut(30 * 1000)//局部写超时30s,单位毫秒
                    .readTimeOut(30 * 1000)//局部读超时30s,单位毫秒
                    .connectTimeout(30 * 1000)//局部连接超时30s,单位毫秒
                    //可以全局统一设置全局URL
                    // 打开该调试开关并设置TAG,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    //.debug("EasyHttp", true)
                    .retryCount(3)//本次请求重试次数
                    .retryDelay(500)//
                    .upJson(jsonObject.toString())
                    .execute(new CallBack<String>() {
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
                            String stateCode = jsonObject.getString("rtCode");
                            if(stateCode.equals("01")){
                                String url=jsonObject.getString("downUrl");
                                String md5=jsonObject.getString("fileMD5");
                                downFile(url,md5);

                            }else{

                            }

                        }
                    });

        } catch (Exception e) {
            Logger.e("e-->" + e.getMessage());
        }

    }

    private void downFile(String url, final String md5) {
        EasyHttp.downLoad(url)
                .savePath(Environment.getExternalStorageDirectory().getPath() + "/littlesquirrel/Apk")
                .saveName("littlesquirrel.apk")//不设置默认名字是时
                .execute(new DownloadProgressCallBack<String>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        int progress = (int) (bytesRead * 100 / contentLength);
                        Logger.e(progress + "% ");
                    }

                    @Override
                    public void onComplete(final String path) {

                        File file = new File(path);
                        if (MD5Util.getFileMD5(file).equals(md5)) {
                            Logger.e("info--->" + "下载成功");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    clientInstall(path);
                                }
                            }).start();

                        }

                    }
                });
    }

    /*
　　@pararm apkPath 等待安装的app全路径，如：/sdcard/app/app.apk
**/
    private static int clientInstall(String apkPath) {
//        PrintWriter PrintWriter = null;
//        Process process = null;
//        OutputStream out = null;
//        InputStream in = null;
        //InputStream in = null;
        int result = -1;
        DataOutputStream dos = null;
        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            Logger.e("path1--->"+apkPath);
            dos.writeBytes("pm install -r " + apkPath + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
            Logger.e("path2--->"+apkPath);
            Logger.e("result--->"+result);
            if(result==0){
               // 静态注册自启动广播
                    Intent intent=new Intent();
                    //与清单文件的receiver的anction对应
                    intent.setAction("android.intent.action.PACKAGE_REPLACED");
                    //发送广播
                   //sendBroadcast(intent);
            }
           // process = Runtime.getRuntime().exec("su");
//            process = Runtime.getRuntime().exec("su");
//            out = process.getOutputStream();
//            // 调用安装
//            Logger.e("path-->" + apkPath);
//            out.write(("pm install -r " + apkPath + "\n").getBytes());
//            in = process.getInputStream();

//            PrintWriter = new PrintWriter(process.getOutputStream());
//            PrintWriter.println("chmod 777 " + apkPath);
//            PrintWriter
//                    .println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
//            PrintWriter.println("pm install -r " + apkPath);
//            // PrintWriter.println("exit");
//            PrintWriter.flush();
//            PrintWriter.close();
//            in=process.getInputStream();
//            int len = 0;
//            byte[] bs = new byte[256];
//            while (-1 != (len = in.read(bs))) {
//                String state = new String(bs, 0, len);
//                Logger.e("state--->"+state);
//                if (state.equals("success")) {
//                    //安装成功后的操作
//
//                    //静态注册自启动广播
//                    Intent intent=new Intent();
//                    //与清单文件的receiver的anction对应
//                    intent.setAction("android.intent.action.PACKAGE_REPLACED");
//                    //发送广播
//                   // sendBroadcast(intent);
//                }
//            }
//
//
//           // int value = process.waitFor();
//            //execLinuxCommand();
//           // Logger.e("静默安装返回值：" + value);
//            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("安装apk出现异常");
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           // if (process != null) {
//                process.destroy();
//            }
//            try {
//                if (out != null) {
//                    out.flush();
//                    out.close();
//                }
//
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


        }
        return result;
    }

    private static void execLinuxCommand() {
        String cmd = "sleep 120; am start -n com.panda.littlesquirrel.activity.UserSelectActivity";
        //Runtime对象
        Runtime runtime = Runtime.getRuntime();
        try {
            Process localProcess = runtime.exec("su");
            OutputStream localOutputStream = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
            localDataOutputStream.writeBytes(cmd);
            localDataOutputStream.flush();
            Logger.e("设备准备重启");
        } catch (IOException e) {
            Logger.e("strLine:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
