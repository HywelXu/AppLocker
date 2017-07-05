package com.hywel.applocker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hywel.applocker.BuildConfig;
import com.hywel.applocker.R;
import com.hywel.applocker.model.AppInfo;
import com.hywel.applocker.utils.AndroidTools;
import com.hywel.applocker.utils.SpUtil;

import java.util.List;

/**
 * Created by hywel on 2017/7/3.
 */

public class AppAdapter extends Adapter<AppAdapter.AppViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<AppInfo> mAppInfos;

    public AppAdapter(Context mContext, List<AppInfo> mAppInfos) {
        this.mContext = mContext;
        this.mAppInfos = mAppInfos;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppViewHolder(mLayoutInflater.inflate(R.layout.applist_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final AppViewHolder holder, final int position) {
        final AppInfo appInfo = mAppInfos.get(position);

        renderItemView(holder, appInfo, position);
//        renderItemLockStatus(holder.sAppSwitch, appInfo, position);
    }

    /**
     * 渲染 ItemView 中的各控件
     *
     * @param holder   AppViewHolder
     * @param appInfo  AppInfo
     * @param position int
     */
    private void renderItemView(final AppViewHolder holder, final AppInfo appInfo, final int position) {
        final String appName = appInfo.getAppName();

        holder.sAppName.setText(appName);
        holder.sAppIcon.setImageDrawable(appInfo.getAppDrawable());
        holder.sAppSwitch.setChecked(appInfo.isLocked());
        holder.sAppSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AppAdapter", "appName: " + appName + " isChecked: " + holder.sAppSwitch.isChecked());
                Log.d("AppAdapter", "id: " + appInfo.getId());

                changeItemLockStatus(holder.sAppSwitch, appInfo, position);
            }
        });
    }


    private void renderItemLockStatus(CheckBox checkBox, AppInfo info, int position) {
        List<AppInfo> lockedPackNames = SpUtil.getInstance().getLockedPackNames();
        if (BuildConfig.DEBUG)
            Log.d("AppAdapter", "lockedPackNames size-> " + lockedPackNames.size());

        if (AndroidTools.isListValidate(lockedPackNames) && position < lockedPackNames.size()) {
            if (lockedPackNames.get(position).getId() == info.getId()) {
                checkBox.isChecked();
            } else {
//                checkBox.setChecked(false);
                info.setLocked(false);
            }
        }
    }

    /**
     * 更改 ItemView 中 CheckBox 的状态
     *
     * @param checkBox CheckBox
     * @param info     AppInfo
     * @param position int
     */
    private void changeItemLockStatus(CheckBox checkBox, AppInfo info, int position) {
        if (checkBox.isChecked()) {
            info.setLocked(true);
            SpUtil.getInstance().saveLockedPackNameItem(info);
        } else {
            info.setLocked(false);
            SpUtil.getInstance().removeLockedPackNameItem(info);
        }
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mAppInfos == null ? 0 : mAppInfos.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        //应用图标
        ImageView sAppIcon;
        //应用名称
        TextView sAppName;
        //应用上锁
        CheckBox sAppSwitch;

        AppViewHolder(View itemView) {
            super(itemView);
            sAppIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            sAppName = (TextView) itemView.findViewById(R.id.app_name);
            sAppSwitch = (CheckBox) itemView.findViewById(R.id.app_switch);
        }

    }
}
