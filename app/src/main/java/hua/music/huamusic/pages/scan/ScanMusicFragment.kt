package hua.music.huamusic.pages.scan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseFragment
import kotterknife.bindView

/**
 * 扫描音乐界面首页fragment
 *
 * @author hua
 * @version 2018/1/11 9:43
 *
 */
class ScanMusicFragment : BaseFragment() {
    private val ivPhone: ImageView by bindView(R.id.iv_phone)
    private val ivSearch: ImageView by bindView(R.id.iv_search)
    private val btnWhole: Button by bindView(R.id.btn_whole)
    private val btnCustom: Button by bindView(R.id.btn_custom)
    private val ivSetting: ImageView by bindView(R.id.iv_setting)
    private val tvSetting: TextView by bindView(R.id.tv_setting)

    override fun getLayoutId(): Int {
        return R.layout.fragment_scan_music
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        btnWhole.setOnClickListener {
            val intent = Intent(activity, ScanWholeActivity::class.java)
            startActivity(intent)
        }
        btnCustom.setOnClickListener {
            val intent = Intent(activity, ScanCustomActivity::class.java)
            startActivity(intent)
        }
        ivSetting.setOnClickListener {
            jumpScanSetting()
        }
        tvSetting.setOnClickListener {
            jumpScanSetting()
        }
    }

    private fun jumpScanSetting() {
        val intent = Intent(activity, ScanSettingActivity::class.java)
        startActivity(intent)
    }

}