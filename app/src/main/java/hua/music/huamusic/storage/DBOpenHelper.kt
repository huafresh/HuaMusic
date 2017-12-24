package hua.music.huamusic.storage

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * SQLite数据库操作帮助类
 *
 * @author hua
 * @version 2017/10/22 10:40
 */

class DBOpenHelper : SQLiteOpenHelper {

    companion object {
        val START_VERSION = 1
        val DB_NAME = "news.db"
    }

    constructor(context: Context) :
            this(context, DB_NAME, null, START_VERSION)

    constructor(context: Context, version: Int) :
            this(context, DB_NAME, null, version)

    constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) :
            super(context, name, factory, version)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(MapTable.CREATE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }


}
