package com.hm.animationdemo.anim

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.Transformation

class Custom3DRotateYAnimation(
    private val fromDegrees: Float,
    private val toDegrees: Float,
    private val centerX: Float,
    private val centerY: Float,
    private val depthZ: Float,
    private val reverse: Boolean
) : Animation() {

    private lateinit var camera: Camera

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        camera = Camera()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val degrees = fromDegrees + ((toDegrees - fromDegrees) * interpolatedTime)
        val matrix = t.matrix
        camera.save()

        // 添加透视效果
        if (reverse) {
            camera.translate(0f, 0f, depthZ * interpolatedTime)
        } else {
            camera.translate(0f, 0f, depthZ * (1f - interpolatedTime))
        }

        // 绕 Y 轴旋转
        camera.rotateY(degrees)
        camera.getMatrix(matrix)

        // 设置旋转中心
        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)

        camera.restore()
    }
}