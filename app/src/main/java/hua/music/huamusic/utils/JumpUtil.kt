package hua.music.huamusic.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import hua.music.huamusic.pages.down.DownActivity
import hua.music.huamusic.pages.local.LocalActivity
import hua.music.huamusic.pages.recent.RecentActivity

/**
 * Activity跳转中心
 *
 * @author hua
 * @version 2017/12/22 9:48
 *
 */
object JumpUtil {

    /**
     * 页面类型
     */
    val ACTIVITY_TYPE_LOCAL = "activity_type_local"
    val ACTIVITY_TYPE_RECENT = "activity_type_recent"
    val ACTIVITY_TYPE_DOWNLOAD = "activity_type_download"
    val ACTIVITY_TYPE_SONG_LIST = "activity_type_song_list"

    /**
     * 启动Activity
     * @param type Activity的类型
     */
    fun startActivity(context: Context, type: String) {
        val intent = Intent()
        intent.setClass(context, getClassByType(type) ?: return)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun getClassByType(type: String): Class<*>? {
        var cls: Class<*>? = null
        cls = when (type) {
            ACTIVITY_TYPE_LOCAL -> {
                LocalActivity().javaClass
            }
            ACTIVITY_TYPE_RECENT -> {
                RecentActivity().javaClass
            }
            ACTIVITY_TYPE_DOWNLOAD -> {
                DownActivity().javaClass
            }
            else -> {
                null
            }
        }
        return cls
    }

}