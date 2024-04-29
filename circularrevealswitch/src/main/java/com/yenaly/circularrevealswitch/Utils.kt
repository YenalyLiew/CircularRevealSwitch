@file:JvmName("Utils")

package com.yenaly.circularrevealswitch

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsControllerCompat
import java.lang.ref.WeakReference

/**
 * Extension property to get the Activity from a Context object.
 * It unwraps the Context object if it's a ContextWrapper.
 * Throws an IllegalStateException if the Context is not an Activity.
 */
internal val Context.activity: Activity
    get() {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        error("Activity not found")
    }

/**
 * Extension property to get the screen width in pixels.
 */
internal val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

/**
 * Extension property to get the screen height in pixels.
 */
internal val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

/**
 * Extension function to create a weak reference to an object.
 */
@Suppress("NOTHING_TO_INLINE")
internal inline fun <T> T.weak() = WeakReference(this)

/**
 * Property to check if the current theme is dark mode.
 * Returns true if the current theme is dark mode, false otherwise.
 */
val isNightMode: Boolean
    get() = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

/**
 * Extension property to get or set the appearance of status bars in a Window.
 * If set to true, the status bars will appear light.
 */
var Window.isAppearanceLightStatusBars: Boolean
    set(value) {
        val decorView = decorView
        val wic = WindowInsetsControllerCompat(this, decorView)
        wic.isAppearanceLightStatusBars = value
    }
    get() {
        val decorView = decorView
        val wic = WindowInsetsControllerCompat(this, decorView)
        return wic.isAppearanceLightStatusBars
    }