package com.hm.animationdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hm.animationdemo.R;
import com.hm.animationdemo.chapter5.SVGActivity;
import com.hm.animationdemo.chapter6.PaintActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_listen_animation:
                Intent starter = new Intent(this, ViewAnimationActivity.class);
                startActivity(starter);
                overridePendingTransition(R.anim.enter_ainm, R.anim.exit_ainm);
                break;
            case R.id.btn_property_animation:
                PropertyAnimatorEntranceActivity.launch(this);
                break;
            case R.id.btnLayoutTransition:
                LayoutAnimationActivity.Companion.launch(this);
                break;
            case R.id.btnSvg:
                SVGActivity.launch(this);
                break;
            case R.id.btnPaint:
                PaintActivity.launch(this);
                break;
            default:
                break;
        }

    }
}
