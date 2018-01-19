package hua.music.huamusic.pages.scan

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity

/**
 * 扫描音乐界面
 *
 * @author hua
 * @version 2018/1/11 9:38
 *
 */
class ScanHomeActivity : BaseActivity() {

    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)
        toolbar.setTitle(R.string.scan_music_title)
    }

    override fun onCreateFragment(): Fragment? {
        return ScanHomeFragment()
    }

}