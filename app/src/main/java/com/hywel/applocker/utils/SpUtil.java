package com.hywel.applocker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hywel.applocker.BuildConfig;
import com.hywel.applocker.Config;
import com.hywel.applocker.model.AppInfo;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


public class SpUtil {
    private volatile static SpUtil mInstance;

    private Context mContext;
    private SharedPreferences mPref;
    private Gson gson;

    private SpUtil() {
    }

    public static SpUtil getInstance() {
        if (null == mInstance) {
            synchronized (SpUtil.class) {
                if (null == mInstance) {
                    mInstance = new SpUtil();
                }
            }
        }
        return mInstance;
    }

    //在AppBase里面初始化
    public void init(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (mPref == null) {
            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        //=================================
        //解决 Gson 异常
        // <p>
        // java.lang.SecurityException: Can't make method constructor accessible
        //</p>
        //=================================
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();

    }

    public void putString(String key, String value) {
        Editor editor = mPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        Editor editor = mPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        Editor editor = mPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        Editor editor = mPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return mPref.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        return mPref.getBoolean(key, def);
    }

    public String getString(String key) {
        return mPref.getString(key, "");
    }

    public String getString(String key, String def) {
        return mPref.getString(key, def);
    }

    public long getLong(String key) {
        return mPref.getLong(key, 0);
    }

    public long getLong(String key, int defInt) {
        return mPref.getLong(key, defInt);
    }

    public int getInt(String key) {
        return mPref.getInt(key, 0);
    }

    public long getInt(String key, int defInt) {
        return mPref.getInt(key, defInt);
    }

    public boolean contains(String key) {
        return mPref.contains(key);
    }


    public void remove(String key) {
        Editor editor = mPref.edit();
        editor.remove(key);
        editor.apply();
    }

    public void removeValue(String key, String value) {
        Editor editor = mPref.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clear() {
        Editor editor = mPref.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 储存要上锁的应用包名
     *
     * @param dataValue 要上锁的应用包名
     */
    public void saveLockedPackNameItem(AppInfo dataValue) {
        List<AppInfo> lockedNames = getLockedPackNames();
        if (!AndroidTools.isListValidate(lockedNames)) {
            lockedNames = new ArrayList<>();
        }
        lockedNames.add(dataValue);
        putString(Config.SP_KEY_LOCKED_PACKAGE_NAME, gson.toJson(lockedNames));
    }

    /**
     * 获取要上锁的应用包名集合
     *
     * @return 要上锁的应用包名集合
     */
    public List<AppInfo> getLockedPackNames() {
        List<AppInfo> lockedPackNames = new ArrayList<>();
        try {
            String lockedPackNameJson = getString(Config.SP_KEY_LOCKED_PACKAGE_NAME, "");
            if (BuildConfig.DEBUG) Log.d("SpUtil", "lockedPackNameJson->" + lockedPackNameJson);
            if (!TextUtils.isEmpty(lockedPackNameJson)) {
                lockedPackNames = gson.fromJson(lockedPackNameJson,
                        new TypeToken<List<AppInfo>>() {
                        }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lockedPackNames;
    }

    /**
     * 移除不需要上锁的应用包名
     *
     * @param value 不需要上锁的应用包名
     */
    public void removeLockedPackNameItem(AppInfo value) {
        List<AppInfo> lockedPackNames = getLockedPackNames();
        int containTheItem = isContainTheItem(lockedPackNames, value);
        lockedPackNames.remove(containTheItem);
    }

    /**
     * 是否包含某个已上锁的应用包名
     *
     * @param lockedNames 上锁的应用包名集合
     * @param info        应用包名
     * @return 此应用在集合中的位置
     */
    public int isContainTheItem(List<AppInfo> lockedNames, AppInfo info) {
        for (int i = 0; i < lockedNames.size(); i++) {
            if (lockedNames.get(i).getId() == info.getId()) {
                if (BuildConfig.DEBUG) Log.d("SpUtil", "id: " + lockedNames.get(i).getId());
                return i;
            }
        }
        return -1;
    }

    /**
     * 某个 APP 是否已被锁定
     *
     * @param id APP 的 id
     * @return
     */
    public boolean isTheAppLocked(long id) {
        List<AppInfo> lockedPackNames = getLockedPackNames();
        for (int i = 0; i < lockedPackNames.size(); i++) {
            if (lockedPackNames.get(i).getId() == id) {
                if (BuildConfig.DEBUG) Log.d("SpUtil", "id: " + id);
                return true;
            }
        }
        return false;
    }
}
