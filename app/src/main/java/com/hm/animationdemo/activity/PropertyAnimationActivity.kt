package com.hm.animationdemo.activity

import android.animation.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.hm.animationdemo.R
import com.hm.animationdemo.anim.SwingAnimation
import com.hm.animationdemo.evaluator.CharEvaluator
import kotlinx.android.synthetic.main.activity_property_animation.*

class PropertyAnimationActivity : AppCompatActivity() {

    internal var linearInterpolator: LinearInterpolator? = null
    internal var intEvaluator: IntEvaluator? = null
    internal var timeInterpolator: TimeInterpolator? = null
    internal var holder: PropertyValuesHolder? = null

    private val TAG = javaClass.simpleName


    private var animatorSet: AnimatorSet? = null

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, PropertyAnimationActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_animation)
    }


    fun onClick(view: View) {
        when (view.id) {
            R.id.btnTransAnimator -> {
                val infiniteAnimator = ObjectAnimator.ofFloat(ll_translate_layout, "translationY", 0f, 100f)
                infiniteAnimator.duration = 3000
                infiniteAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                    }
                })
                infiniteAnimator.start()
            }
            R.id.btnRedPacketAnimator -> {
                //角度大于0，是顺时针旋转
                val rotateObjectAnimator = ObjectAnimator.ofFloat(ivRedPacket, "rotation", 0f, 10f, -10f, 10f, -10f, 10f)
                //val rotateObjectAnimator = ObjectAnimator.ofFloat(ivRedPacket, "rotation", 0f, 360f)
                rotateObjectAnimator.duration = 1000L
                //rotateObjectAnimator.setRepeatCount(1);

                //绕中心点旋转，默认
                /*ivBell.setPivotX(ivBell.getWidth() / 2.0F);
                ivBell.setPivotY(ivBell.getHeight() / 2.0F);*/


                /*//绕左上角旋转
                ivBell.setPivotX(0.0F);
                ivBell.setPivotY(0.0F);*/

                //绕水平中心点
                ivRedPacket.pivotY = ivRedPacket.height.toFloat()
                ivRedPacket.pivotX = ivRedPacket.width / 2f

                //绕竖直中心点
                //ivBell.setPivotX(0.0F);

                Log.d(TAG, "onClick: getPivotX=" + ivRedPacket.pivotX)
                Log.d(TAG, "onClick: getPivotY=" + ivRedPacket.pivotY)
                Log.d(TAG, "onClick: getLeft=" + ivRedPacket.left)
                Log.d(TAG, "onClick: getX=" + ivRedPacket.x)
                Log.d(TAG, "onClick: getWidth=" + ivRedPacket.width)

                //rotateObjectAnimator.repeatCount = ValueAnimator.INFINITE
                //rotateObjectAnimator.repeatMode = ValueAnimator.RESTART

                //rotatePauseAnimator.repeatCount = ValueAnimator.INFINITE
                //rotatePauseAnimator.repeatMode = ValueAnimator.RESTART

                rotateObjectAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        /* ivRedPacket.postDelayed({
                             rotateObjectAnimator.start()

                         },500)*/
                    }
                })

                rotateObjectAnimator.start()
            }

            R.id.btnInfinite -> {
                val infiniteAnimator = ObjectAnimator.ofFloat(btnInfinite, "translationY", 0f, 100f)
                infiniteAnimator.repeatCount = ValueAnimator.INFINITE

                infiniteAnimator.repeatMode = ValueAnimator.REVERSE
                infiniteAnimator.duration = 2000
                infiniteAnimator.start()
            }
            R.id.btnTranslateInfinite -> {
                val mLeftAnimator = ObjectAnimator.ofFloat(btnTranslateInfinite, "translationX", 0f, -100f, 100f, 0f)
                //val mRightAnimator = ObjectAnimator.ofFloat(btnTranslateInfinite, "translationX", -100f, 100f)
                //val mBackAnimator = ObjectAnimator.ofFloat(btnTranslateInfinite, "translationX", 100f, 0f)


                //mRightAnimator.repeatCount = ValueAnimator.INFINITE
                //mRightAnimator.repeatMode = ValueAnimator.RESTART

                //mBackAnimator.repeatCount = ValueAnimator.INFINITE
                //mBackAnimator.repeatMode = ValueAnimator.RESTART

                //val animatorSet = AnimatorSet()
                //animatorSet.duration = 3000
                //animatorSet.startDelay = 300

                //animatorSet.playSequentially(mLeftAnimator, mRightAnimator, mBackAnimator)

                mLeftAnimator.duration = 3000
                mLeftAnimator.interpolator = LinearInterpolator()
                mLeftAnimator.repeatCount = ValueAnimator.INFINITE
                mLeftAnimator.repeatMode = ValueAnimator.RESTART
                mLeftAnimator.start()
            }
            R.id.btnKeyFrameWithInterpolator -> {
                val keyFrame0 = Keyframe.ofFloat(0f, 0f)
                val keyFrame1 = Keyframe.ofFloat(0.5f, 100f)
                val keyFrame2 = Keyframe.ofFloat(1f)
                keyFrame2.value = 0f

                //从keyFrame1过渡到keyFrame2会使用这个差值器
                keyFrame2.interpolator = BounceInterpolator()

                val valuesHolder = PropertyValuesHolder.ofKeyframe("rotation",
                        keyFrame0, keyFrame1, keyFrame2)
                val animator = ObjectAnimator.ofPropertyValuesHolder(ivPhone, valuesHolder)
                animator.duration = 3000
                animator.start()
            }

            R.id.btnKeyFrame -> {
                val keyFrame0 = Keyframe.ofFloat(0f, 0f)
                val keyFrame1 = Keyframe.ofFloat(0.1f, -20f)
                val keyFrame2 = Keyframe.ofFloat(0.2f, 20f)
                val keyFrame3 = Keyframe.ofFloat(0.3f, -20f)
                val keyFrame4 = Keyframe.ofFloat(0.4f, 20f)
                val keyFrame5 = Keyframe.ofFloat(0.5f, -20f)
                val keyFrame6 = Keyframe.ofFloat(0.6f, 20f)
                val keyFrame7 = Keyframe.ofFloat(0.7f, -20f)
                val keyFrame8 = Keyframe.ofFloat(0.8f, 20f)
                val keyFrame9 = Keyframe.ofFloat(0.9f, -0f)
                val keyFrame10 = Keyframe.ofFloat(1f, 0f)

                val valuesHolder = PropertyValuesHolder.ofKeyframe("rotation",
                        keyFrame0, keyFrame1, keyFrame2, keyFrame3, keyFrame4,
                        keyFrame5, keyFrame6, keyFrame7, keyFrame8, keyFrame9, keyFrame10
                )
                val animator = ObjectAnimator.ofPropertyValuesHolder(ivPhone, valuesHolder)
                animator.duration = 2000
                animator.start()
            }
            R.id.btnKeyFrameChangeManyProperty -> {
                val keyFrame0 = Keyframe.ofFloat(0f, 0f)
                val keyFrame1 = Keyframe.ofFloat(0.1f, -20f)
                val keyFrame2 = Keyframe.ofFloat(0.2f, 20f)
                val keyFrame3 = Keyframe.ofFloat(0.3f, -20f)
                val keyFrame4 = Keyframe.ofFloat(0.4f, 20f)
                val keyFrame5 = Keyframe.ofFloat(0.5f, -20f)
                val keyFrame6 = Keyframe.ofFloat(0.6f, 20f)
                val keyFrame7 = Keyframe.ofFloat(0.7f, -20f)
                val keyFrame8 = Keyframe.ofFloat(0.8f, 20f)
                val keyFrame9 = Keyframe.ofFloat(0.9f, -0f)
                val keyFrame10 = Keyframe.ofFloat(1f, 0f)

                //改变旋转属性
                val valuesHolder = PropertyValuesHolder.ofKeyframe("rotation",
                        keyFrame0, keyFrame1, keyFrame2, keyFrame3, keyFrame4,
                        keyFrame5, keyFrame6, keyFrame7, keyFrame8, keyFrame9, keyFrame10
                )

                val scaleXFrame0 = Keyframe.ofFloat(0f, 1f)
                val scaleXFrame1 = Keyframe.ofFloat(0.1f, 1.1f)
                val scaleXFrame2 = Keyframe.ofFloat(0.9f, 1.1f)
                val scaleXFrame3 = Keyframe.ofFloat(1f, 1f)

                val scaleXHolder = PropertyValuesHolder.ofKeyframe("scaleX",
                        scaleXFrame0,
                        scaleXFrame1,
                        scaleXFrame2,
                        scaleXFrame3)

                val scaleYFrame0 = Keyframe.ofFloat(0f, 1f)
                val scaleYFrame1 = Keyframe.ofFloat(0.1f, 1.1f)
                val scaleYFrame2 = Keyframe.ofFloat(0.9f, 1.1f)
                val scaleYFrame3 = Keyframe.ofFloat(1f, 1f)

                val scaleYHolder = PropertyValuesHolder.ofKeyframe("scaleY",
                        scaleYFrame0,
                        scaleYFrame1,
                        scaleYFrame2,
                        scaleYFrame3)

                val animator = ObjectAnimator.ofPropertyValuesHolder(ivPhone,
                        valuesHolder, scaleXHolder, scaleYHolder)
                animator.duration = 2000
                animator.start()
            }

            R.id.btn_property_value_holder -> {
                val valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.5f, 1f)
                val valuesHolder2 = PropertyValuesHolder.ofFloat("rotation", 0f, 90f, 0f)
                val valuesHolder3 = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.3f, 1f)
                val valuesHolder = ObjectAnimator.ofPropertyValuesHolder(btn_property_value_holder, valuesHolder1, valuesHolder2, valuesHolder3)
                valuesHolder.duration = 2000
                valuesHolder.start()
            }
            R.id.btnPropertyValueHolderOfObject -> {
                Log.d(TAG, "onClick: btnPropertyValueHolderOfObject")
                val valuesHolder1 = PropertyValuesHolder.ofObject("charText",
                        CharEvaluator(), 'A', 'Z')
                val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(btnPropertyValueHolderOfObject, valuesHolder1)
                objectAnimator.duration = 2000
                objectAnimator.start()
            }
            R.id.btn_animation_xml -> {
                //获取AnimatorSet
                /*val animatorSet = AnimatorInflater.loadAnimator(this, R.animator.property_animation) as AnimatorSet
                animatorSet.setTarget(btn_animation_xml)
                animatorSet.start()*/

                //获取ValueAnimator
                val valueAnimator = AnimatorInflater.loadAnimator(this, R.animator.value_animator_first) as ValueAnimator

                valueAnimator.addUpdateListener { animation ->
                    Log.d(TAG, "animatedValue:${animation.animatedValue}")
                }
                valueAnimator.start()
            }

            R.id.btn_bg_animation -> {
                val colorAnimator = ObjectAnimator.ofInt(btn_bg_animation, "backgroundColor", -0x7f80, -0x7f7f01)
                colorAnimator.duration = 5000
                colorAnimator.setEvaluator(ArgbEvaluator())
                colorAnimator.repeatMode = ValueAnimator.REVERSE
                colorAnimator.repeatCount = 4
                colorAnimator.start()
            }
            R.id.btn_animation_set -> {
                /* AnimatorSet set = new AnimatorSet();
                List<Animator> animatorList = new ArrayList<>();
                animatorList.add(ObjectAnimator.ofFloat(btnAnimationSet, "scaleX", 1, 1.5F));
                animatorList.add(ObjectAnimator.ofFloat(btnAnimationSet, "scaleY", 1, 0.5F));
                set.play(ObjectAnimator.ofFloat(btnAnimationSet, "scaleX", 1, 1.5F)).;
                set.setDuration(3000).start();*/
                val set = AnimatorInflater.loadAnimator(this, R.animator.ani_set) as AnimatorSet
                set.setTarget(btn_animation_set)
                set.start()
            }
            R.id.tv_listen_animation -> {
                val objectAnimator = ObjectAnimator.ofFloat(tv_listen_animation, "scaleX", 1.0f, 2.0f)
                objectAnimator.duration = 3000
                /*objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Log.e(TAG, "onAnimationStart");
                    }
                });*/
                objectAnimator.start()
            }
            R.id.btn_pro_animation -> {

                Log.e(TAG, "btn_pro_animation")
                btn_pro_animation?.let {
                    //注意：可以直接利用TextView的 getWidth和stWidth方法即可，不需要使用ViewWrapper
                    val viewWrapper = ViewWrapper(it)
                    val animator = ObjectAnimator.ofInt(viewWrapper, "width", 800)
                    animator.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationCancel(animation: Animator) {
                            Log.e(TAG, "onAnimationStart")
                        }
                    })
                    animator.duration = 3000
                    animator.start()
                }
            }
            R.id.btn_value_animation -> {
                Log.e(TAG, "btn_pro_animation")
                val valueAnimator = ValueAnimator.ofInt(0, 100)
                valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {

                    var mEvaluator = IntEvaluator()

                    override fun onAnimationUpdate(animation: ValueAnimator) {
                        val currentValue = animation.animatedValue as Int
                        Log.e(TAG, "onAnimationUpdate currentValue = $currentValue")
                        val fraction = animation.animatedFraction
                        btn_value_animation.layoutParams.width = mEvaluator.evaluate(fraction, btn_value_animation.width, 800)
                        btn_value_animation.requestLayout()
                    }
                })
                valueAnimator.duration = 3000
                valueAnimator.start()
            }
            R.id.btn_bell_shake_animation -> {
                Log.e(TAG, "btn_pro_animation")

                val swingAnimation = SwingAnimation(
                        0f, 30f, -30f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.0f)
                swingAnimation.duration = 800     //动画持续时间
                swingAnimation.fillAfter = false  //是否保持动画结束画面
                //swingAnimation.setRepeatCount(2);
                swingAnimation.startOffset = 500   //动画播放延迟
                ivBell!!.startAnimation(swingAnimation)
            }
            R.id.btn_rotate_animation -> {
                //角度大于0，是顺时针旋转
                val rotateObjectAnimator = ObjectAnimator.ofFloat(ivBell,
                        "rotation", 0f, 40f, -40f, 40f, -40f, 40f, -40f, 40f, -40f, 0f)
                rotateObjectAnimator.duration = 1500L
                //rotateObjectAnimator.setRepeatCount(1);

                //绕中心点旋转，默认
                /*ivBell.setPivotX(ivBell.getWidth() / 2.0F);
                ivBell.setPivotY(ivBell.getHeight() / 2.0F);*/


                /*//绕左上角旋转
                ivBell.setPivotX(0.0F);
                ivBell.setPivotY(0.0F);*/

                //绕水平中心点
                ivBell!!.pivotY = 0.0f

                //绕竖直中心点
                //ivBell.setPivotX(0.0F);

                Log.d(TAG, "onClick: getPivotX=" + ivBell!!.pivotX)
                Log.d(TAG, "onClick: getPivotY=" + ivBell!!.pivotY)
                Log.d(TAG, "onClick: getLeft=" + ivBell!!.left)
                Log.d(TAG, "onClick: getX=" + ivBell!!.x)
                Log.d(TAG, "onClick: getWidth=" + ivBell!!.width)
                rotateObjectAnimator.interpolator = LinearInterpolator()
                rotateObjectAnimator.start()
            }
            R.id.btnChangeText -> {
                val animChangeText = ValueAnimator.ofObject(CharEvaluator(),
                        'A', 'Z')
                animChangeText.addUpdateListener { animation ->
                    val text = animation.animatedValue as Char
                    btnChangeText.text = text.toString()
                }
                animChangeText.duration = 2000
                animChangeText.start()
            }
            R.id.btnAnimatorSetBuilder -> {

                val colorAnimator = ObjectAnimator.ofInt(btnAnimatorSetBuilder, "backgroundColor", -0x7f80, -0x7f7f01)
                colorAnimator.setEvaluator(ArgbEvaluator())

                val translateY = ObjectAnimator.ofFloat(btnAnimatorSetBuilder, "translationY", 0f, 400f, 0f)
                val translateX = ObjectAnimator.ofFloat(btnAnimatorSetBuilder, "translationX", 0f, 400f, 0f)
                translateY.repeatCount = ValueAnimator.INFINITE

                animatorSet = AnimatorSet()

                animatorSet?.play(colorAnimator)?.with(translateX)?.before(translateY)

                animatorSet?.duration = 2000

                animatorSet?.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                        Log.d(TAG, "onAnimationRepeat: ")
                        //这个方法并不会执行A
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        Log.d(TAG, "onAnimationEnd: ")
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        Log.d(TAG, "onAnimationCancel: ")
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        Log.d(TAG, "onAnimationStart: ")
                    }

                })

                animatorSet?.start()

            }
            R.id.btnCancelAnimatorSetBuilder -> {
                animatorSet?.cancel()
            }

            R.id.btnAnimatorSetDelay -> {

                val tvTranslateY = ObjectAnimator.ofFloat(btnAnimatorSetDelay,
                        "translationY", 0f, 50f, 0f)

                val tvTranslateX = ObjectAnimator.ofFloat(btnCancelAnimatorSetBuilder,
                        "translationX", 0f, 50f, 0f)

                tvTranslateY.startDelay = 2000
                tvTranslateX.startDelay = 2000

                val animatorSetDelay = AnimatorSet()

                animatorSetDelay.play(tvTranslateX).with(tvTranslateY)

                animatorSetDelay.startDelay = 0

                animatorSetDelay.duration = 2000

                animatorSetDelay.start()

            }


            else -> {
                //do nothing
            }
        }
    }

    override fun onStop() {
        super.onStop()
        animatorSet?.cancel()
    }

    private class ViewWrapper(private val mTarget: View) {

        var width: Int
            get() {
                Log.e("PropertyAnima", "width" + mTarget.width)
                return mTarget.width
            }
            set(width) {
                mTarget.layoutParams.width = width
                mTarget.requestLayout()
            }
    }

}
