#AnimationDemo

Interpolator

Evaluator

属性动画源码学习

以改变一个TextView的`scaleX`为例

```
ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tvAnimation, "scaleX", 1.0F, 2.0F);
    objectAnimator.setDuration(3000);
    objectAnimator.start();
```

### 动画的创建过程

```
public static ObjectAnimator ofFloat(Object target, String propertyName, float... values) {
	  //注释1处
    ObjectAnimator anim = new ObjectAnimator(target, propertyName);
    //注释2处
    anim.setFloatValues(values);
    return anim;
}
```

注释1处调用了ObjectAnimator的构造方法
```
private ObjectAnimator(Object target, String propertyName) {
    setTarget(target);
    setPropertyName(propertyName);
}
```
```
 @Override
 public void setTarget(@Nullable Object target) {
      final Object oldTarget = getTarget();
      if (oldTarget != target) {
          if (isStarted()) {
              cancel();
       }
       //将传入的目标对象使用WeakReference包裹，然后赋值给mTarget
       mTarget = target == null ? null : new WeakReference<Object>(target);
       // 新的目标对象会导致动画在启动之前重新初始化。
       mInitialized = false;
    }
}
```

```
 /**
     * 设置目标对象执行动画的属性
     *
     /
public void setPropertyName(@NonNull String propertyName) {

        // 我们此时只对一个属性左动画，并没有mValues进行赋值，所以此时mValues为null;
            if (mValues != null) {
            PropertyValuesHolder valuesHolder = mValues[0];
            String oldName = valuesHolder.getPropertyName();
            valuesHolder.setPropertyName(propertyName);
            mValuesMap.remove(oldName);
            mValuesMap.put(propertyName, valuesHolder);
        }
        //只用一个属性名来保存要执行动画的属性即可
        mPropertyName = propertyName;
        // 新的属性/值/目标对象会导致动画在启动之前重新初始化。
        mInitialized = false;
    }
```
ObjectAnimator对象构建完了，我们继续看一下注释2处

注释2处，调用了ObjectAnimator的setFloatValues方法

```
@Override
    public void setFloatValues(float... values) {
    		//经过setPropertyName方法，我们知道mValues == null
        if (mValues == null || mValues.length == 0) {
           if (mProperty != null) {
                setValues(PropertyValuesHolder.ofFloat(mProperty, values));
            } else {
            		//我们也没有为mProperty赋值，所以会执行这行代码
                setValues(PropertyValuesHolder.ofFloat(mPropertyName, values));
            }
        } else {
            super.setFloatValues(values);
        }
    }
```

先看下PropertyValuesHolder的ofFloat方法，这个方法是返回一个PropertyValuesHolder对象

```
public static PropertyValuesHolder ofFloat(String propertyName, float... values) {
			//构建了一个FloatPropertyValuesHolder对象
        return new FloatPropertyValuesHolder(propertyName, values);
}

//FloatPropertyValuesHolder的构造函数
public FloatPropertyValuesHolder(String propertyName, float... values) {
				  //内部将我们传入的propertyName赋值给mPropertyName
            super(propertyName);
            //调用setFloatValues方法
            setFloatValues(values);
        }
```

FloatPropertyValuesHolder的setFloatValues方法
```
@Override
public void setFloatValues(float... values) {
	  //调用父类PropertyValuesHolder的setFloatValues方法
    super.setFloatValues(values);
    //将获取到的关键帧赋值给mFloatKeyframes
    mFloatKeyframes = (Keyframes.FloatKeyframes) mKeyframes;
}
```

PropertyValuesHolder的setFloatValues方法

```
public void setFloatValues(float... values) {
	 //float类型
    mValueType = float.class;
    //获取关键帧
    mKeyframes = KeyframeSet.ofFloat(values);
}
```


KeyframeSet的ofFloat方法

```
public static KeyframeSet ofFloat(float... values) {
        boolean badValue = false;
        //我们传入的values长度为2
        int numKeyframes = values.length;
        FloatKeyframe keyframes[] = new FloatKeyframe[Math.max(numKeyframes,2)];
        if (numKeyframes == 1) {
            //说明values只有一个值
            keyframes[0] = (FloatKeyframe) Keyframe.ofFloat(0f);
            keyframes[1] = (FloatKeyframe) Keyframe.ofFloat(1f, values[0]);
            if (Float.isNaN(values[0])) {
                badValue = true;
            }
        } else {
            //values长度大于1，第一个关键帧从values[0]开始
            keyframes[0] = (FloatKeyframe) Keyframe.ofFloat(0f, values[0]);
            for (int i = 1; i < numKeyframes; ++i) {
                keyframes[i] = (FloatKeyframe) Keyframe.ofFloat((float) i / (numKeyframes - 1), values[i]);
                if (Float.isNaN(values[i])) {
                    badValue = true;
                }
            }
        }
        if (badValue) {
            Log.w("Animator", "Bad value (NaN) in float animator");
        }
        //返回只有两个关键帧的FloatKeyframeSet对象
        return new FloatKeyframeSet(keyframes);
    }
```

