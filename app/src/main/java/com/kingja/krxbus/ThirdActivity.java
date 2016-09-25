package com.kingja.krxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import rx.Observable;
import rx.functions.Action1;

public class ThirdActivity extends AppCompatActivity {
    private String TAG=getClass().getSimpleName();
    private Observable<EventMsg> mObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        mObservable = RxBus.getDefault().registerSticky(EventMsg.class, ThirdActivity.class);
        mObservable.subscribe(new Action1<EventMsg>() {
            @Override
            public void call(EventMsg eventMsg) {
                Log.e(TAG, "收到事件: " + eventMsg.getContent());
            }
        });

    }

    public void onPost(View view) {
        RxBus.getDefault().post(new EventMsg("来自星星的我"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
