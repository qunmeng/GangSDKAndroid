package com.qm.gangsdk.ui.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by lijiyuan on 2017/10/16.
 *
 * activity管理类
 *
 */

public class XLActivityManager {
    private static XLActivityManager instance;
    private Stack<Activity> activityStack;  //activity栈

    //单例模式
    public static XLActivityManager getInstance() {
        if (instance == null) {
            instance = new XLActivityManager();
        }
        return instance;
    }

    /**
     * 把activity压入栈中
     * @param activity        activity对象
     */
    public void pushOneActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取栈顶的activity，先进后出原则
     */
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }

    /**
     * 移除一个activity
     */
    public void popOneActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
            }
        }
    }

    //退出所有activity
    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) break;
                popOneActivity(activity);
            }
        }
    }
}
