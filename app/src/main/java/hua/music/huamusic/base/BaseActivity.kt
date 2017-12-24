package hua.music.huamusic.base

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.FrameLayout
import hua.music.huamusic.R
import hua.music.huamusic.home.HomeFragment
import hua.music.huamusic.views.ViewFactory
import kotterknife.bindView

/**
 * 基类Activity
 *
 * @author hua
 * @version 2017/12/22 16:40
 *
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private val toolBar: Toolbar by bindView(R.id.tool_bar)
    private val flContent: FrameLayout by bindView(R.id.fl_content)
    private val flMusicController: FrameLayout by bindView(R.id.fl_music_controller)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        initToolBar(toolBar)
        initContent()
        initMusicController()
        setListeners()
    }


    private fun setListeners() {
        flMusicController.setOnClickListener {
            onMusicControllerClick()
        }
    }

    /**
     * 初始化ToolBar时调用
     */
    open protected fun initToolBar(toolbar: Toolbar) {

    }

    private fun initContent() {
        var content = supportFragmentManager.findFragmentById(R.id.fl_content)
        if (content == null) {
            content = onCreateContent()
        }
        if (content != null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fl_content, content)
                    .commit()
        }
    }

    /**
     * 创建内容，直接提供实例，不要先find
     */
    open protected fun onCreateContent(): Fragment? {
        return null
    }

    private fun initMusicController() {
        val controller = onCreateMusicController()
        if (controller != null) {
            flMusicController.visibility = View.VISIBLE
            flMusicController.addView(controller)
        }
    }

    /**
     * 创建音乐控制器
     */
    open protected fun onCreateMusicController(): View? {
        return ViewFactory.createView(this, ViewFactory.TYPE_MUSIC_CONTROLLER)
    }

    /**
     * 底部音乐控制器点击时调用
     */
    open protected fun onMusicControllerClick() {

    }

}