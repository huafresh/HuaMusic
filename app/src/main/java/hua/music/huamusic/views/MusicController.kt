package hua.music.huamusic.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import hua.music.huamusic.R

/**
 * 底部音乐播放控制器
 * @author hua
 * @version 2017/12/22 17:34
 *
 */
class MusicController : IView {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mView: View? = null
    }

    private var mContent: Context? = null

    override fun getView(context: Context): View {
        if (mView == null) {
            mContent = context.applicationContext
            val inflater = LayoutInflater.from(mContent)
            mView = inflater.inflate(R.layout.music_controller, null, false)
        }
        return mView!!
    }

}