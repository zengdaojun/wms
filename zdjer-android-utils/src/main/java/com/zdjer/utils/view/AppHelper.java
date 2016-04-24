package com.zdjer.utils.view;

import android.app.Activity;
import android.os.Process;

import java.util.Stack;

/**
 * Created by zdj on 16/4/12.
 */
public class AppHelper {

    private static Stack<Activity> statckActivity;

    /**
     * 添加Activity到堆栈
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (statckActivity == null) {
            statckActivity = new Stack<Activity>();
        }
        statckActivity.add(activity);
    }

    /**
     * 获得当前的Activity（堆栈中最后一个）
     *
     * @return Activity
     */
    public static Activity getCurrentActivity() {
        return statckActivity.lastElement();
    }

    /**
     * 获得Activity
     *
     * @param activityClass Activity类名
     * @return Activity
     */
    public static Activity getActivity(Class<?> activityClass) {
        if (statckActivity != null) {
            for (Activity activity : statckActivity) {
                if (activity.getClass().equals(activityClass)) {
                    return activity;
                }
            }
        }
        return null;
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            statckActivity.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param activityClass Activity类名
     */
    public static void finishActivity(Class<?> activityClass) {
        for (Activity activity : statckActivity) {
            if (activity.getClass().equals(activityClass)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束当前的Acivity（最后一个添加的）
     */
    public static void finishActivity() {
        Activity activity = statckActivity.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束所有的Activity
     */
    public static void finishAllActivity() {
        int size = statckActivity.size();
        for (int i = 0; i < size; i++) {
            if (statckActivity.get(i) != null) {
                statckActivity.get(i).finish();
            }
        }
        statckActivity.clear();
    }

    /**
     * 退出应用程序
     *
     */
    public static void appExit() {
        finishAllActivity();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
