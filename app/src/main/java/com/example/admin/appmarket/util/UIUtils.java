package com.example.admin.appmarket.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.example.admin.appmarket.application.BaseApplication;

public class UIUtils {
    /**
     * 获取上下文对象
      * @return
     */
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    public static Handler getHandler() {
        return BaseApplication.getHandler();
    }

    /**
     * 获取主线程id
     * @return
     */
    public static int getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    /**
     * 获取主线程对象
     * @return
     */
    public static Thread getMainThread() {
        return BaseApplication.getMainThread();
    }

    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            getHandler().post(runnable);
        }
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 从string.xml中获取字符串
     * @param stringId
     * @return
     */
    public static String getString(int stringId) {
        return getResources().getString(stringId);
    }

    /**
     * 添加string类型数组的方法
     * @param stringArrayId
     * @return
     */
    public static String[] getStringArray(int stringArrayId) {
        return getResources().getStringArray(stringArrayId);
    }

    /**
     * 通过资源文件id获取图片对象
     * @param drawableID
     * @return
     */
    public static Drawable getDrawable(int drawableID) {
        return getResources().getDrawable(drawableID);
    }

    /**
     * dip-->px
     * @param dip
     * @return
     */
    public static int dip2px(int dip) {
        float d = getResources().getDisplayMetrics().density;
        return (int) (dip * d + 0.5);
    }

    /**
     * px--->dp
     * @param px
     * @return
     */
    public static int px2dip(int px) {
        float d = getResources().getDisplayMetrics().density;
        return (int) (px / d + 0.5);
    }

    public static ColorStateList getColorStateList(int mTabTextColorResId) {
        return getResources().getColorStateList(mTabTextColorResId);
    }

    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }
}
