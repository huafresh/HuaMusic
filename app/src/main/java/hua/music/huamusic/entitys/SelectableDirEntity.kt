package hua.music.huamusic.entitys

/**
 * 可设置选择状态的目录
 *
 * @author hua
 * @version 2018/1/11 15:01
 */
data class SelectableDirEntity(
        var isSelected: Boolean,
        var dirPath: String
)