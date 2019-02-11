package com.hm.animationdemo.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.hm.animationdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.animation.ObjectAnimator.ofFloat;

public class PropertyAnimationActivity extends AppCompatActivity {

    LinearInterpolator linearInterpolator;
    IntEvaluator intEvaluator;
    TimeInterpolator timeInterpolator;
    PropertyValuesHolder holder;

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.btn_listen_animation)
    Button btnAnimation;
    @BindView(R.id.btn_animation_set)
    Button btnAnimationSet;
    @BindView(R.id.btn_bg_animation)
    Button btnBgAnimation;
    @BindView(R.id.btn_animation_xml)
    Button btnAnimationXml;
    @BindView(R.id.btn_pro_animation)
    Button btnProAnimation;
    @BindView(R.id.btn_value_animation)
    Button btnValueAnimation;

    @BindView(R.id.btn_property_value_holder)
    Button btnPropertyValueHolder;

    public static void launch(Context context) {
        Intent starter = new Intent(context, PropertyAnimationActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_property_value_holder, R.id.btn_listen_animation, R.id.btn_animation_xml,
            R.id.btn_bg_animation, R.id.btn_animation_set, R.id.btn_pro_animation,
            R.id.btn_value_animation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_property_value_holder:
                PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1F, 1.5F,1F);
                PropertyValuesHolder valuesHolder2 = PropertyValuesHolder.ofFloat("rotationX", 0F, 90F, 0F);
                PropertyValuesHolder valuesHolder3 = PropertyValuesHolder.ofFloat("alpha", 1.0F, 0.3F, 1F);
                ObjectAnimator valuesHolder = ObjectAnimator.ofPropertyValuesHolder(btnPropertyValueHolder, valuesHolder1, valuesHolder2, valuesHolder3);
                valuesHolder.setDuration(2000);
                valuesHolder.start();
                break;
            case R.id.btn_animation_xml:
                AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.property_animation);
                animatorSet.setTarget(btnAnimationXml);
                animatorSet.start();
                break;
            case R.id.btn_bg_animation:
                final ValueAnimator colorAnimator = ObjectAnimator.ofInt(btnBgAnimation, "backgroundColor", 0xFFFF8080, 0xFF8080FF);
                colorAnimator.setDuration(5000);
                colorAnimator.setEvaluator(new ArgbEvaluator());
                colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
                colorAnimator.setRepeatCount(4);
                colorAnimator.start();
                break;
            case R.id.btn_animation_set:
               /* AnimatorSet set = new AnimatorSet();
                List<Animator> animatorList = new ArrayList<>();
                animatorList.add(ObjectAnimator.ofFloat(btnAnimationSet, "scaleX", 1, 1.5F));
                animatorList.add(ObjectAnimator.ofFloat(btnAnimationSet, "scaleY", 1, 0.5F));
                set.play(ObjectAnimator.ofFloat(btnAnimationSet, "scaleX", 1, 1.5F)).;
                set.setDuration(3000).start();*/
                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.ani_set);
                set.setTarget(btnAnimationSet);
                set.start();
                break;
            case R.id.btn_listen_animation:
                ObjectAnimator objectAnimator = ofFloat(btnAnimation, "scaleX", 1.5F);
                objectAnimator.setDuration(3000);
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Log.e(TAG, "onAnimationStart");
                    }
                });
                objectAnimator.start();
                break;
            case R.id.btn_pro_animation:
                Log.e(TAG, "btn_pro_animation");
                ViewWrapper viewWrapper = new ViewWrapper(btnProAnimation);
                ObjectAnimator animator = ObjectAnimator.ofInt(viewWrapper, "width", 800);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Log.e(TAG, "onAnimationStart");
                    }
                });
                animator.setDuration(3000);
                animator.start();
                break;
            case R.id.btn_value_animation:
                Log.e(TAG, "btn_pro_animation");
                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    IntEvaluator mEvaluator = new IntEvaluator();

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int currentValue = (int) animation.getAnimatedValue();
                        Log.e(TAG, "onAnimationUpdate currentValue = " + currentValue);
                        float fraction = animation.getAnimatedFraction();
                        btnValueAnimation.getLayoutParams().width = mEvaluator.evaluate(fraction, btnValueAnimation.getWidth(), 800);
                        btnValueAnimation.requestLayout();
                    }
                });
                valueAnimator.setDuration(3000);
                valueAnimator.start();
                break;
            default:
                break;
        }
    }

    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View mTarget) {
            this.mTarget = mTarget;
        }

        public int getWidth() {
            Log.e("PropertyAnima", "width" + mTarget.getWidth());
            return mTarget.getWidth();
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }
}
