package com.tang.webapp.demo

import android.app.Activity
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import org.json.JSONObject

/**
 * Created by tanghongtu on 2020/7/2.
 */
class JavaScriptInterface(val context: Activity, val webView: WebView) {

    private val bottomDialog: BottomDialog by lazy {
        BottomDialog(context)
    }

    @JavascriptInterface
    fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    @JavascriptInterface
    fun getPhoneNumber(string: String) {
        val json = JSONObject(string)
        val method = json.optString("callback")
        Thread(Runnable {
            //从服务器获取json
            val json = "{\"phoneNumber\":\"13313131314\"}"
            context.runOnUiThread {
                webView.loadUrl("javascript:$method($json)")
            }
        }).start()
    }

    @JavascriptInterface
    fun dial(phone: String) {
        bottomDialog.setPhoneNumber(phone)
        bottomDialog.show()
    }

}