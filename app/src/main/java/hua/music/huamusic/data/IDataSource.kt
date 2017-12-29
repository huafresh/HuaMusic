package hua.music.huamusic.data

import android.content.Context
import hua.music.huamusic.base.CallBack
import hua.music.huamusic.entitys.AlbumEntity
import hua.music.huamusic.entitys.AuthorEntity
import hua.music.huamusic.entitys.DirEntity
import hua.music.huamusic.entitys.Music
import io.reactivex.Flowable

/**
 * 音乐数据源接口
 *
 * Created by hua on 2017/12/24.
 */
interface IDataSource {

    /**
     * 获取本地音乐单曲列表。
     * 此方法不开线程
     */
    fun getSingleSongList(context: Context): List<Music>?
}