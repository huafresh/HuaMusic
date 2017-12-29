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
import android.widget.Toast
import hua.music.huamusic.base.BaseActivity
import hua.music.huamusic.home.HomeFragment
import hua.music.huamusic.pages.recent.RecentActivity
import hua.music.huamusic.service.MusicPlayerService
import hua.music.huamusic.wrapper.permission.PermissionHelper
import hua.music.huamusic.wrapper.permission.PermissionListener

@SuppressLint("InlinedApi")
class MainActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {

    private var mAlertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //提前扫描音乐列表，需要的时候进行观察，会直接收到最新的列表数据
//        MusicLiveModel.getInstance().scanLocalMusic(this.applicationContext)

        PermissionHelper.get(this).requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                object : PermissionListener {
                    override fun onGranted(permissions: Array<out String>?) {

                    }

                    override fun onDenied(permissions: Array<out String>?) {
                        mAlertDialog = AlertDialog.Builder(this@MainActivity)
                                .setTitle("警告")
                                .setMessage("本应用需要扫描sd卡")
                                .setPositiveButton("确定") { dialog, which ->
                                    finish()
                                }
                                .setCancelable(false)
                                .create()
                        mAlertDialog?.show()
                    }
                })

    }

    override fun onBackPressed() {
        if (mAlertDialog?.isShowing == true) {
            return
        }
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)
//        val toolBarChild = LayoutInflater.from(this).
//                inflate(R.layout.main_tool_bar, toolbar, false)
//        toolbar.addView(toolBarChild)
        toolbar.setNavigationIcon(R.drawable.left_menu_button)
        toolbar.title = getString(R.string.app_name)
        val menuItem = toolbar.menu.add(0, RecentActivity.CLEAR_ID, 0, getString(R.string.action_recent_clear))
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            RecentActivity.CLEAR_ID -> {
                Toast.makeText(this, "clear", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
        return item != null
    }

    override fun onCreateContent(): Fragment? {
        return HomeFragment()
    }


}
