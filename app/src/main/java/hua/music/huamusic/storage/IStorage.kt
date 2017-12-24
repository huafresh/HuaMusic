package hua.music.huamusic.storage

/**
 * 存储接口，定义数据存储的一般方法
 *
 * @author hua
 * @version 2017/10/21 11:27
 */

interface IStorage<in K, V> {

    /**
     * 存储数据
     *
     * @param key   键值
     * @param value 数据
     * @return 是否成功
     */
    fun save(key: K, value: V): Boolean

    /**
     * 获取数据
     *
     * @param key 键值
     * @return 与键值对应的数据
     */
    fun get(key: K): V?

    /**
     * 加密存储数据
     *
     * @param key    键值
     * @param content 明文数据
     * @return 是否成功
     */
    fun saveEncrypt(key: String, content: String): Boolean

    /**
     * 获取解密后的数据
     *
     * @param key 键值
     * @return 解密后的明文数据
     */
    fun getDecrypt(key: String): String?

    /**
     * 删除指定数据
     *
     * @param key 键值
     * @return 是否成功
     */
    fun delete(key: K): Boolean

    /**
     * 删除所有数据
     *
     * @return 是否成功
     */
    fun clear(): Boolean
}
