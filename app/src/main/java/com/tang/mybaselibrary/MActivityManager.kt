package com.tang.mybaselibrary

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * @author tanghongtu
 * @date 2020/9/15
 */
object MActivityManager {

    //GC ROOT对象 局部变量，静态变量，常量，native方法引用的对象
    var currentActivity: WeakReference<Activity>? = null
    val activityLifecycleCallback = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            this@MActivityManager.currentActivity = WeakReference(activity)
        }

        override fun onActivityResumed(activity: Activity) {
        }

    }

}