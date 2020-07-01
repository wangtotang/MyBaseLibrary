package com.tang.alipay.demo

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alipay.sdk.app.EnvUtils
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
        //沙箱测试
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX)

        btnPay.setOnClickListener {
            val money = etMoney.text.toString()
            onPay(money)
        }
    }

    private fun onPay(money: String) {
        if (TextUtils.isEmpty(money)) {
            showToast("金额不能为空")
            return
        }

        //1.调用商户支付接口，传递参数（商品，支付，用户信息）
        //这里服务器会将参数按规则拼接并加签

        //2.解析返回json，获得第三方支付核心参数
        //这里简化直接加签并获得支付参数
        val orderInfo = requestServer(money)
        Log.d(TAG, orderInfo)
        //3.异步调用第三方支付
        Thread(Runnable {
            val alipay = PayTask(this)
            val result = alipay.payV2(orderInfo, true)

            //4.同步校验支付结果
            runOnUiThread {

                val payResult = PayResult(result)
                // 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                Log.d(TAG, "result:${payResult.resultStatus}")

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

    private fun requestServer(money: String): String {
        //沙箱账号
        val appId = "2016102600762753"
        val privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC0ncsO5o8JwVkZdYQA8UsfypZ05QDmHTtjgY5qO4hr5RzLWRtMEr2XoICU5wbzn6bYTGmF3N9A3XsOytlguMft5e3x2LMcwzjQsm6EbtFOz3kOo95U0qCS+ZGrl9UD8odKbU6bBjmPZbZDlyXjXCGXYhg7p2uJzbl+V0cf3AX2enb9FAHkpDiK/hOT0q3yLW1FCE4nwm5BB22JBS/ok8P/gd9XqxAc3YMxXwEi6NfHA4wWW7qHPHtTws0z6vDIdcpgFmO+ixoOC+tyfSrL3qzC7v7XNTq13UenYR68uQuswbgKMZtDPhEuEu1mM1bi2Z3Hx9yc7kbfj81rU12TPzZXAgMBAAECggEAGttPw2JzKH8wkYGQ5Fb81bhZEP6wWIdGOnCcTZ2WlnyWAIK6dWBgArjiBcIonXdC5uzN0BLcu5dzYeq10hUH3Y8QB6MyVveMbjPqDRUxWGevV4b0W2aKa/BiQcKs8Q5SIbYJNzIAwxrbtEc4YgtkD0+NzOkDK5WEnI27zLf5xwGEkJ2ekFvsijpLna4hlutbxvygxYvvGVWlXwZMAOmpwmYV/Yos4syNvcYjJZz9MNm1+ycZeEJYklmZ7a/vAs5OoHG2VwlL/lvQOUv1BWVo87jAXjg0KD9W43vtzW25Vdf99yEjgi94kOgeXiuHe+UYY4XroZc/HGBuiq1CbhfBEQKBgQDr73yQCU+XnlbDEqlP0eRvP6ASKzkrMVPqPXlPF6RDiEud+ND7MHiJo8OT8XwHsgnQfmSQzVYirbTwtdmNrUcdvVg10x9RL/x4qrjR5RyV5MupA6ueAGqOJFs3yvv8DMZeLQpfOb2yppnoFZLFL0M3BElpd4ya8EN+dBNWpf4TuQKBgQDD+faQMvQ/P7/hg9JNXVZzTpxVdrLotk+/YgrJiRifb60x9b3iChFerv/VBKhYc/8LMeelvKL+p/Dnkh2O2jVjWWosiK6dS3QFO2b5y464+cgftgw3UkrlXGKFrw0+L7+Q1/L3iwMzJc9B8DF05glnTI/jkVPjRN8AaoBVOmDCjwKBgE7tWtpEgx/RlGzFvWY6bRTaUA5D72gHpG7kCcTvv7/nxIQgTKj7WheRdUf0rO1WHfpljfby+jG3sqQj8Zd3tCa6xNucuQlL6EXlttZgsuNiGs97rtm4gezzNKDBgvKaw3GV17Zrdo5hNXNyZBsa6H1rLbPejmb2oV2YLy/5b7nhAoGAA8w2QPkbzpX3q/EYBDPI1xcw5F8WfHf796N4vgsQFRbe7lbqxRSY3ZLallAuyvgEez0OViHfrrumaXZlaD1GR5SWACLt71Jk/yz1g+Wz8FW20RaAChpoJSWzKxAHW6slNTNaG/3vo/AZohd7fd5vVDchmrFYRNEy2Rk3P2+QLHUCgYBJnHKF8W4qmq/rZmsKzZu7VYLl5cP5UcrKqRXybq+jYnNRq8L5GOjfmmLGtc2HYzonYAMe6ClBqvFbMPnGoSFOj1FKccWmvp1baLsIqhhhcsRTSdud0jrTo0h7PFd+HHh6vSr9HV9u9qfWQ9mflsUCEcfhuQ0Yg5VOg/Vdhj9smw=="

        val params: Map<String, String> = OrderInfoUtil2_0.buildOrderParamMap(appId, "测试支付", money)
        val orderParam: String = OrderInfoUtil2_0.buildOrderParam(params)
        val sign: String = OrderInfoUtil2_0.getSign(params, privateKey, true)
        return "$orderParam&$sign"
    }
}
