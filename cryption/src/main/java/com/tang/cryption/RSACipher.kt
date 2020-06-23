package com.tang.cryption

import java.io.ByteArrayOutputStream
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * Created by tanghongtu on 2020/6/22.
 *
 * RSA非对称加密
 */
object RSACipher {

    private const val TRANSFORMATION: String = "RSA"
    private const val ALGORITHM: String = "RSA"
    private const val ENCRYPT_MAX_VALUE = 245
    private const val DECRYPT_MAX_VALUE = 256

    fun generateKeyPair():Pair<String, String> {
        val generator = KeyPairGenerator.getInstance(TRANSFORMATION)
        val genKeyPair = generator.genKeyPair()
        return Pair(String(Base64.getEncoder().encode(genKeyPair.public.encoded)),
            String(Base64.getEncoder().encode(genKeyPair.private.encoded)))
    }

    fun encryptWithPrivateKey(input: String, privateKey: String): String {
        val byteArray = doCrypt(getPrivateKey(privateKey), Cipher.ENCRYPT_MODE, input.toByteArray(), ENCRYPT_MAX_VALUE)
        return String(Base64.getEncoder().encode(byteArray))
    }

    fun decryptWithPublicKey(input: String, publicKey: String): String {
        val byteArray = doCrypt(getPublicKey(publicKey), Cipher.DECRYPT_MODE, Base64.getDecoder().decode(input.toByteArray()), DECRYPT_MAX_VALUE)
        return String(byteArray)
    }

    fun encryptWithPublicKey(input: String, publicKey: String): String {
        val byteArray = doCrypt(getPublicKey(publicKey), Cipher.ENCRYPT_MODE, input.toByteArray(), ENCRYPT_MAX_VALUE)
        return String(Base64.getEncoder().encode(byteArray))
    }

    fun decryptWithPrivateKey(input: String, privateKey: String): String {
        val byteArray = doCrypt(getPrivateKey(privateKey), Cipher.DECRYPT_MODE, Base64.getDecoder().decode(input.toByteArray()), DECRYPT_MAX_VALUE)
        return String(byteArray)
    }

    fun getPublicKey(publicKey: String): PublicKey {
        val instance = KeyFactory.getInstance(ALGORITHM)
        val keySpec = X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.toByteArray()))
        return instance.generatePublic(keySpec)
    }

    fun getPrivateKey(privateKey: String): PrivateKey {
        val instance = KeyFactory.getInstance(ALGORITHM)
        val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.toByteArray()))
        return instance.generatePrivate(keySpec)
    }

    private fun doCrypt(
        key: Key,
        mode: Int,
        input: ByteArray,
        maxValue: Int
    ): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(mode, key)
        var offset = 0
        val bos = ByteArrayOutputStream()
        var bufferArray: ByteArray? = null
        bos.use {
            while (input.size - offset > 0) {
                if (input.size - offset >= maxValue) {
                    bufferArray = cipher.doFinal(input, offset, maxValue)
                    offset += maxValue
                } else {
                    bufferArray =
                        cipher.doFinal(input, offset, input.size - offset)
                    offset = input.size
                }
                it.write(bufferArray)
            }
        }
        return bos.toByteArray()
    }

}