package com.tang.cryption

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * Created by tanghongtu on 2020/6/22.
 *
 * DES 对称加密
 */
object DESCipher {

    private const val TRANSFORMATION = "DES/ECB/PKCS5Padding"
    private const val ALGORITHM = "DES"

    /**
     * key必须是8的整数位
     */
    fun encrypt(input: String, key: String):String {

        //1.创建cipher
        val instance = Cipher.getInstance(TRANSFORMATION)
        //2.初始化cipher
        val secretKeySpec = DESKeySpec(key.toByteArray())
        val spec = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(secretKeySpec)
        instance.init(Cipher.ENCRYPT_MODE, spec)
        //3.dofinal
        val byteArray = instance.doFinal(input.toByteArray())

        return String(Base64.encode(byteArray, Base64.NO_WRAP))
    }

    fun decrypt(input: String, key: String):String {

        //1.创建cipher
        val instance = Cipher.getInstance(TRANSFORMATION)
        //2.初始化cipher
        val secretKeySpec = DESKeySpec(key.toByteArray())
        val spec = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(secretKeySpec)
        instance.init(Cipher.DECRYPT_MODE, spec)
        //3.dofinal
        val byteArray = instance.doFinal(Base64.decode(input.toByteArray(), Base64.NO_WRAP))

        return String(byteArray)
    }

}