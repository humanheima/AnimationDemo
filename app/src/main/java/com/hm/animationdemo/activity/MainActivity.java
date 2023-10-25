package com.hm.animationdemo.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import com.hm.animationdemo.R;
import com.hm.animationdemo.chapter5.SVGActivity;
import com.hm.animationdemo.chapter6.PaintActivity;
import com.hm.animationdemo.chapter7.BezierCurveActivity;

public class MainActivity extends AppCompatActivity {


    private Button btnActivityOptions;
    private ImageView ivScenery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(android.view.Window.FEATURE_CONTENT_TRANSITIONS);
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.trans_slide);
        getWindow().setExitTransition(transition);
        getWindow().setEnterTransition(transition);
        //getWindow().setAllowEnterTransitionOverlap(false);
        //getWindow().setAllowReturnTransitionOverlap(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnActivityOptions = findViewById(R.id.btn_activity_options_compat);
        ivScenery = findViewById(R.id.iv_scenery);
    }

    @RequiresApi(api = VERSION_CODES.M)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_frame_animation:
                testFrameAnimation();
                break;
            case R.id.btn_transition_anim:
                testTransitionAnimation();
                break;
            case R.id.btn_activity_options_compat:
                //testActivityOptions();
                testActivityOptions1();
                break;
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
            case R.id.btnBezierCurve:
                BezierCurveActivity.launch(this);
                break;
            default:
                break;
        }

    }

    private void testActivityOptions() {
        ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(this,
                R.anim.enter_ainm, R.anim.exit_ainm);
        ActivityOptionsAnimationActivity.launch(this, activityOptions.toBundle());
    }

    @RequiresApi(api = VERSION_CODES.M)
    private void testActivityOptions1() {
//        ActivityOptions activityOptions = ActivityOptions.makeClipRevealAnimation(ivScenery,
//                ivScenery.getWidth() / 2, ivScenery.getHeight() / 2, ScreenUtil.dpToPx(this, 100),
//                ScreenUtil.dpToPx(this, 100));

        // Note：感觉效果不是很明显
//        ActivityOptions activityOptions = ActivityOptions.makeClipRevealAnimation(ivScenery,
//                0, 0, ScreenUtil.dpToPx(this, 100),
//                ScreenUtil.dpToPx(this, 100));

        // Note：感觉这个效果还可以
//        ActivityOptions activityOptions = ActivityOptions.makeScaleUpAnimation(ivScenery,
//                ivScenery.getWidth() / 2, ivScenery.getHeight() / 2, ScreenUtil.dpToPx(this, 100),
//                ScreenUtil.dpToPx(this, 100));

        // Note：感觉效果不咋地
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scenery);
        ActivityOptions activityOptions = ActivityOptions.makeThumbnailScaleUpAnimation(ivScenery, bitmap,
                0, 0);

        ActivityOptionsAnimationActivity.launch(this, activityOptions.toBundle());
    }

    private void testTransitionAnimation() {
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
//        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, ivScenery,
//                "ivScenery");

        TransitionAnimActivity.launch(this, activityOptions.toBundle());

    }

    private void testFrameAnimation() {
        FrameAnimationActivity.Companion.launch(this);
    }

}
