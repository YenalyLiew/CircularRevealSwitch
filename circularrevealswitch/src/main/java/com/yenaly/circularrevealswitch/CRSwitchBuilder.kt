package com.yenaly.circularrevealswitch

import android.view.View
import android.view.View.OnClickListener
import android.view.animation.Interpolator
import com.yenaly.circularrevealswitch.interpolator.CubicBezierInterpolator

/**
 * Abstract class CRSwitchBuilder is used to build a circular reveal animation.
 * @property view The view on which the circular reveal animation will be applied.
 */
@Suppress("UNCHECKED_CAST")
abstract class CRSwitchBuilder<T : CRSwitchBuilder<T>>(@JvmField val view: View) {
    /**
     * The duration of the animation in milliseconds. Default is 400ms.
     */
    @JvmField
    var duration: Long = 400L

    /**
     * The interpolator to be used for the animation. Default is EaseInOutQuad.
     */
    @JvmField
    var interpolator: Interpolator = CubicBezierInterpolator.EaseInOutQuad

    /**
     * The OnClickListener for the view. Default is null.
     */
    @JvmField
    var onClickListener: OnClickListener? = null

    /**
     * Sets the duration of the animation.
     * @param duration The duration in milliseconds.
     * @return Returns the CRSwitchBuilder instance.
     */
    open fun duration(duration: Long): T = apply {
        this.duration = duration
    } as T

    /**
     * Sets the interpolator for the animation.
     * @param interpolator The interpolator to be used.
     * @return Returns the CRSwitchBuilder instance.
     */
    open fun interpolator(interpolator: Interpolator): T = apply {
        this.interpolator = interpolator
    } as T

    /**
     * Sets the OnClickListener for the view.
     * @param onClickListener The OnClickListener to be set.
     * @return Returns the CRSwitchBuilder instance.
     */
    open fun onClickListener(onClickListener: OnClickListener?): T = apply {
        this.onClickListener = onClickListener
    } as T

    abstract fun build(): Any
}