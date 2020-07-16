package com.tang.https.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.io.ByteArrayOutputStream
import java.net.URL
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onRequest(view: View) {
        Thread(Runnable {

            //1.打开链接
            val url = URL("https://10.0.2.2:8443/alipay.json")
            val httpsURLConnection = url.openConnection() as HttpsURLConnection

            //https
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())

            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)

            val inputs = assets.open("alipay.cer")
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val cer = certificateFactory.generateCertificate(inputs)
            keyStore.setCertificateEntry("alipay", cer)

            trustManagerFactory.init(keyStore)
            val trustManager = trustManagerFactory.trustManagers

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManager, null)
            httpsURLConnection.sslSocketFactory = sslContext.socketFactory
            httpsURLConnection.hostnameVerifier = HostnameVerifier { hostname, session ->
                true
            }

            //2.读取并输出
            val inputStream = httpsURLConnection.inputStream
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = inputStream.read(buffer)
            while (len != -1) {
                outputStream.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
            val toString = outputStream.toString()
            println(toString)

        }).start()

    }


}
