package com.hm.animationdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.hm.animationdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FrameAnimationActivity extends AppCompatActivity {

    @BindView(R.id.btn_frame_animation)
    Button btnFrameAnimation;

    public static void launch(Context context) {
        Intent starter = new Intent(context, FrameAnimationActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);
        ButterKnife.bind(this);
        btnFrameAnimation.setBackgroundResource(R.drawable.animation_drawable);
        AnimationDrawable drawable = (AnimationDrawable) btnFrameAnimation.getBackground();
        drawable.start();
    }
}
