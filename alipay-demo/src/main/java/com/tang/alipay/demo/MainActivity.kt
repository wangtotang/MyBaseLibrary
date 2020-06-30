package com.tang.alipay.demo

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alipay.sdk.app.PayTask
import com.tang.alipay.demo.utils.OrderInfoUtil2_0
import com.tang.alipay.demo.utils.PayResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
         const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPay.setOnClickListener {
            val money = etMoney.text.toString().toDoubleOrNull()
            onPay(money)
        }
    }

    private fun onPay(money: Double?) {

        //1.调用商户支付接口，传递参数（商品，支付，用户信息）
        //这里服务器会将参数按规则拼接并加签

        //2.解析返回json，获得第三方支付核心参数
        //这里简化直接加签并获得支付参数
        val orderInfo = requestServer()
        Log.d(TAG, orderInfo)
        //3.异步调用第三方支付
        Thread(Runnable {
            val alipay = PayTask(this)
            val result = alipay.payV2("charset=UTF-8&biz_content={\"out_trade_no\":\"202007010047202678141\",\"total_amount\":\"88.88\",\"subject\":\"iPhone+Xs+Max+256G\",\"buyer_id\":\"2088102175953034\",\"goods_detail\":[],\"seller_id\":\"2088102175107499\"}&method=alipay.trade.create&sign=N04JKdj9BGhtsTlZ/SzmHd9hJ1eQXDprDU280p+ft2eah6gH+hVCpZ6hDuQUwBMeKOu8Yd2fPy2UdpbLDYXoYBtdG1XEPyxj+LLEo51BMPDUd7tzkpL/7ILkWWEwoZzYsq8tFdtHtQ/waJs3OHdj4aRRdOYmgx2u4eS7fI5DXyLKhgVS2MgaK9zwyxt94GTJq77wRg01UdKBtnX7fSFM8AgVNmB8XFXJXzhJ/fW195/LlX8sJDBzsJNHIWuH43axXX7Z4UajC2BXVCmfJ8NjUVWBEArTAo3TuuJSbpX+p0uLB4C8L6NTcwo5mOq23zDLzJq5o4ABu0SrH8D7h7ATVA==&app_id=2016091100484533&sign_type=RSA2&version=1.0&timestamp=2020-07-01+00:47:20", true)

            //4.同步校验支付结果
            runOnUiThread {

                val payResult = PayResult(result)
                // 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                Log.d(TAG, "result:${payResult.result}")
                when (payResult.resultStatus) {
                    "9000" -> showToast("支付成功")
                    "6001" -> showToast("支付取消")
                    else -> showToast("支付失败")
                }
            }
        }).start()


    }

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }

    private fun requestServer(): String {
        val appId = "2021001170675147"
        val privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCRqv0haVAtLJz39kv2mRWvCgvF93JuTy/N+WcCT7O8qzE9d7XMkvQprgUTNUuL5lxjC/IYKWQ228PlX78yKhes1RwvaYPhxXc2HZFj9eeE2CA138I1McNt2st+5JvWd1aP2hCz4T0eQsknsl8TqaPwuVwih6GFH4jt3+1AYVcWuHf0ThA55UiN6hNdPraufXRIK/hdtByfrNfmm6RrWvxmkXrzygTdilh/gTY9yqBCa5FcSSgz4pe0zlhyL/+QLVYGw3fbWdiFn9FBckMDEXUXz+/tO9CTCl8IwZD1hHgNgW9jaZUHpndosn2mU2zhs5F0l4IMI3Qh17JOFZnQ70vhAgMBAAECggEAWpeSsZLDcDTMslGU0JpIKAIBbG8F1aCdxGWVMJTM3UtCZk3q3H2QJ9O4CEge4bS0RE0LlQc48f0fCanQSKxw6OXoj3v5rxGVoCwnXZlzEOUwsxCjwV1fgxSNdi9K08iIJxKhTJ2J2yYB4BRImPvpny5IJJlhVF9WU0nuTRm8hb3nOm/zYqrTHHvhmGsIfybwZeO3x1QfWH7VtlcJfwP6Q7BDb0R/5ykfmvcJMdTjMAc3BtuUvyBds/mRR3r+l+GAK+TD8x/ZAcU3sFhlUFBv4UqxBQR9lSh/ODGMJEgWoruGnBkzyBQD+wAuk6YPRwp3ZHl2ySPnwnwrfNCbXuM8gQKBgQDKGOdyMjIX96nftx2TObFSp3gsHB8gEzFO2j2RkOZ+hFcgez0LkD9j+ABSGEDk161PRtxeyjThOAGLv2oyRN9oj/sU9r/ObudPLKoBYCWxPqxk4TiAb5A1NIW+si8CxRVPJ71+v6ZeOktWuc5iCyR/fUWMhCvQjRNr9Tm72J3FeQKBgQC4hR4y4qQRDFbmlFmvyN2R9s0cvyBx77IaaGd4lGZ9W7LdnuCqsP2U0bpfqbBxDplIHOutcgmvrUGK4TMtqfh+UdUJg/BTncKi7vUq4dufKAynQBXWkPRAKOvppFK9Lq6R+Kr+scX2Ft+WDEjtdhwc8HfwQ4z1DjQZ+Nwd/SanqQKBgQCGfW5Ae5EEjCc7HqQK3sDXjWUWjZxU/xC4Ndn1LiWFmlJU5k5ojJTy1Q55k3RUFgdak4CBgDUSj+/PbRGh5F18OxzPBan9GabK9KEqCy7qNhgJhivQpO2lyo8aqX9Ja5hTtdGQML/pQwfx7fKfImUmhlorOaXiwo9jL609CdlsmQKBgCsUgVF2EsIcNGnPNwVQi1KZ4ZeVk/il8toxh/7h/u/DqgYOIvwXKI1HThEpqBUtSGJKxkePeW13sPbAz1cY+PhdGLUJBeIv0hKCYeEqLEGDczjVQIQBAdW8+B9rixm6PW29NgcS8FodSwRYR+qRCc1D42zICK/qKgXqUVH7GbxJAoGAfzgmqQPzvbTnn4fYMUGiPyJhvvgXuk5UKEn0rJNwvPYgCuiRaBiGaK+JPIUKejF1SNhIjovRnUTzHKZ+HYfT9haOFAv5j7uyohTdhMY51AgVpP499vN6g4dJ4eLIZSc6QSFDxEGfAWqUzHOuCSlqPsj3ReVQUQthrFPrqHiY/Ak="

        val params: Map<String, String> = OrderInfoUtil2_0.buildOrderParamMap(appId)
        val orderParam: String = OrderInfoUtil2_0.buildOrderParam(params)
        val sign: String = OrderInfoUtil2_0.getSign(params, privateKey, true)
        return "$orderParam&$sign"
    }
}
