package com.panda.littlesquirrel.entity;

import org.json.JSONObject;

/**
 * Created by jinjing on 2019/5/3.
 */

public class ApiModle {
    /*
     轮播图片
     */

    public static String getImages(String deviceID,int type){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("deviceID",deviceID);
            jsonObject.put("type",type);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
