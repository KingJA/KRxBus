package com.kingja.krxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

    }

    public void onPost(View view) {
        RxBus.getDefault().post(new EventMsg("来自星星的我"));
    }
}
