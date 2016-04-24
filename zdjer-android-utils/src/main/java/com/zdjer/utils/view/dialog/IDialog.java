package com.zdjer.utils.view.dialog;

import android.app.Dialog;

/**
 * @author zdj
 */
public interface IDialog {

    /**
     * 隐藏等待Dialog
     */
    void hideWaitDialog();

    /**
     * @return
     */
    Dialog showWaitDialog();

    /**
     * @param resId
     * @return
     */
    Dialog showWaitDialog(int resId);

    /**
     * @param message
     * @return
     */
    Dialog showWaitDialog(String message);
}
