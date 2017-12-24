package hua.music.huamusic.pages.recent

import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity
import hua.music.huamusic.utils.dp2px
import hua.music.huamusic.utils.sp2px
import hua.music.huamusic.views.ViewFactory

/**
 * @author hua
 * @version 2017/12/22 10:00
 *
 */
class RecentActivity : BaseActivity() {

    override fun initToolBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.setTitle(R.string.menu_recent)
        val textView = TextView(this)
        textView.setText(R.string.action_recent_clear)
        val textParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        textParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        val paddingLeftRight = textView.dp2px(10f)
//        val paddingLeftRight = 10
        textView.setPadding(paddingLeftRight, 0, paddingLeftRight, 0)
        val container = RelativeLayout(this)
        container.addView(textView, textParams)
        toolbar.addView(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onCreateContent(): Fragment? {
        return RecentFragment()
    }

}