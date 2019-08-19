package com.hm.animationdemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hm.animationdemo.activity.PointViewActivity;
import com.hm.animationdemo.activity.PropertyAnimationActivity;
import com.hm.animationdemo.activity.ViewAnimationActivity;

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
    }

    @OnClick({R.id.btn_listen_animation, R.id.btn_property_animation, R.id.btn_launch_PointViewActivity})
    public void onClick(View view) {
        //Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_animation);
       /* AlphaAnimation animation=new AlphaAnimation(0,1);
        animation.setDuration(2000);
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
        animation.setFillAfter(true);
        btnAnimation.startAnimation(animation);*/
        switch (view.getId()) {
            case R.id.btn_listen_animation:
                Intent starter = new Intent(this, ViewAnimationActivity.class);
                startActivity(starter);
                overridePendingTransition(R.anim.enter_ainm, R.anim.exit_ainm);
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