到此，PropertyValuesHolder的构造方法走完了。

然后执行 ObjectAnimator.setValues() 并传如构建好的FloatPropertyValuesHolder对象。ObjectAnimator 直接调用了父类的 ValueAnimator的setValues方法

```
public void setValues(PropertyValuesHolder... values) {
			//value长度为1
        int numValues = values.length;
        //这里为mValues赋值了
        mValues = values;
        mValuesMap = new HashMap<String, PropertyValuesHolder>(numValues);
        for (int i = 0; i < numValues; ++i) {
            PropertyValuesHolder valuesHolder = values[i];
            //将属性名和属性值的持有者保存到HashMap中
            mValuesMap.put(valuesHolder.getPropertyName(), valuesHolder);
        }
        // 新的属性/值/目标对象会导致动画在启动之前重新初始化。
        mInitialized = false;
    }
```

到此，ObjectAnimator的setFloatValues方法执行完毕，ObjectAnimator创建完毕。

### 动画的开始过程

```
@Override
public void start() {
    //检测如果动画已经执行，则停止动画。
    AnimationHandler.getInstance().autoCancelBasedOn(this);
    //...
    //调用父类ValueAnimator的start方法
    super.start();
}
```
ValueAnimator的start方法

```
@Override
    public void start() {
        start(false);
    }

```

```
 /**
     * @param playBackwards 标志动画是否应该反向播放
     */
private void start(boolean playBackwards) {
			//还是要有Looper对象的
        if (Looper.myLooper() == null) {
            throw new AndroidRuntimeException("Animators may only be run on Looper threads");
        }
        mReversing = playBackwards;
        mSelfPulse = !mSuppressSelfPulseRequested;
        // Special case: reversing from seek-to-0 should act as if not seeked at all.
        if (playBackwards && mSeekFraction != -1 && mSeekFraction != 0) {
            if (mRepeatCount == INFINITE) {
                // Calculate the fraction of the current iteration.
                float fraction = (float) (mSeekFraction - Math.floor(mSeekFraction));
                mSeekFraction = 1 - fraction;
            } else {
                mSeekFraction = 1 + mRepeatCount - mSeekFraction;
            }
        }
        mStarted = true;
        mPaused = false;
        mRunning = false;
        mAnimationEndRequested = false;
        // Resets mLastFrameTime when start() is called, so that if the animation was running,
        // calling start() would put the animation in the
        // started-but-not-yet-reached-the-first-frame phase.
        mLastFrameTime = -1;
        mFirstFrameTime = -1;
        mStartTime = -1;
        //注释1处，
        addAnimationCallback(0);

        if (mStartDelay == 0 || mSeekFraction >= 0 || mReversing) {
            // If there's no start delay, init the animation and notify start listeners right away
            // to be consistent with the previous behavior. Otherwise, postpone this until the first
            // frame after the start delay.
            //注释2处
            startAnimation();
            if (mSeekFraction == -1) {
                // No seek, start at play time 0. Note that the reason we are not using fraction 0
                // is because for animations with 0 duration, we want to be consistent with pre-N
                // behavior: skip to the final value immediately.
                //注释3处，内部调用setCurrentFraction方法
                setCurrentPlayTime(0);
            } else {
                setCurrentFraction(mSeekFraction);
            }
        }
    }

```

start(boolean playBackwards)方法的注释1处，**这个方法就是动画能够动起来的关键**
```
private void addAnimationCallback(long delay) {
        if (!mSelfPulse) {
            return;
        }
        //添加一个AnimationFrameCallback，ValueAnimator实现了AnimationFrameCallback接口
        getAnimationHandler().addAnimationFrameCallback(this, delay);
    }

```
getAnimationHandler()返回的是一个AnimationHandler对象。

AnimationHandler的addAnimationFrameCallback方法

