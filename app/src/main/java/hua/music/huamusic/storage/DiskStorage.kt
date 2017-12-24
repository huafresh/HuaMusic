package hua.music.huamusic.storage

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.text.TextUtils

/**
 * 持久化存储键值对形式的数据
 *
 * @author hua
 * @version 2017/10/22 11:21
 */
@SuppressLint("Recycle")
class DiskStorage(context: Context) : IStorage<String, String> {

    private val dbOpenHelper: DBOpenHelper = DBOpenHelper(context)
    private val tableName: String = MapTable.TABLE_NAME
    private val whereClause: String = MapTable.Columns.COLUMN_KEY + "=?"

    override fun save(key: String, value: String): Boolean {
        val writableDatabase = dbOpenHelper.writableDatabase

        val values = ContentValues()
        values.put(MapTable.Columns.COLUMN_KEY, key)
        values.put(MapTable.Columns.COLUMN_VALUE, value)

        val existValue = get(key)
        return if (!TextUtils.isEmpty(existValue)) {
            val ret = writableDatabase.update(tableName, values, whereClause, arrayOf(key))
            ret > 0
        } else {
            val ret = writableDatabase.insert(tableName, null, values)
            ret != -1L
        }
    }

    override fun get(key: String): String? {
        val readableDatabase = dbOpenHelper.readableDatabase
        val cursor = readableDatabase.query(MapTable.TABLE_NAME, arrayOf(MapTable.Columns.COLUMN_VALUE),
                whereClause, arrayOf(key), null, null, null) ?: return null
        var value: String? = null
        if (cursor.moveToFirst()) {
            value = cursor.getString(0)
        }
        cursor.close()

        return value
    }

    override fun saveEncrypt(key: String, content: String): Boolean {
        val encryptData = EncryptUtil.encryptByAES(content, key) ?: return false
        return save(key, encryptData)
    }

    override fun getDecrypt(key: String): String? {
        val encryptData = get(key) ?: return null
        return EncryptUtil.decryptByAES(encryptData, key)
    }

    override fun delete(key: String): Boolean {
        val writableDatabase = dbOpenHelper.writableDatabase
        return writableDatabase.delete(tableName, whereClause, arrayOf(key)) > 0
    }


    override fun clear(): Boolean {
        val readableDatabase = dbOpenHelper.readableDatabase
        val cursor = readableDatabase.query(tableName,
                null, null,
                null, null,
                null, null) ?: return false
        while (cursor.moveToNext()) {
            val key = cursor.getString(cursor.getColumnIndex(MapTable.Columns.COLUMN_KEY))
            delete(key)
        }

        cursor.close()
        return true
    }


}
