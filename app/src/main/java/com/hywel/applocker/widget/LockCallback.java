package com.hywel.applocker.widget;

/**
 * 自定义手势密码锁回调接口
 */
public interface LockCallback {
    /**
     * 选取手势密码点长度不足
     */
    int POINT_LENGTH_SHORT = 0x01;
    /**
     * 两次输入手势密码不一致
     */
    int TWICE_NOT_SAME = 0x02;
    /**
     * 两次输入手势密一致
     */
    int TWICE_LINE_SAME = 0x03;
    /**
     * 第一条手势密码设置完成
     */
    int FIRST_LINE_OVER = 0x04;

    /**
     * 手势密码回调函数
     */
    void onLockCallback(int type);
}