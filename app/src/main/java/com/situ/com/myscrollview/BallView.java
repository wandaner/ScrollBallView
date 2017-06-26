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

public class BallView extends ViewGroup implements GestureDetector.OnGestureListener {
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

    private void init(){
        detector = new GestureDetector(this);
//        initSize();
//        initView();
    }
    private void initView(){
        if(n==0||m==0){
            return;
        }
        int index = 0;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                View view = View.inflate(context,R.layout.item_view,null);
                Button bt = (Button) view.findViewById(R.id.bt);
                bt.setText(""+(index+1));
                this.addView(view);
                view.setTag(index++);
            }
        }
    }
    private void initSize(){
        if(ballViewWidth ==0|| ballViewHeight ==0){
            ballViewWidth = Utils.GetScreenWidth(context);
            ballViewHeight = Utils.GetScreenHeight(context);
        }
        if(itemWidth==0||itemHeight==0){
            View view = View.inflate(context, R.layout.item_view, null);
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);

            view.measure(w, h);
            itemWidth = view.getMeasuredWidth();
            itemHeight = view.getMeasuredHeight();
        }
        this.m = (ballViewWidth -padding)/(itemWidth+margin_row)+2;
        this.n = (ballViewHeight -padding)/(itemHeight+margin_col)+1;
        Log.e(TAG,"screen:"+ ballViewWidth +"*"+ ballViewHeight
                +"\r\nitem:"+itemWidth+"*"+itemHeight);
        Log.e(TAG,"n->"+n+"   m->"+m);
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
    private boolean isMoveAction = false;//false:不可移动；true:可以移动
    private float mLastMotionX;
    private float mLastMotionY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //获取触摸事件在按钮上的位置(记录初始位置)
        float startX = (float) 0.0;
        float startY = (float) 0.0;
        //获取触摸事件在按钮上的位置
        final float x = ev.getX();
        final float y = ev.getY();
        //获取触摸事件在屏幕上的位置
        final int rawX = (int) ev.getRawX();
        final int rawY = (int) ev.getRawY();
        Log.e(TAG,"-->:x"+x+"   y:"+y+"   isMoveAction:"+isMoveAction);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 重置isMoveAction
                isMoveAction = false;
                // 获取 motionX & motionY
                mLastMotionX = x;
                mLastMotionY = y;
                // 记录点击事件点下的位置，用于判断点击或者滑动
                startX = x;
                startY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // When action move set isMoveAction true.
                isMoveAction = true;
                // 单指触摸
                if (ev.getPointerCount() == 1) {
                    // Compute delta X.Y
                    int deltaX = 0;
                    int deltaY = 0;
                    deltaX = (int) (x - mLastMotionX);
                    deltaY = (int) (y - mLastMotionY);
                    mLastMotionX = x;
                    mLastMotionY = y;
                    if(!isMoveAction) {
                        if (Math.pow(x - startX, 2) + Math.pow(y - startY, 2) > 20) {
                            isMoveAction = true;
                        }
                    }else {
                        //TODO 判断是否可以移动才可以移动，判断未加
                    }

                }
                break;
            case MotionEvent.ACTION_UP:

                if (!isMoveAction) {
                    View view = getClickItem(rawX, rawY);
                    if (view != null) {
                        Toast.makeText(this.getContext(),view.getTag()+"",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

        return this.detector.onTouchEvent(ev);
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
    //GestureDetector 的接口实现
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
