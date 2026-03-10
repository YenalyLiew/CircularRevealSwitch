package com.yenaly.circularrevealswitch

import android.app.Activity
import android.app.Application
import android.content.Intent
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
class CRActivityLifecycleCallback(
    private val activityName: String,
    private val targetUuid: String,
    private val onMatched: (Activity) -> Unit
) : Application.ActivityLifecycleCallbacks {

    companion object {
        const val TAG = "CRActivityLifecycleCallback"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val intentUuid = activity.intent.getStringExtra(CircularRevealSwitch.KEY_RECREATE_UUID)
        if (BuildConfig.ENABLE_LOGGING) {
            Log.d(TAG, "onActivityCreated: $activity, received uuid: $intentUuid")
        }
        if (activityName == activity.javaClass.name && intentUuid == targetUuid) {
            if (BuildConfig.ENABLE_LOGGING) {
                Log.d(TAG, "Activity matched successfully!")
            }
            activity.intent.removeExtra(CircularRevealSwitch.KEY_RECREATE_UUID)
            activity.application.unregisterActivityLifecycleCallbacks(this)
            onMatched(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        if (BuildConfig.ENABLE_LOGGING) {
            Log.d(TAG, "onActivityDestroyed: $activity")
        }
    }
}