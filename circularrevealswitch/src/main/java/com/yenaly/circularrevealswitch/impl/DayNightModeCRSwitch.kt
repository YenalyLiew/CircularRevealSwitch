package com.yenaly.circularrevealswitch.impl

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.yenaly.circularrevealswitch.CRSwitchBuilder
import com.yenaly.circularrevealswitch.CircularRevealSwitch
import com.yenaly.circularrevealswitch.SwitchAnimation
import com.yenaly.circularrevealswitch.SwitchListener
import com.yenaly.circularrevealswitch.isNightMode
import com.yenaly.circularrevealswitch.screenHeight
import com.yenaly.circularrevealswitch.screenWidth

/**
 * DayNightModeCRSwitch is a subclass of CircularRevealSwitch that provides a switch for toggling between day and night modes.
 * It uses animations for the transition between the two modes.
 *
 * @property animToDayMode Animation to be used when switching to day mode.
 * @property animToNightMode Animation to be used when switching to night mode.
 * @property onDayModeAnimStart Runnable to be executed at the start of the day mode animation.
 * @property onDayModeAnimEnd Runnable to be executed at the end of the day mode animation.
 * @property onNightModeAnimStart Runnable to be executed at the start of the night mode animation.
 * @property onNightModeAnimEnd Runnable to be executed at the end of the night mode animation.
 * @property toNightMode Boolean indicating whether the switch is set to night mode.
 *
 * @constructor Creates a new DayNightModeCRSwitch with the specified builder.
 *
 * @param builder The builder used to construct the DayNightModeCRSwitch.
 */
open class DayNightModeCRSwitch(builder: Builder) :
    CircularRevealSwitch<DayNightModeCRSwitch.Builder>(builder) {

    @JvmField
    protected var animToDayMode: SwitchAnimation = builder.animToDayMode

    @JvmField
    protected var animToNightMode: SwitchAnimation = builder.animToNightMode

    @JvmField
    protected var onDayModeAnimStart: Runnable? = builder.onDayModeAnimStart

    @JvmField
    protected var onDayModeAnimEnd: Runnable? = builder.onDayModeAnimEnd

    @JvmField
    protected var onNightModeAnimStart: Runnable? = builder.onNightModeAnimStart

    @JvmField
    protected var onNightModeAnimEnd: Runnable? = builder.onNightModeAnimEnd

    @JvmField
    protected var toNightMode = false

    init {
        // Set up listeners for the start and end of the shrink and expand animations.
        onShrinkListener = object : SwitchListener {
            override fun onAnimStart() {
                if (toNightMode) onNightModeAnimStart?.run() else onDayModeAnimStart?.run()
            }

            override fun onAnimEnd() {
                if (toNightMode) onNightModeAnimEnd?.run() else onDayModeAnimEnd?.run()
            }
        }

        onExpandListener = object : SwitchListener {
            override fun onAnimStart() {
                if (toNightMode) onNightModeAnimStart?.run() else onDayModeAnimStart?.run()
            }

            override fun onAnimEnd() {
                if (toNightMode) onNightModeAnimEnd?.run() else onDayModeAnimEnd?.run()
            }
        }
    }

    override fun onClick(v: View) {
        if (!isViewClickable) return
        super.onClick(v)
        onDayNightModeClick(v)
    }

    /**
     * Handles a click on the switch, toggling between day and night modes and starting the appropriate animation.
     *
     * @param v The view that was clicked.
     */
    protected open fun onDayNightModeClick(v: View) {
        if (DEBUG) {
            Log.d(TAG, "onDayNightModeClick")
        }

        val screenshot = window.takeScreenshotCompat()
        toNightMode = !isNightMode

        AppCompatDelegate.setDefaultNightMode(
            if (toNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        handler.postCompat {
            reassignActivity()
            if (DEBUG) {
                Log.d(TAG, "post_time: ${System.currentTimeMillis() - beforeStartTime} ms")
            }
            animateDayNightMode(screenshot)
        }
    }

    /**
     * Starts the animation for the transition between day and night modes.
     *
     * @param screenshot A screenshot of the current window, used for the animation.
     */
    protected open fun animateDayNightMode(screenshot: Bitmap) {
        if (DEBUG) {
            Log.d(TAG, "animateDayNightMode")
            Log.d(TAG, buildString {
                appendLine("decorView - height: ${decorView.height}, width: ${decorView.width}")
                appendLine("decorView.rootView - height: ${decorView.rootView.height}, width: ${decorView.rootView.width}")
                appendLine("Screen height: ${applicationContext.screenHeight}, width: ${applicationContext.screenWidth}")
                appendLine("toNight: $toNightMode")
            })
        }

        val iv = createImageView(applicationContext)
        val radius = calcRadius(x, y)

        val animation = if (toNightMode) animToNightMode else animToDayMode
        if (animation === SwitchAnimation.SHRINK) {
            animateShrink(iv, screenshot, radius)
        } else {
            animateExpand(iv, screenshot, radius)
        }
    }

    /**
     * Builder class for creating a DayNightModeCRSwitch.
     *
     * @property animToNightMode Animation to be used when switching to night mode.
     * @property animToDayMode Animation to be used when switching to day mode.
     * @property onNightModeAnimStart Runnable to be executed at the start of the night mode animation.
     * @property onNightModeAnimEnd Runnable to be executed at the end of the night mode animation.
     * @property onDayModeAnimStart Runnable to be executed at the start of the day mode animation.
     * @property onDayModeAnimEnd Runnable to be executed at the end of the day mode animation.
     *
     * @constructor Creates a new Builder with the specified view.
     *
     * @param view The view to be used for the DayNightModeCRSwitch.
     */
    open class Builder(view: View) : CRSwitchBuilder<Builder>(view) {

        @JvmField
        var animToNightMode: SwitchAnimation = SwitchAnimation.EXPAND

        @JvmField
        var animToDayMode: SwitchAnimation = SwitchAnimation.SHRINK

        @JvmField
        var onNightModeAnimStart: Runnable? = null

        @JvmField
        var onNightModeAnimEnd: Runnable? = null

        @JvmField
        var onDayModeAnimStart: Runnable? = null

        @JvmField
        var onDayModeAnimEnd: Runnable? = null

        open fun animToNightMode(animToNight: SwitchAnimation) = apply {
            this.animToNightMode = animToNight
        }

        open fun animToDayMode(animToDay: SwitchAnimation) = apply {
            this.animToDayMode = animToDay
        }

        open fun onNightModeAnimStart(onNightModeAnimStart: Runnable?) = apply {
            this.onNightModeAnimStart = onNightModeAnimStart
        }

        open fun onNightModeAnimEnd(onNightModeAnimEnd: Runnable?) = apply {
            this.onNightModeAnimEnd = onNightModeAnimEnd
        }

        open fun onDayModeAnimStart(onDayModeAnimStart: Runnable?) = apply {
            this.onDayModeAnimStart = onDayModeAnimStart
        }

        open fun onDayModeAnimEnd(onDayModeAnimEnd: Runnable?) = apply {
            this.onDayModeAnimEnd = onDayModeAnimEnd
        }

        /**
         * Builds a new DayNightModeCRSwitch with the current settings of this builder.
         *
         * @return The newly created DayNightModeCRSwitch.
         */
        override fun build(): DayNightModeCRSwitch = DayNightModeCRSwitch(this)
    }
}