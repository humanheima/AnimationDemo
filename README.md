### 基于源码9.0

属性动画源码学习

1. 在每一次绘制信号到来的时候，改变View的属性的值，导致View重新绘制。然后在下一帧到来的时候，显示重新绘制后的View。
如果此时动画时间没到，那么会再次改变属性的值，导致View重新绘制。然后在下一帧到来的时候，显示重新绘制后的View。这个过程
连续执行，直到动画时间结束。动画结束后，就不会再改变属性的值，也不会导致View的重新绘制。


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
    //保存要执行动画的对象
    setTarget(target);
    //设置要执行动画的对象的属性
    setPropertyName(propertyName);
}
```
ObjectAnimator的setTarget方法
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
       // 设置新的目标对象会导致动画在启动之前重新初始化。
       mInitialized = false;
    }
}
```
ObjectAnimator的setPropertyName方法
```
 /**
 * 设置目标对象执行动画的属性
 */
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
    // 设置新的属性/值/目标对象会导致动画在启动之前重新初始化。
    mInitialized = false;
}
```
ObjectAnimator对象构建完了，我们继续看一下注释2处。注释2处，调用了ObjectAnimator的setFloatValues方法

```
@Override
public void setFloatValues(float... values) {
    //经过setPropertyName方法，我们知道此时mValues == null
    if (mValues == null || mValues.length == 0) {
       if (mProperty != null) {
            setValues(PropertyValuesHolder.ofFloat(mProperty, values));
        } else {
             //注释3处，我们也没有为mProperty赋值，所以会执行这行代码
             setValues(PropertyValuesHolder.ofFloat(mPropertyName, values));
        }
    } else {
           super.setFloatValues(values);
    }
}
```

注释3处，先看下PropertyValuesHolder的ofFloat方法，这个方法是返回一个PropertyValuesHolder对象

```
public static PropertyValuesHolder ofFloat(String propertyName, float... values) {
    //构建了一个FloatPropertyValuesHolder对象
    return new FloatPropertyValuesHolder(propertyName, values);
}
```
FloatPropertyValuesHolder的构造函数
```
public FloatPropertyValuesHolder(String propertyName, float... values) {
     //调用父类构造函数，内部将我们传入的propertyName赋值给mPropertyName
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
    //返回只有两个关键帧的FloatKeyframeSet对象
    return new FloatKeyframeSet(keyframes); }
```

到此，PropertyValuesHolder的构造方法走完了。

然后执行 ObjectAnimator.setValues() 并传入构建好的FloatPropertyValuesHolder对象。ObjectAnimator 直接调用了父类`ValueAnimator`的setValues方法

```
public void setValues(PropertyValuesHolder... values) {
        //value长度为1
        int numValues = values.length;
        //这里为mValues赋值了
        mValues = values;
        mValuesMap = new HashMap<String, PropertyValuesHolder>(numValues);
        for (int i = 0; i < numValues; ++i) {
            PropertyValuesHolder valuesHolder = values[i];
            //将执行动画改变的属性名（在这个例子中就是scaleX）和属性值的持有者保存到HashMap中
            mValuesMap.put(valuesHolder.getPropertyName(), valuesHolder);
        }
        // 设置新的属性/值/目标对象会导致动画在启动之前重新初始化。
        mInitialized = false;
}
```

到此，ObjectAnimator的setFloatValues方法执行完毕，ObjectAnimator创建完毕。

### 开启动画
ObjectAnimator的start方法
```
@Override
public void start() {
    //检测如果动画已经执行，则停止动画。
    AnimationHandler.getInstance().autoCancelBasedOn(this);
    //...
    //调用父类的start方法
    super.start();
}
```

ValueAnimator的start方法
```
@Override
public void start() {
    //调用重载函数
    start(false);
}
```

