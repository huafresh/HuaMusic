package hua.music.huamusic.pages.local

import android.app.Service
import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hua.music.huamusic.R
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.pages.adapters.SingleSongAdapter
import hua.music.huamusic.pages.play.PlayActivity
import hua.music.huamusic.service.MusicPlayerService
import hua.music.huamusic.storage.StorageManager
import hua.music.huamusic.utils.JsonParseUtil
import kotterknife.bindView

/**
 * 本地音乐单曲列表
 *
 * Created by hua on 2017/12/23.
 */
class SingleSongFragment : Fragment() {

    private val btnNoMusic: TextView by bindView(R.id.btn_no_music)
    private val includeNoData: ConstraintLayout by bindView(R.id.include_no_data)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private lateinit var mAdapter: SingleSongAdapter
    private var mView: View? = null
    private var mPlayBinder: MusicPlayerService.PlayerBinder? = null
    private lateinit var mConnection: ServiceConnection

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (mView == null) {
            mView = inflater?.inflate(R.layout.fragment_local_common, container, false)
        }
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindPlayService()
        initViews()
        setListeners()
        setObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity.unbindService(mConnection)
    }

    private fun bindPlayService() {
        val intent = Intent(activity, MusicPlayerService::class.java)
        mConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                mPlayBinder = null
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mPlayBinder = service as MusicPlayerService.PlayerBinder?
            }
        }
        activity.bindService(intent, mConnection, Service.BIND_AUTO_CREATE)
    }

    private fun initViews() {
        //初始化RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        mAdapter = SingleSongAdapter(activity)
        recyclerView.adapter = mAdapter
    }

    private fun setListeners() {
        mAdapter.setOnItemClickListener { view, position ->
            val first = StorageManager.getInstance.getFromDisk(MusicPlayerService.KEY_FIRST_PLAY)
            if (first == null) {
                firstPlay(position)
            } else {
                noFirstPlay(position)
            }
        }
        btnNoMusic.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun firstPlay(position: Int) {
        noFirstPlay(position)
        val intent = Intent(activity, PlayActivity::class.java)
        activity.startActivity(intent)
    }

    private fun noFirstPlay(position: Int) {
        val music = mAdapter.getDataList()[position]
        if (mPlayBinder?.getCurMusic()?.equals(music) == true) {
            if (mPlayBinder?.isPlaying() == true) {
                mPlayBinder?.pause()
            } else {
                mPlayBinder?.start()
            }
        } else {
            mPlayBinder?.play(music)
        }
    }

    private fun setObservers() {
        //观察单曲列表
        MusicLiveModel.getInstance().singleSongList.observe(this, Observer {
            if (it != null && it.size > 0) {
                recyclerView.visibility = View.VISIBLE
                includeNoData.visibility = View.GONE
                mAdapter.setDataList(it)
                mAdapter.notifyDataSetChanged()
            } else {
                recyclerView.visibility = View.GONE
                includeNoData.visibility = View.VISIBLE
            }
        })

    }


}