package com.situ.com.myscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by xukai on 2017/6/26.
 */

public class BallViewButton extends Button {
    private Context context;
    public BallViewButton(Context context) {
        super(context);
        init(context);
    }

    public BallViewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        this.context = context;
    }

    private float startX = (float) 0.0;
    private float startY = (float) 0.0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
//                setBackgroundResource(R.drawable.bg_item_press);
                Log.e("xukai.button","------------------>ACTION_DOWN:"+startX+"*"+startY);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("xukai.button","------------------>ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                float x = event.getRawX();
                float y = event.getRawY();
                Log.e("xukai.button","------------------->Up:"+x+"*"+y+"    "+startX+"*"+startY);
                float re = (float) (Math.pow((x-startX),2)+Math.pow((y-startY),2));
//                Log.e("xukai.button","------------------>"+re);
                if(re<10){
                    ClickCallBack();
                }
//                Log.e("xukai.button","------------------>ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }
    private void ClickCallBack(){
        if(this.getTag()!=null){
//            Log.e("xukai.button","---0---");
            Toast.makeText(context,this.getTag()+"",Toast.LENGTH_SHORT).show();
        }else {
//            Log.e("xukai.button","---1---");
        }
    }
}
