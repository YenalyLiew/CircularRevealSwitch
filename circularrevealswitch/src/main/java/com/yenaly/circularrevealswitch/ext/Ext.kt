package com.yenaly.circularrevealswitch.ext

import android.view.View
import android.view.View.OnClickListener
import android.view.animation.Interpolator
import androidx.annotation.StyleRes
import com.yenaly.circularrevealswitch.SwitchAnimation
import com.yenaly.circularrevealswitch.impl.DayNightModeCRSwitch
import com.yenaly.circularrevealswitch.impl.ThemeCRSwitch

/**
 * Extension function for View to set a DayNightModeSwitcher.
 *
 * @param duration The duration of the animation. Default is 400ms.
 * @param interpolator The interpolator to be used for the animation.
 * @param animToDayMode The animation to be used when switching to day mode.
 * @param animToNightMode The animation to be used when switching to night mode.
 * @param onNightModeAnimStart The action to be performed when the animation to night mode starts.
 * @param onNightModeAnimEnd The action to be performed when the animation to night mode ends.
 * @param onDayModeAnimStart The action to be performed when the animation to day mode starts.
 * @param onDayModeAnimEnd The action to be performed when the animation to day mode ends.
 * @param onClick The action to be performed when the view is clicked.
 */
fun View.setDayNightModeSwitcher(
    duration: Long = -1,
    interpolator: Interpolator? = null,
    animToDayMode: SwitchAnimation? = null,
    animToNightMode: SwitchAnimation? = null,
    onNightModeAnimStart: Runnable? = null,
    onNightModeAnimEnd: Runnable? = null,
    onDayModeAnimStart: Runnable? = null,
    onDayModeAnimEnd: Runnable? = null,
    onClick: OnClickListener? = null,
) {
    val switcher = DayNightModeCRSwitch.Builder(this).apply {
        if (duration > -1L) this.duration(duration)
        interpolator?.let(this::interpolator)
        animToDayMode?.let(this::animToDayMode)
        animToNightMode?.let(this::animToNightMode)
        this.onNightModeAnimStart(onNightModeAnimStart)
        this.onNightModeAnimEnd(onNightModeAnimEnd)
        this.onDayModeAnimStart(onDayModeAnimStart)
        this.onDayModeAnimEnd(onDayModeAnimEnd)
        this.onClickListener(onClick)
    }.build()
    switcher.setSwitcher()
}

/**
 * Extension function for View to set a ThemeSwitcher.
 *
 * @param toTheme The theme to be switched to.
 * @param duration The duration of the animation. Default is 400ms.
 * @param interpolator The interpolator to be used for the animation.
 * @param animToTheme The animation to be used when switching to the theme.
 * @param onAnimStart The action to be performed when the animation starts.
 * @param onAnimEnd The action to be performed when the animation ends.
 * @param onClick The action to be performed when the view is clicked.
 */
fun View.setThemeSwitcher(
    @StyleRes toTheme: Int,
    duration: Long = -1,
    interpolator: Interpolator? = null,
    animToTheme: SwitchAnimation? = null,
    onAnimStart: Runnable? = null,
    onAnimEnd: Runnable? = null,
    onClick: OnClickListener? = null,
) {
    val switcher = ThemeCRSwitch.Builder(this, toTheme).apply {
        if (duration > -1L) this.duration(duration)
        interpolator?.let(this::interpolator)
        animToTheme?.let(this::animToTheme)
        this.onAnimStart(onAnimStart)
        this.onAnimEnd(onAnimEnd)
        this.onClickListener(onClick)
    }.build()
    switcher.setSwitcher()
}