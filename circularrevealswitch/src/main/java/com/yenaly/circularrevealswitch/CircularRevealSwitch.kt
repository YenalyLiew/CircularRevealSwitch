package com.yenaly.circularrevealswitch

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Interpolator
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.core.animation.addListener
import androidx.core.view.children
import androidx.core.view.drawToBitmap
import androidx.core.view.isInvisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference
import kotlin.math.hypot

/**
 * This class is used to create a circular reveal switch animation.
 * It is an abstract class that implements View.OnTouchListener.
 * It has a companion object that contains a debug flag and a tag for logging.
 * It also has an enum class Animation that defines the types of animations that can be performed.
 *
 * @param crSwitchBuilder The builder object that contains the configuration for the circular reveal switch.
 */
abstract class CircularRevealSwitch<T : CRSwitchBuilder<T>>(crSwitchBuilder: T) :
    View.OnTouchListener, OnClickListener, LifecycleEventObserver {

    companion object {
        const val TAG = "CircularRevealSwitch"
        internal const val DEBUG = BuildConfig.ENABLE_LOGGING

        /**
         *
         */
        @JvmStatic
        protected var isViewClickable = true
    }

    // Interpolator for the animation
    @JvmField
    protected var interpolator: Interpolator = crSwitchBuilder.interpolator

    // Duration of the animation
    @JvmField
    protected var duration: Long = crSwitchBuilder.duration

    // The view on which the animation is to be performed
    // after recreating the view, the view reference might be lost
    @JvmField
    protected val view: WeakReference<View> = crSwitchBuilder.view.weak()

    // The context of the view
    // after recreating the view, the context reference might be lost
    @JvmField
    protected val context: WeakReference<Context> = view.get()!!.context.weak()

    // The application context of the context
    @JvmField
    protected val applicationContext: Context = context.get()!!.applicationContext

    // The activity of the context
    @JvmField
    protected val activity: WeakReference<Activity> = context.get()!!.activity.weak()

    // The window of the activity
    @JvmField
    protected val window: WeakReference<Window> = activity.get()!!.window.weak()

    // The decor view of the window
    @JvmField
    protected val decorView: ViewGroup = window.get()!!.decorView as ViewGroup

    // OnClickListener for the view
    @JvmField
    protected var onClickListener: OnClickListener? = crSwitchBuilder.onClickListener

    // Handler for the main looper
    @JvmField
    protected val handler = Handler(Looper.getMainLooper())

    // Listener for the shrink animation
    @JvmField
    protected var onShrinkListener: SwitchListener? = null

    // Listener for the expand animation
    @JvmField
    protected var onExpandListener: SwitchListener? = null

    // X and Y coordinates for the animation
    @JvmField
    protected var x = 0F

    @JvmField
    protected var y = 0F

    @JvmField
    protected var locationInWindow = IntArray(2)

    /**
     * This method sets the switcher for the circular reveal animation.
     */
    open fun setSwitcher() {
        if (DEBUG) {
            Log.d(
                TAG, "init -> " +
                        "activity: ${activity.get()}, " +
                        "window: ${window.get()}, " +
                        "decorView: $decorView"
            )
        }
        val activityInstance = activity.get()!!
        if (activityInstance is ComponentActivity) {
            activityInstance.lifecycle.addObserver(this)
        }
        view.get()!!.setOnTouchListener(this)
        view.get()!!.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        onClickListener?.onClick(v)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                // Do not use event.rawX and event.rawY
                // It is not accurate in floating window mode
                // x = event.rawX
                // y = event.rawY
                v.getLocationInWindow(locationInWindow)
                x = event.x + locationInWindow[0]
                y = event.y + locationInWindow[1]
                if (DEBUG) {
                    Log.d(TAG, "onTouch: x = $x, y = $y")
                }
            }
        }
        return false
    }

    /**
     * This method takes a screenshot of the window.
     *
     * @return Bitmap
     */
    protected open fun Window.screenshot() = decorView.rootView.drawToBitmap()

    /**
     * This method creates an ImageView with match_parent layout parameters.
     *
     * @param context The context to create the ImageView in.
     * @return ImageView
     */
    protected open fun createImageView(context: Context): ImageView {
        return ImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    /**
     * This method performs the shrink animation.
     *
     * @param iv The ImageView to perform the animation on.
     * @param screenshot The screenshot to set as the bitmap of the ImageView.
     * @param radius The radius of the circular reveal.
     */
    protected open fun animateShrink(iv: ImageView, screenshot: Bitmap, radius: Float) {
        iv.setImageBitmap(screenshot)
        decorView.addView(iv, -1)

        if (DEBUG) {
            decorView.children.forEach {
                Log.d(TAG, "animateShrink: $it")
            }
        }

        createShrinkAnimator(
            iv,
            x.toInt(), y.toInt(),
            radius, 0F
        ).apply {
            interpolator = this@CircularRevealSwitch.interpolator
            duration = this@CircularRevealSwitch.duration
            addListener(onStart = {
                isViewClickable = false
                onShrinkListener?.onAnimStart()
            }, onEnd = {
                isViewClickable = true
                decorView.removeView(iv)
                onShrinkListener?.onAnimEnd()
                if (DEBUG) {
                    Log.d(
                        TAG, "shrink-end -> " +
                                "activity: ${activity.get()}, " +
                                "window: ${window.get()}, " +
                                "decorView: $decorView"
                    )
                }
            }, onCancel = {
                isViewClickable = true
                decorView.removeView(iv)
                onShrinkListener?.onAnimCancel()
            })
        }.start()
    }

    /**
     * This method performs the expand animation.
     *
     * @param iv The ImageView to perform the animation on.
     * @param screenshot The screenshot to set as the bitmap of the ImageView.
     * @param radius The radius of the circular reveal.
     */
    protected open fun animateExpand(iv: ImageView, screenshot: Bitmap, radius: Float) {
        decorView.addView(iv, 0)

        if (DEBUG) {
            decorView.children.forEach {
                Log.d(TAG, "animateExpand: $it")
            }
        }

        decorView.isInvisible = true
        createExpandAnimator(
            decorView,
            x.toInt(), y.toInt(),
            0F, radius
        ).apply {
            interpolator = this@CircularRevealSwitch.interpolator
            duration = this@CircularRevealSwitch.duration
            addListener(onStart = {
                isViewClickable = false
                decorView.isInvisible = false
                onExpandListener?.onAnimStart()
            }, onEnd = {
                isViewClickable = true
                decorView.removeView(iv)
                onExpandListener?.onAnimEnd()
                if (DEBUG) {
                    Log.d(
                        TAG, "expand-end -> " +
                                "activity: ${activity.get()}, " +
                                "window: ${window.get()}, " +
                                "decorView: $decorView"
                    )
                }
            }, onCancel = {
                isViewClickable = true
                decorView.removeView(iv)
                onExpandListener?.onAnimCancel()
            })
        }.start()
    }

    /**
     * This method calculates the radius for the circular reveal.
     *
     * @param x The x coordinate for the circular reveal.
     * @param y The y coordinate for the circular reveal.
     * @return Float
     */
    protected open fun calcRadius(x: Float, y: Float): Float {
        if (DEBUG) {
            Log.d(
                TAG, "calcRadius -> " +
                        "activity: ${activity.get()}, " +
                        "window: ${window.get()}, " +
                        "decorView: $decorView"
            )
        }
        val screenHeight = decorView.height
        val screenWidth = decorView.width
        val topLeft =
            hypot(x.toDouble(), y.toDouble()).toFloat()
        val topRight =
            hypot((screenWidth - x).toDouble(), y.toDouble()).toFloat()
        val bottomLeft =
            hypot(x.toDouble(), (screenHeight - y).toDouble()).toFloat()
        val bottomRight =
            hypot((screenWidth - x).toDouble(), (screenHeight - y).toDouble()).toFloat()

        return maxOf(topLeft, topRight, bottomLeft, bottomRight)
    }

    /**
     * This method creates an Animator for the expand animation.
     * It uses the ViewAnimationUtils.createCircularReveal() method to create a circular reveal animation.
     *
     * @param view The view on which the animation is to be performed.
     * @param centerX The x coordinate of the center of the circular reveal.
     * @param centerY The y coordinate of the center of the circular reveal.
     * @param startRadius The starting radius of the circular reveal.
     * @param endRadius The ending radius of the circular reveal.
     * @return Animator The Animator object for the expand animation.
     */
    protected open fun createExpandAnimator(
        view: View,
        centerX: Int, centerY: Int,
        startRadius: Float, endRadius: Float,
    ): Animator {
        return ViewAnimationUtils.createCircularReveal(
            view,
            centerX, centerY,
            startRadius, endRadius
        )
    }

    /**
     * This method creates an Animator for the shrink animation.
     * It uses the ViewAnimationUtils.createCircularReveal() method to create a circular reveal animation.
     *
     * @param view The view on which the animation is to be performed.
     * @param centerX The x coordinate of the center of the circular reveal.
     * @param centerY The y coordinate of the center of the circular reveal.
     * @param startRadius The starting radius of the circular reveal.
     * @param endRadius The ending radius of the circular reveal.
     * @return Animator The Animator object for the shrink animation.
     */
    protected open fun createShrinkAnimator(
        view: View,
        centerX: Int, centerY: Int,
        startRadius: Float, endRadius: Float,
    ): Animator {
        return ViewAnimationUtils.createCircularReveal(
            view,
            centerX, centerY,
            startRadius, endRadius
        )
    }

    /**
     * This function is used to clear the listeners attached to the view.
     * It sets the onTouchListener and onClickListener of the view to null.
     */
    open fun clearListeners() {
        view.get()?.setOnTouchListener(null)
        view.get()?.setOnClickListener(null)
    }

    /**
     * This function is an override of the onStateChanged function from the LifecycleEventObserver interface.
     * It is called when a lifecycle event occurs.
     *
     * @param source The LifecycleOwner whose state has changed.
     * @param event The Lifecycle.Event that has occurred.
     *
     * In the case of the ON_DESTROY event, it calls the clearListeners function to remove the listeners from the view,
     * and logs that the listeners have been removed if the DEBUG flag is set to true.
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                clearListeners()
                if (DEBUG) {
                    Log.d(TAG, "listeners have been removed")
                }
            }

            else -> Unit
        }
    }
}