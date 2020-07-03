package com.tang.webapp.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*

/**
 * h5与kotlin通信三种方式：
 * 1.h5调用kotlin
 * 2.kotlin调用h5
 * 3.callback方式（h5调用kotlin然后再调h5回传参数）
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mWebView = webView
        setWebView()
    }

    val setWebView = {
        //1.开启H5通信
        mWebView.settings.javaScriptEnabled = true
        //2.添加两个client
        mWebView.webViewClient = MyWebViewClient()
        mWebView.webChromeClient = MyWebChromeClient()
        //添加通信桥梁类
        mWebView.addJavascriptInterface(JavaScriptInterface(this@MainActivity, mWebView), "jsInterface")
        //3.加载网页
        mWebView.loadUrl("file:///android_asset/phonebook.html")
    }

    inner class MyWebViewClient : WebViewClient() {
        //加载完成后执行H5通信
        override fun onPageFinished(view: WebView?, url: String?) {
            //mWebView.loadUrl("javascript:myAlert()")
        }
    }

    class MyWebChromeClient : WebChromeClient() {
        //设置进度条
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
        }
    }
}
