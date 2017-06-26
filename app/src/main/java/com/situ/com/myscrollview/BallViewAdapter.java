package com.situ.com.myscrollview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by rrdev on 2017/6/25.
 */

public abstract class BallViewAdapter{
    private static final String TAG = "xukai.ballViewAdapter";
    public int num;//总计Num
    public Context mContext;
    public BallView ballView;
    public int bv_maxContext;//每页最大的实际button数=bv_realRow * bv_realCol
    public int bv_maxRow;//每一列最大的的虚拟行数
    public int bv_realRow;//ballView创建的实际行数
    public int bv_realCol;//ballView创建的实际列数
    public BallViewAdapter(Context context,int num){
        this.mContext = context;
        this.num = num;
    }
    public void initView(int index){
        if(num<=0){
            return;
        }
        ballView.removeAllViewsInLayout();
        int count = 0;
        for(int i = index;i<num;i++){
            if(count==bv_maxContext||i==num){
                break;
            }
            View view = getView(i);
//            computeItemSize(view);
            ballView.addView(view);
            count++;
        }

    }
    public void setMaxContext(int maxContext){
        this.bv_maxContext = maxContext;
    }
    public void setMaxRow(int maxRow){
        this.bv_maxRow = maxRow;
    }
    public void setBallView(BallView ballView){
        this.ballView = ballView;
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        this.ballView.measure(w,h);
        computeItemSize();
        setMN();
    }
    private void setMN(){
        int bv_width = ballView.getBallViewWidth();
        int bv_height = ballView.getBallViewHeight();
        int it_width = ballView.getItemWidth();
        int it_height = ballView.getItemHeight();
        bv_realRow = bv_height/it_height;
        bv_realCol = bv_width/it_width+2;
        ballView.setN(bv_realRow);
        ballView.setM(bv_realCol);
        Log.e(TAG,"0bv:"+bv_width+"*"+bv_height+"  it:"+it_width+"*"+it_height);
        Log.e(TAG,"n_m:"+bv_realCol+"*"+bv_realRow);
    }
    private void computeItemSize(){
        View view = getView(0);
        int bv_w = ballView.getBallViewWidth();
        int bv_h = ballView.getBallViewHeight();
        if(bv_w==0||bv_h==0){
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            ballView.setItemWidth(view.getMeasuredWidth());
            ballView.setItemHeight(view.getMeasuredHeight());
            Log.e(TAG,"1bv:"+bv_w+"*"+bv_h+"  it:"+view.getMeasuredWidth()+"*"+view.getMeasuredHeight());
        }else{
            if(ballView.getItemWidth()==0||ballView.getItemHeight()==0){
                view.measure(bv_w, bv_h);
                ballView.setItemWidth(view.getMeasuredWidth());
                ballView.setItemHeight(view.getMeasuredHeight());
                Log.e(TAG,"2bv:"+bv_w+"*"+bv_h+"  it:"+view.getMeasuredWidth()+"*"+view.getMeasuredHeight());
            }
        }
    }
    public abstract View getView(int t);
    public abstract void bindView(View child, int t);
}
