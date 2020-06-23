package com.tang.cryption

import java.security.MessageDigest
import kotlin.experimental.and

/**
 * Created by tanghongtu on 2020/6/23.
 *
 * 消息摘要
 */
object MessageDigestUtils {

    fun md5(input: String): String {
        return digest(input, "MD5")
    }

    fun sha1(input: String): String {
        return digest(input, "SHA-1")
    }
    fun sha256(input: String): String {
        return digest(input, "SHA-256")
    }

    private fun digest(input: String, algorithm: String): String {
        val md = MessageDigest.getInstance(algorithm)
        val digest = md.digest(input.toByteArray())
        val stringBuilder = StringBuilder()
        digest.forEach {
            val hexInt: Int = it.toInt() and 0xFF
            val hexString = Integer.toHexString(hexInt)
            if (hexString.length == 1) {
                stringBuilder.append(0).append(hexString)
            } else {
                stringBuilder.append(hexString)
            }
        }
        return stringBuilder.toString()
    }


}