package com.example.admin.appmarket.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by admin on 2016/4/5.
 */
public class DrawableUtil {
    /**
     * 画一张圆角图片
     * @param rgb 图片的颜色
     * @param r 圆角图片的内切圆的半径
     * @return
     */
    public static Drawable getDrawable(int rgb, int r) {
        GradientDrawable drawable = new GradientDrawable();
        //绘画一个矩形
        drawable.setGradientType(GradientDrawable.RECTANGLE);
        //告知绘画矩形的颜色
        drawable.setColor(rgb);
        //告知内切圆的半径
        drawable.setCornerRadius(r);
        return drawable;
    }

    /**
     * 图片选择器
     * @param pressDrawable 选中的图片
     * @param normalDrawable 未选中和默认的图片
     * @return
     */
    public static StateListDrawable getStateListDrawable(Drawable pressDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        //选中的图片设置
        stateListDrawable.addState(new int[]{
                android.R.attr.state_enabled,
                android.R.attr.state_pressed}, pressDrawable);
        //未选中的图片设置
        stateListDrawable.addState(new int[]{
                android.R.attr.state_enabled,}, normalDrawable);
        //默认图片设置
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }

}
