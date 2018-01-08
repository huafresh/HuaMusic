package hua.music.huamusic.entitys


/**
 * 首页菜单实体
 * @author hua
 * @version 2017/12/21 15:48
 *
 */
data class MenuEntity(
        var id: Int,
        var iconResId: Int,
        var name: String,
        var sum: Int,
        var isPlay: Boolean
)