ValueAnimato的start(boolean playBackwards) 方法精简版
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
    //...
    mStarted = true;
    mPaused = false;
    mRunning = false;
    mAnimationEndRequested = false;
    //... 
    //注释4处，
    addAnimationCallback(0);
    if (mStartDelay == 0 || mSeekFraction >= 0 || mReversing) {
          //注释5处如果没有启动延迟，就立即启动动画 
          startAnimation();
          if (mSeekFraction == -1) {
          //注释6处，没有快进，从0开始播放动画 
          setCurrentPlayTime(0);
        } else {
            setCurrentFraction(mSeekFraction);
        }
    }
}
```

注释4处，**这个方法就是动画能够动起来的关键**
```
private void addAnimationCallback(long delay) {
    if (!mSelfPulse) {
        return;
    }
    //添加一个AnimationFrameCallback，ValueAnimator实现了AnimationFrameCallback接口
    getAnimationHandler().addAnimationFrameCallback(this, delay);
}
```
getAnimationHandler()返回的是一个AnimationHandler对象。注意一下AnimationHandler是用线程本地变量来保存的。

AnimationHandler的addAnimationFrameCallback方法精简版

```
public void addAnimationFrameCallback(final AnimationFrameCallback callback, long delay) {
    //todo debug的时候发现如果是 Button的话，这个mAnimationCallbacks.size==2，如果是TextView的话，
    //mAnimationCallbacks.size==0考虑这个是Button点击有水波纹的动画，
    //暂时不去管mAnimationCallbacks.size!=0的情况
    if (mAnimationCallbacks.size() == 0) {
         //注释7处，注意一下这个mFrameCallback对象
        getProvider().postFrameCallback(mFrameCallback);
    }
    if (!mAnimationCallbacks.contains(callback)) {
         //注释8处，把传入的回调添加到mAnimationCallbacks中
         mAnimationCallbacks.add(callback);
    }
    //...
}
```

注释2处的getProvider方法返回的是一个MyFrameCallbackProvider对象

```
private AnimationFrameCallbackProvider getProvider() {
   //todo 很好奇 debug的时候发现这个时候mProvider已经不为null了，指向了一个
   //MyFrameCallbackProvider实例，猜测是因为已经初始化过AnimationHandler，并且调用过AnimationHandler的setProvider方法。
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
       //调用Choreographer的postFrameCallback方法
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
        //注释9处
        doAnimationFrame(getProvider().getFrameTime());
        if (mAnimationCallbacks.size() > 0) {
        //注释10处，如果mAnimationCallbacks.size() > 0，再次调用
        //MyFrameCallbackProvider的postFrameCallback方法，并把mFrameCallback传进去了
        //注意这里的this就是mFrameCallback
        getProvider().postFrameCallback(this);
     }
   }
};
```
Choreographer类的postFrameCallback方法
```
public void postFrameCallback(FrameCallback callback) {
    //调用重载函数，传入的延迟是0毫秒
    postFrameCallbackDelayed(callback, 0);
}
```

Choreographer类的postFrameCallbackDelayed方法
```
public void postFrameCallbackDelayed(FrameCallback callback, long delayMillis) {
   //...		
   postCallbackDelayedInternal(CALLBACK_ANIMATION,callback, FRAME_CALLBACK_TOKEN, delayMillis);
}
```
注意一下我们传入的4个参数
callbackType：CALLBACK_ANIMATION
action：mFrameCallback
token：FRAME_CALLBACK_TOKEN
delayMillis： 0

Choreographer类的postCallbackDelayedInternal方法 
```
private void postCallbackDelayedInternal(int callbackType,Object action, Object token, long delayMillis) {
            
    //注意我们传入的token是FRAME_CALLBACK_TOKEN，传入的action是Choreographer.FrameCallback
    synchronized (mLock) {
        //开机以来的时间
        final long now = SystemClock.uptimeMillis();
        final long dueTime = now + delayMillis;//我们传入的delayMillis是0
        //注意这里添加的回调是一个CallbackRecord，当CallbackRecord的run方法被调用的时候，
        //内部会调用我们传入的action（就是mFrameCallback）的doFrame方法。
        mCallbackQueues[callbackType].addCallbackLocked(dueTime, action, token);
        if (dueTime <= now) {//进入此分支
           //没有延迟，直接开始调度
           scheduleFrameLocked(now);
        } else {
          //有延迟，就通过handler延时发送一个what=MSG_DO_SCHEDULE_CALLBACK的message
            Message msg = mHandler.obtainMessage(MSG_DO_SCHEDULE_CALLBACK, action);
            //注意我们把回调类型赋值给msg的arg1字段了
            msg.arg1 = callbackType;
            msg.setAsynchronous(true);
            mHandler.sendMessageAtTime(msg, dueTime);
        }
    }
}
```

Choreographer类的scheduleFrameLocked方法精简版
```
private void scheduleFrameLocked(long now) {
    if (!mFrameScheduled) {
        mFrameScheduled = true;
        if (USE_VSYNC) {
            //debug发现此条件为true,看到这个变量隐隐约约觉得动画正常情况下是每16毫秒执行一帧
            // 如果是主线程，就直接调度，否则通过handler把消息发送到主线程              
            if (isRunningOnLooperThreadLocked()) {//我们在这个例子中，是在主线程开启动画的
                scheduleVsyncLocked();
            } else {
                Message msg = mHandler.obtainMessage(MSG_DO_SCHEDULE_VSYNC);
                msg.setAsynchronous(true);
                mHandler.sendMessageAtFrontOfQueue(msg);
            }
        }
        //...
   }
}

