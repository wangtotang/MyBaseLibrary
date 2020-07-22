package com.tang.cryption

import android.util.Base64
import java.security.Signature

/**
 * Created by tanghongtu on 2020/6/23.
 *
 * 数字签名: sha256withRSA
 */
object SHA256WithRSA {

    private const val ALGORITHM = "SHA256withRSA"

    fun signature(input: String, privateKey: String): String {
        //1.创建signature
        val instance = Signature.getInstance(ALGORITHM)
        //2.私钥初始化
        instance.initSign(RSACipher.getPrivateKey(privateKey))
        //3.设置数据源
        instance.update(input.toByteArray())
        //4.进行签名
        val sign = instance.sign()

        return String(Base64.encode(sign, Base64.NO_WRAP))
    }

    fun verify(source: String, signature: String, publicKey: String): Boolean {
        //1.创建signature
        val instance = Signature.getInstance(ALGORITHM)
        //2.公钥初始化
        instance.initVerify(RSACipher.getPublicKey(publicKey))
        //3.设置数据源
        instance.update(source.toByteArray())
        //4.进行验证
        return instance.verify(Base64.decode(signature.toByteArray(), Base64.NO_WRAP))
    }

}