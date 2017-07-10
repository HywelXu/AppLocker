package com.hywel.applocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.hywel.applocker.LockerApplication;
import com.hywel.applocker.R;
import com.hywel.applocker.fragment.SysAppFragment;
import com.hywel.applocker.fragment.UserAppFragment;
import com.hywel.applocker.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 获取手机中所安装的所有应用
 * todo 1.上滑特效 2.获取手机应用集合 3.搜索功能
 */
public class AppActivity extends FragmentActivity {
    @BindView(R.id.app_search_view)
    SearchView mSearchView;
    @BindView(R.id.viewpager_apps)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.password_panel_guide)
    TextView mPasswordGuideTV;
    @BindView(R.id.edit_search_tv)
    TextView mEditSearchTV;
    @BindView(R.id.password_panel_guide_layout)
    LinearLayout mPasswordGuideLayout;
    @BindView(R.id.iv_setting)
    ImageView mSettingIV;
    @BindView(R.id.iv_icon)
    ImageView mIconIV;
    @BindView(R.id.iv_icon_setting)
    ImageView mIconSettingIV;


    private List<String> fragmentTitles;
    private List<Fragment> fragments;

    private SysAppFragment mSystemSysAppFragment;
    private UserAppFragment mUserAppListFragment;

    private int sysAppCount;
    private int userAppCount;
    private boolean hasSearched;
    private List<AppInfo> allSysAppInfos = new ArrayList<>();
    private List<AppInfo> allUserAppInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        ButterKnife.bind(this);

        setAppList();
        startHeaderAnim();
        setViewPager();
    }

    private void setAppList() {
        //        allSysAppInfos = LoadApkUtils.getAllSysAppInfos(this);
//        allUserAppInfos = LoadApkUtils.getAllUserAppInfos(this);
        allSysAppInfos = LockerApplication.allSysAppInfos;
        allUserAppInfos = LockerApplication.allUserAppInfos;
        sysAppCount = allSysAppInfos.size();
        userAppCount = allUserAppInfos.size();
    }

    /**
     * 设置 ViewPager
     */
    private void setViewPager() {
        fragmentTitles = new ArrayList<>();
        fragmentTitles.add("系统应用（" + sysAppCount + "）");
        fragmentTitles.add("用户应用（" + userAppCount + "）");

        fragments = new ArrayList<>();
        mSystemSysAppFragment = SysAppFragment.newInstance(allSysAppInfos);
        mUserAppListFragment = UserAppFragment.newInstance(allUserAppInfos);
        fragments.add(mSystemSysAppFragment);
        fragments.add(mUserAppListFragment);

        mViewPager.setAdapter(new AppFragmentAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * 布局头部的动画
     */
    private void startHeaderAnim() {
        mIconSettingIV.animate().rotationY(360f).setDuration(1000).setInterpolator(new AnticipateInterpolator()).start();
        mPasswordGuideTV.animate().rotationX(360f).setDuration(1500).setInterpolator(new DecelerateInterpolator()).start();
    }

    /**
     * 处理搜索功能
     */
    public void handleSearch(View view) {
        hasSearched = true;
        Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
        animSearchLayout(hasSearched);
        searchMethod();
    }

    private void searchMethod() {
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(AppActivity.this, "query" + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
//                if (!TextUtils.isEmpty(newText)) {
//                    Toast.makeText(AppActivity.this, "newText" + newText, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(AppActivity.this, "newText is null", Toast.LENGTH_SHORT).show();
//                }
                return false;
            }
        });
    }

    /**
     * 用以切换区域配色
     *
     * @param hasSearched 是否点击过搜索按钮
     */
    private void animSearchLayout(boolean hasSearched) {
        Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.drop_in);
        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.drop_out);
        if (hasSearched) {
            if (mSearchView.getVisibility() == View.GONE) {
                mSearchView.setVisibility(View.VISIBLE);
            }
            mPasswordGuideTV.setText("搜索应用");
            mPasswordGuideLayout.setBackgroundResource(R.drawable.shape_password_panel_header_search_mode);
            mSearchView.setBackgroundResource(R.drawable.bg_frame_search_dark);
            mSearchView.startAnimation(animationIn);
            mEditSearchTV.startAnimation(animationOut);
            mEditSearchTV.setVisibility(View.GONE);
        } else {
            if (mEditSearchTV.getVisibility() == View.GONE) {
                mEditSearchTV.setVisibility(View.VISIBLE);
            }
            mPasswordGuideTV.setText("加密应用");
            mPasswordGuideLayout.setBackgroundResource(R.drawable.shape_password_panel_header_applayout);
            mSearchView.setBackgroundResource(R.drawable.bg_frame_search);
            mSearchView.startAnimation(animationOut);
            mEditSearchTV.startAnimation(animationIn);
            mSearchView.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (hasSearched) {
                    animSearchLayout(false);
                    hasSearched = false;
                } else {
                    onBackPressed();
                }
                break;

        }
        return true;

    }

    /**
     * 进入设置页
     */
    public void gotoSetting(View view) {
        startActivity(new Intent(AppActivity.this, SettingActivity.class));
    }

    public void onBackArrowClicked(View view) {
        onBackPressed();
    }

    private class AppFragmentAdapter extends FragmentPagerAdapter {

        AppFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }

}