```
public void addAnimationFrameCallback(final AnimationFrameCallback callback, long delay) {
			//todo 发现如果是 Button的话，这个mAnimationCallbacks.size==2，如果是TextView的话，mAnimationCallbacks.size==0
			//考虑这个是Button点击有水波纹的动画，暂时不去管mAnimationCallbacks.size!=0的情况
        if (mAnimationCallbacks.size() == 0) {
        	  //注释2处，注意一下这个mFrameCallback对象
            getProvider().postFrameCallback(mFrameCallback);
        }
        if (!mAnimationCallbacks.contains(callback)) {
        	  //注释3处，把传入的回调添加到mAnimationCallbacks中
            mAnimationCallbacks.add(callback);
        }
			//暂时不考虑延时的情况
        if (delay > 0) {
            mDelayedCallbackStartTime.put(callback, (SystemClock.uptimeMillis() + delay));
        }
    }
```


注释2处的getProvider方法返回的是一个MyFrameCallbackProvider对象

```
private AnimationFrameCallbackProvider getProvider() {
			//todo 很好奇 debug的时候发现这个时候mProvider已经不为null了，指向了是一个MyFrameCallbackProvider实例
        if (mProvider == null) {
            mProvider = new MyFrameCallbackProvider();
        }
        return mProvider;
    }
```

MyFrameCallbackProvider的postFrameCallback方法

```
private class MyFrameCallbackProvider implements AnimationFrameCallbackProvider {
	//获取Choreographer对象
   final Choreographer mChoreographer = Choreographer.getInstance();

   @Override
   public void postFrameCallback(Choreographer.FrameCallback callback) {
       //调用Choreographer的postFrameCallbac方法
       mChoreographer.postFrameCallback(callback);
   }
   
   //...
}
```


看一下mFrameCallback的声明
```
private final Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {
    @Override
    public void doFrame(long frameTimeNanos) {
    doAnimationFrame(getProvider().getFrameTime());
    if (mAnimationCallbacks.size() > 0) {
           //注释3处，如果mAnimationCallbacks.size() > 0，
           //再次调用了MyFrameCallbackProvider的postFrameCallback方法，并把自己传进去了，这里注意，动画动起来的原因就在这里，后面细说
           getProvider().postFrameCallback(this);
        }
    }
};
```

Choreographer类的postFrameCallbackDelayed方法

```
public void postFrameCallbackDelayed(FrameCallback callback, long delayMillis) {
    if (callback == null) {
        throw new IllegalArgumentException("callback must not be null");
    }
			
    postCallbackDelayedInternal(CALLBACK_ANIMATION,callback, FRAME_CALLBACK_TOKEN, delayMillis);
}
```

Choreographer类的postCallbackDelayedInternal方法 

```
private void postCallbackDelayedInternal(int callbackType,Object action, Object token, long delayMillis) {
            
        //注意我们传入的token是FRAME_CALLBACK_TOKEN，传入的action是Choreographer.FrameCallback
        
        synchronized (mLock) {
        	  //开机以来的时间
            final long now = SystemClock.uptimeMillis();
            final long dueTime = now + delayMillis;//我们传入的delayMillis是0
            //注意这里添加的回调是一个CallbackRecord，当CallbackRecord的run方法被调用的时候，内部会调用我们传入的action（就是FrameCallback）的doFrame方法
            mCallbackQueues[callbackType].addCallbackLocked(dueTime, action, token);

            if (dueTime <= now) {//进入此分支
            		//没有延迟，直接开始调度
                scheduleFrameLocked(now);
            } else {
            		//有延迟，就通过handler延时发送一个what=MSG_DO_SCHEDULE_CALLBACK的message
                Message msg = mHandler.obtainMessage(MSG_DO_SCHEDULE_CALLBACK, action);
                msg.arg1 = callbackType;
                msg.setAsynchronous(true);
                mHandler.sendMessageAtTime(msg, dueTime);
            }
        }
    }

```
Choreographer类的scheduleFrameLocked方法

