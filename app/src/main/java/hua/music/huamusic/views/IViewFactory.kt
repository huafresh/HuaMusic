package hua.music.huamusic.views

import android.content.Context
import android.view.View

/**
 * 视图接口
 * @author hua
 * @version 2017/12/22 17:35
 *
 */
interface IViewFactory {

    /**
     * 获取视图
     */
    fun getView(context: Context): View?
}