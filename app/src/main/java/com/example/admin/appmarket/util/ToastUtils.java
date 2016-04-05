package com.example.admin.appmarket.util;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.appmarket.R;

public class ToastUtils {

	/** 短暂显示Toast提示(来自res) **/
	public static void showShortToast(int resId) {
		showToast(UIUtils.getContext().getString(resId), Toast.LENGTH_SHORT);
	}

	/** 短暂显示Toast提示(来自String) **/
	public static void showShortToast(String text) {
		showToast(text, Toast.LENGTH_SHORT);
	}

	/** 长时间显示Toast提示(来自res) **/
	public static void showLongToast(int resId) {
		showToast(UIUtils.getContext().getString(resId), Toast.LENGTH_LONG);
	}

	/** 长时间显示Toast提示(来自String) **/
	public static void showLongToast(String text) {
		showToast(text, Toast.LENGTH_LONG);
	}

	private static void showToast(CharSequence text, int duration) {
		Context context = UIUtils.getContext();
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;

		LinearLayout layoutRoot = new LinearLayout(context);
		layoutRoot.setLayoutParams(layoutParams);
		layoutRoot.setOrientation(LinearLayout.VERTICAL);
		layoutRoot.setBackgroundResource(R.drawable.toast_bg);
		layoutRoot.setMinimumWidth(UIUtils.dip2px(200));
		int padding = UIUtils.dip2px(12);
		layoutRoot.setPadding(padding, padding, padding, padding);
		
		TextView contentView = new TextView(context);
		contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		contentView.setTextColor(Color.parseColor("#FF4F4F4F"));
		contentView.setText(text);
		layoutRoot.addView(contentView, layoutParams);

		Toast toast = new Toast(context);
		toast.setDuration(duration);
		toast.setView(layoutRoot);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
