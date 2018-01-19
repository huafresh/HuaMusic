package hua.music.huamusic.entitys

/**
 * 歌词实体
 *
 * @author hua
 * @version 2018/1/19 16:59
 */
data class LrcEntity(var id: String, var accessKey: String) {
    var songName: String? = null //对应歌曲名称
    var author: String? = null //对应歌曲歌手
    var duration: String? = null //时长
    var score: String? = null //评分，也就是与对应歌曲的匹配度，满分60分，对应三颗星
    var path: String? = null //本地存放路径
    var language: String? = null //语言
}