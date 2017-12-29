package hua.music.huamusic.entitys

/**
 * 歌手实体
 * @author hua
 * @version 2017/12/21 15:48
 *
 */
data class AuthorEntity(
        var name: String?, //歌手名称
        var cover: String?, //头像url
        var sum: Int? //歌曲数目
)