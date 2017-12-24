package hua.music.huamusic.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import hua.music.huamusic.base.CallBack
import hua.music.huamusic.entitys.Music
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

/**
 * 数据来源实现
 * Created by hua on 2017/12/24.
 */
class DataSourceImpl private constructor() : IDataSource {

    private val mThreadPool: ExecutorService = Executors.newCachedThreadPool()
    private val mMainHandler = Handler(Looper.getMainLooper())

    private object Holder {
        val sInstance = DataSourceImpl()
    }

    companion object {
        fun getInstance(): DataSourceImpl {
            return Holder.sInstance
        }
    }

    override fun scanLocalMusicList(context: Context, callBack: CallBack<List<Music>>) {
        mThreadPool.submit({
            var list: List<Music>? = null
            try {
                list = getMusicListFromProvider(context)
            } catch (e: Exception) {
                mMainHandler.post { callBack.onFailed(e) }
            }
            if (list == null) {
                mMainHandler.post { callBack.onFailed(Exception("未知异常")) }
            } else {
                mMainHandler.post { callBack.onSuccess(list) }
            }
        })
    }

    @SuppressLint("Recycle")
    private fun getMusicListFromProvider(context: Context): List<Music> {
        val resolver = context.contentResolver
        val cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER) ?: throw RuntimeException("扫描失败")
        val list = mutableListOf<Music>()
        for (i in 0 until cursor.count) {
            cursor.moveToNext()
            val isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC))
            if (isMusic != 0) {
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val author = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val fileSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
                val filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val fileDir = filePath.substring(0, filePath.length - fileName.length - 1)
                val music = Music(title, author, filePath, fileDir, album, null)
                list.add(music)
            }
        }
        cursor.close()
        return list
    }
}