package com.tang.https.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onRequest(view: View) {
        Thread(Runnable {


        }).start()

    }


}
