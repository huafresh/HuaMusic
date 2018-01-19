package hua.music.huamusic.data

import android.arch.core.util.Function
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.content.Context
import hua.music.huamusic.data.livedatas.SingleListLiveData
import hua.music.huamusic.data.livedatas.ScanFilterDirLiveData
import hua.music.huamusic.entitys.*
import hua.music.huamusic.storage.StorageManager
import hua.music.huamusic.utils.JsonParseUtil

/**
 * 使用lifecycle框架，持有了多个音乐列表数据的LiveData，
 * 并且是单例类，实现整个应用全局共享音乐列表。
 *
 * Created by hua on 2017/12/23.
 */
class MusicLiveModel private constructor() : ViewModel() {

    companion object {
        /**
         * 列表缓存key
         */
        val KEY_STORE_RECENT = "key_recent"
        val KEY_STORE_DOWNED = "key_downed"
        val KEY_STORE_DOWNING = "key_downing"

        /**
         * 获取单例实例
         */
        fun getInstance(): MusicLiveModel {
            return Holder.sInstance
        }

        private object Holder {
            val sInstance = MusicLiveModel()
        }
    }

    /**
     * 本地音乐单曲列表
     */
    val singleSongList: SingleListLiveData = SingleListLiveData()
    /**
     * 本地音乐歌手列表
     */
    var localAuthorList: LiveData<List<HashMap<AuthorEntity, MutableList<Music>>>>
    /**
     * 本地音乐专辑列表
     */
    var localAlbumList: LiveData<List<HashMap<AlbumEntity, MutableList<Music>>>>
    /**
     * 本地音乐文件夹列表
     */
    var localDirList: LiveData<List<HashMap<DirEntity, MutableList<Music>>>>


    /**
     * 最近播放列表
     */
    val recentList: MutableLiveData<MutableList<Music>> = MutableLiveData()
    /**
     * 已下载列表
     */
    val downedList: MutableLiveData<MutableList<Music>> = MutableLiveData()

    /**
     * 下载中列表
     */
    val downingList: MutableLiveData<MutableList<Music>> = MutableLiveData()

    /**
     * 过滤目录列表。扫描设置中使用。
     */
    val scanFilterList: ScanFilterDirLiveData = ScanFilterDirLiveData()

//    /**
//     * 外部存储文件夹列表，自定义扫描中使用
//     */
//    val externalDirList: MutableLiveData<MutableList<ExternalDirEntity>> = MutableLiveData()

    /**
     * toast
     */
    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    /**
     * 是否展示loading
     */
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        //相关本地列表与单曲列表建立关联，此后单曲列表改变，则相关列表会及时刷新
        localAuthorList = Transformations.map(singleSongList, Function<List<Music>,
                List<HashMap<AuthorEntity, MutableList<Music>>>> {
            DataUtil.resolveLocalAuthorList(it)
        })
        localAlbumList = Transformations.map(singleSongList, Function<List<Music>,
                List<HashMap<AlbumEntity, MutableList<Music>>>> {
            DataUtil.resolveLocalAlbumList(it)
        })
        localDirList = Transformations.map(singleSongList, Function<List<Music>,
                List<HashMap<DirEntity, MutableList<Music>>>> {
            DataUtil.resolveLocalDirList(it)
        })

        //恢复最近播放列表
        val recentString = StorageManager.getInstance.getFromDisk(KEY_STORE_RECENT)
        if (recentString != null) {
            recentList.value = JsonParseUtil.parseJsonToList(recentString, Music::class.java)?.toMutableList()
        } else {
            recentList.value = mutableListOf()
        }
    }

    /**
     * 扫描本地音乐。
     */
    fun scanMusic(context: Context) {
        singleSongList.scanLocalMusic(context)
    }

    /**
     * 添加最近播放
     */
    fun addRecentPlay(music: Music) {
        if (recentList.value?.contains(music) == false) {
            recentList.value?.add(0, music)
        }
    }

}