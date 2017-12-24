package hua.music.huamusic.pages.down

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity

/**
 * 下载管理
 *
 * @author hua
 * @version 2017/12/22 10:00
 *
 */
class DownActivity : BaseActivity() {

    override fun initToolBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.setTitle(R.string.menu_down)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_down, menu)
        return true
    }

    override fun onCreateContent(): Fragment? {
        return DownFragment()
    }

}