```

Choreographer类的scheduleVsyncLocked方法

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
         //调用native方法，这个方法其实是向native层注册成为观察者，用来接收下一帧的屏幕刷新信号
         nativeScheduleVsync(mReceiverPtr);
      }
}
```
到这里,注释7处`getProvider().postFrameCallback(mFrameCallback);`这行代码执行就完毕了。

DisplayEventReceiver对应的native类是[NativeDisplayEventReceiver](http://androidxref.com/9.0.0_r3/xref/frameworks/base/core/jni/android_view_DisplayEventReceiver.cpp)

注意：我们向native层注册成为观察者，用来接收下一帧的屏幕刷新信号，当下一帧屏幕刷新信号到来的时候会会调用DisplayEventReceiver的onVsync方法。
从注册成为观察者到收到下一帧的屏幕刷新信号这个过程是异步的。

FrameDisplayEventReceiver重写了DisplayEventReceiver的onVsync方法。

FrameDisplayEventReceiver的onVsync方法

```
 private final class FrameDisplayEventReceiver extends DisplayEventReceiver
            implements Runnable {
         
        //...   
 			@Override
        public void onVsync(long timestampNanos, int builtInDisplayId, int frame) {
            // ...            
           
            mTimestampNanos = timestampNanos;
            mFrame = frame;
            //这个要注意，message的callback就是FrameDisplayEventReceiver, FrameDisplayEventReceiver实现了Runnable接口
            //当message被处理的时候，会调用FrameDisplayEventReceiver的run方法
            Message msg = Message.obtain(mHandler, this);
            msg.setAsynchronous(true);
            mHandler.sendMessageAtTime(msg, timestampNanos / TimeUtils.NANOS_PER_MS);
        }
        
        /*message被handler处理的时候会调用run方法*/
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
        
        synchronized (mLock) {
        //...
        try {
            Trace.traceBegin(Trace.TRACE_TAG_VIEW, "Choreographer#doFrame");
            AnimationUtils.lockAnimationClock(frameTimeNanos / TimeUtils.NANOS_PER_MS);

            mFrameInfo.markInputHandlingStart();
            //处理输入事件
            doCallbacks(Choreographer.CALLBACK_INPUT, frameTimeNanos);

    
            mFrameInfo.markAnimationsStart();
            //处理动画事件
            //注释1处，我们在Choreographer类的postCallbackDelayedInternal方法中传入的类型是CALLBACK_ANIMATION
            doCallbacks(Choreographer.CALLBACK_ANIMATION, frameTimeNanos);
            mFrameInfo.markPerformTraversalsStart();
            //处理View绘制事件
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
                //根据callbackType取出所有的CallbackRecord执行
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

如果我们的callbackType类型是Choreographer.CALLBACK_ANIMATION

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
如果我们的callbackType类型是Choreographer.CALLBACK_ANIMATION，就会执行FrameCallback的doFrame方法

这里的FrameCallback就是`mFrameCallback`
```
private final Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
        	  //调用AnimationHandler的doAnimationFrame方法
            doAnimationFrame(getProvider().getFrameTime());
            if (mAnimationCallbacks.size() > 0) {
            	   //注释3处，如果mAnimationCallbacks.size() > 0，再次调用MyFrameCallbackProvider的postFrameCallback方法，来监听下一帧的屏幕刷新信号，
            	   //这里传入的this就是mFrameCallback  
                getProvider().postFrameCallback(this);
            }
        }
 };
