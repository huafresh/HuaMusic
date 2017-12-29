package hua.music.huamusic.pages.local

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hua.music.huamusic.R
import hua.music.huamusic.data.MusicLiveModel

/**
 * 扫描音乐界面
 * @author hua
 * @version 2017/12/26 14:19
 *
 */
class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_music)
        MusicLiveModel.getInstance().scanLocalMusic(this)
    }


}