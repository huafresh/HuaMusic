package hua.music.huamusic.data.livedatas

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.provider.MediaStore
import android.text.TextUtils
import hua.music.huamusic.App
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.storage.StorageManager
import hua.music.huamusic.utils.JsonParseUtil
import io.reactivex.Flowable
import io.reactivex.functions.Function
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 本地单曲列表
 *
 * @author hua
 * @version 2018/1/12 10:58
 *
 */
@SuppressLint("Recycle")
class MusicListLiveData : MutableLiveData<List<Music>>() {

    companion object {
        val KEY_STORE_SINGLE_SONG = "key_single_song"
    }

    override fun onActive() {
        scanLocalMusic(App.sInstance)
    }

    override fun onInactive() {
        //缓存本地单曲列表
        if (value != null) {
            val singleSongString = JsonParseUtil.parseObjectToString(value)
            if (singleSongString != null) {
                StorageManager.getInstance.saveToDisk(KEY_STORE_SINGLE_SONG, singleSongString)
            }
        }
    }

    internal fun scanLocalMusic(context: Context) {
        //先获取本地缓存的音乐列表，然后Provider扫描
        val cacheJson = StorageManager.getInstance.getFromDisk(KEY_STORE_SINGLE_SONG) ?: ""
        val cacheData = Flowable.just(cacheJson)
                .flatMap(Function<String, Flowable<List<Music>>> {
                    var list = emptyList<Music>()
                    if (!TextUtils.isEmpty(it)) {
                        list = JsonParseUtil.parseJsonToList(it, Music::class.java)?: emptyList()
                    }
                    Flowable.just(list)
                })
        val scanData = Flowable.just("")
                .subscribeOn(Schedulers.newThread())
                .flatMap(Function<String, Flowable<List<Music>>> {
                    Flowable.just(getLocalMusicListFromProvider(context))
                })
        Flowable.concat(cacheData, scanData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    value = it
                }
    }

    private fun getLocalMusicListFromProvider(context: Context): List<Music> {
        val resolver = context.contentResolver
        val cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER) ?: return emptyList()
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