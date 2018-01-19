package hua.music.huamusic.pages.play

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.bumptech.glide.Glide
import hua.music.huamusic.R
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.service.EventListener
import hua.music.huamusic.service.MusicPlayerService
import hua.music.huamusic.utils.CommonUtil
import jp.wasabeef.glide.transformations.BlurTransformation
import kotterknife.bindView

/**
 * 播放页面
 *
 * @author hua
 * @version 2017/12/29 16:56
 *
 */
class PlayActivity : AppCompatActivity(), View.OnClickListener, EventListener {
    private val ivBackground: ImageView by bindView(R.id.iv_background)
    private val viewHeader: View by bindView(R.id.view_header)
    private val bacBack: View by bindView(R.id.bac_back)
    private val ivBack: ImageView by bindView(R.id.iv_back)
    private val bacShare: View by bindView(R.id.bac_share)
    private val ivShare: ImageView by bindView(R.id.iv_share)
    private val tvTitle: TextView by bindView(R.id.tv_title)
    private val tvAuthor: TextView by bindView(R.id.tv_author)
    private val tvLrcTextView: View by bindView(R.id.tv_lrc_textView)
    private val bacButtons: View by bindView(R.id.bac_buttons)
    private val ivLove: ImageView by bindView(R.id.iv_love)
    private val ivDown: ImageView by bindView(R.id.iv_down)
    private val ivComment: ImageView by bindView(R.id.iv_comment)
    private val ivMore: ImageView by bindView(R.id.iv_more)
    private val bacSeekBar: View by bindView(R.id.bac_seek_bar)
    private val tvTimeCurrent: TextView by bindView(R.id.tv_time_current)
    private val seekBar: SeekBar by bindView(R.id.seek_bar)
    private val tvTimeTotal: TextView by bindView(R.id.tv_time_total)
    private val bacController: View by bindView(R.id.bac_controller)
    private val ivPlayMode: ImageView by bindView(R.id.iv_play_mode)
    private val ivPrevious: ImageView by bindView(R.id.iv_previous)
    private val ivPlay: ImageView by bindView(R.id.iv_play)
    private val ivNext: ImageView by bindView(R.id.iv_next)
    private val ivPlayList: ImageView by bindView(R.id.iv_play_list)
    private lateinit var mConnection: ServiceConnection
    private var mPlayBinder: MusicPlayerService.PlayerBinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        initStatusBar()
        bindPlayService()
        setListeners()
    }

    private fun initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            CommonUtil.setStatusBarTranslucent(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayBinder?.removeEventListener(this)
        unbindService(mConnection)
    }

    private fun bindPlayService() {
        val intent = Intent(this, MusicPlayerService::class.java)
        mConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                mPlayBinder = null
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mPlayBinder = service as MusicPlayerService.PlayerBinder?
                mPlayBinder?.addEventListener(this@PlayActivity)
                initViews()
            }
        }
        bindService(intent, mConnection, Service.BIND_AUTO_CREATE)
    }

    private fun initViews() {
        refreshPageWithCurMusic()
        when (mPlayBinder?.getPlayMode()) {
            MusicPlayerService.PLAY_MODE_NORMAL -> {
                ivPlayMode.setImageResource(R.drawable.play_mode_normal)
            }
            MusicPlayerService.PLAY_MODE_REPEAT -> {
                ivPlayMode.setImageResource(R.drawable.play_mode_repeat)
            }
            MusicPlayerService.PLAY_MODE_RANDOM -> {
                ivPlayMode.setImageResource(R.drawable.play_mode_random)
            }
            else -> {
            }
        }
        if (mPlayBinder?.isPlaying() == true) {
            ivPlay.setImageResource(R.drawable.selector_pause)
        } else {
            ivPlay.setImageResource(R.drawable.selector_play)
        }
    }

    private fun refreshPageWithCurMusic() {
        val music = mPlayBinder?.getCurMusic()
        Glide.with(this)
                .load(R.drawable.test_cover2)
                .bitmapTransform(BlurTransformation(this,25,5))
                .into(ivBackground)
        tvTitle.text = music?.title ?: getString(R.string.default_title)
        tvAuthor.text = music?.author ?: getString(R.string.default_author)
        tvTimeTotal.text = "${music?.duration ?: getString(R.string.default_time)}"
    }

    private fun setListeners() {
        bacBack.setOnClickListener(this)
        bacShare.setOnClickListener(this)
        ivPlayMode.setOnClickListener(this)
        ivPrevious.setOnClickListener(this)
        ivPlay.setOnClickListener(this)
        ivNext.setOnClickListener(this)
        ivPlayList.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bac_back -> {
                finish()
            }
            R.id.bac_share -> {
                //todo 分享
            }
            R.id.iv_play_mode -> {
                switchPlayMode()
            }
            R.id.iv_previous -> {
                mPlayBinder?.previous()
            }
            R.id.iv_play -> {
                if (mPlayBinder?.isPlaying() == true) {
                    mPlayBinder?.pause()
                } else {
                    mPlayBinder?.start()
                }
            }
            R.id.iv_next -> {
                mPlayBinder?.next()
            }
            R.id.iv_play_list -> {
                //todo 展示播放列表
            }
            else -> {

            }
        }
    }

    private fun switchPlayMode() {
        val curMode = mPlayBinder?.getPlayMode() ?: return
        when (curMode) {
            MusicPlayerService.PLAY_MODE_NORMAL -> {
                ivPlayMode.setImageResource(R.drawable.play_mode_random)
                mPlayBinder?.setPlayMode(MusicPlayerService.PLAY_MODE_RANDOM)
            }
            MusicPlayerService.PLAY_MODE_RANDOM -> {
                ivPlayMode.setImageResource(R.drawable.play_mode_repeat)
                mPlayBinder?.setPlayMode(MusicPlayerService.PLAY_MODE_REPEAT)
            }
            MusicPlayerService.PLAY_MODE_REPEAT -> {
                ivPlayMode.setImageResource(R.drawable.play_mode_normal)
                mPlayBinder?.setPlayMode(MusicPlayerService.PLAY_MODE_NORMAL)
            }
            else -> {
            }
        }
    }

    override fun onEvent(type: Int, vararg arg: Any) {
        when (type) {
            EventListener.STARTED -> {
                ivPlay.setImageResource(R.drawable.selector_pause)
            }
            EventListener.PAUSED -> {
                ivPlay.setImageResource(R.drawable.selector_play)
            }
            EventListener.UPDATING_PROGRESS -> {
                val curTime = arg[0] as Int
                tvTimeCurrent.text = CommonUtil.formatTimeToString(curTime)
                val music = mPlayBinder?.getCurMusic()
                val percent = curTime / (music?.duration ?: curTime)
                seekBar.progress = percent
            }
            EventListener.RESOURCE_CHANGED -> {
                refreshPageWithCurMusic()
            }
            else -> {
            }
        }
    }

}