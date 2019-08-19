package com.hm.animationdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.hm.animationdemo.R;

public class PointViewActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent starter = new Intent(context, PointViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_view);
    }
}
