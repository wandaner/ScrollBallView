package com.situ.com.myscrollview;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private BallView bv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bv = (BallView) findViewById(R.id.ballView);
        MyBallViewAdapter adapter = new MyBallViewAdapter(this,160);
        bv.setAdapter(adapter);
        Toast.makeText(this,""+bv.getChildCount(),Toast.LENGTH_LONG).show();
    }
}
