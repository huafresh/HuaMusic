package hua.music.huamusic.home

import android.content.Context
import hua.music.huamusic.wrapper.recyclerview.LinearItemDecoration

/**
 * 最后一根横线绘制
 *
 * @author hua
 * @version 2017/12/27 15:22
 *
 */
class HasLastLinearDivider(context: Context):LinearItemDecoration(context) {

    override fun hasLast(): Boolean {
        return true
    }

}