package hua.music.huamusic.storage

import android.support.v4.util.ArrayMap
import android.text.TextUtils

/**
 * 存储数据到内存
 *
 * @author hua
 * @version 2017/10/21 11:26
 */

class MemoryStorage : IStorage<String, Any> {

    companion object {
        private val sArrayMap = ArrayMap<String, Any>()
    }

    override fun save(key: String, value: Any): Boolean {
        sArrayMap.put(key, value)
        return true
    }

    override operator fun get(key: String): Any? {
        return sArrayMap[key]
    }

    override fun saveEncrypt(key: String, content: String): Boolean {
        val encryptData = EncryptUtil.encryptByAES(content, key) ?: return false
        save(key, encryptData)
        return true
    }

    override fun getDecrypt(key: String): String? {
        val encryptData = get(key) as String?
        return EncryptUtil.decryptByAES(encryptData ?: return null, key)
    }

    override fun delete(key: String): Boolean {
        return sArrayMap.remove(key) != null
    }

    override fun clear(): Boolean {
        sArrayMap.clear()
        return true
    }


}
