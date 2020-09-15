package com.tang.mybaselibrary

import android.app.Activity
import android.view.View
import android.view.ViewGroup

/**
 * @author tanghongtu
 * @date 2020/9/15
 */
class ActivityExt(val activity: Activity) {

    private var contentParent: ViewGroup = activity.findViewById<ViewGroup>(android.R.id.content)

    fun addContentView(view: View, params: ViewGroup.LayoutParams) {
        contentParent.addView(view, params)
    }

    fun removeContentView(view: View){
        contentParent.removeView(view)
    }

}