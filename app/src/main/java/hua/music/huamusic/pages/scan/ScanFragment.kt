package hua.music.huamusic.pages.scan

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseFragment
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.widget.ScanMusicView
import kotterknife.bindView
import java.io.File

/**
 * @author hua
 * @version 2018/1/15 11:27
 */
@SuppressLint("SetTextI18n")
class ScanFragment : BaseFragment() {
    private val btnRetry: Button by bindView(R.id.btn_retry)
    private val btnCancel: Button by bindView(R.id.btn_cancel)
    private val scanMusic: ScanMusicView by bindView(R.id.scan_music)
    private val tvCompletePrefix: TextView by bindView(R.id.tv_complete_prefix)
    private val tvCompleteSum: TextView by bindView(R.id.tv_complete_sum)
    private val tvCompleteSuffix: TextView by bindView(R.id.tv_complete_suffix)
    private val btnBack: Button by bindView(R.id.btn_back)

    private val mMainHandler = Handler(Looper.getMainLooper())
    private var scanCount = 0
    private var scanThread: ScanThread? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_scan
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setListeners()
        addObservers()
    }

    private fun setListeners() {
        btnCancel.setOnClickListener {
            scanMusic.stop()
            showRetry()
            scanThread?.interrupt()
        }
        btnRetry.setOnClickListener {
            startScan()
        }
        btnBack.setOnClickListener {
            activity.finish()
        }
    }

    private fun addObservers() {
        MusicLiveModel.getInstance().singleSongList.observe(this, Observer {
            startScan()
        })
    }

    private fun startScan() {
        scanCount = 0
        val pathList = arguments.getStringArrayList(ScanActivity.KEY_SCAN_PATH_LIST)
        if (scanThread != null) {
            scanThread?.running = false
        }
        scanThread = ScanThread(pathList)
        scanThread?.start()
        scanMusic.start()
        showCancel()
    }

    private fun showCancel() {
        tvCompletePrefix.visibility = View.INVISIBLE
        tvCompleteSum.visibility = View.INVISIBLE
        tvCompleteSuffix.visibility = View.INVISIBLE
        btnCancel.visibility = View.VISIBLE
        btnRetry.visibility = View.GONE
        btnBack.visibility = View.GONE
    }

    private fun showRetry() {
        tvCompletePrefix.visibility = View.INVISIBLE
        tvCompleteSum.visibility = View.INVISIBLE
        tvCompleteSuffix.visibility = View.INVISIBLE
        btnCancel.visibility = View.GONE
        btnRetry.visibility = View.VISIBLE
        btnBack.visibility = View.GONE
    }

    private fun showComplete() {
        tvCompletePrefix.visibility = View.VISIBLE
        tvCompleteSum.visibility = View.VISIBLE
        tvCompleteSuffix.visibility = View.VISIBLE
        btnCancel.visibility = View.GONE
        btnRetry.visibility = View.GONE
        btnBack.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        scanThread?.running = false
    }

    private inner class ScanThread(list: List<String>) : Thread() {
        private val pathList = list
        var running = true

        override fun run() {
            super.run()
            pathList.forEach {
                scan(it)
            }

            //扫描结束
            if (running) {
                mMainHandler.postDelayed(Runnable {
                    tvCompleteSum.text = scanCount.toString() + "首"
                    scanMusic.complete()
                    showComplete()
                    //因为扫描只是通知系统数据库更新，所以速度很快，所以为了出现扫描动画，
                    //这里进行延时
                }, 3000)
            }
        }

        private fun scan(path: String) {
            if (!running) {
                return
            }
            val file = File(path)
            if (file.isDirectory) {
                file.listFiles().forEach {
                    scan(it.absolutePath)
                }
            }

            //是文件，则判断是否为mp3文件，如果是并且列表中不存在，则通知系统添加到数据库
            val filePath = file.absolutePath
            if (isMp3(filePath) && !MusicLiveModel.getInstance().singleSongList.isExist(filePath)) {
                MediaScannerConnection.scanFile(activity, arrayOf(filePath), null, null)
                scanCount += 1
            }
        }

        private fun isMp3(path: String): Boolean {
            return path.endsWith(".mp3")
        }

    }


}