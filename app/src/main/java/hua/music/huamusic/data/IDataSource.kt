package hua.music.huamusic.data

import android.content.Context
import hua.music.huamusic.base.CallBack
import hua.music.huamusic.entitys.Music

/**
 * 本地数据源接口
 *
 * Created by hua on 2017/12/24.
 */
interface IDataSource {

    /**
     * 扫描本地音乐列表
     */
    fun scanLocalMusicList(context: Context, callBack: CallBack<List<Music>>)

}