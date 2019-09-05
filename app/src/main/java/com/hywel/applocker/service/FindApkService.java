package com.hywel.applocker.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hywel.applocker.BuildConfig;
import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.utils.LoadApkUtils;

/**
 * Created by hywel on 2017/7/5.
 */

public class FindApkService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Log.d("FindApkService", "onCreate");
        new LoadApkTask().execute();
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

    /**
     * 获取用户应用列表
     */
    private static class LoadApkTask extends AsyncTask<Object, Void, Object> {
        private PackageManager packageManager;
        private long time;

        private LoadApkTask() {
            packageManager = LockerApplication.getInstance().getContext().getPackageManager();
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
            LoadApkUtils.initSysAndUserAppLists(packageManager);
            return null;
        }
    }
}
