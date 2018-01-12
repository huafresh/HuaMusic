package hua.music.huamusic.pages.local

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.utils.CommonUtil

/**
 * 本地音乐界面
 *
 * @author hua
 * @version 2017/12/22 10:00
 *
 */
class LocalActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MusicLiveModel.getInstance().scanLocalMusic(this.applicationContext)

        setObservers()
    }

    private fun setObservers() {
        MusicLiveModel.getInstance().toastLiveData.observe(this, Observer {
            if (!TextUtils.isEmpty(it)) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)
        toolbar.title = getString(R.string.menu_local)
        toolbar.overflowIcon = CommonUtil.getDrawable(this, R.drawable.actionbar_more, null)
        toolbar.inflateMenu(R.menu.menu_local)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.search -> {
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show()
            }
            R.id.scan -> {
                Toast.makeText(this, "scan", Toast.LENGTH_SHORT).show()
            }
            R.id.order -> {
                Toast.makeText(this, "order", Toast.LENGTH_SHORT).show()
            }
            else -> {

            }
        }
        return item != null
    }

    override fun onCreateFragment(): Fragment? {
        return LocalFragment()
    }

}