package com.yenaly.circularrevealswitch.interpolator

import android.graphics.PointF
import android.view.animation.Interpolator
import kotlin.math.abs

/**
 * CubicBezierInterpolator is an interpolator that uses cubic Bezier curves to interpolate between two points.
 *
 * @property start The start point of the curve.
 * @property end The end point of the curve.
 */
open class CubicBezierInterpolator(start: PointF, end: PointF) : Interpolator {

    companion object {
        // same as Telegram's animation interpolator
        @JvmField
        internal val EaseInOutQuad = CubicBezierInterpolator(0.455, 0.03, 0.515, 0.955)
    }

    @JvmField
    protected var start: PointF

    @JvmField
    protected var end: PointF

    @JvmField
    protected var a = PointF()

    @JvmField
    protected var b = PointF()

    @JvmField
    protected var c = PointF()

    init {
        require(start.x in 0F..1F) { "startX value must be in the range [0, 1]" }
        require(end.x in 0F..1F) { "endX value must be in the range [0, 1]" }
        this.start = start
        this.end = end
    }

    constructor(startX: Float, startY: Float, endX: Float, endY: Float) : this(
        PointF(startX, startY), PointF(endX, endY)
    )

    constructor(startX: Double, startY: Double, endX: Double, endY: Double) : this(
        startX.toFloat(), startY.toFloat(),
        endX.toFloat(), endY.toFloat()
    )

    override fun getInterpolation(time: Float): Float {
        return getBezierCoordinateY(getXForTime(time))
    }

    protected fun getBezierCoordinateY(time: Float): Float {
        c.y = 3 * start.y
        b.y = 3 * (end.y - start.y) - c.y
        a.y = 1 - c.y - b.y
        return time * (c.y + time * (b.y + time * a.y))
    }

    protected fun getXForTime(time: Float): Float {
        var x = time
        var z: Float
        for (i in 1..13) {
            z = getBezierCoordinateX(x) - time
            if (abs(z.toDouble()) < 1e-3) {
                break
            }
            x -= z / getXDerivate(x)
        }
        return x
    }

    private fun getXDerivate(t: Float): Float {
        return c.x + t * (2 * b.x + 3 * a.x * t)
    }

    private fun getBezierCoordinateX(time: Float): Float {
        c.x = 3 * start.x
        b.x = 3 * (end.x - start.x) - c.x
        a.x = 1 - c.x - b.x
        return time * (c.x + time * (b.x + time * a.x))
    }
}