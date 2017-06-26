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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.bg_item_press);
                Log.e("xukai.button","------------------>ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("xukai.button","------------------>ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundResource(R.drawable.bg_item_normal);
                Log.e("xukai.button","------------------>ACTION_UP");
                break;
        }
        return true;
    }
}
