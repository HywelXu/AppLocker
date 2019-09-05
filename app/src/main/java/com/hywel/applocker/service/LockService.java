package com.hywel.applocker.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.R;
import com.hywel.applocker.activity.PwdVerifyActivity;
import com.hywel.applocker.model.SimpleAppInfo;
import com.hywel.applocker.utils.AppManager;
import com.hywel.applocker.utils.LoadApkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hywel on 2017/7/13.
 * 监听 App 启动
 */

public class LockService extends Service {

    private Context context;
    private List<String> packageList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = LockerApplication.getInstance().getContext();
        loopForRunningApp();
        registerPwdLockReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppManager.flag = false;
        unregisterPwdLockReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void loopForRunningApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (AppManager.flag) {//最好是能够监测到有程序被启动，这样太耗性能；
                    synchronized (LockService.class) {
                        String launcherTopApp = getLauncherTopApp();
                        String runningTopApp = getRunningTopApp();
                        Log.e("LockService", "launcherTopApp-->" + launcherTopApp + "\n runningTopApp-->" + runningTopApp);

                        if (TextUtils.isEmpty(launcherTopApp) && TextUtils.isEmpty(runningTopApp)) {
                            return;
                        }
                        //若为本身
//                        if (runningTopApp.equals(getPackageName())) return;

//                        List<SimpleAppInfo> mLockedPackNames = SpUtil.getInstance().getLockedPackNameList();

                        List<SimpleAppInfo> mLockedPackNames = LockerApplication.mLockedSimpleAppInfos;
                        int vTargetAppInUserApps = LoadApkUtils.findTargetAppInApps(mLockedPackNames, launcherTopApp);
                        int vTargetAppInApps = LoadApkUtils.findTargetAppInApps(mLockedPackNames,
                                runningTopApp);

                        if (vTargetAppInApps >= 0) {
                            SimpleAppInfo vAppInfo = LockerApplication.mLockedSimpleAppInfos.get(vTargetAppInApps);
                            boolean vSetUnLock = vAppInfo.isSetUnLock();
                            if (!vSetUnLock) {
                                vAppInfo.setUnLock(true);//本次开锁
                                sendNotification(vAppInfo.getAppName());
                                passwordLock(vAppInfo.getPackageName());
                            }
                        }
                        SystemClock.sleep(500);
                    }
                }
            }
        }).start();
    }

    private void openPwdVerify() {
        startActivity(new Intent(this, PwdVerifyActivity.class));
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder implements IMyBinder {

        @Override
        public void setPackageNames(List<String> packageNames) {
            packageList.clear();
            packageList.addAll(packageNames);
            Log.e("LockService", "MyBinder-->" + packageNames.size());
        }
    }

    MyBinder myBinder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    /**
     * 转到解锁界面
     */
    private void passwordLock(String packageName) {
        LockerApplication.getInstance().clearAllActivity();

        PwdVerifyActivity.performPwdVerify(this, packageName, AppConstants.UNLOCK_FROM_OTHERS);
    }

    private PwdLockReceiver mPwdLockReceiver;

    private void registerPwdLockReceiver() {
        mPwdLockReceiver = new PwdLockReceiver();
        IntentFilter vIntentFilter = new IntentFilter(AppConstants.PWD_LOCK_BROAD_ACTION);
        registerReceiver(mPwdLockReceiver, vIntentFilter);
    }

    private void unregisterPwdLockReceiver() {
        if (mPwdLockReceiver != null) {
            unregisterReceiver(mPwdLockReceiver);
        }
    }

    public class PwdLockReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && AppConstants.PWD_LOCK_BROAD_ACTION.equals(intent.getAction())) {
                String vPackageName = intent.getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
//                AndroidTools.renderUnlockElementsByPackageName(vPackageName);
                Intent vLaunchIntentForPackage = getPackageManager().getLaunchIntentForPackage(vPackageName);
                startActivity(vLaunchIntentForPackage);
            }
        }
    }

    /**
     * 获取栈顶 App
     *
     * @return packageName
     */
    private String getLauncherTopApp() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        /** 获取当前正在运行的任务栈列表， 越是靠近当前运行的任务栈会被排在第一位，之后的以此类推 */
        List<ActivityManager.RunningTaskInfo> runningTasks = null;
        if (activityManager != null) {
            runningTasks = activityManager.getRunningTasks(1);

            /** 获得当前最顶端的任务栈，即前台任务栈 */
            ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);

            /** 获取前台任务栈的最顶端 Activity */
            ComponentName topActivity = runningTaskInfo.topActivity;

            /** 获取应用的包名 */
            String packageName = topActivity.getPackageName();

            Log.e("hywel", "packageName-->" + packageName);
            return packageName;
        }
        return "";
    }

    private String getRunningTopApp() {
        //5.0以后需要用这方法
        UsageStatsManager sUsageStatsManager = null;
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 10000;
        String result = "";
        UsageEvents.Event event = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
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

        Notification
                notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_lock)
                .setContentTitle("AppLocker")
                .setContentText(info + " is running")
                .build();

        if (manager != null) {
            manager.notify(1021, notification);
        }
    }
}
