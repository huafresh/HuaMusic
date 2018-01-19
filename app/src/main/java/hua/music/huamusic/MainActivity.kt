package hua.music.huamusic

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import hua.music.huamusic.base.BaseActivity
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.data.network.NetData
import hua.music.huamusic.home.HomeFragment
import hua.music.huamusic.pages.recent.RecentActivity
import hua.music.huamusic.service.MusicPlayerService
import hua.music.huamusic.views.MusicController
import hua.music.huamusic.wrapper.permission.PermissionHelper
import hua.music.huamusic.wrapper.permission.PermissionListener


class MainActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {

    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.left_menu_button)
        toolbar.title = getString(R.string.app_name)
        toolbar.inflateMenu(R.menu.menu_home)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.search -> {
                NetData.getInstance().downLoadLrc("",{

                })
            }
            else -> {
            }
        }
        return item != null
    }

    override fun onCreateFragment(): Fragment? {
        return HomeFragment()
    }

}
