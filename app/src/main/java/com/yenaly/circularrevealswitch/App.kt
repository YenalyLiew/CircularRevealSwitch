package com.yenaly.circularrevealswitch

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

/**
 * @project CircularRevealSwitch
 * @author Yenaly Liew
 * @time 2024/05/14 014 14:56
 */
class App : Application(), Application.ActivityLifecycleCallbacks {

    var time = 0L

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d("ActivityTime", "onActivityCreated: $activity, time: ${System.currentTimeMillis() - time}")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d("ActivityTime", "onActivityStarted: $activity, time: ${System.currentTimeMillis() - time}")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d("ActivityTime", "onActivityResumed: $activity, time: ${System.currentTimeMillis() - time}")
    }

    override fun onActivityPaused(activity: Activity) {
        time = System.currentTimeMillis()
        Log.d("ActivityTime", "onActivityPaused: $activity, time: ${System.currentTimeMillis() - time}")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d("ActivityTime", "onActivityStopped: $activity, time: ${System.currentTimeMillis() - time}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d("ActivityTime", "onActivityDestroyed: $activity, time: ${System.currentTimeMillis() - time}")
    }
}