package com.yenaly.circularrevealswitch

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import java.lang.ref.WeakReference

/**
 * This is an implementation of the ActivityLifecycleCallbacks interface.
 * It is used to track the lifecycle of the current activity.
 * The current activity is stored as a weak reference to prevent memory leaks.
 * This class is particularly useful in scenarios where the DecorView changes after the activity is recreated,
 * as it allows us to get the new activity and obtain the new DecorView.
 *
 * In some devices, the DecorView changes after Activity is recreated,
 * which prevents the smooth reuse of DecorView.
 * To ensure compatibility, we use ActivityLifecycleCallback to get the new activity
 * and obtain the new DecorView.
 */
object CRActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {

    var currentActivity: WeakReference<Activity>? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity.application.unregisterActivityLifecycleCallbacks(this)
        if (BuildConfig.ENABLE_LOGGING) {
            Log.d("CRActivityLifecycleCallback", "onActivityCreated: $activity")
        }
        currentActivity = activity.weak()
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        if (BuildConfig.ENABLE_LOGGING) {
            Log.d("CRActivityLifecycleCallback", "onActivityDestroyed: $activity")
        }
        currentActivity?.clear()
    }
}