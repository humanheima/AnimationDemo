
# View 动画

## scale 


pivotX 和 pivotY的属性 只影响动画的起始位置

android:pivotX ：缩放起点X轴坐标，可以是数值、百分数、百分数p 三种样式，比如 50、50%、50%p。
当为数值时，表示在当前View的左上角，即原点处加上数值。做为起始缩放点；
如果是50%，表示在当前控件的左上角加上自己宽度的50%做为起始点；
如果是50%p，那么就是表示在当前的左上角加上父控件宽度的50%做为起始点；

## rotate

android:fromDegrees ：开始旋转的角度位置，正值代表顺时针方向度数，负值代表逆时针方向度数
android:toDegrees ：结束时旋转到的角度位置，正值代表顺时针方向度数，负值代表逆时针方向度数



参考:[自定义控件三部曲之动画篇（一）——alpha、scale、translate、rotate、set的xml属性及用法](https://blog.csdn.net/harvic880925/article/details/39996643)
