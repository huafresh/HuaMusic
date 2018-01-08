package hua.music.huamusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.storage.StorageManager
import hua.music.huamusic.utils.JsonParseUtil
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

        /**
         * 是否首次播放
         */
        val KEY_FIRST_PLAY = "key_first_play"

        /**
         * 最后播放的音乐
         */
        val KEY_LAST_MUSIC = "key_last_music"
    }

    private val mPlayerBinder = PlayerBinder()
    private val mPlayList = mutableListOf<Music>()
    private var mCurPos = -1
    private var mPlayMode = PLAY_MODE_NORMAL
    private val mMediaPlayer = MediaPlayer()
    private val mEventListeners = mutableListOf<EventListener>()
    private val mExecutorService = Executors.newFixedThreadPool(1)
    private val mProgressRunnable = ProgressRunnable()
    private var isPrepared = false
    private var isFirst = true

    override fun onBind(intent: Intent?): IBinder {
        return mPlayerBinder
    }

    override fun onCreate() {
        //恢复最后一次播放的音乐
        val musicString = StorageManager.getInstance.getFromDisk(KEY_LAST_MUSIC)
        if (musicString != null) {
            val music = JsonParseUtil.parseJsonToObject(musicString, Music::class.java)
            if (music != null) {
                addMusic(music)
                mCurPos = mPlayList.indexOf(music)
            }
        }

        val first = StorageManager.getInstance.getFromDisk(KEY_FIRST_PLAY)
        isFirst = first == null

        setListeners()
    }

    override fun onDestroy() {
        //保存最后播放的音乐
        if (mCurPos != -1) {
            val musicString = JsonParseUtil.parseObjectToString(mPlayList[mCurPos])
            if (musicString != null) {
                StorageManager.getInstance.saveToDisk(KEY_LAST_MUSIC, musicString)
            }
        }

        //保存最近播放列表
        val recentList = MusicLiveModel.getInstance().recentList.value
        val recentString = JsonParseUtil.parseObjectToString(recentList)
        if (recentString != null) {
            StorageManager.getInstance.saveToDisk(MusicLiveModel.KEY_STORE_RECENT, recentString)
        }
    }

    private fun setListeners() {
        mMediaPlayer.setOnPreparedListener {
            sendEvent(EventListener.PREPARED)
        }
        mMediaPlayer.setOnCompletionListener {
            //刚设置就回调，无奈
            if (isPrepared) {
                sendEvent(EventListener.COMPLETED)
                next()
            }
        }
        mMediaPlayer.setOnErrorListener { mp, what, extra ->
            sendEvent(EventListener.ERROR)
            false
        }
    }


    private fun addMusic(music: Music) {
        if (!mPlayList.contains(music)) {
            mPlayList.add(music)
        }
    }

    private fun addMusic(list: List<Music>) {
        mPlayList.addAll(list)
    }

    private fun clearPlayList() {
        mPlayList.clear()
    }

    private fun getCurMusic(): Music? {
        if (mCurPos != -1) {
            return mPlayList[mCurPos]
        }
        return null
    }

    private fun getPlayList(): List<Music> {
        return List(mPlayList.size, { index ->
            mPlayList[index]
        })
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

    private fun getPlayMode(): Int {
        return mPlayMode
    }

    private fun play(music: Music) {
        mMediaPlayer.reset()

        mMediaPlayer.setDataSource(music.filePath)
        val pos = mPlayList.indexOf(music)
        mCurPos = if (pos == -1) {
            mPlayList.add(0, music)
            0
        } else {
            pos
        }
        sendEvent(EventListener.RESOURCE_CHANGED)

        mMediaPlayer.prepare()
        sendEvent(EventListener.PREPARED)

        mMediaPlayer.start()
        sendEvent(EventListener.STARTED)

        mProgressRunnable.mLastPosition = 0
        isPrepared = true

        //开启进度轮询线程
        mProgressRunnable.isRunning = true
        mExecutorService.submit(mProgressRunnable)

        if (isFirst) {
            StorageManager.getInstance.saveToDisk(KEY_FIRST_PLAY, "false")
        }
    }

    private fun pause() {
        try {
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.pause()
                sendEvent(EventListener.PAUSED)
                mProgressRunnable.isRunning = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun start() {
        if (isPrepared) {
            mMediaPlayer.start()
            sendEvent(EventListener.STARTED)
        } else {
            play(mPlayList[mCurPos])
        }
        mProgressRunnable.isRunning = true
        mExecutorService.submit(mProgressRunnable)
    }

    private fun stop() {
        try {
            mMediaPlayer.stop()
            sendEvent(EventListener.STOPPED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mProgressRunnable.isRunning = false
    }

    private fun release() {
        try {
            mMediaPlayer.release()
            sendEvent(EventListener.ENDED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mProgressRunnable.isRunning = false
    }

    private fun isPlaying(): Boolean {
        try {
            return mMediaPlayer.isPlaying
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun previous() {
        val nextMusic = mPlayList[getNextPlayPos(false)]
        play(nextMusic)
    }


    private fun next() {
        val nextMusic = mPlayList[getNextPlayPos(true)]
        play(nextMusic)
    }

    private fun getNextPlayPos(isNext: Boolean): Int {
        when (mPlayMode) {
            PLAY_MODE_NORMAL -> {
                if (isNext) {
                    if (mCurPos == mPlayList.size - 1) {
                        mCurPos = 0
                    } else {
                        mCurPos++
                    }
                } else {
                    if (mCurPos == 0) {
                        mCurPos = mPlayList.size - 1
                    } else {
                        mCurPos--
                    }
                }
            }
            PLAY_MODE_RANDOM -> {
                mCurPos = Random().nextInt(mPlayList.size - 1)
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
        var isRunning = true
        override fun run() {
            while (isRunning) {
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

        fun clearPlayList() {
            this@MusicPlayerService.clearPlayList()
        }

        fun getCurMusic(): Music? {
            return this@MusicPlayerService.getCurMusic()
        }

        /**
         * 获取播放列表。这里返回的是不可变的集合。
         * 如果是要添加音乐到列表，应该使用[addMusic]系列方法
         */
        fun getPlayList(): List<Music> {
            return this@MusicPlayerService.getPlayList()
        }

        fun setPlayMode(mode: Int) {
            this@MusicPlayerService.setPlayMode(mode)
        }

        fun getPlayMode(): Int {
            return this@MusicPlayerService.getPlayMode()
        }

        fun play(music: Music) {
            this@MusicPlayerService.play(music)
        }

        fun pause() {
            this@MusicPlayerService.pause()
        }

        /**
         * 开始播放当前设置的music
         */
        fun start() {
            this@MusicPlayerService.start()
        }

        fun stop() {
            this@MusicPlayerService.stop()
        }

        fun release() {
            this@MusicPlayerService.release()
        }

        fun isPlaying(): Boolean {
            return this@MusicPlayerService.isPlaying()
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