```
private void scheduleFrameLocked(long now) {
        if (!mFrameScheduled) {
            mFrameScheduled = true;
            if (USE_VSYNC) {//debug发现此条件为true,看到这个变量隐隐约约觉得动画正常情况下是每16毫秒执行一帧
                if (DEBUG_FRAMES) {
                    Log.d(TAG, "Scheduling next frame on vsync.");
                }

                // 如果是主线程，就直接调度，否则通过handler把消息发送到主线程              
                if (isRunningOnLooperThreadLocked()) {//我们在这个例子中，是在主线程
                    scheduleVsyncLocked();
                } else {
                    Message msg = mHandler.obtainMessage(MSG_DO_SCHEDULE_VSYNC);
                    msg.setAsynchronous(true);
                    mHandler.sendMessageAtFrontOfQueue(msg);
                }
            } else {
                final long nextFrameTime = Math.max(
                        mLastFrameTimeNanos / TimeUtils.NANOS_PER_MS + sFrameDelay, now);
                if (DEBUG_FRAMES) {
                    Log.d(TAG, "Scheduling next frame in " + (nextFrameTime - now) + " ms.");
                }
                Message msg = mHandler.obtainMessage(MSG_DO_FRAME);
                msg.setAsynchronous(true);
                mHandler.sendMessageAtTime(msg, nextFrameTime);
            }
        }
    }

```

Choreographer类的scheduleVsyncLocked

```
private void scheduleVsyncLocked() {
	 //调用FrameDisplayEventReceiver的scheduleVsync方法，mDisplayEventReceiver在Choreographer类的构造函数中被初始化
    mDisplayEventReceiver.scheduleVsync();
}
```

