package hua.music.huamusic.pages.scan

import android.support.v7.widget.Toolbar
import android.view.MenuItem
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity

/**
 *
 * 扫描设置页面
 *
 * @author hua
 * @version 2018/1/11 11:03
 *
 */
class ScanSettingActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {
    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)

        toolbar.setTitle(R.string.scan_setting_title)
        toolbar.inflateMenu(R.menu.menu_scan_setting)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.scan_setting_confirm -> {
                finish()
                true
            }
            else -> {
                false
            }
        }
    }



}