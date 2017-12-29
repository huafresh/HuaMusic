package hua.music.huamusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import hua.music.huamusic.entitys.Music
import java.util.*
import java.util.concurrent.Executors

/**
 * 音乐播放服务
 *
 * @author hua
 * @version 2017/12/29 14:46
 *
 */
class MusicPlayerService : Service() {

    companion object {
        /**
         * 顺序播放
         */
        val PLAY_MODE_NORMAL = 0
        /**
         * 随机播放
         */
        val PLAY_MODE_RANDOM = 1
        /**
         * 单曲循环
         */
        val PLAY_MODE_REPEAT = 2
    }

    private val mPlayerBinder = PlayerBinder()
    private val mPlayingQueue = mutableListOf<Music>()
    private var mCurPos = 0
    private var mPlayMode = PLAY_MODE_NORMAL
    private val mMediaPlayer = MediaPlayer()
    private val mEventListeners = mutableListOf<EventListener>()
    private val mExecutorService = Executors.newFixedThreadPool(1)
    private val mProgressRunnable = ProgressRunnable()

    override fun onBind(intent: Intent?): IBinder {
        return mPlayerBinder
    }

    override fun onCreate() {
        setListeners()
        mExecutorService.submit(mProgressRunnable)
    }

    private fun setListeners() {
        mMediaPlayer.setOnPreparedListener {
            sendEvent(EventListener.PREPARED)
        }
        mMediaPlayer.setOnCompletionListener {
            sendEvent(EventListener.COMPLETED)
            next()
        }
        mMediaPlayer.setOnErrorListener { mp, what, extra ->
            sendEvent(EventListener.ERROR)
            false
        }
    }


    private fun addMusic(music: Music) {
        if (!mPlayingQueue.contains(music)) {
            mPlayingQueue.add(music)
        }
    }

    private fun addMusic(list: List<Music>) {
        mPlayingQueue.addAll(list)
    }

    private fun setPlayMode(mode: Int) {
        mPlayMode = when (mode) {
            PLAY_MODE_NORMAL -> {
                PLAY_MODE_NORMAL
            }
            PLAY_MODE_RANDOM -> {
                PLAY_MODE_RANDOM
            }
            PLAY_MODE_REPEAT -> {
                PLAY_MODE_REPEAT
            }
            else -> {
                throw IllegalArgumentException("不支持的播放模式")
            }
        }
    }

    private fun play(music: Music) {
        mMediaPlayer.reset()
        mMediaPlayer.setDataSource(music.path)
        mMediaPlayer.prepare()
        sendEvent(EventListener.PREPARED)
        mMediaPlayer.start()
        sendEvent(EventListener.STARTED)

        mProgressRunnable.mLastPosition = 0
    }

    private fun pause() {
        try {
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.pause()
                sendEvent(EventListener.PAUSED)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stop() {
        try {
            mMediaPlayer.stop()
            sendEvent(EventListener.STOPPED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun release() {
        try {
            mMediaPlayer.release()
            sendEvent(EventListener.ENDED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun previous() {
        val nextMusic = mPlayingQueue[getNextPlayPos(false)]
        play(nextMusic)
    }


    private fun next() {
        val nextMusic = mPlayingQueue[getNextPlayPos(true)]
        play(nextMusic)
    }

    private fun getNextPlayPos(isNext: Boolean): Int {
        when (mPlayMode) {
            PLAY_MODE_NORMAL -> {
                if (isNext) {
                    mCurPos++
                } else {
                    mCurPos--
                }
            }
            PLAY_MODE_RANDOM -> {
                mCurPos = Random().nextInt(mPlayingQueue.size)
            }
            else -> {

            }
        }
        return mCurPos
    }

    private fun seekTo(time: Int) {
        try {
            mMediaPlayer.seekTo(time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addEventListener(listener: EventListener) {
        if (!mEventListeners.contains(listener)) {
            mEventListeners.add(listener)
        }
    }

    private fun removeEventListener(listener: EventListener) {
        mEventListeners.remove(listener)
    }

    private fun sendEvent(type: Int, vararg arg: Any) {
        mEventListeners.forEach {
            it.onEvent(type, arg)
        }
    }

    /**
     * 每隔一定的时间发送播放进度
     */
    private inner class ProgressRunnable : Runnable {

        var mLastPosition = 0

        override fun run() {
            while (true) {
                try {
                    val curPosition = mMediaPlayer.currentPosition
                    if (curPosition != mLastPosition) {
                        sendEvent(EventListener.UPDATING_PROGRESS, curPosition)
                        mLastPosition = curPosition
                    }
                    Thread.sleep(500)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 播放服务对外接口
     */
    inner class PlayerBinder : Binder() {

        fun addMusic(music: Music) {
            this@MusicPlayerService.addMusic(music)
        }

        fun addMusic(list: List<Music>) {
            this@MusicPlayerService.addMusic(list)
        }

        fun setPlayMode(mode: Int) {
            this@MusicPlayerService.setPlayMode(mode)
        }

        fun play(music: Music) {
            this@MusicPlayerService.play(music)
        }

        fun pause() {
            this@MusicPlayerService.pause()
        }

        fun stop() {
            this@MusicPlayerService.stop()
        }

        fun release() {
            this@MusicPlayerService.release()
        }

        fun previous() {
            this@MusicPlayerService.previous()
        }

        fun next() {
            this@MusicPlayerService.next()
        }

        fun seekTo(time: Int) {
            this@MusicPlayerService.seekTo(time)
        }

        fun addEventListener(listener: EventListener) {
            this@MusicPlayerService.addEventListener(listener)
        }

        fun removeEventListener(listener: EventListener) {
            this@MusicPlayerService.removeEventListener(listener)
        }
    }


}