package com.yenaly.circularrevealswitch.impl

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import androidx.annotation.StyleRes
import androidx.core.app.ActivityCompat
import com.yenaly.circularrevealswitch.CRSwitchBuilder
import com.yenaly.circularrevealswitch.CircularRevealSwitch
import com.yenaly.circularrevealswitch.SwitchAnimation
import com.yenaly.circularrevealswitch.SwitchListener

/**
 * ThemeCRSwitch is a class that extends CircularRevealSwitch.
 * It provides functionality to switch themes with a circular reveal animation.
 *
 * @property toTheme The theme to switch to.
 * @property animToTheme The animation to use when switching themes.
 * @property onAnimStart A Runnable to be executed when the animation starts.
 * @property onAnimEnd A Runnable to be executed when the animation ends.
 *
 * @constructor Creates a new ThemeCRSwitch with the specified builder.
 *
 * @param builder The builder to use to construct the ThemeCRSwitch.
 */
open class ThemeCRSwitch(builder: Builder) :
    CircularRevealSwitch<ThemeCRSwitch.Builder>(builder) {

    @StyleRes
    @JvmField
    protected var toTheme: Int = builder.toTheme

    @JvmField
    protected var animToTheme: SwitchAnimation = builder.animToTheme

    @JvmField
    protected var onAnimStart: Runnable? = builder.onAnimStart

    @JvmField
    protected var onAnimEnd: Runnable? = builder.onAnimEnd

    init {
        onShrinkListener = object : SwitchListener {
            override fun onAnimStart() {
                onAnimStart?.run()
            }

            override fun onAnimEnd() {
                onAnimEnd?.run()
            }
        }

        onExpandListener = object : SwitchListener {
            override fun onAnimStart() {
                onAnimStart?.run()
            }

            override fun onAnimEnd() {
                onAnimEnd?.run()
            }
        }
    }

    companion object {

        @JvmStatic
        protected var newTheme: Int = -1

        /**
         * Sets the theme of the specified activity to the new theme.
         *
         * @param activity The activity whose theme to change.
         */
        @JvmStatic
        fun setTheme(activity: Activity) {
            if (newTheme != -1) {
                activity.setTheme(newTheme)
            }
        }
    }

    override fun onClick(v: View) {
        if (!isViewClickable) return
        super.onClick(v)
        onThemeClick(v)
    }

    /**
     * Handles a click on the theme switcher.
     *
     * @param v The view that was clicked.
     */
    protected open fun onThemeClick(v: View) {
        val screenshot = window.takeScreenshotCompat()
        if (newTheme == toTheme) return
        newTheme = toTheme
        ActivityCompat.recreate(activity.get()!!)
        handler.postCompat {
            reassignActivity()
            animateTheme(screenshot)
        }
    }

    /**
     * Animates the theme switch.
     *
     * @param screenshot The screenshot to use for the animation.
     */
    protected open fun animateTheme(screenshot: Bitmap) {
        val iv = createImageView(applicationContext)
        val radius = calcRadius(x, y)

        if (animToTheme === SwitchAnimation.SHRINK) {
            animateShrink(iv, screenshot, radius)
        } else {
            animateExpand(iv, screenshot, radius)
        }
    }

    /**
     * Builder is a class that builds a ThemeCRSwitch.
     *
     * @property toTheme The theme to switch to.
     * @property animToTheme The animation to use when switching themes.
     * @property onAnimStart A Runnable to be executed when the animation starts.
     * @property onAnimEnd A Runnable to be executed when the animation ends.
     *
     * @constructor Creates a new Builder with the specified view and theme.
     *
     * @param view The view to use for the ThemeCRSwitch.
     * @param toTheme The theme to switch to.
     */
    open class Builder(
        view: View,
        @StyleRes @JvmField val toTheme: Int,
    ) : CRSwitchBuilder<Builder>(view) {

        @JvmField
        var animToTheme: SwitchAnimation = SwitchAnimation.EXPAND

        @JvmField
        var onAnimStart: Runnable? = null

        @JvmField
        var onAnimEnd: Runnable? = null

        open fun animToTheme(animToTheme: SwitchAnimation) = apply {
            this.animToTheme = animToTheme
        }

        open fun onAnimStart(onAnimStart: Runnable?) = apply {
            this.onAnimStart = onAnimStart
        }

        open fun onAnimEnd(onAnimEnd: Runnable?) = apply {
            this.onAnimEnd = onAnimEnd
        }

        /**
         * Builds a new ThemeCRSwitch with the current settings.
         *
         * @return The newly built ThemeCRSwitch.
         */
        override fun build(): ThemeCRSwitch = ThemeCRSwitch(this)
    }
}