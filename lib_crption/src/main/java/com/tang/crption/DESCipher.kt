package com.tang.crption

import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * Created by tanghongtu on 2020/6/22.
 *
 * DES加密
 */
object DESCipher {

    private val transformation = "DES/ECB/PKCS5Padding"
    private val algorithm = "DES"

    /**
     * key必须是8的整数位
     */
    fun encrypt(input: String, key: String):String {

        //1.创建cipher
        val instance = Cipher.getInstance(transformation)
        //2.初始化cipher
        val secretKeySpec = DESKeySpec(key.toByteArray())
        val spec = SecretKeyFactory.getInstance(algorithm).generateSecret(secretKeySpec)
        instance.init(Cipher.ENCRYPT_MODE, spec)
        //3.dofinal
        val byteArray = instance.doFinal(input.toByteArray())

        return String(Base64.getEncoder().encode(byteArray))
    }

    fun decrypt(input: String, key: String):String {

        //1.创建cipher
        val instance = Cipher.getInstance(transformation)
        //2.初始化cipher
        val secretKeySpec = DESKeySpec(key.toByteArray())
        val spec = SecretKeyFactory.getInstance(algorithm).generateSecret(secretKeySpec)
        instance.init(Cipher.DECRYPT_MODE, spec)
        //3.dofinal
        val byteArray = instance.doFinal(Base64.getDecoder().decode(input.toByteArray()))

        return String(byteArray)
    }

}