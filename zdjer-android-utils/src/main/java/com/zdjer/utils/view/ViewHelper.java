package com.zdjer.utils.view;

import android.view.View;

/**
 * Created by zdj on 16/4/15.
 */
public class ViewHelper {

    /**
     * 聚焦控件
     *
     * @param view
     *            控件对象
     */
    public static void Focus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }
}
