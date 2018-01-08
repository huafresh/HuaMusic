package hua.music.huamusic.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import hua.music.huamusic.base.CallBack
import hua.music.huamusic.entitys.AlbumEntity
import hua.music.huamusic.entitys.AuthorEntity
import hua.music.huamusic.entitys.DirEntity
import hua.music.huamusic.entitys.Music
import io.reactivex.Flowable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 音乐数据来源实现
 *
 * Created by hua on 2017/12/24.
 */
class DataSourceImpl private constructor() : IDataSource {

    private object Holder {
        val sInstance = DataSourceImpl()
    }

    companion object {
        fun getInstance(): DataSourceImpl {
            return Holder.sInstance
        }
    }

    override fun getSingleSongList(context: Context): List<Music>? {
        return try {
            val list = getLocalMusicListFromProvider(context)
            list
        } catch (e: Exception) {
            MusicLiveModel.getInstance().toastLiveData.value = e.message
            null
        }
    }

    @SuppressLint("Recycle")
    private fun getLocalMusicListFromProvider(context: Context): List<Music> {
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
                val dirPath = filePath.substring(0, filePath.length - fileName.length)
                val arrays = dirPath.split("/")
                val dirName = arrays[arrays.size - 2]
                val music = Music(i)
                music.title = title
                music.author = author
                music.album = album
                music.duration = duration
                music.fileSize = fileSize
                music.filePath = filePath
                music.fileName = fileName
                music.dirPath = dirPath
                music.dirName = dirName
                list.add(music)
            }
        }
        cursor.close()
        return list
    }
}