package hua.music.huamusic.pages.local

import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity
import hua.music.huamusic.utils.CommonUtil
import hua.music.huamusic.views.ViewFactory

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
        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.title = getString(R.string.menu_local)
        toolbar.overflowIcon = CommonUtil.getDrawable(this, R.drawable.actionbar_more, null)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_local, menu)
        return true
    }

    override fun onCreateContent(): Fragment? {
        return LocalFragment()
    }

}