package com.hm.animationdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hm.animationdemo.R;
import com.hm.animationdemo.base.BaseActivity;

public class PointViewActivity extends BaseActivity {

    public static void launch(Activity context) {
        Intent starter = new Intent(context, PointViewActivity.class);
        //context.startActivity(starter, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_view);
    }
}
