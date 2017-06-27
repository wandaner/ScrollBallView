package com.situ.com.myscrollview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by xukai on 2017/6/23.
 */

public class BallView extends ViewGroup {
    private static final String TAG = "xukai.ballView";
    private Context context;
    private int AllNum = 100;
    private int childSum;//实际控件数量（n行m列控件阵：childNum = (m)*(n)）为了确保移动不突兀



    private int n = 0;//n行(20是测试数据),默认+1；
    private int m = 0;//m列
    private int rowsSum = 20;
    private int columnSum;


    private int ballViewWidth = 0;
    private int ballViewHeight = 0;
    private int itemWidth = 0;



    private int itemHeight = 0;

    private int margin_row = 20;//横向间距
    private int margin_col = 10;//纵向间距
    private int padding = 10;//距边

    private GestureDetector detector;

    private BallViewAdapter adapter;
    public BallView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int bt_count = this.getChildCount();
        if(n!=0&&m!=0) {
            for (int index = 0; index < bt_count; index++) {
                int i = index / n;
                int j = index % n;
                View view = this.getChildAt(index);
                view.setVisibility(View.VISIBLE);
                view.layout(
                    padding + i * (view.getMeasuredWidth() + margin_row),
                    padding + j * (view.getMeasuredHeight() + margin_col),
                    padding + i * (view.getMeasuredWidth() + margin_row) + view.getMeasuredWidth(),
                    padding + j * (view.getMeasuredHeight() + margin_col) + view.getMeasuredHeight()
                );
            }
        }
    }
    private float mLastMotionX;
    private float mLastMotionY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //获取触摸事件在按钮上的位置(记录初始位置)
        float startX = (float) 0.0;
        float startY = (float) 0.0;
        //获取触摸事件在按钮上的位置
        final float x = ev.getX();
        final float y = ev.getY();
        //获取触摸事件在屏幕上的位置
        final int rawX = (int) ev.getRawX();
        final int rawY = (int) ev.getRawY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取 motionX & motionY
                mLastMotionX = x;
                mLastMotionY = y;
                // 记录点击事件点下的位置，用于判断点击或者滑动
                startX = x;
                startY = y;
                Log.e(TAG,"ACTION_DOWN-->:x"+x+"   y:"+y+"   isMoveAction:");
                break;
            case MotionEvent.ACTION_MOVE:
                // 单指触摸
                if (ev.getPointerCount() == 1) {
                    // Compute delta X.Y
                    int deltaX = 0;
                    int deltaY = 0;
                    deltaX = (int) (x - mLastMotionX);
                    deltaY = (int) (y - mLastMotionY);
                    mLastMotionX = x;
                    mLastMotionY = y;
                    //TODO 判断是否可以移动才可以移动，判断未加
                    ScrollView(deltaX,deltaY);
                }
                Log.e(TAG,"ACTION_MOVE-->:x"+x+"   y:"+y+"   isMoveAction:");
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    private void ScrollView(int deltaX,int deltaY){
        moveChildView(deltaX,deltaY);
    }
    private void moveChildView(int deltaX,int deltaY) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(child.getLeft() + deltaX, child.getTop()+deltaY,
                    child.getRight() + deltaX, child.getBottom()+deltaY);
        }
    }
    private View getClickItem(final int rawX, final int rawY) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            // 获取子控件的边界
            Rect rect = new Rect();
            child.getGlobalVisibleRect(rect);
            // 判断触摸点是否在边界内，如果在，则判断为此控件接受点击信息，如果不在，则返回继续
            if (rect.contains(rawX, rawY)) {
                return child;
            }
        }
        //如果未获取到，则返回null
        return null;
    }
    public void setAdapter(BallViewAdapter adpter){
        this.adapter = adpter;
        adapter.setBallView(this);
        adapter.setMaxContext(m*n);
//        adapter.setMaxRow();
        adapter.initView(0);
    }
    //getter & setter
    public int getBallViewWidth(){
        if(ballViewWidth ==0 ){
            ballViewWidth = Utils.GetScreenWidth(context);
        }
        return ballViewWidth;
    }
    public int getBallViewHeight(){
        if(ballViewHeight==0){
            ballViewHeight = Utils.GetScreenHeight(context);
        }
        return ballViewHeight;
    }
    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }
    public int getItemWidth() {
        return itemWidth;
    }

    public int getItemHeight() {
        return itemHeight;
    }
    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }
}
