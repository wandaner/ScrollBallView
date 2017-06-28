package com.situ.com.myscrollview;

import android.content.Context;
import android.content.Intent;
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


    //为了滚动的三view参数(横向)
    private int mPreIndex;//最后一个控件的位置
    private int mCurrentIndex;//第一个控件的位置
    private View mCurrentView;
    private View mPreView;


    private int mPreIndex1;//最后一个控件的位置
    private int mCurrentIndex1;//第一个控件的位置
    private View mCurrentView1;
    private View mPreView1;
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
    private void initIndex(){
        mCurrentIndex = 0;//视图阵第一个元素的id
        mPreIndex = n*m-1;//视图阵最后一个元素的id
        mCurrentView = getChildAt(mCurrentIndex);
        mPreView = getChildAt(mPreIndex);

        mCurrentIndex1 = 0;//视图阵第一个元素的id
        mPreIndex1 = n*m-1;//视图阵最后一个元素的id
        mCurrentView1 = getChildAt(mCurrentIndex1);
        mPreView1 = getChildAt(mPreIndex1);
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
        //获取触摸事件在按钮上的位置
        final float x = ev.getX();
        final float y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 更新最 新的触摸事件想x,y
                mLastMotionX = x;
                mLastMotionY = y;
                // 记录点击事件点下的位置，用于判断点击或者滑动
//                Log.e(TAG,"ACTION_DOWN-->:x"+x+"   y:"+y+"   isMoveAction:");
                break;
            case MotionEvent.ACTION_MOVE:
                // 单指触摸
                if (ev.getPointerCount() == 1) {
                    // 计算偏移量
                    int deltaX = 0;
                    int deltaY = 0;
                    deltaX = (int) (x - mLastMotionX);
                    deltaY = (int) (y - mLastMotionY);
                    mLastMotionX = x;
                    mLastMotionY = y;
                    //TODO 判断是否可以移动才可以移动，判断未加
                    ScrollView(deltaX,deltaY);
                }
//                Log.e(TAG,"ACTION_MOVE-->:x"+x+"   y:"+y+"   isMoveAction:");
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //获取触摸事件在按钮上的位置
        final float x = ev.getX();
        final float y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 更新最 新的触摸事件想x,y
                mLastMotionX = x;
                mLastMotionY = y;
                // 记录点击事件点下的位置，用于判断点击或者滑动
//                Log.e(TAG,"ACTION_DOWN-->:x"+x+"   y:"+y+"   isMoveAction:");
                break;
            case MotionEvent.ACTION_MOVE:
                // 单指触摸
                if (ev.getPointerCount() == 1) {
                    // 计算偏移量
                    int deltaX = 0;
                    int deltaY = 0;
                    deltaX = (int) (x - mLastMotionX);
                    deltaY = (int) (y - mLastMotionY);
                    mLastMotionX = x;
                    mLastMotionY = y;
                    //TODO 判断是否可以移动才可以移动，判断未加
                    ScrollView(deltaX,deltaY);
                }
//                Log.e(TAG,"ACTION_MOVE-->:x"+x+"   y:"+y+"   isMoveAction:");
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void ScrollView(int deltaX, int deltaY){
        moveChildView(deltaX,deltaY);
        if(deltaY<0){
            moveUp();
        }else {
            moveDown();
        }
        if(deltaX<0){
            moveLeft();
        }else {
            moveRight();
        }

    }
    private void moveLeft(){
        if(mCurrentView.getRight()<0){
            int cur_col = mCurrentIndex/n;
            int pre_col = mPreIndex/n;
            int cur_col_first_id = cur_col*n;
            int pre_col_first_id = pre_col*n;
            for(int i = 0;i<n;i++) {
                View cur_view = getChildAt(i+cur_col_first_id);
                View pre_view = getChildAt(i+pre_col_first_id);
                cur_view.layout(
                        pre_view.getRight() + margin_row,
                        pre_view.getTop(),
                        pre_view.getRight() + margin_row + itemWidth,
                        pre_view.getBottom()
                );
            }
            mPreIndex = cur_col_first_id+n-1;
            mCurrentIndex = (mCurrentIndex+n)%(n*m);
            mCurrentView = getChildAt(mCurrentIndex);
            moveLeft();
        }
    }
    private void moveRight(){
        if(mCurrentView.getLeft()>margin_row){
            int cur_col = mCurrentIndex/n;
            int pre_col = mPreIndex/n;
            int cur_col_first_id = cur_col*n;
            int pre_col_first_id = pre_col*n;
            for(int i = 0;i<n;i++) {
                View cur_view = getChildAt(i+cur_col_first_id);
                View pre_view = getChildAt(i+pre_col_first_id);
                pre_view.layout(
                        cur_view.getLeft() - margin_row - itemWidth,
                        cur_view.getTop(),
                        cur_view.getLeft() - margin_row,
                        cur_view.getBottom()
                );
            }
            mCurrentIndex = pre_col_first_id;
            mPreIndex = (mPreIndex+n*m-n)%(n*m);
            mCurrentView = getChildAt(mCurrentIndex);
            moveRight();
        }
    }
    private void moveUp(){
        if(mCurrentView1.getBottom()<0){
            int cur_col = mCurrentIndex1/n;
            int pre_col = mPreIndex1/n;
            int cur_col_first_id = mCurrentIndex1%n;
            int pre_col_first_id = mPreIndex1%n;
            for(int i = 0;i<m;i++) {
                View cur_view = getChildAt(i*n+cur_col_first_id);
                View pre_view = getChildAt(i*n+pre_col_first_id);
                cur_view.layout(
                        pre_view.getLeft(),
                        pre_view.getBottom()+margin_col,
                        pre_view.getRight(),
                        pre_view.getBottom()+margin_col+itemHeight
                );
            }
            mPreIndex1 = cur_col_first_id+pre_col*n;
            mCurrentIndex1 = (cur_col_first_id+1)%n+cur_col*n;
            mCurrentView1 = getChildAt(mCurrentIndex1);
            moveUp();
        }
    }
    private void moveDown(){
        if(mCurrentView1.getTop()>margin_col){
            int cur_col = mCurrentIndex1/n;
            int pre_col = mPreIndex1/n;
            int cur_col_first_id = mCurrentIndex1%n;
            int pre_col_first_id = mPreIndex1%n;
            for(int i = 0;i<m;i++) {
                View cur_view = getChildAt(i*n+cur_col_first_id);
                View pre_view = getChildAt(i*n+pre_col_first_id);
                pre_view.layout(
                        cur_view.getLeft(),
                        cur_view.getTop()-margin_col-itemHeight,
                        cur_view.getRight(),
                        cur_view.getTop()-margin_col
                );
            }
            mCurrentIndex1 = pre_col_first_id + cur_col*n;
            mPreIndex1 = (pre_col_first_id+n-1)%n+pre_col*n;
            mCurrentView1 = getChildAt(mCurrentIndex1);
            moveDown();
        }
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
        initIndex();
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
