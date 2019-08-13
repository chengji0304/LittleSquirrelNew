package com.panda.littlesquirrel.utils;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.panda.littlesquirrel.R;


/**
 * @description Toast提示消息
 * @author King_wangyao
 * @date 2014-5-20
 * @version 1.0.0
 *
 */
public class ToastUtil extends Toast {

	private Context mContext;

	public ToastUtil(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * 显示Toast消息（带样式效果）
	 * @param icon
	 * @param message
	 */
	public void showTipsToast(int icon, String message) {
		LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.view_tips, null);
		TextView text = (TextView) view.findViewById(R.id.tips_message);
		text.setText(message);
		ToastUtil result = new ToastUtil(mContext);
		result.setView(view);
		result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);//设置位置，此处为垂直居中
		result.setDuration(Toast.LENGTH_SHORT);
		result.show();
		result.setIcon(icon);
		result.setText(message);
	}

	/**
	 * 显示Toast消息（无样式效果）
	 * @param message
	 */
	public void showShortToast(String message) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(message);
		Toast toast = new Toast(mContext);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 300);
		toast.setView(view);
		toast.show();
	}

	public void setIcon(int iconResId) {
		if (getView() == null) {
			throw new RuntimeException("This Toast was not created with Toast.makeText()");
		}
		ImageView iv = (ImageView) getView().findViewById(R.id.tips_icon);
		if (iv == null) {
			throw new RuntimeException("This Toast was not created with Toast.makeText()");
		}
		iv.setImageResource(iconResId);
	}

	@Override
	public void setText(CharSequence text) {
		if (getView() == null) {
			throw new RuntimeException("This Toast was not created with Toast.makeText()");
		}
		TextView tv = (TextView) getView().findViewById(R.id.tips_message);
		if (tv == null) {
			throw new RuntimeException("This Toast was not created with Toast.makeText()");
		}
		tv.setText(text);
	}

}
