package hua.music.huamusic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import kotterknife.bindView

class MainActivity : AppCompatActivity() {
    private val viewHeader: View by bindView(R.id.view_header)
    private val bacLeftMenu: View by bindView(R.id.bac_left_menu)
    private val bacSearch: View by bindView(R.id.bac_search)
    private val flContent: FrameLayout by bindView(R.id.fl_content)
    private val flMusicController: FrameLayout by bindView(R.id.fl_music_controller)

    //val nameEditText: View by bindView(R.id.view_header)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}
