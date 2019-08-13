package com.panda.littlesquirrel.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.EditText;


import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.config.Constant;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {
    private static final String TAG = "StringUtil";
    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186、166</p>
     * <p>电信：133、153、173、177、180、181、189、199</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";


    public StringUtil() {
    }

    private static String currentString = "";

    /**
     * 获取刚传入处理后的string
     *
     * @return
     * @must 上个影响currentString的方法 和 这个方法都应该在同一线程中，否则返回值可能不对
     */
    public static String getCurrentString() {
        return currentString == null ? "" : currentString;
    }

    /**
     * 获取string,为null则返回""
     *
     * @param s
     * @return
     */
    public static String getString(String s) {
        return s == null ? "" : s;
    }

    /**
     * 获取去掉前后空格后的string,为null则返回""
     *
     * @param s
     * @return
     */
    public static String getTrimedString(String s) {
        return getString(s).trim();
    }

    /**
     * 判断字符是否非空
     *
     * @param s
     * @param trim 是否剔除前导空白字符和尾部空白字符
     * @return
     */
    public static boolean isNotEmpty(String s, boolean trim) {
        if (s == null) {
            return false;
        }
        if (trim) {
            //从当前 String 对象移除所有前导空白字符和尾部空白字符。
            s = s.trim();
        }
        if (s.length() <= 0) {
            return false;
        }
        currentString = s;
        return true;
    }

    public static String getVersionName(Context context) {

        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

    /*
      指令解析
     */
    public static Map<String, String> getInfo(String data) {
        String[] details = data.split(";");
        Map<String, String> map = new HashMap<String, String>(details.length);
        if (data != null && data.length() != 0) {
            for (String line : details) {
                String[] arr = line.split(":");
                String key = arr[0];
                String v = arr.length > 1 ? arr[1] : "";
                map.put(key, v);
            }
        }
        return map;
    }

    /**
     * 对字符串处理:将指定位置到指定位置的字符以星号代替
     *
     * @param content 传入的字符串
     * @param begin   开始位置
     * @param end     结束位置
     * @return
     */
    public static String getStarString(String content, int begin, int end) {

        if (begin >= content.length() || begin < 0) {
            return content;
        }
        if (end >= content.length() || end < 0) {
            return content;
        }
        if (begin >= end) {
            return content;
        }
        String starStr = "";
        for (int i = begin; i < end; i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, begin) + starStr + content.substring(end, content.length());

    }

    public static String getPhoneText(EditText tel) {
        String str = tel.getText().toString();
        return replaceBlank(str);
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            if (m.find()) {
                dest = m.replaceAll("");
            }
        }
        return dest;
    }


//    public static String getAESKey(String key, String userkey) {
//
//        String keyDe = null;
//        try {
//            keyDe = new String(RSACoder.decryptByPrivateKey(RSACoder.decryptBASE64(key), privateKey), "utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return keyDe.substring(keyDe.length() - 16, keyDe.length());
//
//    }

//    public static String getZEKKey(String key, String userkey) {
//
//        String keyDe = null;
//        try {
//            keyDe = new String(RSACoder.decryptByPrivateKey(RSACoder.decryptBASE64(key), privateKey), "utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Logger.e("keyDe", keyDe.trim());
//
//        //keyDe.substring(0+3+userkey.length(),keyDe.length());
//        return keyDe.substring(keyDe.length() - 32, keyDe.length());
//
//    }

    /**
     * 根据小时判断是否为上午、中午、下午
     *
     * @param
     * @return
     * @author zhangsq
     */
    public static String getDuringDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 7 && hour < 11) {
            return "上午好!";
        }
        if (hour >= 11 && hour <= 12) {
            return "中午好!";
        }
        if (hour >= 13 && hour <= 18) {
            return "下午好!";
        } else if (hour >= 19 && hour <= 23) {
            return "晚上好!";
        }
        return null;
    }

    /**
     * 纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getNowDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 纯字母
     *
     * @param fstrData
     * @return
     */
    public static boolean isChar(String fstrData) {
        char c = fstrData.charAt(0);
        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }

