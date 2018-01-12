package hua.music.huamusic.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import hua.music.huamusic.R
import hua.music.huamusic.utils.CommonUtil
import hua.music.huamusic.views.MusicController
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
        addContent()
    }

    override fun onResume() {
        super.onResume()
        addMusicController()
    }

    /**
     * 初始化ToolBar时调用
     */
    @CallSuper
    open protected fun initToolBar(toolbar: Toolbar) {
        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.setTitleTextColor(CommonUtil.getColor(this, android.R.color.white, null))
    }

    private fun addContent() {
        var content = supportFragmentManager.findFragmentById(R.id.fl_content)
        if (content == null) {
            content = onCreateFragment()
        }
        if (content != null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fl_content, content)
                    .commit()
        }

    }

    /**
     * 创建fragment内容，直接提供实例，不要先find
     */
    open protected fun onCreateFragment(): Fragment? {
        return null
    }

    private fun addMusicController() {
        val controller = onCreateMusicController()
        if (controller != null) {
            flMusicController.visibility = View.VISIBLE
            flMusicController.addView(controller)
        } else {
            flMusicController.visibility = View.GONE
        }
    }

    /**
     * 创建音乐控制器
     */
    open protected fun onCreateMusicController(): View? {
        val controller = MusicController().getView(this)
        return if (controller?.visibility == View.VISIBLE) {
            controller
        } else {
            null
        }
    }


}