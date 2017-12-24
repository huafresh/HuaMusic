package hua.music.huamusic.data

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.text.TextUtils
import hua.music.huamusic.base.CallBack
import hua.music.huamusic.entitys.AlbumEntity
import hua.music.huamusic.entitys.AuthorEntity
import hua.music.huamusic.entitys.DirEntity
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.storage.StorageManager
import hua.music.huamusic.utils.JsonParseUtil
import java.util.ArrayList

/**
 * 使用lifecycle框架，持有了多个音乐列表数据的LiveData，
 * 并且是单例类，实现全局共享音乐列表。
 *
 * Created by hua on 2017/12/23.
 */
class MusicLiveModel private constructor() : ViewModel() {

    companion object {
        /**
         * 本地音乐单曲列表
         */
        val singleSongList: MutableLiveData<MutableList<Music>> = MutableLiveData()
        val KEY_SINGLE_SONG = "key_single_song"
        /**
         * 本地音乐歌手列表
         */
        val localAuthorList: MutableLiveData<MutableList<AuthorEntity>> = MutableLiveData()
        val KEY_LOCAL_AUTHOR = "key_local_author"
        /**
         * 本地音乐专辑列表
         */
        val localAlbumList: MutableLiveData<MutableList<AlbumEntity>> = MutableLiveData()
        val KEY_LOCAL_ALBUM = "key_local_album"
        /**
         * 本地音乐文件夹列表
         */
        val localDirList: MutableLiveData<MutableList<DirEntity>> = MutableLiveData()
        val KEY_LOCAL_DIR = "key_local_dir"

        /**
         * 最近播放列表
         */
        val recentList: MutableLiveData<MutableList<Music>> = MutableLiveData()
        val KEY_RECENT = "key_recent"

        /**
         * 已下载列表
         */
        val downedList: MutableLiveData<MutableList<Music>> = MutableLiveData()
        val KEY_DOWNED = "key_downed"
        /**
         * 下载中列表
         */
        val downingList: MutableLiveData<MutableList<Music>> = MutableLiveData()
        val KEY_DOWNING = "key_downing"

        /**
         * 获取单例实例
         */
        fun getInstance(): MusicLiveModel {
            return Holder.sInstance
        }
    }

    /**
     * toast
     */
    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    /**
     * 是否展示loading
     */
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()


    private object Holder {
        val sInstance = MusicLiveModel()
    }

    /**
     * 开始扫描音乐
     */
    fun scanMusic(context: Context) {
        loadingLiveData.value = true
        val disk = StorageManager.getInstance.getFromDisk(KEY_SINGLE_SONG)
        if (!TextUtils.isEmpty(disk)) {
            val list = JsonParseUtil.parseJsonToList(disk!!, Music::class.java)
            if (list != null) {
                singleSongList.value = list.toMutableList()
                loadingLiveData.value = false
            }
        }

        DataSourceImpl.getInstance().scanLocalMusicList(context, object : CallBack<List<Music>> {
            override fun onSuccess(data: List<Music>) {
                loadingLiveData.value = false
                singleSongList.value = data.toMutableList()
                StorageManager.getInstance.saveToDisk(KEY_SINGLE_SONG,
                        JsonParseUtil.parseObjectToString(singleSongList) ?: "")
            }

            override fun onFailed(e: Exception) {
                loadingLiveData.value = false
                toastLiveData.value = e.message
            }
        })
    }


}