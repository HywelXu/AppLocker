package com.hywel.applocker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hywel.applocker.R;
import com.hywel.applocker.model.GestureLockInfo;
import com.hywel.applocker.widget.GestureLockView.GestureLockCallback;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by hywel on 2017/6/30.
 * 密码图案面板
 */

public class PasswordPanel extends ViewGroup {

    private Context mContext;
    /**
     * 布局宽度
     */
    private int laywidth;
    /**
     * 布局高度
     */
    private int layheight;
    List<GestureLockInfo> arrayGuestInfo;
    private Drawable drawableNormal;
    private Drawable drawableSelected;
    /**
     * 绘制轨迹路线的画笔
     */
    private Paint linePaint;
    /**
     * 是否是校验密码
     */
    private boolean isVerify;
    /**
     * 手势轨迹线条宽度
     */
    private int lineWidth;
    /**
     * 以选中点集合
     */
    private ArrayList<GestureLockInfo> arrayChoosed;
    /**
     * 当前手指触摸位置的X坐标
     */
    private float nowX;
    /**
     * 当前手指触摸位置的Y坐标
     */
    private float nowY;
    /**
     * 手指是否抬起
     */
    private boolean isTouchup;
    /**
     * 是否允许绘制轨迹线
     */
    private boolean isallowDrawLine = true;
    /**
     * 是否校验错误
     */
    private boolean isError;
    /**
     * 记录手势密码缓存
     */
    private StringBuilder haschoosed;
    /**
     * 手势密码回调接口
     */
    private GestureLockCallback xCallback;
    private Drawable drawableError;
    /**
     * 正常手势轨迹线条颜色
     */
    private int lineCorlorNormal;
    /**
     * 错误轨迹线条颜色
     */
    private int lineCorlorError;
    /**
     * 默认颜色
     */
    private int defaultColor = 0xFF0000;

    public PasswordPanel(Context context) {
        this(context, null);
    }

