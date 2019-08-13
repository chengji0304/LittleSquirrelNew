package com.panda.littlesquirrel.entity;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by jinjing on 2019/3/28.
 */

public class UserInfoCache {
    public static String category;
    public static boolean loginStatue;
    public static LinkedHashMap<String,Float> records=new LinkedHashMap<>();
    public static String  telNum;
    public static void addRecord(float paramFloat)
    {
        if (records.containsKey(category))
        {
            records.put(category, Float.valueOf(paramFloat + ((Float)records.get(category)).floatValue()));
            return;
        }
        records.put(category, Float.valueOf(paramFloat));
    }

    public static void clearUserInfo()
    {
        loginStatue =false;
        telNum = null;
        category = null;
        records.clear();
    }

    public static float getTotalIntegral()
    {
        float f = 0.0F;
        Iterator localIterator = records.keySet().iterator();
        while (localIterator.hasNext())
        {
            String str = (String)localIterator.next();
            if ((str.equals("玻璃")) || (str.equals("有害垃圾")))
                f += ((Float)records.get(str)).floatValue();
        }
        return f;
    }

    public static float getTotalMoney()
    {
        float f = 0.0F;
        Iterator localIterator = records.keySet().iterator();
        while (localIterator.hasNext())
        {
            String str = (String)localIterator.next();
            if ((!str.equals("玻璃")) && (!str.equals("有害垃圾")))
                f += ((Float)records.get(str)).floatValue();
        }
        return f;
    }
}
