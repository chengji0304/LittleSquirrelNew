package com.panda.littlesquirrel.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinjing on 2019/3/27.
 */

public class GarbageParam implements Parcelable {

    private String category;
    private String fullState;
    private String  money;
    private String quantity;

    @Override
    public String toString() {
        return "GarbageParam{" +
                "category='" + category + '\'' +
                ", fullState='" + fullState + '\'' +
                ", money='" + money + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }

    public GarbageParam() {
    }

    public GarbageParam(String category, String fullState,  String quantity,String money) {
        this.category = category;
        this.fullState = fullState;
        this.money = money;
        this.quantity = quantity;
    }

    protected GarbageParam(Parcel in) {
        category = in.readString();
        fullState = in.readString();
        money = in.readString();
        quantity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(fullState);
        dest.writeString(money);
        dest.writeString(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GarbageParam> CREATOR = new Creator<GarbageParam>() {
        @Override
        public GarbageParam createFromParcel(Parcel in) {
            return new GarbageParam(in);
        }

        @Override
        public GarbageParam[] newArray(int size) {
            return new GarbageParam[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFullState() {
        return fullState;
    }

    public void setFullState(String fullState) {
        this.fullState = fullState;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
