package hua.music.huamusic.pages.play.downlrc

import android.arch.lifecycle.MutableLiveData
import hua.music.huamusic.entitys.LrcEntity
import hua.music.huamusic.entitys.Music

/**
 * 某首歌对应的可选歌词列表
 *
 * @author hua
 * @version 2018/1/19 16:58
 *
 */
class LrcListLiveData(music:Music) : MutableLiveData<LrcEntity>() {
    private var music: Music = music

    override fun onActive() {



    }


}