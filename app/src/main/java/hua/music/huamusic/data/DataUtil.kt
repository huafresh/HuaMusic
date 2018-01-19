package hua.music.huamusic.data

import android.widget.TextView
import hua.music.huamusic.entitys.AlbumEntity
import hua.music.huamusic.entitys.AuthorEntity
import hua.music.huamusic.entitys.DirEntity
import hua.music.huamusic.entitys.Music

/**
 * 处理列表数据
 *
 * @author hua
 * @version 2018/1/12 11:33
 *
 */
internal object DataUtil {

    /**
     * 解析得到本地歌手列表
     */
    internal fun resolveLocalAuthorList(list: List<Music>):
            List<HashMap<AuthorEntity, MutableList<Music>>> {
        val resultList = mutableListOf<HashMap<AuthorEntity, MutableList<Music>>>()
        out@ for (music in list) {
            val author = music.author
            for (item in resultList) {
                //理论上这里只会有一个key
                for (key in item.keys) {
                    try {
                        if (key.name == author) {
                            val mutableList = item[key]
                            mutableList!!.add(music)
                            //必须要remove，否则hashcode不一致
                            item.remove(key)
                            key.sum = key.sum!! + 1
                            item.put(key, mutableList)
                            continue@out
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            //到此说明此歌手不在列表，so add it
            val authorEntity = AuthorEntity(author, null, 0)
            val musicList = mutableListOf<Music>()
            musicList.add(music)
            val item = hashMapOf<AuthorEntity, MutableList<Music>>()
            item.put(authorEntity, musicList)
            resultList.add(item)
        }
        return resultList
    }

    internal fun resolveLocalAlbumList(list: List<Music>):
            List<HashMap<AlbumEntity, MutableList<Music>>> {
        val resultList = mutableListOf<HashMap<AlbumEntity, MutableList<Music>>>()
        out@ for (music in list) {
            val album = music.album
            for (item in resultList) {
                //理论上这里只会有一个key
                for (key in item.keys) {
                    if (key.name == album) {
                        val mutableList = item[key]
                        mutableList!!.add(music)
                        //必须要remove，否则hashcode不一致
                        item.remove(key)
                        key.sum = key.sum!! + 1
                        item.put(key, mutableList)
                        continue@out
                    }
                }
            }
            //到此说明此专辑不在列表，so add it
            val albumEntity = AlbumEntity(album, null, 0, music.author)
            val item = hashMapOf<AlbumEntity, MutableList<Music>>()
            val musicList = mutableListOf<Music>()
            musicList.add(music)
            item.put(albumEntity, musicList)
            resultList.add(item)
        }
        return resultList
    }

    internal fun resolveLocalDirList(list: List<Music>):
            List<HashMap<DirEntity, MutableList<Music>>> {
        val resultList = mutableListOf<HashMap<DirEntity, MutableList<Music>>>()
        out@ for (music in list) {
            val dir = music.dirName
            for (item in resultList) {
                //理论上这里只会有一个key
                for (key in item.keys) {
                    if (key.name == dir) {
                        val mutableList = item[key]
                        mutableList!!.add(music)
                        //必须要remove，否则hashcode不一致
                        item.remove(key)
                        key.sum = key.sum!! + 1
                        item.put(key, mutableList)
                        continue@out
                    }
                }
            }
            //到此说明此文件夹不在列表，so add it
            val dirEntity = DirEntity(dir, music.dirPath, 1)
            val item = hashMapOf<DirEntity, MutableList<Music>>()
            val musicList = mutableListOf<Music>()
            musicList.add(music)
            item.put(dirEntity, musicList)
            resultList.add(item)
        }
        return resultList
    }

}