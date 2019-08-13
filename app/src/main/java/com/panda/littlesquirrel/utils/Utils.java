package com.panda.littlesquirrel.utils;


import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Utils {
    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    public static boolean hourMinuteBetween(String currentTime, String startTime, String endTimer) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date start = null;
        Date end = null;
        Date current=null;
        try {
            current=simpleDateFormat.parse(currentTime);
            start = simpleDateFormat.parse(startTime);
            end = simpleDateFormat.parse(endTimer);
            Long lcurrent=current.getTime();
            Long  lstart=start.getTime();
            Long  lend=end.getTime();

            if(current.getTime()>=start.getTime()&&current.getTime()<=end.getTime()){
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    ;

    public static void disableShowInput(EditText et) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            //ShowSoftInputOnFocus方法是EditText从TextView继承来的的
            //可以用来设置当EditText获得焦点时软键盘是否可见
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(et, false);
        } catch (Exception e) {//TODO: handle exception
        }
        try {
            //这里是没用的，直接删掉即可，因为EditText类，TextView类，View类中，
            // 都没有setSoftInputShownOnFocus这个方法，不可能获得method对象的
            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(et, false);
        } catch (Exception e) {//TODO: handle exception
        }
    }

    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * Judge the String whether a empty
     * true: empty false: not empty
     *
     * @param str String
     * @return
     */
    public static boolean isEmpty(final String str) {
        if ((str == null) || (str.length() == 0)) {
            return true;
        }
        return false;
    }

    /**
     * Judge the Collection whether a empty
     * true: empty false: not empty
     *
     * @param col Collection
     * @return
     */
    public static boolean isEmpty(final Collection<?> col) {
        if ((col == null) || (col.size() == 0)) {
            return true;
        }
        return false;
    }

    /**
     * Judge the Object whether empty
     * true: empty false: not empty
     *
     * @param obj Object
     * @return
     */
    public static boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        return false;
    }

    /**
     * Judge the Map whether empty
     * true: empty false: not empty
     *
     * @param map Map
     * @return
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        if ((map == null) || (map.size() == 0)) {
            return true;
        }
        return false;
    }

    public static String getDateByMins(long longtime) {
        Date date = new Date(longtime);


        return dateFormat.format(date);

    }


    /**
     * 生成xml文件时的时间格式设定
     */
    public static String getCommentTime() {
        String s = "MMM dd yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(s, Locale.US);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 生成xml文件时的时间格式设定
     */
    public static String getCommentDate() {
        String s = "EEE MMM dd HH:mm:ss yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(s, Locale.US);
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDate() {
        String s = "MM.dd.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(s, Locale.US);
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDatetime() {
        String s = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(s, Locale.US);
        return simpleDateFormat.format(new Date());
    }

    public static String getDateStr() {
        String s = "yyyyMMdd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(s, Locale.US);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 验证所给的字符串是否为数字
     *
     * @param _temp
     * @return
     */
    public static boolean isNumeric(String _temp) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(_temp).matches();
    }

    /**
     * 验证所给的字符串格式
     * eg: 555.32 or 555
     *
     * @param var
     * @return
     */
    public static boolean isVar(String var) {
        Pattern pattern = Pattern.compile("[0-9]+|[0-9]+.[0-9]+");
        return pattern.matcher(var).matches();
    }

    /**
     * 去掉所有空格和制表符号
     *
     * @param str
     * @return
     */
    public static String removeAllBlank(String str) {

        str = str.replaceAll("(\\s|\\t)*", "");

        return str;
    }


    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static void hideKeyBoard(Activity context) {
        if (context != null && context.getCurrentFocus() != null) {
            ((InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(context.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static String readAssert(Context context, String fileName) {
        String jsonString = "";
        String resultString = "";
        try {
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString = new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

}