```

AnimationHandler的doAnimationFrame方法

```
private void doAnimationFrame(long frameTime) {
        long currentTime = SystemClock.uptimeMillis();
        //如果native异步调用回来的时候mAnimationCallbacks.size()会不会可能为0呢。
        final int size = mAnimationCallbacks.size();
        for (int i = 0; i < size; i++) {
            final AnimationFrameCallback callback = mAnimationCallbacks.get(i);
            if (callback == null) {
                continue;
            }
            if (isCallbackDue(callback, currentTime)) {
            		//执行动画的某一帧
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
        //这里注意一下，这个方法会移除mAnimationCallbacks中为null的item。当mAnimationCallbacks.size==0的时候，
        我们就不会再向native层注册接受屏幕刷新信号了，也就不会在执行动画，以及View的绘制事件。        
       //cleanUpList();
    }
```
注意一下，如果如果native异步调用回来的时候mAnimationCallbacks.size()==0，那么就不会调用`doAnimationFrame`执行动画，也不会调用`getProvider().postFrameCallback(this);`
重新监听下一帧的屏幕刷新信号。如果是这种情况，那么动画是怎么执行的呢？继续往下看。

到这里第一次getProvider().postFrameCallback(mFrameCallback)已经执行完毕了。

我们回到AnimationHandler的addAnimationFrameCallback方法
```
public void addAnimationFrameCallback(final AnimationFrameCallback callback, long delay) {
			//todo 发现如果是 Button的话，这个mAnimationCallbacks.size==2，如果是TextView的话，mAnimationCallbacks.size==0
			//考虑这个是Button点击有水波纹的动画，暂时不去管mAnimationCallbacks.size!=0的情况
        if (mAnimationCallbacks.size() == 0) {
        	  //注释2处，注意一下这个mFrameCallback对象
            getProvider().postFrameCallback(mFrameCallback);
        }
        //注意，这里只有没有添加过的时候，才会添加
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
继续执行注释3处，把传入的回调添加到mAnimationCallbacks中。这个时候mAnimationCallbacks.size()>0了。

然后我们回到ValueAnimator的`start(boolean playBackwards)`方法的注释2处 

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
        if (isPulsingInternal()) {//如果已经进入到了动画循环
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

这里调用反射来设置我们的目标对象（在这个例子中是一个TextView）的属性值，在这个例子中就是设置`scaleX`。然后View设置完`scaleX`属性值后会调用`invalidate`方法，
关于`invalidate`方法不多说，最终会调用`ViewRootImpl#scheduleTraversals()`。

ViewRootImpl的scheduleTraversals()方法
```
void scheduleTraversals() {
        if (!mTraversalScheduled) {
            mTraversalScheduled = true;
            mTraversalBarrier = mHandler.getLooper().getQueue().postSyncBarrier();
            //这里Choreographer,post了一个回调类型为Choreographer.CALLBACK_TRAVERSAL的runnable，
            mChoreographer.postCallback(
                    Choreographer.CALLBACK_TRAVERSAL, mTraversalRunnable, null);
            if (!mUnbufferedInputDispatch) {
                scheduleConsumeBatchedInput();
            }
            notifyRendererOfFramePending();
            pokeDrawLockIfNeeded();
        }
    }

```

```
mChoreographer.postCallback(Choreographer.CALLBACK_TRAVERSAL, mTraversalRunnable, null);

```
Choreographer post了一个回调类型为Choreographer.CALLBACK_TRAVERSAL的runnable这个过程和上文中post回调类型为CALLBACK_ANIMATION的过程是类似的就不看了。
我们知道最终会导致Choreographer类的doFrame方法被调用

Choreographer类的doFrame方法

```
 void doFrame(long frameTimeNanos, int frame) {
        
        synchronized (mLock) {
        //...
        try {
            Trace.traceBegin(Trace.TRACE_TAG_VIEW, "Choreographer#doFrame");
            AnimationUtils.lockAnimationClock(frameTimeNanos / TimeUtils.NANOS_PER_MS);

            mFrameInfo.markInputHandlingStart();
            //处理输入事件
            doCallbacks(Choreographer.CALLBACK_INPUT, frameTimeNanos);

    
            mFrameInfo.markAnimationsStart();
            //处理动画事件，
            //注释1处，我们在Choreographer类的postCallbackDelayedInternal方法中传入的类型是CALLBACK_ANIMATION
            doCallbacks(Choreographer.CALLBACK_ANIMATION, frameTimeNanos);
            mFrameInfo.markPerformTraversalsStart();
            //处理View绘制事件
            doCallbacks(Choreographer.CALLBACK_TRAVERSAL, frameTimeNanos);

            doCallbacks(Choreographer.CALLBACK_COMMIT, frameTimeNanos);
        } finally {
            AnimationUtils.unlockAnimationClock();
            Trace.traceEnd(Trace.TRACE_TAG_VIEW);
        }
 }

```
在这个方法里，我们先是处理动画事件， 这个时候，重新调用mFrameCallback的doFrame方法，这个时候mAnimationCallbacks.size是满足的，
所以会改变View的属性，在这个例子中就是`scaleX`,并且重新调用MyFrameCallbackProvider的postFrameCallback方法，来监听下一帧的屏幕刷新信号。
```
private final Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
        	  //调用AnimationHandler的doAnimationFrame方法
            doAnimationFrame(getProvider().getFrameTime());
            if (mAnimationCallbacks.size() > 0) {
            	   //注释3处，如果mAnimationCallbacks.size() > 0，再次调用MyFrameCallbackProvider的postFrameCallback方法，来监听下一帧的屏幕刷新信号，
            	   //这里传入的this就是mFrameCallback  
                getProvider().postFrameCallback(this);
            }
        }
 };
```
处理完动画事件后，会继续处理处理View绘制事件,因为这个时候，处理动画事件的时候已经把View的属性值改变了，然后在处理View绘制事件的时候，View就发生了变化，这就是动画。先别急，再看一下处理View绘制事件的过程。

```
doCallbacks(Choreographer.CALLBACK_TRAVERSAL, frameTimeNanos);
```
```
private static final class CallbackRecord {
        public CallbackRecord next;
        public long dueTime;
        public Object action; // Runnable or FrameCallback
        public Object token;

        public void run(long frameTimeNanos) {
            if (token == FRAME_CALLBACK_TOKEN) {
                ((FrameCallback)action).doFrame(frameTimeNanos);
            } else {
            		 //处理View绘制事件
                ((Runnable)action).run();
            }
        }
    }
```

这里的Runnable就是ViewRootImpl里面的mTraversalRunnable，我们看一下mTraversalRunnable这个对象
```
final class TraversalRunnable implements Runnable {
        @Override
        public void run() {
        	  //调用ViewRootImpl的doTraversal方法
            doTraversal();
    }
}
```

调用ViewRootImpl的doTraversal方法
```
void doTraversal() {
        if (mTraversalScheduled) {
            mTraversalScheduled = false;
            mHandler.getLooper().getQueue().removeSyncBarrier(mTraversalBarrier);

            if (mProfile) {
                Debug.startMethodTracing("ViewAncestor");
            }
				  //调用performTraversals此方法
            performTraversals();

            if (mProfile) {
                Debug.stopMethodTracing();
                mProfile = false;
            }
        }
    }

```

ViewRootImpl的performTraversals方法
```
private void performTraversals() {

  //...
  performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
  //...
  performLayout(lp, mWidth, mHeight);
  //...
  performDraw();
  //...
}
```


到这里动画的源码分析过程基本结束，总结一下

1. ValueAnimator在start之前，向native层注册成为监听者，当下一帧的屏幕刷新信号到来的时候，如果mAnimationCallbacks的size>0，则改变目标对象的属性，然后从监听者列表中移除自己，并重新注册成为监听者，监听下一帧的屏幕刷新信号的到来。如果mAnimationCallbacks的size<=0,说明我们没有注册成为监听者，或者已经从监听者列表中把自己移除了，那么就不会再接受下一帧的屏幕刷新信号，也就不会则改变目标对象的属性，此时意味着动画结束。
2. 在start方法中调用setCurrentPlayTime方法改变目标对象的属性，导致导致View重新绘制。
3. View重新绘制会导致Chreographer会处理动画事件和View绘制事件。
4. Chreographer在处理动画事件过程中会setCurrentPlayTime方法改变目标对象的属性，导致导致View重新绘制。并重新注册成为监听者，监听下一帧的屏幕刷新信号。Chreographer在处理View绘制事件的时候会重新绘制View，导致View的样子发生改变。
5. 当下一帧屏幕刷新信号到来的时候如果mAnimationCallbacks的size>0，则改变目标对象的属性，然后从监听者列表中移除自己，并重新注册成为监听者，监听下一帧的屏幕刷新信号的到来。如果mAnimationCallbacks的size<=0,说明我们没有注册成为监听者，或者已经从监听者列表中把自己移除了，那么就不会再接受下一帧的屏幕刷新信号，也就不会则改变目标对象的属性，此时意味着动画结束。
6. 如此循环就形成了动画最终动画结束的时候从监听者列表中把自己移除了，那么就不会再接受下一帧的屏幕刷新信号，也就不会则改变目标对象的属性。



### TimeInterpolator（时间插值器） & TypeEvaluator（类型估值算法，即估值器）


TimeInterpolator（时间插值器）：根据时间流逝的百分比计算出当前属性值改变的百分比。
TypeEvaluator（类型估值算法，即估值器）：根据当前属性改变的百分比来计算改变后的属性值。

那么TimeInterpolator和TypeEvaluator是怎么协同工作的呢？

答：它们是实现非匀速动画的重要手段。属性动画是对属性做动画，属性要实现动画，首先由TimeInterpolator（插值器）
根据时间流逝的百分比计算出当前属性值改变的百分比，并且插值器将这个百分比返回，这个时候插值器的工作就完成了。
比如插值器返回的值是0.5，很显然我们要的不是0.5，而是当前属性的值，即当前属性变成了什么值，
这就需要估值器根据当前属性改变的百分比来计算改变后的属性值，根据这个属性值，我们就可以设置当前属性的值了。

[Android属性动画（二）之插值器与估值器](https://blog.csdn.net/qq_24530405/article/details/50630744) 










