package com.tang.cryption

/**
 * Created by tanghongtu on 2020/6/22.
 *
 * 凯撒加密
 */
object CaesarCipher {

    fun encrypt(input: String, key: Int): String {
        val charArray = input.toCharArray()
        for (i in charArray.indices) {
            charArray[i] = charArray[i] + key
        }
        return String(charArray)
    }

    fun decrypt(input: String, key: Int): String {
        val charArray = input.toCharArray()
        for (i in charArray.indices) {
            charArray[i] = charArray[i] - key
        }
        return String(charArray)
    }

}