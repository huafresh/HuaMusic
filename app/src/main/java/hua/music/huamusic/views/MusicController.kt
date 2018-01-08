package hua.music.huamusic.views

import android.app.Activity
import android.app.Service
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import hua.music.huamusic.R
import hua.music.huamusic.pages.play.PlayActivity
import hua.music.huamusic.service.EventListener
import hua.music.huamusic.service.MusicPlayerService
import hua.music.huamusic.storage.StorageManager

/**
 * 底部音乐播放控制器
 *
 * @author hua
 * @version 2017/12/22 17:34
 *
 */
class MusicController : IViewFactory, EventListener, LifecycleObserver {
    private var ivCover: ImageView? = null
    private var tvTitle: TextView? = null
    private var tvAuthor: TextView? = null
    private var bacPlayBtn: View? = null
    private var ivPlayBtn: ImageView? = null
    private var bacPlayList: View? = null
    private var ivPlayList: ImageView? = null

    private var mPlayBinder: MusicPlayerService.PlayerBinder? = null
    private var mConnection: ServiceConnection? = null
    private var mView: View? = null
    private var mContext: Context? = null

    override fun getView(context: Context): View? {
        if (mView == null) {
            mContext = context
            val inflater = LayoutInflater.from(mContext)
            mView = inflater.inflate(R.layout.music_controller, null, false)
            findViews()
            bindPlayService()
            setListeners()
        }
        detach()

        val first = StorageManager.getInstance.getFromDisk(MusicPlayerService.KEY_FIRST_PLAY)
        if (first != null) {
            mView?.visibility = View.VISIBLE
        } else {
            mView?.visibility = View.GONE
        }

        return mView
    }


    fun detach() {
        val parent = mView?.parent
        if (parent != null && parent is ViewGroup) {
            parent.removeView(mView)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (mConnection != null) {
            mContext?.unbindService(mConnection)
        }
    }

    private fun findViews() {
        ivCover = mView?.findViewById(R.id.iv_cover)
        tvTitle = mView?.findViewById(R.id.tv_title)
        tvAuthor = mView?.findViewById(R.id.tv_author)
        ivPlayBtn = mView?.findViewById(R.id.iv_play_btn)
        ivPlayList = mView?.findViewById(R.id.iv_play_list)
        bacPlayBtn = mView?.findViewById(R.id.bac_play_btn)
        bacPlayList = mView?.findViewById(R.id.bac_play_list)
    }

    private fun bindPlayService() {
        val intent = Intent(mContext, MusicPlayerService::class.java)
        mConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                mPlayBinder = null
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mPlayBinder = service as MusicPlayerService.PlayerBinder?
                mPlayBinder?.addEventListener(this@MusicController)
                refreshControllerWithCurMusic()
            }
        }
        mContext?.bindService(intent, mConnection, Service.BIND_AUTO_CREATE)
    }

    private fun setListeners() {
        mView?.setOnClickListener {
            val intent = Intent(mContext, PlayActivity::class.java)
            if (mContext !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            mContext?.startActivity(intent)
        }
        bacPlayBtn?.setOnClickListener {
            if (mPlayBinder?.isPlaying() == true) {
                mPlayBinder?.pause()
            } else {
                mPlayBinder?.start()
            }
        }
        ivPlayList?.setOnClickListener {
            //todo 弹出播放列表
        }
    }

    private fun refreshControllerWithCurMusic() {
        val music = mPlayBinder?.getCurMusic()
        tvTitle?.text = music?.title ?: mContext?.getString(R.string.default_title)
        tvAuthor?.text = music?.author ?: mContext?.getString(R.string.default_author)
        if (mPlayBinder?.isPlaying() == true) {
            ivPlayBtn?.setImageResource(R.drawable.controller_pause)
        } else {
            ivPlayBtn?.setImageResource(R.drawable.controller_play)
        }
    }

    override fun onEvent(type: Int, vararg arg: Any) {
        when (type) {
            EventListener.STARTED -> {
                ivPlayBtn?.setImageResource(R.drawable.controller_pause)
                if (mView?.visibility == View.GONE) {
                    mView?.visibility = View.VISIBLE
                }
            }
            EventListener.PAUSED -> {
                ivPlayBtn?.setImageResource(R.drawable.controller_play)
            }
            EventListener.RESOURCE_CHANGED -> {
                refreshControllerWithCurMusic()
            }
            else -> {
            }
        }
    }
}