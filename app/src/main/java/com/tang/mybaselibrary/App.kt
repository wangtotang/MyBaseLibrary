package com.tang.mybaselibrary

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

/**
 * @author tanghongtu
 * @date 2020/9/15
 */
class App : Application() {
    
    companion object {
        private const val TAG = "App"
    }

    //注册activity的生命周期回调获得activity，也可以通过BaseActivity的方式，但是这个更通用
    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d(TAG, "onActivityCreated: ")
        }

        override fun onActivityStarted(activity: Activity) {
            Log.d(TAG, "onActivityStarted: ")
        }

        override fun onActivityResumed(activity: Activity) {
            Log.d(TAG, "onActivityResumed: ")
        }

        override fun onActivityPaused(activity: Activity) {
            Log.d(TAG, "onActivityPaused: ")
        }

        override fun onActivityStopped(activity: Activity) {
            Log.d(TAG, "onActivityStopped: ")
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            Log.d(TAG, "onActivitySaveInstanceState: ")
        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onActivityDestroyed: ")
        }

    }
    
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }
    
}