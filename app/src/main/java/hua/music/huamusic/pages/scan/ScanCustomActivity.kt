package hua.music.huamusic.pages.scan

import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity

/**
 * 自定义扫描页面
 *
 * @author hua
 * @version 2018/1/11 11:02
 *
 */
class ScanCustomActivity :BaseActivity(), Toolbar.OnMenuItemClickListener {

    private var isAllSelect: Boolean? = null
    private val fragment = ScanCustomFragment()

    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)
        toolbar.setTitle(R.string.scan_custom_title)
        toolbar.inflateMenu(R.menu.menu_scan_custom)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onCreateFragment(): Fragment? {
        return fragment
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.scan_select_all -> {
                isAllSelect = isAllSelect != true
                if (isAllSelect == true) {
                    item.title = resources.getString(R.string.scan_custom_all_cancel)
                } else{
                    item.title = resources.getString(R.string.scan_custom_all)
                }
                fragment.setAllSelect(isAllSelect)
                fragment.refreshPage()
                true
            }
            else -> {
                false
            }
        }
    }

}