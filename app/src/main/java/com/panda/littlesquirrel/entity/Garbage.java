package com.panda.littlesquirrel.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinjing on 2019/4/3.
 */

public class Garbage implements Parcelable {

    private int image;
    private String  name;

    public Garbage(int image, String name) {
        this.image = image;
        this.name = name;
    }

    protected Garbage(Parcel in) {
        image = in.readInt();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Garbage> CREATOR = new Creator<Garbage>() {
        @Override
        public Garbage createFromParcel(Parcel in) {
            return new Garbage(in);
        }

        @Override
        public Garbage[] newArray(int size) {
            return new Garbage[size];
        }
    };

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
