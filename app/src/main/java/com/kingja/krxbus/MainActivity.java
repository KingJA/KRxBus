package com.kingja.krxbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private String TAG=getClass().getSimpleName();
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSubscription = RxBus.getDefault().register(String.class, MainActivity.class,new Action1<String>() {
            @Override
            public void call(String eventMsg) {
                Log.e(TAG, "收到事件: " + eventMsg);
            }
        });
    }
    public void onSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unsubscribe(SecondActivity.class);
    }
}
