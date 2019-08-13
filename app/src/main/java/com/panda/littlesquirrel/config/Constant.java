package com.panda.littlesquirrel.config;

import android.os.Environment;

/**
 * Created by jinjing on 2019/3/21.
 */

public class Constant {
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
    public static final String ACTION_INTENT_RECEIVER = "UPDATE_ARAM";
    public static final String GARBAGE_TYPE = "gar_type";
    public static final String LOGIN_STATUS = "isLogin";
    public static final String USER_MOBILE = "user_mobile";
    public static final String COLLECTOR_MOBILE = "collector_mobile";
    public static final String GARBAGE_LIST = "garbage_list";
    public static final String DELIVER_LIST = "deliver_list";
    public static final String PRICE_LIST = "price_list";
    //https://www.squirrelf.com/index/showCompanyNews
    // public static final String HTTP_URL = "http://192.168.0.13:8889";
    /**
     * 网络接口基地址
     */
    public static final String SHOOTSCREEN= Environment.getExternalStorageDirectory().getPath() + "/littlesquirrel/pic";
    public static final String ACTION_WATCHDOG_KICK = "android.intent.action.WATCHDOG_KICK";
    public static final String ACTION_WATCHDOG_INIT = "android.intent.action.WATCHDOG_INIT";
    public static final String ACTION_WATCHDOG_STOP = "android.intent.action.WATCHDOG_STOP";
    //public static final String HTTP_URL = "https://testapi.squirrelf.com/";
    public static final String HTTP_URL = "https://api.squirrelf.com:10081/";
    public static final String HTTP_URL1 = "https://www.squirrelf.com/";
    public static final String COLLECT_QRCODE = "collect_code";
    public static final String DEVICEID = "deviceID";
    public static final String USER_QRCODE = "user_code";
    public static final String SIGNKEY = "signKey";
    public static final String LATI ="lati" ;
    public static final String LONGA = "longga";
}
