package hua.music.huamusic.entitys

/**
 * 文件夹实体
 * @author hua
 * @version 2017/12/21 15:48
 *
 */
data class DirEntity(
        var name: String?, //文件夹名称
        var path: String?, //文件夹路径
        var sum: Int? //歌曲数目
)