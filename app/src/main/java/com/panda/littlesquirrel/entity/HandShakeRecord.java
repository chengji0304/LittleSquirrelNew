package com.panda.littlesquirrel.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jinjing on 2019/6/27.
 */

public class HandShakeRecord implements Parcelable{
    private String deviceID;//设备ID
    private String iccid;//sim卡id

    private ArrayList<BoxBean> param=new ArrayList<>();//用户投递记录

    public HandShakeRecord() {
    }

    protected HandShakeRecord(Parcel in) {
        deviceID = in.readString();
        iccid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceID);
        dest.writeString(iccid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HandShakeRecord> CREATOR = new Creator<HandShakeRecord>() {
        @Override
        public HandShakeRecord createFromParcel(Parcel in) {
            return new HandShakeRecord(in);
        }

        @Override
        public HandShakeRecord[] newArray(int size) {
            return new HandShakeRecord[size];
        }
    };

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public ArrayList<BoxBean> getParam() {
        return param;
    }

    public void setParam(ArrayList<BoxBean> param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "HandShakeRecord{" +
                "deviceID='" + deviceID + '\'' +
                ", iccid='" + iccid + '\'' +
                ", param=" + param +
                '}';
    }
}
