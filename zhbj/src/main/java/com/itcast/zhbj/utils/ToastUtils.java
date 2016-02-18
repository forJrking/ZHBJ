package com.itcast.zhbj.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void showShort(Context context, String res) {
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String res) {
        Toast.makeText(context, res, Toast.LENGTH_LONG).show();
    }
}
