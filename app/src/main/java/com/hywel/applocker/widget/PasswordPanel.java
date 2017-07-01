package com.hywel.applocker.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hywel on 2017/6/30.
 * 密码图案面板
 */

public class PasswordPanel extends View {

    private static final int DOT_RADIUS = 20;

    private Paint mDotPaint;
    private Point[] points;

    public PasswordPanel(Context context) {
        this(context, null);
    }

    public PasswordPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setColor(Color.parseColor("#cccccc"));
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setStrokeJoin(Paint.Join.ROUND);
        mDotPaint.setStrokeCap(Paint.Cap.ROUND);

        Paint mSelDotPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelDotPoint.setColor(Color.parseColor("#00aadd"));
        mSelDotPoint.setStyle(Paint.Style.FILL);
        mSelDotPoint.setStrokeJoin(Paint.Join.ROUND);
        mSelDotPoint.setStrokeCap(Paint.Cap.ROUND);

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        Log.d("PasswordPanel", "width:" + width);
        Log.d("PasswordPanel", "height:" + height);
//        int dividerWidth = (width - 6 * DOT_RADIUS) / 4;
//        int dividerHeight = (height - 6 * DOT_RADIUS) / 4;
        int dividerWidth = width / 4;
        int dividerHeight = height / 4;

        //// TODO: 2017/6/30 算出9个点的坐标
        //从左到右，从上到下开始罗列
        Point point11 = new Point(dividerWidth, dividerHeight);
        Point point12 = new Point(dividerWidth * 2, dividerHeight);
        Point point13 = new Point(dividerWidth * 3, dividerHeight);

        Point point21 = new Point(dividerWidth, dividerHeight * 2);
        Point point22 = new Point(dividerWidth * 2, dividerHeight * 2);
        Point point23 = new Point(dividerWidth * 3, dividerHeight * 2);

        Point point31 = new Point(dividerWidth, dividerHeight * 3);
        Point point32 = new Point(dividerWidth * 2, dividerHeight * 3);
        Point point33 = new Point(dividerWidth * 3, dividerHeight * 3);

        points = new Point[]{
                point11, point12, point13,
                point21, point22, point23,
                point31, point32, point33};

        for (Point point : points) {
            drawRoundDot(canvas, point);
        }
    }

    private void drawRoundDot(Canvas canvas, Point point) {
        canvas.drawCircle(point.getX(), point.getY(), DOT_RADIUS, mDotPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                return true;


            case MotionEvent.ACTION_MOVE:
                handleActionMove();
                return true;


            case MotionEvent.ACTION_UP:
                handleActionUp();
                return true;

            case MotionEvent.ACTION_CANCEL:
                handleActionCancel();
                return true;
        }


        return false;

    }

    private void handleActionCancel() {

    }

    private void handleActionUp() {

    }

    private void handleActionMove() {

    }

    private void handleActionDown(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (x < points[0].getX() || y > points[points.length - 1].getY()) {
            return;
        }
        //// TODO: 2017/6/30 当手指点按的是某个 dot ，寻找方法以确定手指落下的位置是否与某个 dot 重合，并且给其增加感应
    }
}
