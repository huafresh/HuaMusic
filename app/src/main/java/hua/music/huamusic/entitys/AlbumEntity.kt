package hua.music.huamusic.entitys

/**
 * 专辑实体
 * @author hua
 * @version 2017/12/21 15:48
 *
 */
data class AlbumEntity(
        var name: String?, //专辑名称
        var cover: String?, //封面url
        var sum: Int?, //歌曲数目
        var author: String? //歌手
)