FrameDisplayEventReceiver继承了DisplayEventReceiver，scheduleVsync方法在DisplayEventReceiver就实现了
```
 /**
     * 计划在下一个显示帧（这个帧是指16ms的那个帧吧）开始的时候发送一个竖直同步信号VSYNC 
     */
    public void scheduleVsync() {
        if (mReceiverPtr == 0) {
            Log.w(TAG, "Attempted to schedule a vertical sync pulse but the display event "
                    + "receiver has already been disposed.");
        } else {
        	  //mReceiverPtr 会在DisplayEventReceiver的构造函数中被初始化
				  //调用native方法来调度
            nativeScheduleVsync(mReceiverPtr);
        }
    }
```
DisplayEventReceiver对应的native类是[NativeDisplayEventReceiver](http://androidxref.com/9.0.0_r3/xref/frameworks/base/core/jni/android_view_DisplayEventReceiver.cpp)

这个native层的调用过程暂且不管，最终会调用DisplayEventReceiver的onVsync方法，FrameDisplayEventReceiver重写了这个方法
（这里为什么最终会调用DisplayEventReceiver的onVsync方法，暂时还不清楚）

FrameDisplayEventReceiver的onVsync方法

```
 private final class FrameDisplayEventReceiver extends DisplayEventReceiver
            implements Runnable {
         
        //...   
 			@Override
        public void onVsync(long timestampNanos, int builtInDisplayId, int frame) {
            // ...            
            // Post the vsync event to the Handler.
            // The idea is to prevent incoming vsync events from completely starving
            // the message queue.  If there are no messages in the queue with timestamps
            // earlier than the frame time, then the vsync event will be processed immediately.
            // Otherwise, messages that predate the vsync event will be handled first.
            long now = System.nanoTime();
            if (timestampNanos > now) {
                Log.w(TAG, "Frame time is " + ((timestampNanos - now) * 0.000001f)
                        + " ms in the future!  Check that graphics HAL is generating vsync "
                        + "timestamps using the correct timebase.");
                timestampNanos = now;
            }

            if (mHavePendingVsync) {
                Log.w(TAG, "Already have a pending vsync event.  There should only be "
                        + "one at a time.");
            } else {
                mHavePendingVsync = true;
            }

            mTimestampNanos = timestampNanos;
            mFrame = frame;
            //这个要注意，message的callback就是FrameDisplayEventReceiver, FrameDisplayEventReceiver实现了Runnable接口
            //当message被处理的时候，会调用FrameDisplayEventReceiver的run方法
            Message msg = Message.obtain(mHandler, this);
            msg.setAsynchronous(true);
            mHandler.sendMessageAtTime(msg, timestampNanos / TimeUtils.NANOS_PER_MS);
        }
        
        /*message 被handler处理的时候会调用run方法*/
        @Override
        public void run() {
            mHavePendingVsync = false;
            //调用 Choreographer的doFrame方法
            doFrame(mTimestampNanos, mFrame);
        }
        
 
 }

```
Choreographer类的doFrame方法 

```
 void doFrame(long frameTimeNanos, int frame) {
        final long startNanos;
        synchronized (mLock) {
        try {
            Trace.traceBegin(Trace.TRACE_TAG_VIEW, "Choreographer#doFrame");
            AnimationUtils.lockAnimationClock(frameTimeNanos / TimeUtils.NANOS_PER_MS);

            mFrameInfo.markInputHandlingStart();
            doCallbacks(Choreographer.CALLBACK_INPUT, frameTimeNanos);

				  
            mFrameInfo.markAnimationsStart();
            //注释1处，我们在Choreographer类的postCallbackDelayedInternal方法中传入的类型是CALLBACK_ANIMATION
            doCallbacks(Choreographer.CALLBACK_ANIMATION, frameTimeNanos);
            mFrameInfo.markPerformTraversalsStart();
            doCallbacks(Choreographer.CALLBACK_TRAVERSAL, frameTimeNanos);

            doCallbacks(Choreographer.CALLBACK_COMMIT, frameTimeNanos);
        } finally {
            AnimationUtils.unlockAnimationClock();
            Trace.traceEnd(Trace.TRACE_TAG_VIEW);
        }
 }

```

Choreographer类的doCallbacks方法精简版

```
 void doCallbacks(int callbackType, long frameTimeNanos) {
        CallbackRecord callbacks;
        synchronized (mLock) {
        		
            final long now = System.nanoTime();
            callbacks = mCallbackQueues[callbackType].extractDueCallbacksLocked(
                    now / TimeUtils.NANOS_PER_MS);
            if (callbacks == null) {
                return;
            }
            mCallbacksRunning = true;

           
            //...
            try {
            Trace.traceBegin(Trace.TRACE_TAG_VIEW, CALLBACK_TRACE_TITLES[callbackType]);
            for (CallbackRecord c = callbacks; c != null; c = c.next) {
                //取出所有的CallbackRecord执行
                c.run(frameTimeNanos);
            }
        } finally {
            synchronized (mLock) {
                mCallbacksRunning = false;
                do {
                    final CallbackRecord next = callbacks.next;
                    recycleCallbackLocked(callbacks);
                    callbacks = next;
                } while (callbacks != null);
            }
            Trace.traceEnd(Trace.TRACE_TAG_VIEW);
        }
    }
```

```
public void run(long frameTimeNanos) {
      if (token == FRAME_CALLBACK_TOKEN) {
      		//执行这行代码
          ((FrameCallback)action).doFrame(frameTimeNanos);
      } else {
          ((Runnable)action).run();
      }   
}
```
这里的FrameCallback就是`mFrameCallback`
```
private final Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
        	  //调用AnimationHandler的doAnimationFrame方法
            doAnimationFrame(getProvider().getFrameTime());
            if (mAnimationCallbacks.size() > 0) {
            	   //注释3处，如果mAnimationCallbacks.size() > 0，再次调用MyFrameCallbackProvider的postFrameCallback方法，并把自己传进去了，   
            	   //
                getProvider().postFrameCallback(this);
            }
        }
 };
```

AnimationHandler的doAnimationFrame方法

```
private void doAnimationFrame(long frameTime) {
        long currentTime = SystemClock.uptimeMillis();
        //第一次的时候，mAnimationCallbacks.size() == 0
        final int size = mAnimationCallbacks.size();
        for (int i = 0; i < size; i++) {
            final AnimationFrameCallback callback = mAnimationCallbacks.get(i);
            if (callback == null) {
                continue;
            }
            if (isCallbackDue(callback, currentTime)) {
                callback.doAnimationFrame(frameTime);
                if (mCommitCallbacks.contains(callback)) {
                    getProvider().postCommitCallback(new Runnable() {
                        @Override
                        public void run() {
                            commitAnimationFrame(callback, getProvider().getFrameTime());
                        }
                    });
                }
            }
        }
        cleanUpList();
    }
```

到这里第一次getProvider().postFrameCallback(mFrameCallback)已经执行完毕了，此时mAnimationCallbacks.size() == 0，不会执行动画。

我们回到AnimationHandler的addAnimationFrameCallback方法
```
public void addAnimationFrameCallback(final AnimationFrameCallback callback, long delay) {
			//todo 发现如果是 Button的话，这个mAnimationCallbacks.size==2，如果是TextView的话，mAnimationCallbacks.size==0
			//考虑这个是Button点击有水波纹的动画，暂时不去管mAnimationCallbacks.size!=0的情况
        if (mAnimationCallbacks.size() == 0) {
        	  //注释2处，注意一下这个mFrameCallback对象
            getProvider().postFrameCallback(mFrameCallback);
        }
        if (!mAnimationCallbacks.contains(callback)) {
        	  //注释3处，把传入的回调添加到mAnimationCallbacks中
            mAnimationCallbacks.add(callback);
        }
			//暂时不考虑延时的情况
        if (delay > 0) {
            mDelayedCallbackStartTime.put(callback, (SystemClock.uptimeMillis() + delay));
        }
    }


```
继续执行注释3处，把传入的回调添加到mAnimationCallbacks中。

然后我们回到ValueAnimator的`start(boolean playBackwards)`的注释2处 

```
/**
     * 通过把动画加入到一个活动的动画列表来启动动画。这个方法必须在UI线程调用
     */
    private void startAnimation() {
        //...
        mAnimationEndRequested = false;
        //注释3处
        initAnimation();
        mRunning = true;
        if (mSeekFraction >= 0) {
            mOverallFraction = mSeekFraction;
        } else {
            mOverallFraction = 0f;
        }
        //在这个例子中我们并没有添加任何的AnimatorListener，所以mListeners为null
        if (mListeners != null) {
            notifyStartListeners();
        }
    }
```
在注释3处，调用了 initAnimation()方法,这里注意一下ObjectAnimator重写了这个方法，我们先来看一下ObjectAnimator的initAnimation方法。

ObjectAnimator的initAnimation方法

```
/**
     * 这个方法在处理动画的第一帧之前会被立即调用。如果动画的启动延迟不为0，
     * 那么这个方法会在延迟结束以后执行。
     * 这个方法负责动画的最终初始化步骤。包括设置mEvaluator，如果用户没有提供的话，
     * 还有设置setter/getter方法，如果用户没有提供的话。
     *
     * 复写这方法应该调用超类的方法来确保内部的机制被正确的设置。
     */
    @CallSuper
    @Override
    void initAnimation() {
        if (!mInitialized) {//如果动画还没有被初始话
            //获取要执行动画的对象
            final Object target = getTarget();
            if (target != null) {
                //获取要改变的属性
                final int numValues = mValues.length;
                for (int i = 0; i < numValues; ++i) {
                    //设置每个属性的setter/getter方法
                    mValues[i].setupSetterAndGetter(target);
                }
            }
            //调用父类的initAnimation方法
            super.initAnimation();
        }
    }

```
PropertyValuesHolder的setupSetterAndGetter方法

```
/**
     * 内部方法（ObjectAnimator会调用）在运行动画之前设置setter/getter方法。
     * 如果没有手动的为这个对象设置setter方法，则会根据提供的属性名称，目标对象和值类型自动派生。
     * 如果没有设置getter方法，那么当且仅当给定的值是null的情况下才会派生getter方法。如果值是null，
     * 那么 getter方法（无论是提供的还是派生的），会被调用来把这些null值设置成目标对象的属性的值。
     *
     * @param target setter方法（和可能的getter方法）存在的对象。
     */
    void setupSetterAndGetter(Object target) {
        if (mProperty != null) {
            // check to make sure that mProperty is on the class of target
            try {
                Object testValue = null;
                //获取所有的动画帧
                List<Keyframe> keyframes = mKeyframes.getKeyframes();
                int keyframeCount = keyframes == null ? 0 : keyframes.size();
                for (int i = 0; i < keyframeCount; i++) {
                    Keyframe kf = keyframes.get(i);
                    //如果Keyframe没有值，或者要从目标对象中获取关键帧的值
                    if (!kf.hasValue() || kf.valueWasSetOnStart()) {
                        if (testValue == null) {
                            testValue = convertBack(mProperty.get(target));
                        }
                        //为关键帧设置值
                        kf.setValue(testValue);
                        kf.setValueWasSetOnStart(true);
                    }
                }
                return;
            } catch (ClassCastException e) {
                Log.w("PropertyValuesHolder","No such property (" + mProperty.getName() +
                        ") on target object " + target + ". Trying reflection instead");
                mProperty = null;
            }
        }
        // We can't just say 'else' here because the catch statement sets mProperty to null.
        if (mProperty == null) {
            //如果对象不存在对应的属性，比如View没有girlFriend属性，
            Class targetClass = target.getClass();
            if (mSetter == null) {
                //设置setter，并把setter方法保存在一个map中，这样不用每次都使用反射来获取setter了。
                setupSetter(targetClass);
            }
            List<Keyframe> keyframes = mKeyframes.getKeyframes();
            int keyframeCount = keyframes == null ? 0 : keyframes.size();
            for (int i = 0; i < keyframeCount; i++) {
                Keyframe kf = keyframes.get(i);
                if (!kf.hasValue() || kf.valueWasSetOnStart()) {
                    if (mGetter == null) {
                        //设置getter，并把getter方法保存在一个map中，这样不用每次都使用反射来获取getter了。
                        setupGetter(targetClass);
                        if (mGetter == null) {
                            // Already logged the error - just return to avoid NPE
                            return;
                        }
                    }
                    try {
                        Object value = convertBack(mGetter.invoke(target));
                        kf.setValue(value);
                        kf.setValueWasSetOnStart(true);
                    } catch (InvocationTargetException e) {
                        Log.e("PropertyValuesHolder", e.toString());
                    } catch (IllegalAccessException e) {
                        Log.e("PropertyValuesHolder", e.toString());
                    }
                }
            }
        }
    }
```

Value的initAnimation方法
```
void initAnimation() {
     if (!mInitialized) {
         int numValues = mValues.length;
         for (int i = 0; i < numValues; ++i) {
         		//调用mInitialized的init方法
             mValues[i].init();
         }
         //到这里mInitialized才为true
         mInitialized = true;
     }
 }

```
PropertyValuesHolder的init方法

```
 /**
     * 内部方法，ValueAnimator调用这个方法来设置用来计算动画值的TypeEvaluator
     */
    void init() {
        if (mEvaluator == null) {
            // We already handle int and float automatically, but not their Object
            // 我们这里是Float类型所以mEvaluator被赋值为sFloatEvaluator
            mEvaluator = (mValueType == Integer.class) ? sIntEvaluator :
                    (mValueType == Float.class) ? sFloatEvaluator :
                    null;
        }
        if (mEvaluator != null) {
            // 将sFloatEvaluator传递给mKeyframes
            mKeyframes.setEvaluator(mEvaluator);
        }
    }
```
startAnimation方法到此结束，接下来看一下setCurrentPlayTime方法

```
/**
     * 设置动画的位置到指定的时间点。参数playTime应该在0到动画的时长之间。
     * 如果动画还没开始，那么当动画开始的时候，那么动画会设置当前的播放时间为playTime，然后从这个时间点继续执行
     * 如果动画已经开始了，那么动画会设置当前的播放时间为playTime，然后从这个时间点继续执行，
     *
     * @param playTime 动画前进或后退的时间（以毫秒为单位）。
     */
    public void setCurrentPlayTime(long playTime) {
        //根据传入的时间，计算动画执行的百分比
        float fraction = mDuration > 0 ? (float) playTime / mDuration : 1;
        //调用setCurrentFraction方法
        setCurrentFraction(fraction);
    }

```
```
public void setCurrentFraction(float fraction) {
        //如果初始化了，不会重复初始化
        initAnimation();
        //计算对应的百分比
        fraction = clampFraction(fraction);
        mStartTimeCommitted = true; // do not allow start time to be compensated for jank
        if (isPulsingInternal()) {//如果已经进入到了动画循环,我们先不考虑这种情况
            long seekTime = (long) (getScaledDuration() * fraction);
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            // Only modify the start time when the animation is running. Seek fraction will ensure
            // non-running animations skip to the correct start time.
            mStartTime = currentTime - seekTime;
        } else {
            // 动画还没开始循环，或者正在处于启动延迟等待中
            //为mSeekFraction赋值，
            mSeekFraction = fraction;
        }
        mOverallFraction = fraction;
        final float currentIterationFraction = getCurrentIterationFraction(fraction, mReversing);
        //感觉这个方法有戏啊
        animateValue(currentIterationFraction);
    }
```
ObjectAnimator重写了animateValue方法，我们先看下ObjectAnimator的animateValue方法
```
    @CallSuper
    @Override
    void animateValue(float fraction) {
        final Object target = getTarget();
        if (mTarget != null && target == null) {
            //没有目标对象了，还做个毛动画，清除动画并返回
            cancel();
            return;
        }
        //调用父类的animateValue
        super.animateValue(fraction);
        int numValues = mValues.length;
        for (int i = 0; i < numValues; ++i) {
        	  //注释3处property设置新的值
            mValues[i].setAnimatedValue(target);
        }
    }
```

ValueAnimator的animateValue方法

```
@CallSuper
    void animateValue(float fraction) {
        fraction = mInterpolator.getInterpolation(fraction);
        mCurrentFraction = fraction;
        int numValues = mValues.length;
        //每个属性根据fraction计算当前的值
        for (int i = 0; i < numValues; ++i) {
            mValues[i].calculateValue(fraction);
        }
        if (mUpdateListeners != null) {
            int numListeners = mUpdateListeners.size();
            for (int i = 0; i < numListeners; ++i) {
                mUpdateListeners.get(i).onAnimationUpdate(this);
            }
        }
    }
```
```
/**
     * 使用 PropertyValuesHolder的evaluator来计算值
     *
     * @param fraction 动画的经过时间插值部分。
     */
    void calculateValue(float fraction) {
        Object value = mKeyframes.getValue(fraction);
        mAnimatedValue = mConverter == null ? value : mConverter.convert(value);
    }
```
具体怎么计算的先不看。然后我们回到ObjectAnimator的animateValue方法的注释3处
```
mValues[i].setAnimatedValue(target);
```
调用了PropertyValuesHolder的setAnimatedValue方法

```
/**
     * ObjectAnimator 调用这个方法把ValueAnimator 计算出来的值转化成目标对象的属性的值
     * @param target 设置值的目标对象
     */
    void setAnimatedValue(Object target) {
        if (mProperty != null) {
        	  //我们传入的是float类型，所以调用FloatProperty的set方法
            mProperty.set(target, getAnimatedValue());
        }
        if (mSetter != null) {
            try {
                mTmpValueArray[0] = getAnimatedValue();
                //设置目标对象的属性值
                mSetter.invoke(target, mTmpValueArray);
            } catch (InvocationTargetException e) {
                Log.e("PropertyValuesHolder", e.toString());
            } catch (IllegalAccessException e) {
                Log.e("PropertyValuesHolder", e.toString());
            }
        }
    }
```

有疑问：猜测每16ms会有一个VSYC信号，导致doAnimationFrame被调用，然后会调用animateBasedOnTime，然后会调用animateValue更新动画的值，
并导致View重新绘制，产生动画效果


## 关于pivotX 和pivotY的问题

### view动画改变轴心点

```
android:pivotX 表示缩放/旋转起点 X 轴坐标，可以是整数值、百分数（或者小数）、百分数p 三种样式，
比如 50、50% 、0.5、50%p。需要明确的是，这里以进行动画控件的左上角为原点坐标，
当属性值为数值，如 50 时，表示原点坐标加上 50px，作为起始点；
如果是百分数，比如 50%，表示原点坐标加上自己宽度的 50%（即控件水平中心）作为起始点 ；
如果是 50%p（字母 p 是 parent 的意思），取值的基数是父控件，因此 50%p 就是表示在原点坐标加上父控件宽度的 50% 作为起始点 x 轴坐标。
--------------------- 
作者：hust_twj 
来源：CSDN 
原文：https://blog.csdn.net/hust_twj/article/details/78587989 
版权声明：本文为博主原创文章，转载请附上博文链接！

```

```
if (rotateAnimation == null) {
            //相对于view自身做动画
            rotateAnimation = RotateAnimation(0F, -90F,
                    Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F)
            rotateAnimation?.duration = 1000L
            rotateAnimation?.fillAfter = true

            //相对于parent做动画
            rotateAnimation = RotateAnimation(0F, -90F,
                    Animation.RELATIVE_TO_PARENT, 0.5F, Animation.RELATIVE_TO_PARENT, 0.0F)
            rotateAnimation?.duration = 1000L
            rotateAnimation?.fillAfter = false

            //相对于绝对坐标做动画
            rotateAnimation = RotateAnimation(0F, -90F,
                    Animation.ABSOLUTE, btnRotate.width / 2.0F, Animation.ABSOLUTE, btnRotate.height / 2.0F)
            rotateAnimation?.duration = 1000L
            rotateAnimation?.fillAfter = false
        }
        btnRotate.startAnimation(rotateAnimation)
```
### 属性动画 改变轴心点
```
  //角度大于0，是顺时针旋转
                ObjectAnimator rotateObjectAnimator = ObjectAnimator.ofFloat(ivBell,
                        "rotation", 0F, 45F, -45F, 45F, -45F, 45F, -45F, 0F);
                rotateObjectAnimator.setDuration(3000);

                //绕中心点旋转，默认
                /*ivBell.setPivotX(ivBell.getWidth() / 2.0F);
                ivBell.setPivotY(ivBell.getHeight() / 2.0F);*/


                /*//绕左上角旋转
                ivBell.setPivotX(0.0F);
                ivBell.setPivotY(0.0F);*/

               /* //绕水平中心点
                ivBell.setPivotY(0.0F);*/

                //绕竖直中心点
                ivBell.setPivotX(0.0F);
```



