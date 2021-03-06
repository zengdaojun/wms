package com.zdjer.win.view.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zdjer.win.view.LoginActivity;

/**
 * Created by zdj on 16/3/23.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, LoginActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}