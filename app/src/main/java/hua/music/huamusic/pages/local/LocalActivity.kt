package hua.music.huamusic.pages.local

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity

/**
 * 本地音乐界面
 *
 * @author hua
 * @version 2017/12/22 10:00
 *
 */
class LocalActivity : BaseActivity() {

    override fun initToolBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.mipmap.ic_launcher)
        toolbar.title = getString(R.string.local)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val itemSearch = MenuItem()
        menu.add

        super.onCreateOptionsMenu(menu)

    }

}