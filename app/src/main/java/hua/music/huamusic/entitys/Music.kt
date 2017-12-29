package hua.music.huamusic.entitys

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * 音乐实体
 *
 * @author hua
 * @version 2017/12/21 15:48
 */
data class Music(
        @JsonProperty("title") var title: String?, //歌名
        @JsonProperty("author") var author: String?, //艺术家
        @JsonProperty("path") var path: String?, //音乐文件全路径
        @JsonProperty("dirPath") var dirPath: String?, //文件夹路径
        @JsonProperty("dirName") var dirName: String?, //文件夹名称
        @JsonProperty("album") var album: String?, //专辑
        @JsonProperty("lyricsPath") var lyricsPath: String? //歌词路径
)
