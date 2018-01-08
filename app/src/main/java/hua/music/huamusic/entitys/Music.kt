package hua.music.huamusic.entitys

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * 音乐实体
 *
 * @author hua
 * @version 2017/12/21 15:48
 */
data class Music(@JsonProperty("id") val id: Int) {
    @JsonProperty("title")
    var title: String? = null //歌名
    @JsonProperty("author")
    var author: String? = null //艺术家
    @JsonProperty("duration")
    var duration: Int? = null //时长
    @JsonProperty("fileName")
    var fileName: String? = null //文件名称
    @JsonProperty("fileSize")
    var fileSize: Int? = null //文件大小
    @JsonProperty("filePath")
    var filePath: String? = null //音乐文件全路径
    @JsonProperty("dirPath")
    var dirPath: String? = null //文件夹路径
    @JsonProperty("dirName")
    var dirName: String? = null //文件夹名称
    @JsonProperty("album")
    var album: String? = null //专辑
    @JsonProperty("lyricsPath")
    var lyricsPath: String? = null //歌词路径
}
