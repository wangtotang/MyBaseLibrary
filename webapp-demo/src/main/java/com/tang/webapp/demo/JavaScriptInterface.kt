package com.tang.webapp.demo

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

/**
 * Created by tanghongtu on 2020/7/2.
 */
class JavaScriptInterface(val context: Context) {

    @JavascriptInterface
    fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

}