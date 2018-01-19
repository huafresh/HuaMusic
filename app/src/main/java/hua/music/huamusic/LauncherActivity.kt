package hua.music.huamusic

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hua.music.huamusic.wrapper.permission.PermissionHelper
import hua.music.huamusic.wrapper.permission.PermissionListener

/**
 * 启动页
 * @author hua
 * @version 2018/1/16 13:52
 *
 */
@SuppressLint("InlinedApi")
class LauncherActivity : AppCompatActivity() {

    private var mAlertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionHelper.get(this).requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                object : PermissionListener {
                    override fun onGranted(permissions: Array<out String>?) {
                        jumpMainActivity()
                    }

                    override fun onDenied(permissions: Array<out String>?) {
                        mAlertDialog = AlertDialog.Builder(this@LauncherActivity)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        if (mAlertDialog?.isShowing == true) {
            return
        }
        super.onBackPressed()
    }

    private fun jumpMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}