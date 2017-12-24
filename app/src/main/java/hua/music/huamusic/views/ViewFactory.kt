package hua.music.huamusic.views

import android.content.Context
import android.view.View

/**
 * 视图工厂，根据类型创建视图
 * Created by hua on 2017/12/23.
 */
class ViewFactory {

    companion object {
        val TYPE_MUSIC_CONTROLLER = 0

        /**
         * 创建视图，[type]是视图的类型
         */
        fun createView(context: Context, type: Int): View? {
            when (type) {
                TYPE_MUSIC_CONTROLLER -> {
                    return MusicController().getView(context)
                }
                else -> {
                }
            }
            return null
        }
    }


}