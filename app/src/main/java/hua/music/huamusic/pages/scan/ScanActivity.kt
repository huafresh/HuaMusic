package hua.music.huamusic.pages.scan

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity

/**
 * 真正的扫描界面
 *
 * @author hua
 * @version 2018/1/15 11:08
 *
 */
class ScanActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {

    companion object {
        val KEY_SCAN_PATH_LIST = "key_scan_path_list"
    }

    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)
        toolbar.inflateMenu(R.menu.menu_scan)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.scan_close -> {
                finish()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onCreateFragment(): Fragment? {
        return ScanFragment()
    }

}