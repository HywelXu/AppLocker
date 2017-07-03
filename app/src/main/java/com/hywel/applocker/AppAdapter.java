package com.hywel.applocker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.hywel.applocker.model.AppInfo;

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
    public void onBindViewHolder(AppViewHolder holder, int position) {
//holder.sAppIcon.setImageResource();
        holder.sAppName.setText(mAppInfos.get(position).getAppName());
        holder.sAppSwitch.setChecked(mAppInfos.get(position).isLocked());
    }

    @Override
    public int getItemCount() {
        return mAppInfos == null ? 0 : mAppInfos.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {

        ImageView sAppIcon;
        TextView sAppName;
        Switch sAppSwitch;

        AppViewHolder(View itemView) {
            super(itemView);
            sAppIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            sAppName = (TextView) itemView.findViewById(R.id.app_name);
            sAppSwitch = (Switch) itemView.findViewById(R.id.app_switch);
        }

    }
}
