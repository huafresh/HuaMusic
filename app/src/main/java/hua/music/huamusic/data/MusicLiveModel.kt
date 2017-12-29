package hua.music.huamusic.data

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.text.TextUtils
import hua.music.huamusic.entitys.AlbumEntity
import hua.music.huamusic.entitys.AuthorEntity
import hua.music.huamusic.entitys.DirEntity
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.storage.StorageManager
import hua.music.huamusic.utils.JsonParseUtil
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * 使用lifecycle框架，持有了多个音乐列表数据的LiveData，
 * 并且是单例类，实现全局共享音乐列表。
 *
 * Created by hua on 2017/12/23.
 */
class MusicLiveModel private constructor() : ViewModel() {

    companion object {
        val KEY_STORE_SINGLE_SONG = "key_single_song"
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
    val singleSongList: MutableLiveData<MutableList<Music>> = MutableLiveData()
    /**
     * 本地音乐歌手列表
     */
    val localAuthorList: MutableLiveData<MutableList<HashMap<AuthorEntity, MutableList<Music>>>>
            = MutableLiveData()
    /**
     * 本地音乐专辑列表
     */
    val localAlbumList: MutableLiveData<MutableList<HashMap<AlbumEntity, MutableList<Music>>>>
            = MutableLiveData()
    /**
     * 本地音乐文件夹列表
     */
    val localDirList: MutableLiveData<MutableList<HashMap<DirEntity, MutableList<Music>>>>
            = MutableLiveData()
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
     * toast
     */
    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    /**
     * 是否展示loading
     */
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        val recentString = StorageManager.getInstance.getFromDisk(KEY_STORE_RECENT)
        if (recentString != null) {
            recentList.value = JsonParseUtil.parseJsonToList(recentString, Music::class.java)?.toMutableList()
        }
    }

    /**
     * 开始扫描本地音乐，以便初始化本地音乐相关的列表。
     * 可以在进入主界面时就调用，提前初始化，提升进入本地音乐界面的响应速度。
     */
    fun scanLocalMusic(context: Context) {
        loadingLiveData.value = true
        //rx的处理逻辑是：先获取本地单曲音乐列表（先缓存后provider扫描）
        //然后根据本地单曲音乐列表解析得到歌手、专辑、文件夹等列表
        Flowable.just(StorageManager.getInstance.getFromDisk(KEY_STORE_SINGLE_SONG) ?: "")
                .subscribeOn(Schedulers.newThread())
                .flatMap(Function<String?, Flowable<MutableList<Music>>> {
                    var list: MutableList<Music>? = null
                    if (!TextUtils.isEmpty(it)) {
                        list = JsonParseUtil.parseJsonToList(it, Music::class.java)?.toMutableList()
                    }
                    Flowable.just(list ?: mutableListOf())
                })
                .map(Function<MutableList<Music>, MutableList<Music>> {
                    var resultList = it
                    if (resultList.size == 0) {
                        val list = DataSourceImpl.getInstance().getSingleSongList(context)
                        if (list != null) {
                            resultList = list.toMutableList()
                        }
                    }
                    resultList
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    singleSongList.value = it
                    resolveLocalAuthorListAndUpdate(it)
                    resolveLocalAlbumListAndUpdate(it)
                    resolveLocalDirListAndUpdate(it)
                    //缓存本地单曲列表
                    val singleSongString = JsonParseUtil.parseObjectToString(it)
                    if (singleSongString != null) {
                        StorageManager.getInstance.saveToDisk(KEY_STORE_SINGLE_SONG, singleSongString)
                    }
                    loadingLiveData.value = false
                }
    }

    private fun resolveLocalAuthorListAndUpdate(list: MutableList<Music>) {
        localAuthorList.value?.clear()
        val resultList = mutableListOf<HashMap<AuthorEntity, MutableList<Music>>>()
        out@ for (music in list) {
            val author = music.author
            for (item in resultList) {
                //理论上这里只会有一个key
                for (key in item.keys) {
                    if (key.name == author) {
                        item[key]!!.add(music)
                        key.sum!!.inc()
                        continue@out
                    }
                }
            }
            //到此说明此歌手不在列表，so add it
            val authorEntity = AuthorEntity(author ?: "未知歌手", null, 0)
            val item = hashMapOf<AuthorEntity, MutableList<Music>>()
            val musicList = mutableListOf<Music>()
            musicList.add(music)
            item.put(authorEntity, musicList)
            resultList.add(item)
        }
        localAuthorList.value = resultList
    }

    private fun resolveLocalAlbumListAndUpdate(list: List<Music>) {
        localAlbumList.value?.clear()
        val resultList = mutableListOf<HashMap<AlbumEntity, MutableList<Music>>>()
        out@ for (music in list) {
            val album = music.album
            for (item in resultList) {
                //理论上这里只会有一个key
                for (key in item.keys) {
                    if (key.name == album) {
                        item[key]!!.add(music)
                        key.sum!!.inc()
                        continue@out
                    }
                }
            }
            //到此说明此专辑不在列表，so add it
            val albumEntity = AlbumEntity(album ?: "未知专辑", null, 0)
            val item = hashMapOf<AlbumEntity, MutableList<Music>>()
            val musicList = mutableListOf<Music>()
            musicList.add(music)
            item.put(albumEntity, musicList)
            resultList.add(item)
        }
        localAlbumList.value = resultList
    }

    private fun resolveLocalDirListAndUpdate(list: List<Music>) {
        localDirList.value?.clear()
        val resultList = mutableListOf<HashMap<DirEntity, MutableList<Music>>>()
        out@ for (music in list) {
            val dir = music.dirName
            for (item in resultList) {
                //理论上这里只会有一个key
                for (key in item.keys) {
                    if (key.name == dir) {
                        item[key]!!.add(music)
                        key.sum!!.inc()
                        continue@out
                    }
                }
            }
            //到此说明此文件夹不在列表，so add it
            val dirEntity = DirEntity(dir ?: "未知文件夹", music.dirPath, 0)
            val item = hashMapOf<DirEntity, MutableList<Music>>()
            val musicList = mutableListOf<Music>()
            musicList.add(music)
            item.put(dirEntity,musicList)
            resultList.add(item)
        }
        localDirList.value = resultList
    }

}