package com.panda.littlesquirrel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.entity.PriceInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * @description 配置环境Preferences类
 * @author King_wangyao
 * @date 2014-5-20
 * @version 1.0.0
 *
 */
public class PreferencesUtil {

	private static final String TAG = PreferencesUtil.class.getSimpleName();

	private Context mContext;


	private static final String PREFES_NAME = "com.panda.littlesquirrel";// 配置文件名称

	private static final String PREFES_DEFAULT_VALUE = "";// 配置文件默认值

	public PreferencesUtil(Context context) {
		this.mContext = context;
	}

	/**
	 * 以Preferences文件方式存储信息
	 *
	 * @param key
	 * @param value
	 */
	public void writePrefs(String key, String value) {
		// 指定操作的文件名称
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFES_NAME, Activity.MODE_PRIVATE);
		// 编辑文件
		SharedPreferences.Editor edit = sharedPreferences.edit();
		// 保存内容
		try {
			edit.putString(key, value);
		} catch (Exception e) {
			Logger.e(TAG, "AESEncryptor ocure error:" + e.getMessage());
		}
		// 提交更新
		edit.commit();
	}

	/**
	 * 以Preferences文件方式读取信息
	 *
	 * @param key
	 * @return
	 */
	public String readPrefs(String key) {
		// 指定操作的文件名称
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFES_NAME, Activity.MODE_PRIVATE);
		// 指定读取字符的名称
		String value = sharedPreferences.getString(key, PREFES_DEFAULT_VALUE);
		return value;
	}
	/**
	 * public static boolean getBoolean(String key,boolean defaultValue) {
	 return sPrefs.getBoolean(key, defaultValue);
	 }
	 public static void setBoolean(String key,boolean value) {
	 sPrefs.edit().putBoolean(key, value).commit();
	 }
	 */

   public void deletPrefs(String key){
	   // 指定操作的文件名称
	   SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFES_NAME, Activity.MODE_PRIVATE);
	   // 编辑文件
	   SharedPreferences.Editor edit = sharedPreferences.edit();
	   // 保存内容
	   try {
		  edit.remove(key);
	   } catch (Exception e) {
		   Logger.e(TAG, "AESEncryptor ocure error:" + e.getMessage());
	   }
	   // 提交更新
	   edit.commit();

   }
	/**
	 * 清除Preferences文件记录
	 */
	public void clearPrefs() {
		// 指定操作的文件名称
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFES_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 保存List
	 * @param tag
	 * @param datalist
	 */
	public void setDataList(String tag, ArrayList<GarbageParam> datalist) {
		// 指定操作的文件名称
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFES_NAME, Activity.MODE_PRIVATE);
		// 编辑文件
		SharedPreferences.Editor edit = sharedPreferences.edit();
		if (null == datalist || datalist.size() <= 0){
			return;
		}
		Gson gson = new Gson();
		//转换成json数据，再保存
		String strJson = gson.toJson(datalist);
	//	edit.clear();
		edit.putString(tag, strJson);
		edit.commit();

	}


	/**
	 * 获取List
	 * @param tag
	 * @return
	 */
	/**
	 * 获取List
	 * @param tag
	 * @return
	 */
	public  ArrayList<GarbageParam> getDataList(String tag) {
		// 指定操作的文件名称
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFES_NAME, Activity.MODE_PRIVATE);

		ArrayList<GarbageParam> datalist=new ArrayList<GarbageParam>();
		String strJson = sharedPreferences.getString(tag, null);
		if (null == strJson) {
			return datalist;
		}
		Gson gson = new Gson();
		datalist = gson.fromJson(strJson, new TypeToken<ArrayList<GarbageParam>>() {
		}.getType());
		//datalist=JSON.parseArray(strJson,String.class);
		return datalist;

	}

	/*
	 保存价格list
	 */
	public void setPriceList(String tag, ArrayList<PriceInfo> list) {
		// 指定操作的文件名称
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFES_NAME, Activity.MODE_PRIVATE);
		// 编辑文件
		SharedPreferences.Editor edit = sharedPreferences.edit();
		if (null == list || list.size() <= 0){
			return;
		}
		Gson gson = new Gson();
		//转换成json数据，再保存
		String strJson = gson.toJson(list);
		//	edit.clear();
		edit.putString(tag, strJson);
		edit.commit();

	}

	/*
	 获取价格list
	 */
	public  ArrayList<PriceInfo> getPriceList(String tag) {
		// 指定操作的文件名称
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFES_NAME, Activity.MODE_PRIVATE);

		ArrayList<PriceInfo> datalist=new ArrayList<PriceInfo>();
		String strJson = sharedPreferences.getString(tag, null);
		if (null == strJson) {
			return datalist;
		}
		Gson gson = new Gson();
		datalist = gson.fromJson(strJson, new TypeToken<ArrayList<PriceInfo>>() {
		}.getType());
		//datalist=JSON.parseArray(strJson,String.class);
		return datalist;

	}

}
