package hua.music.huamusic.pages.recent

import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity

/**
 * 最近播放页面
 *
 * @author hua
 * @version 2017/12/22 10:00
 *
 */
class RecentActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {

    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)
        toolbar.setTitle(R.string.menu_recent)
        toolbar.inflateMenu(R.menu.menu_recent)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.clear -> {
                Toast.makeText(this, "clear", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
        return item != null
    }

    override fun onCreateContent(): Fragment? {
        return RecentFragment()
    }

}