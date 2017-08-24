package com.hm.animationdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.hm.animationdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_listen_animation)
    Button btnAnimation;

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

    @OnClick({R.id.btn_listen_animation, R.id.btn_view_animation, R.id.btn_property_animation, R.id.btn_launch_PointViewActivity})
    public void onClick(View view) {

       /* AlphaAnimation animation=new AlphaAnimation(0,1);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
       /* Rotate3dAnimation animation = new Rotate3dAnimation(0, 180, 0.5F, 1.0F, 0.5F, false);
        animation.setDuration(2000);
        */
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
            default:
                break;
        }

    }
}