//    public static String sendJson(Map<String, String> params, String aes){
//        try {
//            JSONObject jsonObject = new JSONObject(params);
//            String encryptResultStr = AESEncryptor.encrypt(aes, jsonObject.toString());
//            JSONObject send = new JSONObject();
//            send.put("code",encryptResultStr);
//            return send.toString();
//        }catch (Exception e){
//            e.printStackTrace();
//
//        }
//        return null;
//    }
//    public static String getJson(Map<String, String> params, String aes, String md5, String userKey) {
////        for (Map.Entry<String, String> entry : params.entrySet()) {
////            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
////        }
//        try {
//            String signMsg = StringUtil.sortByAscii(params, "signature");
//            signMsg += md5;
//            String signMD5 = MD5Util.md5Digest(signMsg).toUpperCase();
//            params.put("signature", signMD5);
//            JSONObject jsonObject = new JSONObject(params);
//            String encryptResultStr = AESEncryptor.encrypt(aes, jsonObject.toString());
//            JSONObject send = new JSONObject();
//            send.put("code", encryptResultStr);
//            send.put("userKey", userKey);
//            Log.e("send", send.toString().trim());
//            return send.toString().trim();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    public static String sortByAscii(Map<String, String> params, String signName) {
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            if (!key.equals(signName)) {
                sb.append(params.get(key));
            }
        }
        return sb.toString();
    }

    public static String mapToString(Map json) throws Exception {
        StringBuffer sb = new StringBuffer();
        SortedMap<String, String> dataMapIn = new TreeMap<String, String>();
        for (Object key : json.keySet()) {
            if (key.toString().equals("MD5")) {
                continue;
            }
            String value = json.get(key).toString();
            dataMapIn.put(key.toString(), value);
        }

        for (String key : dataMapIn.keySet()) {
            if (key.toString().equals("MD5")) {
                continue;
            }
            String value = json.get(key).toString();
            sb.append(value);
        }

        return sb.toString();
    }

    public static String ByAscii(Map<String, Object> params, String signName) {
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            if (!key.equals(signName)) {
                sb.append(params.get(key));
            }
        }
        return sb.toString();
    }

    public static String getRandom(String mobile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (!StringUtil.isEmpty(mobile)) {
            stringBuffer.append(getStringRandom(10)).append(mobile).append(getTimeString());
        } else {
            stringBuffer.append(getStringRandom(10)).append(getStringRandom(11)).append(getTimeString());
        }

        return stringBuffer.toString();

    }

    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 转换时间日期格式字串为long型
     *
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static Long convertTimeToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static String getRate(String str) {
        StringBuilder sb = new StringBuilder();
        String[] strings = str.split("\\+");
        String a = strings[0];
        String b = strings[1];
        String result = new BigDecimal(b).multiply(new BigDecimal(100)).floatValue() + "";
        //  String resutl=String.valueOf(Double.valueOf(b)*100);
        return "每笔" + a + "元" + "+" + result + "%";
    }

    public static String getDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /*
     获取随机26个字母
     */
    public static char[] generate() {

        char[] letters = {
                'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's',
                'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b',
                'n', 'm'
        };
        boolean[] flags = new boolean[letters.length];
        char[] chs = new char[26];
        for (int i = 0; i < chs.length; i++) {
            int index;
            do {
                index = (int) (Math.random() * (letters.length));
            } while (flags[index]);// 判断生成的字符是否重复
            chs[i] = letters[index];
            flags[index] = true;
        }
        return chs;
    }

    /**
     * AES MD5 解密
     *
     * @param str
     * @return
     */
    public static String decode(String str) {
        String res = "";
        int xy = Integer.parseInt(str.substring(0, 1) + str.substring(str.length() - 1));
        str = str.substring(1, str.length() - 1);
        int arr[] = new int[str.length() / 2];
        for (int i = 0; i < str.length(); i += 2) {
            arr[i / 2] = Integer.parseInt(str.substring(i, i + 2));
        }
        for (int i = 0; i < arr.length; i++) {
            res += (char) (arr[i] + xy);
        }
        return res;
    }

    /**
     * 获取随机数
     *
     * @param numCount
     * @return
     */
    public String getRandom(int numCount) {
        String result = "";
        String[] alphaBeta = {
                "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P",
                "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p",
                "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        };
        while (result.length() < numCount) {
            int i = new Random().nextInt(alphaBeta.length);
            result += alphaBeta[i];
        }
        return result;
    }

    /**
     * SHA256 签名
     *
     * @param strSrc
     * @return
     */
    public static String hexEncrypt(String strSrc) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(strSrc.getBytes("UTF-8"));
            byte[] dd = md.digest();
            //System.out.println("11111111---"+CryptoUtils.byte2hex(dd).toLowerCase());
            return byte2hex(dd).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int i = 0; i < b.length; i++) {
            stmp = Integer.toHexString(b[i] & 0xFF);
            if (stmp.length() == 1) {
                hs += "0" + stmp;
            } else {
                hs += stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static String joinMap(Map<String, String> map) {
        StringBuffer b = new StringBuffer();
        boolean first = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {


            //b.append(entry.getKey());
            //b.append('=');
            if (entry.getValue() != null) {
                b.append(trimToEmpty(entry.getValue()));
            }
        }
        return b.toString();
    }

    public static String getTotal(String a, String b) {
        return new BigDecimal(a).add(new BigDecimal(b)).toString();


    }

    public static String joinMapValue(Map<String, String> map, char connector) {
        StringBuffer b = new StringBuffer();
        boolean first = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!first) {
                b.append(connector);
            }
            first = false;

            b.append(entry.getKey());
            b.append('=');
            if (entry.getValue() != null) {
                b.append(trimToEmpty(entry.getValue()));
            }
        }
        return b.toString();
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getSignData(Map<String, Object> params) {
        StringBuffer content = new StringBuffer();
        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = params.get(key).toString();
            if (value != null) {
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            } else {
                content.append((i == 0 ? "" : "&") + key + "=");
            }
        }
        return content.toString();
    }

    /**
     * 获取流水号
     *
     * @return
     */
    public static String getSerialNumber() {
        String serialNumber = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) +
                (int) (Math.random() * (9999 - 1000 + 1) + 1000);
        return serialNumber;
    }

    public static String getInTradeOrderNo(Context context) throws Exception {
        //获取本机IP
        //InetAddress addr = InetAddress.getLocalHost();
        String ip = IpGetUtil.getIPAddress(context);//获得本机IP
        String ipId = ip.substring(ip.length() - 3);
        if (ipId.contains(".")) {
            ipId = ipId.replaceAll("\\.", "0");
        }
        String random = random(9);
        String realTime = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
        //tradeId
        String uid = "A" + realTime + ipId + random;
        return uid;
    }

    public static String random(int length) {
        String random = "";
        /*随机数函数*/
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            /*生成36以内的随机数，不含36，并转化为36位*/
            random += Integer.toString(r.nextInt(36), 36);
        }
        return random;
    }

    public static boolean isChars(String str) {
//        Pattern p = Pattern.compile("[a-zA-Z]+");
//        Matcher m = p.matcher(str);
        return str.matches("[a-zA-Z]+");
    }

    public static String getSign(Map<String, String> params) {
        StringBuffer content = new StringBuffer();
        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = params.get(key).toString();
            if (value != null) {
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            } else {
                content.append((i == 0 ? "" : "&") + key + "=");
            }
        }
        return content.toString();
    }

    public static String trimToEmpty(String s) {
        return s == null ? "" : s.trim();
    }

    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        } else if (s.trim().equals("")) {
            return true;
        } else if (s.trim().equals("null")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNumber(String s) {

        try {
            Float.parseFloat(s);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public static String getPassword(String pwd) {
        for (; pwd.length() < 32; ) {
            pwd += "0";
        }
        return pwd;
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 比较第一个数组中的 元素是否和 bt2 中相同 （从srcPos开始比较）
     *
     * @param src    被比较的byte数组
     * @param srcPos 开始点
     * @param bt2    目标数组
     * @param length 长度
     * @return
     */
    public static boolean isEqualsByte(byte[] src, int srcPos, byte[] bt2,
                                       int length) {

        byte[] temp = new byte[length];
        System.arraycopy(src, srcPos, temp, 0, length);

        return Arrays.equals(temp, bt2);

    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    /**
     * 字符串格式化为日期时间格式
     *
     * @param format   原来格式 yyyyMMdd HHmmss
     * @param toformat 目标格式 yyyy-MM-dd HH:mm:ss
     * @param time     时间或日期
     * @return 目标日期时间字符串
     */
    public static String str2DateTime(String format, String toformat,
                                      String time) {
        String str = "";
        Date date;

        try {
            date = new SimpleDateFormat(format).parse(time);
            str = new SimpleDateFormat(toformat).format(date);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }

        return str;

    }

    public static <T> List<T> removeNull(List<? extends T> oldList) {
        // 临时集合
        List<T> listTemp = new ArrayList();
        for (int i = 0; i < oldList.size(); i++) {
            // 保存不为空的元素
            if (oldList.get(i) != null) {
                listTemp.add(oldList.get(i));
            }
        }
        return listTemp;
    }

    public static String getTime(String s) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
            Date date = (Date) sdf1.parse(s);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * short转换为字节
     *
     * @param s
     * @return
     */
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        // for (int i = 0; i < 2; i++) {
        // int offset = (targets.length - 1 - i) * 8;
        // targets[i] = (byte) ((s >>> offset) & 0xff);
        // }
        targets[0] = (byte) (s & 0x00ff);
        targets[1] = (byte) ((s & 0xff00) >> 8);
        return targets;
    }

    /**
     * short转换为字节
     *
     * @param s
     * @return
     */
    public static byte[] shortToByteArrayTwo(short s) {
        byte[] targets = new byte[2];
        // for (int i = 0; i < 2; i++) {
        // int offset = (targets.length - 1 - i) * 8;
        // targets[i] = (byte) ((s >>> offset) & 0xff);
        // }
        targets[1] = (byte) (s & 0x00ff);
        targets[0] = (byte) ((s & 0xff00) >> 8);
        return targets;
    }

    /**
     * short[]转换为字节[]
     *
     * @param s
     * @return
     */
    public static byte[] shortArrayToByteArray(short[] s) {
        byte[] targets = new byte[s.length * 2];
        for (int i = 0; i < s.length; i++) {
            byte[] tmp = shortToByteArray(s[i]);

            targets[2 * i] = tmp[0];
            targets[2 * i + 1] = tmp[1];
        }
        return targets;
    }

    /**
     * byte[]到Short
     *
     * @param buf
     * @return
     */
    public static short[] byteArraytoShort(byte[] buf) {
        short[] targets = new short[buf.length / 2];
        short vSample;
        int len = 0;
        for (int i = 0; i < buf.length; i += 2) {
            vSample = (short) (buf[i] & 0x00FF);
            vSample |= (short) ((((short) buf[i + 1]) << 8) & 0xFF00);
            targets[len++] = vSample;
        }
        return targets;
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            //sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 十六进制转换字符串
     *
     * @param
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        try {
            return new String(bytes, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return "";
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b) {
        if (b == null)
            return "";
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            // sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * bytes字符串转换为Byte值
     *
     * @param src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        if ((src.length() % 2) != 0)
            src = "0" + src;
        int l = src.length() / 2;
        //System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = Integer.decode(
                    "0x" + src.substring(i * 2, m) + src.substring(m, n))
                    .byteValue();
        }
        return ret;
    }

    /*
     获取金额
     */
    public static String getTotalPrice(String perPrice, String quantity, String number) {
        String bigDecimalMoney = new BigDecimal(perPrice) + "";
        String payMoney = null;
        //  Logger.e("bigDecimalMoney-->"+bigDecimalMoney);
        if (number.equals("6")) {
            payMoney = new BigDecimal(bigDecimalMoney).multiply(new BigDecimal(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toString();
        } else {
            payMoney = new BigDecimal(bigDecimalMoney).multiply(new BigDecimal(getTotalWeight(quantity))).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toString();
        }
        Logger.e("payMoney--->" + payMoney);
        return payMoney;
    }
    /*
    获取重量
     */

    public static String getTotalWeight(String quantity) {

        // Logger.e("bigDecimalMoney-->"+bigDecimalMoney);
        String weight = new BigDecimal(quantity).divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        Logger.e("totalWeight" +
                weight);
        return weight;
//        if(totalWeight.substring(totalWeight.indexOf("."),totalWeight.length()).equals("00")){
//            return totalWeight.substring(0,totalWeight.indexOf("."));
//            Logger.e("totalWeight-->"+totalWeight.substring(0,totalWeight.indexOf(".")));
//        }else if(totalWeight.substring(totalWeight.length()-1,totalWeight.length()).equals("0")){
//            return totalWeight.substring(0,totalWeight.length()-1);
//        }else{
//            return totalWeight;
//        }

    }

    /**
     * String的字符串转换成unicode的String
     *
     * @param strText 全角字符串
     * @return String 每个unicode之间无分隔符
     * @throws Exception
     */
    public static String strToUnicode(String strText) throws Exception {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
                str.append("\\u" + strHex);
            else
                // 低位在前面补00
                str.append("\\u00" + strHex);
        }
        return str.toString();
    }

    /**
     * unicode的String转换成String的字符串
     *
     * @param hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    public static String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++) {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }

    /**
     * @param src byteêy×é 3¤?èó|μ±?a4
     * @return ×a??oóμ???Dí?á1? ×a??°′??D???×??úDòà′?D?¨
     * @author Junhua Wu
     */
    public static int byteToInt(byte[] src) {
        int tmp = 0;
        for (int i = 0; i < src.length; i++) {
            tmp += ((int) src[i] << (i * 8)) & (0xFF << (i * 8));
        }

        return tmp;
    }

    /**
     * @param src ??Díêy?Y
     * @return ×a??oóμ?byteêy×é?á1? ×a??°′??D???×??úDòà′?D?¨
     * @author Junhua Wu
     */
    public static byte[] intToByte(int src) {
        byte[] tmp = new byte[4];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = (byte) (((int) src >> (i * 8)) & 0xFF);
        }
        return tmp;
    }

    public static byte[] intToByte1024(int src) {
        byte[] tmp = new byte[1024];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = (byte) (((int) src >> (i * 8)) & 0xFF);
        }
        return tmp;
    }


    public static String byteTostrGBK(byte[] data) {
        String result = "";
        try {

            result = new String(data, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            //  Logger.e("StringUtil", "byteTostrGBK", e);

        }
        return result;
    }

    /* *
     *
     * 把16进制字符串转换成字节数组 @param hex @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] intToByteArray(int i) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (i & 0xFF);
        targets[1] = (byte) ((i >> 8) & 0xFF);
        targets[2] = (byte) ((i >> 16) & 0xFF);
        targets[3] = (byte) ((i >> 24) & 0xFF);
        return targets;
    }

    /**
     * 把字节数组转化成int类型，小端模式
     *
     * @param b
     * @return
     */
    public static int byteArrayToInt(byte[] b) {
        int result = 0;
        result = (b[0] & 0xFF) | (b[1] << 8 & 0xFFFF) | (b[2] << 16 & 0xFFFFFF) | (b[3] << 24 & 0xFFFFFFFF);
        return result;
    }

    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch(Constant.REGEX_MOBILE_EXACT, input);
    }

    private static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }


}
