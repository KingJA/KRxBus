package com.kingja.krxbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import rx.Subscription;
import rx.functions.Action1;

public class SecondActivity extends AppCompatActivity {
    private String TAG=getClass().getSimpleName();
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
         mSubscription = RxBus.getDefault().register(EventMsg.class,SecondActivity.class, new Action1<EventMsg>() {
            @Override
            public void call(EventMsg eventMsg) {
                Log.e(TAG, "收到事件: " + eventMsg.getContent());
            }
        });
    }
    public void onThird(View view) {
        RxBus.getDefault().postSticky(new EventMsg("延迟消息"),ThirdActivity.class);
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }

    /**
     * 在销毁的时候注销，避免多次发送
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unsubscribe(SecondActivity.class);
    }
}
