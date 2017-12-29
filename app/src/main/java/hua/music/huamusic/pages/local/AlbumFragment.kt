package hua.music.huamusic.pages.local

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hua.music.huamusic.R
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.entitys.AlbumEntity
import hua.music.huamusic.entitys.AuthorEntity
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.pages.adapters.AlbumListAdapter
import hua.music.huamusic.pages.adapters.AuthorListAdapter
import hua.music.huamusic.pages.adapters.SingleSongAdapter
import kotterknife.bindView

/**
 * 本地音乐专辑列表
 * Created by hua on 2017/12/23.
 */
class AlbumFragment : Fragment() {
    private val btnNoMusic: TextView by bindView(R.id.btn_no_music)
    private val includeNoData: ConstraintLayout by bindView(R.id.include_no_data)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private lateinit var mAdapter: AlbumListAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_local_common, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        setListeners()
        setObservers()
    }

    private fun initViews() {
        //初始化RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        mAdapter = AlbumListAdapter(activity)
        recyclerView.adapter = mAdapter
    }

    private fun setListeners() {
        mAdapter.setOnItemClickListener { view, position ->

        }
        btnNoMusic.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setObservers() {
        //观察专辑列表
        MusicLiveModel.getInstance().localAlbumList.observe(this, Observer {
            val list = mutableListOf<AlbumEntity>()
            it?.forEach {
                list.add(it.keys.firstOrNull()!!)
            }

            if (list.size > 0) {
                recyclerView.visibility = View.VISIBLE
                includeNoData.visibility = View.GONE
                mAdapter.setDataList(list)
                mAdapter.notifyDataSetChanged()
            } else {
                recyclerView.visibility = View.GONE
                includeNoData.visibility = View.VISIBLE
            }
        })
    }

}