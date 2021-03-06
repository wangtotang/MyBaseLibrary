package com.tang.cryption

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Created by tanghongtu on 2020/6/22.
 *
 * AES对称加密
 */
object AESCipher {

    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private const val ALGORITHM = "AES"

    fun encrypt(input: String, key: String): String {
        //1.创建cipher
        val instance = Cipher.getInstance(TRANSFORMATION)
        //2.初始化cipher
        val secretKeySpec = SecretKeySpec(key.toByteArray(), ALGORITHM)
        instance.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        //3.dofinal
        val byteArray = instance.doFinal(input.toByteArray())
        return String(Base64.encode(byteArray, Base64.NO_WRAP))
    }

    fun decrypt(input: String, key: String): String {
        //1.创建cipher
        val instance = Cipher.getInstance(TRANSFORMATION)
        //2.初始化cipher
        val secretKeySpec = SecretKeySpec(key.toByteArray(), ALGORITHM)
        instance.init(Cipher.DECRYPT_MODE, secretKeySpec)
        //3.dofinal
        val byteArray = instance.doFinal(Base64.decode(input.toByteArray(), Base64.NO_WRAP))

        return String(byteArray)
    }

}