    public PasswordPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setWillNotDraw(false);
        setCustomAttributes(attrs);
        init();
        addChildMethod();
        arrayChoosed = new ArrayList<>();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        laywidth = r - l;
        layheight = b - t;
        float onewidth = laywidth / 3;
        float oneheight = layheight / 3;
        for (int i = 0; i < getChildCount(); i++) {
            ImageView mView = (ImageView) getChildAt(i);
            float w = mView.getMeasuredWidth();// 获取View的宽度
            float h = mView.getMeasuredHeight();// 获取View的高度
            int cum = i % 3;
            int row = i / 3;
            float centerX = onewidth * cum + onewidth / 2;
            float centerY = oneheight * row + oneheight / 2;
            float left = centerX - w / 2;
            float top = centerY - h / 2;
            mView.layout((int) left, (int) top, (int) (left + w),
                    (int) (top + h));

            GestureLockInfo info = new GestureLockInfo();
            info.setLeftX(left);
            info.setRightX(left + w);
            info.setTopY(top);
            info.setBottomY(top + h);
            info.setCenterX(centerX);
            info.setCenterY(centerY);
            info.setPosition(i);
            info.setImageView(mView);
            info.setState(GestureLockInfo.IMAGE_NORMAL);
            arrayGuestInfo.add(info);
        }
    }

    /**
     * 初始化一些属性值的方法
     */
    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs,
                R.styleable.GestureLock);
        drawableNormal = a.getDrawable(R.styleable.GestureLock_drawablenormal);
        drawableSelected = a
                .getDrawable(R.styleable.GestureLock_drawableselected);
        drawableError = a.getDrawable(R.styleable.GestureLock_drawableeerror);
        lineCorlorNormal = a.getColor(R.styleable.GestureLock_linecolornormal,
                defaultColor);
        lineCorlorError = a.getColor(R.styleable.GestureLock_linecolorerror,
                defaultColor);
        lineWidth = a.getDimensionPixelSize(R.styleable.GestureLock_linewidth,
                5);
    }

    /**
     * 添加子View的方法
     */
    private void addChildMethod() {
        for (int i = 0; i < 9; i++) {
            ImageView image = new ImageView(mContext);
            image.setImageDrawable(drawableNormal);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            this.addView(image);
        }
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);// 设置非填充
        linePaint.setStrokeWidth(lineWidth);
    }

    /**
     * 获取当前选中点的方法
     *
     * @param pressX
     * @param pressY
     * @return 得到的封装点击信息的对象
     */
    private GestureLockInfo getGuestLockMethod(float pressX, float pressY) {
        for (GestureLockInfo info : arrayGuestInfo) {
            if (!(info.getLeftX() <= pressX && pressX <= info.getRightX())) {
                continue;
            }
            if (!(info.getTopY() <= pressY && pressY <= info.getBottomY())) {
                continue;
            }
            return info;
        }
        return null;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取测量值和模式
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
        arrayGuestInfo = new ArrayList<>();

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            int widthSpec = 0;
            int heightSpec = 0;
            LayoutParams params = v.getLayoutParams();
            if (params.width > 0) {
                widthSpec = MeasureSpec.makeMeasureSpec(params.width,
                        MeasureSpec.EXACTLY);
            } else if (params.width == -1) {
                widthSpec = MeasureSpec.makeMeasureSpec(widthSize,
                        MeasureSpec.EXACTLY);
            } else if (params.width == -2) {
                widthSpec = MeasureSpec.makeMeasureSpec(widthSize,
                        MeasureSpec.AT_MOST);
            }
            if (params.height > 0) {
                heightSpec = MeasureSpec.makeMeasureSpec(params.height,
                        MeasureSpec.EXACTLY);
            } else if (params.height == -1) {
                heightSpec = MeasureSpec.makeMeasureSpec(heightSize,
                        MeasureSpec.EXACTLY);
            } else if (params.height == -2) {
                heightSpec = MeasureSpec.makeMeasureSpec(heightSize,
                        MeasureSpec.AT_MOST);
            }
            v.measure(widthSpec, heightSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
//        Log.d("PasswordPanel", "width:" + width);
//        Log.d("PasswordPanel", "height:" + height);

        if (isVerify && isError) {// 绘制错误提示轨迹
            drawaLineMethod(canvas, null, true);
            for (GestureLockInfo info : arrayChoosed) {
                info.getImageView().setImageDrawable(drawableError);
            }
            isallowDrawLine = false;
            myHandler.sendEmptyMessageDelayed(0, 1000);
        } else {
            if (isTouchup) {
                if (arrayChoosed.size() < 4) {
                    if (arrayChoosed.size() > 0) {
                        if (xCallback != null) {
                            xCallback
                                    .onLockCallback(GestureLockCallback.POINT_LENGTH_SHORT);
                        }
                    }
                    clearMethod();
                } else {
                    if (!isVerify) {
                        haschoosed = new StringBuilder();
                        for (GestureLockInfo xinfo : arrayChoosed) {
                            haschoosed.append(xinfo.getPosition());
                            isVerify = true;
                        }
                        clearMethod();
                        if (xCallback != null) {
                            xCallback
                                    .onLockCallback(GestureLockCallback.FIRST_LINE_OVER);
                        }
                    } else {
                        StringBuilder verifychoosed = new StringBuilder();
                        for (GestureLockInfo xinfo : arrayChoosed) {
                            verifychoosed.append(xinfo.getPosition());
                        }
                        if (haschoosed.toString().equals(
                                verifychoosed.toString())) {
                            if (xCallback != null) {
                                clearMethod();
                                xCallback
                                        .onLockCallback(GestureLockCallback.TWICE_LINE_SAME);
                            }
                        } else {
                            if (xCallback != null) {
                                xCallback
                                        .onLockCallback(GestureLockCallback.TWICE_NOT_SAME);
                                isError = true;
                                postInvalidate();
                            }
                        }
                    }
                }
            } else {
                drawaLineMethod(canvas, new float[]{nowX, nowY}, false);
            }
        }

    }

    /**
     * 绘制轨迹线条的方法
     *
     * @param canvas 画布
     * @param nowpts 手指当前按下位置的坐标
     */
    private void drawaLineMethod(Canvas canvas, float[] nowpts, boolean isError) {
        if (isError) {
            linePaint.setColor(lineCorlorError);
        } else {
            linePaint.setColor(lineCorlorNormal);
        }
        if (arrayChoosed != null && arrayChoosed.size() > 0) {
            float[] pts = new float[nowpts != null ? (arrayChoosed.size() + 1) * 4
                    : arrayChoosed.size() * 4];
            pts[0] = arrayChoosed.get(0).getCenterX();
            pts[1] = arrayChoosed.get(0).getCenterY();
            for (int i = 0; i < arrayChoosed.size(); i++) {
                if (i < arrayChoosed.size() - 1) {
                    pts[i * 4 + 2] = arrayChoosed.get(i).getCenterX();
                    pts[i * 4 + 3] = arrayChoosed.get(i).getCenterY();
                    pts[i * 4 + 4] = arrayChoosed.get(i).getCenterX();
                    pts[i * 4 + 5] = arrayChoosed.get(i).getCenterY();
                } else {
                    pts[i * 4 + 2] = arrayChoosed.get(i).getCenterX();
                    pts[i * 4 + 3] = arrayChoosed.get(i).getCenterY();
                    if (nowpts != null) {
                        pts[i * 4 + 4] = arrayChoosed.get(i).getCenterX();
                        pts[i * 4 + 5] = arrayChoosed.get(i).getCenterY();
                    }
                }
            }
            if (nowpts != null) {
                pts[pts.length - 2] = nowpts[0];
                pts[pts.length - 1] = nowpts[1];
            }
            canvas.drawLines(pts, linePaint);
        }
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    isallowDrawLine = true;
                    clearMethod();
                    postInvalidate();
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isallowDrawLine) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleActionDown(event);
                    break;

                case MotionEvent.ACTION_MOVE:
                    handleActionMove(event);
                    break;

                case MotionEvent.ACTION_UP:
                    handleActionUp();
                    break;

                default:
                    break;
            }
        }
        return true;
    }


    private void handleActionUp() {
        isTouchup = true;
        postInvalidate();
    }

    private void handleActionMove(MotionEvent event) {
        nowX = event.getX();
        nowY = event.getY();
        GestureLockInfo info = getGuestLockMethod(nowX, nowY);
        if (info != null && !isPointChooseMethod(info)) {
            if (arrayChoosed.size() > 0) {
                GestureLockInfo info3 = getBetweenPoint(
                        arrayChoosed.get(arrayChoosed.size() - 1),
                        info);
                if (info3 != null && !isPointChooseMethod(info3)) {// 判断中间点并选中
                    info3.getImageView().setImageDrawable(
                            drawableSelected);
                    arrayChoosed.add(info3);
                }
            }
            info.getImageView().setImageDrawable(drawableSelected);
            arrayChoosed.add(info);
        }
        postInvalidate();

    }

    private void handleActionDown(MotionEvent event) {
        clearMethod();
        isTouchup = false;
        nowX = event.getX();
        nowY = event.getY();

        GestureLockInfo info = getGuestLockMethod(x, y);
        if (info != null) {
            info.getImageView().setImageDrawable(getResources().getDrawable(R.drawable.selected_dot));
            arrayChoosed.add(info);
        }
        postInvalidate();
    }

    /**
     * 获取当前点是否已经选择过的方法
     *
     * @param getinfo 手势图标点集对象
     * @return 已经选择过 true 没有选择过 false
     */
    private boolean isPointChooseMethod(GestureLockInfo getinfo) {
        for (GestureLockInfo info : arrayChoosed) {
            if (info.getCenterX() == getinfo.getCenterX()
                    && info.getCenterY() == getinfo.getCenterY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取中间点的方法
     *
     * @param startpoint 起始点
     * @param endpoint   结束点
     * @return 得到的中间点
     */
    private GestureLockInfo getBetweenPoint(GestureLockInfo startpoint,
                                            GestureLockInfo endpoint) {
        GestureLockInfo point = null;
        int betweennum = -1;
        switch (startpoint.getPosition()) {
            case 0:
                switch (endpoint.getPosition()) {
                    case 2:
                        betweennum = 1;
                        break;
                    case 6:
                        betweennum = 3;
                        break;
                    case 8:
                        betweennum = 4;
                        break;

                    default:
                        break;
                }
                break;
            case 1:
                switch (endpoint.getPosition()) {
                    case 7:
                        betweennum = 4;
                        break;

                    default:
                        break;
                }
                break;
            case 2:
                switch (endpoint.getPosition()) {
                    case 0:
                        betweennum = 1;
                        break;
                    case 6:
                        betweennum = 4;
                        break;
                    case 8:
                        betweennum = 5;
                        break;

                    default:
                        break;
                }
                break;
            case 3:
                switch (endpoint.getPosition()) {
                    case 5:
                        betweennum = 4;
                        break;

                    default:
                        break;
                }
                break;
            case 5:
                switch (endpoint.getPosition()) {
                    case 3:
                        betweennum = 4;
                        break;

                    default:
                        break;
                }
                break;
            case 6:
                switch (endpoint.getPosition()) {
                    case 0:
                        betweennum = 3;
                        break;
                    case 2:
                        betweennum = 4;
                        break;
                    case 8:
                        betweennum = 7;
                        break;

                    default:
                        break;
                }
                break;
            case 7:
                switch (endpoint.getPosition()) {
                    case 1:
                        betweennum = 4;
                        break;

                    default:
                        break;
                }
                break;
            case 8:
                switch (endpoint.getPosition()) {
                    case 2:
                        betweennum = 5;
                        break;
                    case 0:
                        betweennum = 4;
                        break;
                    case 6:
                        betweennum = 7;
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }
        if (betweennum != -1) {
            for (GestureLockInfo info : arrayGuestInfo) {
                if (info.getPosition() == betweennum) {
                    point = info;
                    break;
                }
            }
        }
        return point;
    }

    /**
     * 清空绘制状态的方法
     */
    public void clearMethod() {
        isError = false;
        arrayChoosed.clear();
        for (GestureLockInfo info : arrayGuestInfo) {
            info.getImageView().setImageDrawable(drawableNormal);
        }
    }

    /**
     * 获取是否是手势密码验证
     */
    public boolean isVerify() {
        return isVerify;
    }

    /**
     * 设置是否是手势密码验证
     */
    public void setVerify(boolean isVerify) {
        this.isVerify = isVerify;
    }

    /**
     * 获取手势密码缓存
     */
    public StringBuilder getHaschoosed() {
        return haschoosed;
    }

    /**
     * 设置手势密码缓存
     */
    public void setHaschoosed(StringBuilder haschoosed) {
        this.haschoosed = haschoosed;
    }

    /**
     * 获取手势密码回调接口
     */
    public GestureLockCallback getxCallback() {
        return xCallback;
    }

    /**
     * 设置手势密码回调接口
     */
    public void setxCallback(GestureLockCallback xCallback) {
        this.xCallback = xCallback;
    }

}
