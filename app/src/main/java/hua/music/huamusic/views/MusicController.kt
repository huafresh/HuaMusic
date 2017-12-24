package hua.music.huamusic.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hua.music.huamusic.R

/**
 * 底部音乐播放控制器
 * @author hua
 * @version 2017/12/22 17:34
 *
 */
class MusicController : IViewFactory {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mView: View? = null
    }

    override fun getView(context: Context): View {
        if (mView == null) {
            val inflater = LayoutInflater.from(context.applicationContext)
            mView = inflater.inflate(R.layout.music_controller, null, false)
        }
        val parent = mView?.parent
        if (parent != null && parent is ViewGroup) {
            parent.removeView(mView)
        }
        return mView!!
    }

}