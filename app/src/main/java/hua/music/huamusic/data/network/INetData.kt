package hua.music.huamusic.data.network

import hua.music.huamusic.entitys.LrcEntity

/**
 * 获取网络资源
 *
 * @author hua
 * @version 2018/1/18 16:49
 *
 */
interface INetData {

    /**
     * 搜索歌词，[any]是搜索结果列表
     */
    fun searchLrc(songName: String, author: String, duration: String,
                  callBack: (any: Any) -> Unit)

    /**
     * 下载歌词，返回歌词
     */
    fun downLoadLrc(name: String,
                    callBack: (any: Any) -> Unit)

}