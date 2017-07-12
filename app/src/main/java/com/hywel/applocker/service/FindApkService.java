package com.hywel.applocker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hywel.applocker.BuildConfig;
import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.model.AppInfo;
import com.hywel.applocker.utils.LoadApkUtils;
import com.hywel.applocker.utils.SpUtil;

import java.util.HashSet;
import java.util.List;

/**
 * Created by hywel on 2017/7/5.
 */

public class FindApkService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Log.d("FindApkService", "onCreate");
//        LockerApplication.allSysAppInfos = LoadApkUtils.getAllSysAppInfos(this);
//        LockerApplication.allUserAppInfos = LoadApkUtils.getAllUserAppInfos(this);
        new LoadApkTask(getApplicationContext()).execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (BuildConfig.DEBUG) Log.d("FindApkService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (BuildConfig.DEBUG) Log.d("FindApkService", "onBind");

        return null;
    }


    private class LoadApkTask extends AsyncTask {
        private Context context;
        private PackageManager packageManager;
        private long time;

        public LoadApkTask(Context context) {
            this.context = context;
            packageManager = context.getPackageManager();
        }

        @Override
        protected void onPostExecute(Object o) {
            time = System.currentTimeMillis() / 1000 - time;

            Log.d("LoadApkTask", "onPostExecute 用时" + time + "s");
        }

        @Override
        protected void onPreExecute() {
            time = System.currentTimeMillis() / 1000;
            Log.d("LoadApkTask", "onPreExecute");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            initSysAndUserAppLists();
            return null;
        }

        private void initSysAndUserAppLists() {
            List<PackageInfo> applications = LoadApkUtils.getAllApplications(context);
            for (int i = 0; i < applications.size(); i++) {
                PackageInfo info = applications.get(i);
                AppInfo appInfo = new AppInfo();

                // 获取到程序的包名
                String packageName = info.packageName;
                // 获取到版本号
                String versionName = info.versionName;

                ApplicationInfo applicationInfo = info.applicationInfo;
                // 获取程序名
                String appName = applicationInfo.loadLabel(packageManager).toString();
                // 获取到程序图标
//                Drawable icon = applicationInfo.loadIcon(packageManager);
//                int uid = applicationInfo.uid;

                appInfo.setId((long) (1 + 2 * i));
                appInfo.setAppName(appName);
//                appInfo.setAppDrawable(((BitmapDrawable) icon).getBitmap());
                appInfo.setApplicationInfo(applicationInfo);
                appInfo.setPackageName(packageName);
                appInfo.setVersionName(versionName);
//                List<String> lockedAppPackNames = getLockedAppPackNames(context);
                HashSet<String> packNames = SpUtil.getInstance(context).getPackNames();
                if (packNames.contains(packageName)) {
                    appInfo.setLocked(true);
                } else {
                    appInfo.setLocked(false);
                }

                if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                    LockerApplication.allSysAppInfos.add(appInfo);
                } else {
                    LockerApplication.allUserAppInfos.add(appInfo);
                }
            }
        }
    }

}
