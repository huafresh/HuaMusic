package hua.music.huamusic.views

import android.content.Context
import android.view.View

/**
 * @author hua
 * @version 2017/12/22 17:35
 *
 */
interface IView {

    /**
     * 获取视图
     */
    fun getView(context: Context): View
}