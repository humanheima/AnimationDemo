package com.hm.animationdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hm.animationdemo.R;
import com.hm.animationdemo.base.BaseActivity;

public class SharedComponentActivity extends BaseActivity {


    public static void launch(Context context) {
        Intent starter = new Intent(context, SharedComponentActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_component);
    }
}
