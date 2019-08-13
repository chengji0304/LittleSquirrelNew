package com.panda.littlesquirrel.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinjing on 2019/5/5.
 */

public class PriceInfo implements Parcelable {

    private String type;
    private String userPrice;
    private String collectorPrice;

    public PriceInfo(){

    }

    public PriceInfo(String type, String userPrice, String collectorPrice) {
        this.type = type;
        this.userPrice = userPrice;
        this.collectorPrice = collectorPrice;
    }

    protected PriceInfo(Parcel in) {
        type = in.readString();
        userPrice = in.readString();
        collectorPrice = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(userPrice);
        dest.writeString(collectorPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PriceInfo> CREATOR = new Creator<PriceInfo>() {
        @Override
        public PriceInfo createFromParcel(Parcel in) {
            return new PriceInfo(in);
        }

        @Override
        public PriceInfo[] newArray(int size) {
            return new PriceInfo[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserPrice() {
        return userPrice;
    }

    public void setUserPrice(String userPrice) {
        this.userPrice = userPrice;
    }

    public String getCollectorPrice() {
        return collectorPrice;
    }

    public void setCollectorPrice(String collectorPrice) {
        this.collectorPrice = collectorPrice;
    }
}
