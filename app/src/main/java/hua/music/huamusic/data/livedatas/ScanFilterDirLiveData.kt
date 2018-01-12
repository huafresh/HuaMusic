package hua.music.huamusic.data.livedatas

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import hua.music.huamusic.App
import hua.music.huamusic.data.DataUtil
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.entitys.SelectableDirEntity
import io.reactivex.Flowable
import io.reactivex.functions.Function
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 扫描设置过滤目录列表
 *
 * @author hua
 * @version 2018/1/12 13:44
 *
 */
class ScanFilterDirLiveData : MutableLiveData<List<SelectableDirEntity>>() {
    override fun onActive() {
        getFilterDirList(App.sInstance)
    }

    internal fun getFilterDirList(context: Context) {
        Flowable.just("")
                .subscribeOn(Schedulers.newThread())
                .flatMap(Function<String, Flowable<MutableList<SelectableDirEntity>>> {
                    Flowable.just(internalGetFilterDirList(context))
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    value = it
                }
    }


    private fun internalGetFilterDirList(context: Context): MutableList<SelectableDirEntity> {
        val resolver = context.contentResolver
        val cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER)

        //存储包含音频文件的顶层目录
        val list = mutableListOf<SelectableDirEntity>()
        for (i in 0 until cursor.count) {
            cursor.moveToNext()
            val isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) != 0
            val filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            val fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
            val dirPath = filePath.substring(0, filePath.length - fileName.length)

            var add = true
            list.forEach {
                if (it.dirPath == getExternalPrefix(dirPath)) {
                    add = false
                    it.isSelected = isMusic
                }
            }

            if (add) {
                val entity = SelectableDirEntity(!isMusic, getExternalPrefix(dirPath)!!)
                list.add(entity)
            }
        }
        cursor.close()
        return list
    }

    private fun getExternalPrefix(dirPath: String): String? {
        Environment.getExternalStorageDirectory().listFiles().forEach {
            if (dirPath.startsWith(it.absolutePath)) {
                return it.absolutePath
            }
        }
        return null
    }

}