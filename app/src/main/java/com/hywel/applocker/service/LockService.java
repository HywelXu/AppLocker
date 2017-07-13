package com.hywel.applocker.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.R;
import com.hywel.applocker.activity.MainActivity;
import com.hywel.applocker.utils.SpUtil;

import java.util.HashSet;
import java.util.List;

/**
 * Created by hywel on 2017/7/13.
 * 监听 App 启动
 */

public class LockService extends Service {

    private HashSet<String> packNames;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        /** 获取系统服务 ActivityManager */
        packNames = SpUtil.getInstance(context).getPackNames();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String launcherTopApp = getLauncherTopApp();
//                String runningTopApp = getRunningTopApp();
//                Log.d("LockService", "launcherTopApp-->" + launcherTopApp + "\n runningTopApp-->" + runningTopApp);
                if (!TextUtils.isEmpty(launcherTopApp))
                    sendNotification(launcherTopApp);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 转到解锁界面
     */
    private void passwordLock(String packageName) {
        LockerApplication.getInstance().clearAllActivity();
        Intent intent = new Intent(this, MainActivity.class);

//        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
//        intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_FINISH);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 获取栈顶 App
     *
     * @return packageName
     */
    private String getLauncherTopApp() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        /** 获取当前正在运行的任务栈列表， 越是靠近当前运行的任务栈会被排在第一位，之后的以此类推 */
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);

        /** 获得当前最顶端的任务栈，即前台任务栈 */
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);

        /** 获取前台任务栈的最顶端 Activity */
        ComponentName topActivity = runningTaskInfo.topActivity;

        /** 获取应用的包名 */
        String packageName = topActivity.getPackageName();

        Log.i("hywel", "packageName-->" + packageName);
        return packageName;
    }

    private String getRunningTopApp() {
        //5.0以后需要用这方法
        UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 10000;
        String result = "";
        UsageEvents.Event event = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!TextUtils.isEmpty(result)) {
                Log.i("LockService", "result-->" + result);
                return result;
            }
        }
        return "";
    }

    private void sendNotification(String info) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setContentTitle("AppLocker");
//        builder.setContentText(info + "is running");
//        builder.setSmallIcon(R.mipmap.ic_lock);
//        builder.build();

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_lock)
                    .setContentTitle("AppLocker")
                    .setContentText(info + " is running")
                    .build();
        }

        manager.notify(1021, notification);
    }
}
