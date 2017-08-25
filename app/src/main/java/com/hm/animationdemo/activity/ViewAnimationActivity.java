package com.hm.animationdemo.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.hm.animationdemo.R;
import com.hm.animationdemo.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewAnimationActivity extends BaseActivity {

    @BindView(R.id.btn_rotate)
    Button btnRotate;
    @BindView(R.id.btn_alpha)
    Button btnAlpha;
    @BindView(R.id.btn_scale)
    Button btnScale;
    @BindView(R.id.btn_translate)
    Button btnTranslate;
    @BindView(R.id.btn_animation_set)
    Button btnAnimationSet;
    private TranslateAnimation translateAnimation;
    private AlphaAnimation alphaAnimation;
    private ScaleAnimation scaleAnimation;
    private RotateAnimation rotateAnimation;
    private AnimationSet animationSet;

    private static final String TAG = "ViewAnimationActivity";

    public static void launch(Activity context) {
        Intent starter = new Intent(context, ViewAnimationActivity.class);
        context.startActivity(starter, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        ButterKnife.bind(this);
        translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.translate_animation);
        scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.scale_animation);
        alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.alpha_animation);
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        animationSet = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.set_animation);

    }

    @OnClick(R.id.btn_rotate)
    public void onBtnRotateClicked() {
        btnRotate.startAnimation(rotateAnimation);
    }

    @OnClick(R.id.btn_alpha)
    public void onBtnAlphaClicked() {
        btnAlpha.startAnimation(alphaAnimation);
    }

    @OnClick(R.id.btn_scale)
    public void onBtnScaleClicked() {
        btnScale.startAnimation(scaleAnimation);
    }

    @OnClick(R.id.btn_translate)
    public void onBtnTranslateClicked() {
        btnTranslate.startAnimation(translateAnimation);
    }

    @OnClick(R.id.btn_animation_set)
    public void onViewClicked() {
        btnAnimationSet.startAnimation(animationSet);
    }
}
