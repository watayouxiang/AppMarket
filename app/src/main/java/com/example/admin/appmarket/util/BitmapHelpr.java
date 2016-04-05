package com.example.admin.appmarket.util;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelpr {

    private BitmapHelpr() {
    }

    private static BitmapUtils bitmapUtils;

    public static BitmapUtils getBitmapUtils(Context context) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(context);
        }
        return bitmapUtils;
    }

}
