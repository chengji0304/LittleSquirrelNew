package com.panda.littlesquirrel.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jinjing on 2019/4/1.
 */

public class SelcetInfo implements Parcelable{

    private int image;
    private String tyep;
    private String typeName;
    private String fullstatus;
    private String perprice;
    private String quantity;
    private String canNum;

    public SelcetInfo(int image, String tyep, String typeName, String fullstatus, String perprice, String quantity,String camNo) {
        this.image = image;
        this.tyep = tyep;
        this.typeName = typeName;
        this.fullstatus = fullstatus;
        this.perprice = perprice;
        this.quantity = quantity;
        this.canNum=camNo;
    }


    protected SelcetInfo(Parcel in) {
        image = in.readInt();
        tyep = in.readString();
        typeName = in.readString();
        fullstatus = in.readString();
        perprice = in.readString();
        quantity = in.readString();
        canNum=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeString(tyep);
        dest.writeString(typeName);
        dest.writeString(fullstatus);
        dest.writeString(perprice);
        dest.writeString(quantity);
        dest.writeString(canNum);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SelcetInfo> CREATOR = new Creator<SelcetInfo>() {
        @Override
        public SelcetInfo createFromParcel(Parcel in) {
            return new SelcetInfo(in);
        }

        @Override
        public SelcetInfo[] newArray(int size) {
            return new SelcetInfo[size];
        }
    };

    public String getCanNum() {
        return canNum;
    }

    public void setCanNum(String canNum) {
        this.canNum = canNum;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTyep() {
        return tyep;
    }

    public void setTyep(String tyep) {
        this.tyep = tyep;
    }

    public String getFullstatus() {
        return fullstatus;
    }

    public void setFullstatus(String fullstatus) {
        this.fullstatus = fullstatus;
    }

    public String getPerprice() {
        return perprice;
    }

    public void setPerprice(String perprice) {
        this.perprice = perprice;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
