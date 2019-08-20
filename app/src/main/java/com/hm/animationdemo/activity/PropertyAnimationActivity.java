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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hm.animationdemo.R;
import com.hm.animationdemo.anim.SwingAnimation;
import com.hm.animationdemo.evaluator.CharEvaluator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PropertyAnimationActivity extends AppCompatActivity {

    LinearInterpolator linearInterpolator;
    IntEvaluator intEvaluator;
    TimeInterpolator timeInterpolator;
    PropertyValuesHolder holder;

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.btn_listen_animation)
    TextView tvAnimation;
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

    @BindView(R.id.ivBell)
    ImageView ivBell;
    @BindView(R.id.btn_bell_shake_animation)
    Button btnBellShake;

    @BindView(R.id.btnChangeText)
    Button btnChangeText;

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
            R.id.btn_value_animation, R.id.btn_bell_shake_animation, R.id.btn_rotate_animation,
            R.id.btnChangeText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_property_value_holder:
                PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1F, 1.5F, 1F);
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
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tvAnimation, "scaleX", 1.0F, 2.0F);
                objectAnimator.setDuration(3000);
                /*objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Log.e(TAG, "onAnimationStart");
                    }
                });*/
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
            case R.id.btn_bell_shake_animation:
                Log.e(TAG, "btn_pro_animation");

                SwingAnimation swingAnimation = new SwingAnimation(
                        0f, 30f, -30f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.0f);
                swingAnimation.setDuration(800);     //动画持续时间
                swingAnimation.setFillAfter(false);  //是否保持动画结束画面
                //swingAnimation.setRepeatCount(2);
                swingAnimation.setStartOffset(500);   //动画播放延迟
                ivBell.startAnimation(swingAnimation);
                break;
            case R.id.btn_rotate_animation:
                //角度大于0，是顺时针旋转
                ObjectAnimator rotateObjectAnimator = ObjectAnimator.ofFloat(ivBell,
                        "rotation", 0F, 40, -40, 40, -40, 40, -40, 40, -40, 0F);
                rotateObjectAnimator.setDuration(1500L);
                //rotateObjectAnimator.setRepeatCount(1);

                //绕中心点旋转，默认
                /*ivBell.setPivotX(ivBell.getWidth() / 2.0F);
                ivBell.setPivotY(ivBell.getHeight() / 2.0F);*/


                /*//绕左上角旋转
                ivBell.setPivotX(0.0F);
                ivBell.setPivotY(0.0F);*/

                //绕水平中心点
                ivBell.setPivotY(0.0F);

                //绕竖直中心点
                //ivBell.setPivotX(0.0F);

                Log.d(TAG, "onClick: getPivotX=" + ivBell.getPivotX());
                Log.d(TAG, "onClick: getPivotY=" + ivBell.getPivotY());
                Log.d(TAG, "onClick: getLeft=" + ivBell.getLeft());
                Log.d(TAG, "onClick: getX=" + ivBell.getX());
                Log.d(TAG, "onClick: getWidth=" + ivBell.getWidth());
                rotateObjectAnimator.setInterpolator(new LinearInterpolator());
                rotateObjectAnimator.start();
                break;
            case R.id.btnChangeText:
                ValueAnimator animChangeText = ValueAnimator.ofObject(new CharEvaluator(),
                        'A', 'Z');
                animChangeText.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        char text = ((char) animation.getAnimatedValue());
                        btnChangeText.setText(String.valueOf(text));
                    }
                });
                animChangeText.setDuration(2000);
                animChangeText.start();
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
