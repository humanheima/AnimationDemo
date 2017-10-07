package com.hm.animationdemo.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.hm.animationdemo.R;
import com.hm.animationdemo.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_listen_animation)
    Button btnAnimation;

    @BindView(R.id.btn_launch_SharedComponentActivity)
    Button btnLaunchSharedComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_animation);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        btnAnimation.startAnimation(animation);
    }

    @OnClick({R.id.btn_listen_animation, R.id.btn_view_animation, R.id.btn_property_animation,
            R.id.btn_launch_PointViewActivity, R.id.btn_launch_SharedComponentActivity, R.id.btn_launch_FrameAnimationActivity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_listen_animation:
                LayoutAnimationActivity.launch(this);
                break;
            case R.id.btn_view_animation:
                ViewAnimationActivity.launch(this);
                break;
            case R.id.btn_property_animation:
                PropertyAnimationActivity.launch(this);
                break;
            case R.id.btn_launch_PointViewActivity:
                PointViewActivity.launch(this);
                break;
            case R.id.btn_launch_SharedComponentActivity:
                Intent starter = new Intent(this, SharedComponentActivity.class);
                startActivity(starter, ActivityOptions.makeSceneTransitionAnimation(
                        this, new Pair<View, String>(btnLaunchSharedComponent, "share_name")).toBundle());
                break;
            case R.id.btn_launch_FrameAnimationActivity:
                FrameAnimationActivity.launch(this);
                break;
            default:
                break;
        }

    }
}
