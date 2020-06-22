package com.tang.crption

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Created by tanghongtu on 2020/6/22.
 *
 * AES加密
 */
object AESCipher {

    private val transformation = "AES/ECB/PKCS5Padding"
    private val algorithm = "AES"

    fun encrypt(input: String, key: String): String {
        //1.创建cipher
        val instance = Cipher.getInstance(transformation)
        //2.初始化cipher
        val secretKeySpec = SecretKeySpec(key.toByteArray(), algorithm)
        instance.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        //3.dofinal
        val byteArray = instance.doFinal(input.toByteArray())

        return String(Base64.getEncoder().encode(byteArray))
    }

    fun decrypt(input: String, key: String): String {
        //1.创建cipher
        val instance = Cipher.getInstance(transformation)
        //2.初始化cipher
        val secretKeySpec = SecretKeySpec(key.toByteArray(), algorithm)
        instance.init(Cipher.DECRYPT_MODE, secretKeySpec)
        //3.dofinal
        val byteArray = instance.doFinal(Base64.getDecoder().decode(input.toByteArray()))

        return String(byteArray)
    }

}