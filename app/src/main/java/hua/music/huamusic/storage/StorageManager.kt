package hua.music.huamusic.storage

import android.content.Context

import hua.music.huamusic.App

/**
 * 数据存储管理。
 * 支持存储键值对数据到内存、数据库等等中。
 *
 * @author hua
 * @version 2017/6/5
 */
class StorageManager private constructor(context: Context) {

    private val mMemoryStorage: MemoryStorage = MemoryStorage()
    private val mDiskStorage: DiskStorage = DiskStorage(context)

    private object Holder {
        val sInstance = StorageManager(App.sInstance)
    }

    companion object {
        val TRUE = "true"
        val FALSE = "false"
        val getInstance: StorageManager
            get() = Holder.sInstance
    }

    /**
     * 存储到内存
     *
     * @param key  键值
     * @param data 数据
     */
    fun saveToMemory(key: String, data: Any) {
        mMemoryStorage.save(key, data)
    }

    /**
     * 加密存储到内存
     *
     * @param key     键值
     * @param content 数据
     */
    fun encryptSaveToMemory(key: String, content: String) {
        mMemoryStorage.saveEncrypt(key, content)
    }

    /**
     * 从内存获取数据
     *
     * @param key 键值
     * @return 数据
     */
    fun getFromMemory(key: String): Any? {
        return mMemoryStorage.get(key)
    }

    /**
     * 从内存获取解密后的数据
     *
     * @param key 键值
     * @return 解密后的数据
     */
    fun decryptGetFromMemory(key: String): String? {
        return mMemoryStorage.getDecrypt(key)
    }

    /**
     * 存储字符串到磁盘。
     *
     * @param key     键值
     * @param content 要存储的内容
     */
    fun saveToDisk(key: String, content: String) {
        mDiskStorage.save(key, content)
    }

    /**
     * 加密存储字符串到磁盘。
     *
     * @param key     键值
     * @param content 要存储的内容
     */
    fun encryptSaveToDisk(key: String, content: String) {
        mDiskStorage.saveEncrypt(key, content)
    }

    /**
     * 从本地获取字符串
     *
     * @param key 键值
     * @return 本地存储的字符串
     */
    fun getFromDisk(key: String): String? {
        return mDiskStorage.get(key)
    }

    /**
     * 从本地获取解密后的字符串
     *
     * @param key 键值
     * @return 解密后的字符串
     */
    fun decryptGetFromDisk(key: String): String? {
        return mDiskStorage.getDecrypt(key)
    }


}
