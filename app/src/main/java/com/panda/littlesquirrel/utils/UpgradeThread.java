package com.panda.littlesquirrel.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.panda.littlesquirrel.activity.HomeActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class UpgradeThread extends Thread {
    private String upgradeMsg;
    private Context context;
    private String readline = "";
    private BufferedReader br;
    private static String LAG = "UpgradeThread";
    public UpgradeThread(String upgradeMsg, Context context){
        this.upgradeMsg = upgradeMsg;
        this.context = context;
    }

    @Override
    public void run() {
        upgrade();
    }

    public String upgrade(){
        try {
            JSONObject jsonObject = new JSONObject(upgradeMsg);
            JSONObject data_board = jsonObject.getJSONObject("data_board");
            String url = data_board.getString("url");
            String[] params = url.split("/");
            int length = params.length;
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/runmachine/"+params[length - 1];
            File file = new File(filePath);
            br = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            String fileName = params[length - 1];
            Log.e("升级信息",fileName+"开始升级");
            HomeActivity.serialPortUtils.sendSerialPort("androidC5:"+fileName+";");
            int number = 0;
            while (true){
                try {
                    Log.e("升级信息","等待");
                        Thread.sleep(10000);
                        Log.e("升级信息","开始读取文件");
                        String sendData="";
                        while ((readline = br.readLine()) != null) {
                            sendData +=readline.trim()+"\r\n";
                            ++number ;
                            if (number % 20 == 0){
                                Thread.sleep(500);
                                HomeActivity.serialPortUtils.sendSerialPort("androidC6:"+sendData+";");
                                Log.e("升级信息",sendData+"" +number);
                                sendData = "";
                            }

                        }
                        Thread.sleep(500);
                    HomeActivity.serialPortUtils.sendSerialPort("androidC6:"+sendData+";");
                        Log.e("升级信息",sendData+"" +number);
                        sendData = "";
                        Log.e("升级信息",String.valueOf(number));
                        Log.e("升级信息","文件读取完毕");
                        Thread.sleep(2000);
                    HomeActivity.serialPortUtils.sendSerialPort("androidC7:1");
                        break;
                }catch (Exception e){

                }
            }
        }catch (Exception e){

        }
        return "OK";
    }
}
