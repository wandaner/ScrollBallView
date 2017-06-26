package com.situ.com.myscrollview;

import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * Created by rrdev on 2017/6/26.
 */

public class MyBallViewAdapter extends BallViewAdapter {
    public MyBallViewAdapter(Context context, int num) {
        super( context,num);
    }

    @Override
    public View getView(int t) {
        View view = View.inflate(mContext,R.layout.item_view,null);
        Button bt = (Button) view.findViewById(R.id.bt);
        bt.setText(""+(t+1));
        view.setTag(t);
        return view;
    }

    @Override
    public void bindView(View child, int t) {

    }
}
