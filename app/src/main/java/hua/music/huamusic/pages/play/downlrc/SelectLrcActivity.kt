package hua.music.huamusic.pages.play.downlrc

import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity

/**
 * 歌词下载页面
 *
 * @author hua
 * @version 2018/1/19 16:29
 *
 */
class SelectLrcActivity :BaseActivity() {

    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)
        toolbar.setTitle(R.string.select_lrc_title)
    }

    override fun onCreateFragment(): Fragment? {
        return SelectLrcFragment()
    }

}