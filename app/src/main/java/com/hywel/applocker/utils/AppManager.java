package com.hywel.applocker.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.hywel.applocker.LockerApplication;

import java.util.Stack;


public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;
    public static boolean flag = true;// 线程退出的标记

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }


    public boolean containActivity(Activity pActivity) {
        if (pActivity != null && activityStack != null) {
            return activityStack.contains(pActivity);
        }
        return false;
    }

    public boolean containActivity(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束当前页面，如果 APP 已经退出则初始化锁定应用的状态
     *
     * @param pActivity Activity
     */
    public void finishAndResetLockerState(Activity pActivity) {
        if (pActivity != null) {
            activityStack.remove(pActivity);
            pActivity.finish();
        }

        if (isAppExit()) {
            if (!flag) {
                FancyToastUtils.showWarningToast("APP 已退出");
                SpUtil.resetLockedPackageState();
            } else {
                FancyToastUtils.showWarningToast("APP 无界面");
                LockerApplication.mLockedSimpleAppInfos = SpUtil.getInstance().getLockedPackNameList();
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityMgr != null) {
                activityMgr.killBackgroundProcesses(context.getPackageName());
                System.exit(0);
            }
        } catch (Exception ignored) {
        }
    }

    public boolean isAppExit() {
        return activityStack == null || activityStack.isEmpty();
    }

    /**
     * 返回到指定 activity
     *
     * @param cls 指定的activity
     */
    public void backToActivity(Class<?> cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                return;
            }
            popActivity(activity);
        }
    }

    /**
     * 弹出画面栈中的某一个 activity
     *
     * @param activity 要弹出的 activity 对象
     */
    public void popActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }

        if (activityStack.isEmpty()) {
            return;
        }

        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
