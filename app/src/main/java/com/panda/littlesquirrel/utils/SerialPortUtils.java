package com.panda.littlesquirrel.utils;

import android.util.Log;


import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;


/**
 * Created by WangChaowei on 2017/12/7.
 */

public class SerialPortUtils {

    private final String TAG = "SerialPortUtils";
    private String path ;
    private int baudrate;
    public boolean serialPortStatus = false; //是否打开串口标志
    public String data_;
    public boolean threadStatus; //线程状态，为了安全终止线程

    public SerialPort serialPort = null;
    public InputStream inputStream = null;
    public OutputStream outputStream = null;
    public StringBuilder sb;

    public SerialPortUtils(String path, int baudrate)
    {
        this.path = path;
        this.baudrate = baudrate;
    }

    /**
     * 打开串口
     * @return serialPort串口对象
     */
    public SerialPort openSerialPort(){
       // Logger.e(path);
        try {
                serialPort = new SerialPort(new File(path),baudrate,0);

                this.serialPortStatus = true;
                threadStatus = false; //线程状态

                //获取打开的串口中的输入输出流，以便于串口数据的收发
                inputStream = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();

                new ReadThread().start(); //开始线程监控是否有数据要接收
        } catch (IOException e) {
            Log.e(TAG, "openSerialPort: 打开串口异常：" + e.toString());
            return serialPort;
        }
        Log.d(TAG, "openSerialPort: 打开串口");
        return serialPort;
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort(){
        try {
            inputStream.close();
            outputStream.close();

            this.serialPortStatus = false;
            this.threadStatus = true; //线程状态
            serialPort.close();
        } catch (IOException e) {
            Log.e(TAG, "closeSerialPort: 关闭串口异常："+e.toString());
            return;
        }
        Log.d(TAG, "closeSerialPort: 关闭串口成功");
    }

    /**
     * 发送串口指令（字符串）
     * @param data String数据指令
     */
    public void sendSerialPort(String data){

        if(!data.endsWith(";")){
            data += ";";
        }
        Log.d(TAG, "sendSerialPort: 发送数据"+data);
        try {
            byte[] sendData = data.getBytes(); //string转byte[]
            this.data_ = new String(sendData); //byte[]转string
            if (sendData.length > 0) {
                outputStream.write(sendData);
                outputStream.write('\n');
                outputStream.flush();
                Log.d(TAG, "sendSerialPort: 串口数据发送成功");
            }
        } catch (IOException e) {
            Log.e(TAG, "sendSerialPort: 串口数据发送失败："+e.toString());
        }

    }

    /**
     * 单开一线程，来读数据
     */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
            while (!threadStatus){
//                Log.d(TAG, "进入线程run");
                //64   1024
                 try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    int length = inputStream.available();
                    byte[] buffer = new byte[length];
                    int size; //读取数据的大小
                    size = inputStream.read(buffer);
                    if (size > 0){

                        if (receiveListener !=null) {
                            Log.d(TAG, "run: 接收到了数据大小：" + new String(buffer).trim());
                            receiveListener.dataReceive(buffer, size);
                       }
                    }
                } catch (IOException e) {
                   // Log.e(TAG, "run: 数据读取异常：" +e.toString());
                }
            }

        }
    }

    public ReceiveListener receiveListener = null;
    public static interface ReceiveListener {
        public void dataReceive(byte[] buffer, int size);
    }
    public void setReceiveListener(ReceiveListener dataReceiveListener) {
        receiveListener = dataReceiveListener;
    }

}
