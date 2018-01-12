package hua.music.huamusic.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import hua.music.huamusic.entitys.*
import io.reactivex.Flowable
import io.reactivex.functions.Function
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 统一数据仓库（本地）
 * 只能通过[MusicLiveModel]实例访问本类
 *
 * Created by hua on 2017/12/24.
 */
@SuppressLint("Recycle")
class DataSourceImpl private constructor() {

    private object Holder {
        val sInstance = DataSourceImpl()
    }

    companion object {
        fun getInstance(): DataSourceImpl {
            return Holder.sInstance
        }
    }







    internal fun getExternalDirList() {
        val externalDir = Environment.getExternalStorageDirectory()
        val resultList = mutableListOf<ExternalDirEntity>()
        externalDir.listFiles().forEach {
            if (it.isDirectory) {
                val entity = ExternalDirEntity(it.name, )
                entity.dirName = it.name
                resultList
            }
        }

    }
}