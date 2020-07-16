package com.tang.util.app;

import android.app.Activity;

import java.util.Stack;

/**
 * ActivityManager,用于退出APP
 * 管理Activity的工具类
 * @author HWJ
 */
public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
    }

    /**
     * 获得单例
     *
     * @return
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 移除栈顶Activity
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
        }
    }

    /**
     * 获得当前栈顶Activity
     * @return
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack.size() > 0) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    /**
     *  将当前Activity推入栈中
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 退出栈中所有Activity除了设置的activity
     * @param cls
     */
    public void popAllActivityExceptionOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 退出栈中所有Activity
     */
    public void popAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }

}
