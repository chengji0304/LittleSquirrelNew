package com.panda.littlesquirrel.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jinjing on 2019/5/6.
 */

public class SaveDeliveryRecord implements Parcelable{
    private String deviceID;//设备ID
    private String account;//用户手机号
    private String serialNum;//流水号
    private String signedKey;//key
    private ArrayList<RecordBean> record=new ArrayList<>();//用户投递记录

    public SaveDeliveryRecord() {
    }

    public SaveDeliveryRecord(String deviceID, String account, String serialNum, ArrayList<RecordBean> record,String signedKey) {
        this.deviceID = deviceID;
        this.account = account;
        this.serialNum = serialNum;
        this.record = record;
        this.signedKey=signedKey;
    }

    protected SaveDeliveryRecord(Parcel in) {
        deviceID = in.readString();
        account = in.readString();
        serialNum = in.readString();
        signedKey=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceID);
        dest.writeString(account);
        dest.writeString(serialNum);
        dest.writeString(signedKey);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SaveDeliveryRecord> CREATOR = new Creator<SaveDeliveryRecord>() {
        @Override
        public SaveDeliveryRecord createFromParcel(Parcel in) {
            return new SaveDeliveryRecord(in);
        }

        @Override
        public SaveDeliveryRecord[] newArray(int size) {
            return new SaveDeliveryRecord[size];
        }
    };

    public String getSignedKey() {
        return signedKey;
    }

    public void setSignedKey(String signedKey) {
        this.signedKey = signedKey;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public ArrayList<RecordBean> getRecord() {
        return record;
    }

    public void setRecord(ArrayList<RecordBean> record) {
        this.record = record;
    }

//    @Override
//    public String toString() {
//        return "SaveDeliveryRecord{" +
//                "deviceID='" + deviceID + '\'' +
//                ", account='" + account + '\'' +
//                ", serialNum='" + serialNum + '\'' +
//                ", record=" + record +
//                '}';
//    }


    @Override
    public String toString() {
        return "SaveDeliveryRecord{" +
                "deviceID='" + deviceID + '\'' +
                ", account='" + account + '\'' +
                ", serialNum='" + serialNum + '\'' +
                ", signedKey='" + signedKey + '\'' +
                ", record=" + record +
                '}';
    }
}
