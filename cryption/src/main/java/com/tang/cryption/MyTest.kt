package com.tang.cryption

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter

private class MyTest {

}

fun main(args: Array<String>) {
    val original = "你好世界"
//    val key = 2
//    val encrypt = CaesarCipher.encrypt(original, key)
//    println("密文：${encrypt}")
//    val originalDecrypt = CaesarCipher.decrypt(encrypt, key)
//    println("明文：${originalDecrypt}")

//    val key = "adcdefgh"
//    val encrypt = DESCipher.encrypt(original, key)
//    println("密文：${encrypt}")
//    val originalDecrypt = DESCipher.decrypt(encrypt, key)
//    println("明文：${originalDecrypt}")

//    val key = "adcdefghadcdefgh"
//    val encrypt = AESCipher.encrypt(original, key)
//    println("密文：${encrypt}")
//    val originalDecrypt = AESCipher.decrypt(encrypt, key)
//    println("明文：${originalDecrypt}")

    //缓存加密
//    val json = "{\n" +
//            "  \"createEmployee\": 0,\n" +
//            "  \"createTime\": \"2020-06-22T08:07:21.687Z\",\n" +
//            "  \"description\": \"string\",\n" +
//            "  \"doorstatus\": 0,\n" +
//            "  \"lockType\": 0,\n" +
//            "  \"lockstatus\": 0,\n" +
//            "  \"porterId\": 0,\n" +
//            "  \"porterName\": \"string\",\n" +
//            "  \"porterPath\": \"string\",\n" +
//            "  \"porterPid\": 0,\n" +
//            "  \"sortId\": 0,\n" +
//            "  \"stationId\": 0,\n" +
//            "  \"updateEmployee\": 0,\n" +
//            "  \"updateTime\": \"2020-06-22T08:07:21.687Z\"\n" +
//            "}"
//
//    val key: String = "1234567812345678"
//    println(readCache(key))


//    val generateKey = RSACipher.generateKey()
//    val encrypt = RSACipher.encryptWithPublicKey(original, generateKey.first)
//    println("密文：${encrypt}")
//    val originalDecrypt = RSACipher.decryptWithPrivateKey(encrypt, generateKey.second)
//    println("明文：${originalDecrypt}")

    val PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtJ3LDuaPCcFZGXWEAPFLH8qWdOUA5h07Y4GOajuIa+Ucy1kbTBK9l6CAlOcG85+m2ExphdzfQN17DsrZYLjH7eXt8dizHMM40LJuhG7RTs95DqPeVNKgkvmRq5fVA/KHSm1OmwY5j2W2Q5cl41whl2IYO6dric25fldHH9wF9np2/RQB5KQ4iv4Tk9Kt8i1tRQhOJ8JuQQdtiQUv6JPD/4HfV6sQHN2DMV8BIujXxwOMFlu6hzx7U8LNM+rwyHXKYBZjvosaDgvrcn0qy96swu7+1zU6td1Hp2EevLkLrMG4CjGbQz4RLhLtZjNW4tmdx8fcnO5G34/Na1Ndkz82VwIDAQAB"
    val PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC0ncsO5o8JwVkZdYQA8UsfypZ05QDmHTtjgY5qO4hr5RzLWRtMEr2XoICU5wbzn6bYTGmF3N9A3XsOytlguMft5e3x2LMcwzjQsm6EbtFOz3kOo95U0qCS+ZGrl9UD8odKbU6bBjmPZbZDlyXjXCGXYhg7p2uJzbl+V0cf3AX2enb9FAHkpDiK/hOT0q3yLW1FCE4nwm5BB22JBS/ok8P/gd9XqxAc3YMxXwEi6NfHA4wWW7qHPHtTws0z6vDIdcpgFmO+ixoOC+tyfSrL3qzC7v7XNTq13UenYR68uQuswbgKMZtDPhEuEu1mM1bi2Z3Hx9yc7kbfj81rU12TPzZXAgMBAAECggEAGttPw2JzKH8wkYGQ5Fb81bhZEP6wWIdGOnCcTZ2WlnyWAIK6dWBgArjiBcIonXdC5uzN0BLcu5dzYeq10hUH3Y8QB6MyVveMbjPqDRUxWGevV4b0W2aKa/BiQcKs8Q5SIbYJNzIAwxrbtEc4YgtkD0+NzOkDK5WEnI27zLf5xwGEkJ2ekFvsijpLna4hlutbxvygxYvvGVWlXwZMAOmpwmYV/Yos4syNvcYjJZz9MNm1+ycZeEJYklmZ7a/vAs5OoHG2VwlL/lvQOUv1BWVo87jAXjg0KD9W43vtzW25Vdf99yEjgi94kOgeXiuHe+UYY4XroZc/HGBuiq1CbhfBEQKBgQDr73yQCU+XnlbDEqlP0eRvP6ASKzkrMVPqPXlPF6RDiEud+ND7MHiJo8OT8XwHsgnQfmSQzVYirbTwtdmNrUcdvVg10x9RL/x4qrjR5RyV5MupA6ueAGqOJFs3yvv8DMZeLQpfOb2yppnoFZLFL0M3BElpd4ya8EN+dBNWpf4TuQKBgQDD+faQMvQ/P7/hg9JNXVZzTpxVdrLotk+/YgrJiRifb60x9b3iChFerv/VBKhYc/8LMeelvKL+p/Dnkh2O2jVjWWosiK6dS3QFO2b5y464+cgftgw3UkrlXGKFrw0+L7+Q1/L3iwMzJc9B8DF05glnTI/jkVPjRN8AaoBVOmDCjwKBgE7tWtpEgx/RlGzFvWY6bRTaUA5D72gHpG7kCcTvv7/nxIQgTKj7WheRdUf0rO1WHfpljfby+jG3sqQj8Zd3tCa6xNucuQlL6EXlttZgsuNiGs97rtm4gezzNKDBgvKaw3GV17Zrdo5hNXNyZBsa6H1rLbPejmb2oV2YLy/5b7nhAoGAA8w2QPkbzpX3q/EYBDPI1xcw5F8WfHf796N4vgsQFRbe7lbqxRSY3ZLallAuyvgEez0OViHfrrumaXZlaD1GR5SWACLt71Jk/yz1g+Wz8FW20RaAChpoJSWzKxAHW6slNTNaG/3vo/AZohd7fd5vVDchmrFYRNEy2Rk3P2+QLHUCgYBJnHKF8W4qmq/rZmsKzZu7VYLl5cP5UcrKqRXybq+jYnNRq8L5GOjfmmLGtc2HYzonYAMe6ClBqvFbMPnGoSFOj1FKccWmvp1baLsIqhhhcsRTSdud0jrTo0h7PFd+HHh6vSr9HV9u9qfWQ9mflsUCEcfhuQ0Yg5VOg/Vdhj9smw=="
//
////    val generateKey = RSACipher.generateKey()
////    val publicKey = generateKey.first
////    val privateKey = generateKey.second
////    println("公钥：${publicKey}")
////    println("私钥：${privateKey}")
//    val encrypt = RSACipher.encryptWithPublicKey(original, PUBLIC_KEY)
//    println("密文：${encrypt}")
//    val originalDecrypt = RSACipher.decryptWithPrivateKey(encrypt, PRIVATE_KEY)
//    println("明文：${originalDecrypt}")

//    val md5 = MessageDigestUtils.md5(original)
//    println("md5位数：${md5.length}")
//    println("md5摘要：${md5}")
//
//    val sha1 = MessageDigestUtils.sha1(original)
//    println("sha1位数：${sha1.length}")
//    println("sha1摘要：${sha1}")
//
//    val sha256 = MessageDigestUtils.sha256(original)
//    println("sha256位数：${sha256.length}")
//    println("sha256摘要：${sha256}")

    val signature = SHA256WithRSA.signature(original, PRIVATE_KEY)
    println("签名：${signature}")
    val verify = SHA256WithRSA.verify(original, signature, PUBLIC_KEY)
    println("验证结果：${verify}")

}

private fun cacheToDesk(json: String, key: String) {
    val bufferedWriter = BufferedWriter(FileWriter("cache"))
    bufferedWriter.write(AESCipher.encrypt(json, "1234567812345678"))
    bufferedWriter.close()
}

private fun readCache(key: String): String {
    val bufferedReader = BufferedReader(FileReader("cache"))
    val stringBuilder = StringBuilder()
    bufferedReader.forEachLine {
        stringBuilder.append(AESCipher.decrypt(it, key))
    }
    return stringBuilder.toString()
}