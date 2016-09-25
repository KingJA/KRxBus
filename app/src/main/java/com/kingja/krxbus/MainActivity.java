package com.kingja.krxbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private String TAG=getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.getDefault().toObservable(Float.class).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof EventMsg) {
                    EventMsg msg= (EventMsg) o;
                    Log.e(TAG, "call: "+msg.getContent());
                }
            }
        });
    }
    public void onSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
