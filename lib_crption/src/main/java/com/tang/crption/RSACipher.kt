package com.tang.crption

import java.io.ByteArrayOutputStream
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * Created by tanghongtu on 2020/6/22.
 */
object RSACipher {

    private val transformation: String = "RSA"
    private val algorithm: String = "RSA"
    private const val ENCRYPT_MAX_VALUE = 245
    private const val DECRYPT_MAX_VALUE = 256

    fun generateKey():Pair<String, String> {
        val generator = KeyPairGenerator.getInstance(transformation)
        val genKeyPair = generator.genKeyPair()
        return Pair(String(Base64.getEncoder().encode(genKeyPair.public.encoded)),
            String(Base64.getEncoder().encode(genKeyPair.private.encoded)))
    }

    fun encryptWithPrivateKey(input: String, privateKey: String): String {
        val instance = KeyFactory.getInstance(algorithm)
        val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.toByteArray()))
        val byteArray = doCrypt(instance.generatePrivate(keySpec), Cipher.ENCRYPT_MODE, input.toByteArray(), ENCRYPT_MAX_VALUE)
        return String(Base64.getEncoder().encode(byteArray))
    }

    fun decryptWithPublicKey(input: String, publicKey: String): String {
        val instance = KeyFactory.getInstance(algorithm)
        val keySpec = X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.toByteArray()))
        val byteArray = doCrypt(instance.generatePublic(keySpec), Cipher.DECRYPT_MODE, Base64.getDecoder().decode(input.toByteArray()), DECRYPT_MAX_VALUE)
        return String(byteArray)
    }

    fun encryptWithPublicKey(input: String, publicKey: String): String {
        val instance = KeyFactory.getInstance(algorithm)
        val keySpec = X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.toByteArray()))
        val byteArray = doCrypt(instance.generatePublic(keySpec), Cipher.ENCRYPT_MODE, input.toByteArray(), ENCRYPT_MAX_VALUE)
        return String(Base64.getEncoder().encode(byteArray))
    }

    fun decryptWithPrivateKey(input: String, privateKey: String): String {
        val instance = KeyFactory.getInstance(algorithm)
        val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.toByteArray()))
        val byteArray = doCrypt(instance.generatePrivate(keySpec), Cipher.DECRYPT_MODE, Base64.getDecoder().decode(input.toByteArray()), DECRYPT_MAX_VALUE)
        return String(byteArray)
    }

    private fun doCrypt(
        key: Key,
        mode: Int,
        input: ByteArray,
        maxValue: Int
    ): ByteArray {
        val cipher = Cipher.getInstance(transformation)
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