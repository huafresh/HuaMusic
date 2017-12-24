package hua.music.huamusic.entitys

/**
 * 音乐实体
 * @author hua
 * @version 2017/12/21 15:48
 *
 */
data class Music(
        var title: String?, //歌名
        var author: String?, //艺术家
        var path: String?, //本地路径
        var dir: String?, //文件夹
        var album: String?, //专辑
        var lyricsPath: String? //歌词路径
)