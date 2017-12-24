package hua.music.huamusic.storage

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Base64

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * 加密工具。支持AES加密和RSA加密
 *
 * @author hua
 * @version 2017/10/21 13:21
 */
@SuppressLint("GetInstance")
object EncryptUtil {

    /**
     * 根据字符串生成AES加密密钥。
     *
     * @param key 用来生成密钥的字符串
     * @return AES加密密钥
     */
    private fun generateAESKey(key: String): Key? {
        val keyGenerator = KeyGenerator.getInstance("AES")
        val sr = SecureRandom.getInstance("SHA1PRNG", "Crypto")
        sr.setSeed(key.toByteArray())
        keyGenerator.init(128, sr)
        val secretKey = keyGenerator.generateKey()
        return SecretKeySpec(secretKey.encoded, "AES")
    }


    /**
     * 使用AES对字符串进行加密
     *
     * @param content 要加密的字符串
     * @param key     用来生成AES密钥。后续解密需要传入相同字符串
     * @return 加密后的字符串
     */
    fun encryptByAES(content: String, key: String): String? {
        val contentBytes = content.toByteArray(charset("utf-8"))
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, generateAESKey(key))
        return Base64.encodeToString(cipher.doFinal(contentBytes), Base64.DEFAULT)
    }

    /**
     * 对字符串进行AES解密
     *
     * @param content 要解密的内容
     * @param key     用来生成密钥，之前加密使用的key
     * @return 解密后的字符串
     */
    fun decryptByAES(content: String, key: String): String? {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, generateAESKey(key))
        return String(cipher.doFinal(Base64.decode(content, Base64.DEFAULT)))
    }

    /**
     * 使用RSA公钥或者私钥加密
     *
     * @param content 要加密的字符串
     * @param key     公钥或者私钥。可以使用方法[loadPrivateKeyFromPem]
     * 或者[loadPublicKeyFromPem]从pem文件中加载公钥或者私钥
     * @return 加密后的字符串
     */
    fun encryptByRSAKey(content: String, key: Key?): String? {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val result = cipher.doFinal(content.toByteArray())
        return Base64.encodeToString(result, Base64.DEFAULT).replace("\n".toRegex(), "")
    }

    /**
     * 使用RSA公钥或者私钥解密
     *
     * @param content 要解密的字符串
     * @param key     公钥或者私钥。可以使用方法[loadPrivateKeyFromPem]
     * 或者[loadPublicKeyFromPem]从pem文件中加载公钥或者私钥
     * @return 解密后的字符串
     */
    fun decryptByRSAKey(content: String, key: Key?): String? {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val result = cipher.doFinal(Base64.decode(content, Base64.DEFAULT))
        return String(result)
    }

    /**
     * 解析.pem文件得到PublicKey对象
     *
     * @param in .pem文件输入流。方法执行完会自动关闭
     * @return PublicKey对象
     */
    fun loadPublicKeyFromPem(`in`: InputStream): PublicKey? {
        //先读取公钥字符串
        val publicString = readPemToString(`in`)
        `in`.close()
        //根据公钥字符串生成PublicKey对象
        val decode = Base64.decode(publicString, Base64.DEFAULT)
        val keySpec = X509EncodedKeySpec(decode)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }

    /**
     * 解析.pem文件得到PublicKey对象
     *
     * @param in .pem文件输入流。方法执行完会自动关闭
     * @return PublicKey对象
     */
    fun loadPrivateKeyFromPem(`in`: InputStream): PrivateKey? {
        //先读取私钥字符串
        val privateString = readPemToString(`in`)
        `in`.close()
        //根据私钥字符串生成PrivateKey对象
        val decode = Base64.decode(privateString, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(decode)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }

    /**
     * 将输入流转换成字符串
     */
    private fun readPemToString(`in`: InputStream): String {
        val builder = StringBuilder()
        val buffIn = BufferedReader(InputStreamReader(`in`))
        var line: String?
        while (true) {
            line = buffIn.readLine() ?: break
            if (line.startsWith("-")) {
                //忽略pem文件的注释内容
                continue
            }
            builder.append(line)
        }
        return builder.toString()
    }

}
