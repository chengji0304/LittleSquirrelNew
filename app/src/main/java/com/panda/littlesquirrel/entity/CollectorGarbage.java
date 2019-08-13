package com.panda.littlesquirrel.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinjing on 2019/4/12.
 */

public class CollectorGarbage implements Parcelable {

    private String type;
    private String total;
    private String quantity;

    public CollectorGarbage(String type, String total, String quantity) {
        this.type = type;
        this.total = total;
        this.quantity = quantity;
    }

    protected CollectorGarbage(Parcel in) {
        type = in.readString();
        total = in.readString();
        quantity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(total);
        dest.writeString(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CollectorGarbage> CREATOR = new Creator<CollectorGarbage>() {
        @Override
        public CollectorGarbage createFromParcel(Parcel in) {
            return new CollectorGarbage(in);
        }

        @Override
        public CollectorGarbage[] newArray(int size) {
            return new CollectorGarbage[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
