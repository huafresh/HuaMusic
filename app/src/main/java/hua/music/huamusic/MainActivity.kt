package hua.music.huamusic

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import hua.music.huamusic.base.BaseActivity
import hua.music.huamusic.home.HomeFragment
import hua.music.huamusic.views.MusicController
import kotterknife.bindView

class MainActivity : BaseActivity() {

    override fun initToolBar(toolbar: Toolbar) {
        val toolBarChild = LayoutInflater.from(this).
                inflate(R.layout.main_tool_bar, toolbar, false)
        toolbar.addView(toolBarChild)
    }

    override fun onCreateContent(): Fragment? {
        return HomeFragment()
    }

    override fun onCreateMusicController(): View? {
        return MusicController().getView(this)
    }

}
