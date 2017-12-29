package hua.music.huamusic.pages.recent

import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.SHOW_AS_ACTION_ALWAYS
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity
import hua.music.huamusic.utils.dp2px
import hua.music.huamusic.utils.sp2px
import hua.music.huamusic.views.ViewFactory

/**
 * 最近播放页面
 * @author hua
 * @version 2017/12/22 10:00
 *
 */
class RecentActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {

    companion object {
        val CLEAR_ID = 1
    }

    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)
        toolbar.setTitle(R.string.menu_recent)
        val menuItem = toolbar.menu.add(0, CLEAR_ID, 0, getString(R.string.action_recent_clear))
        menuItem.setShowAsAction(SHOW_AS_ACTION_ALWAYS)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            CLEAR_ID -> {
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