package hua.music.huamusic.storage

import android.provider.BaseColumns

/**
 * 键值对数据表，用来持久化存储key-value形式的数据。
 * 注意key值得长度不能超过1000。
 *
 * @author hua
 * @version 2017/10/22 10:44
 */

object MapTable {

    val TABLE_NAME = "table_map"
    var CREATE_SQL: String

    init {
        CREATE_SQL = "create table if not exists " + TABLE_NAME + "(" +
                Columns._ID + " integer primary key autoincrement," +
                Columns.COLUMN_KEY + " varchar(1000)," +
                Columns.COLUMN_VALUE + " text)"
    }

    class Columns : BaseColumns {
        companion object {
            val _ID = "_id"
            val COLUMN_KEY = "key"
            val COLUMN_VALUE = "value"
        }
    }


}
