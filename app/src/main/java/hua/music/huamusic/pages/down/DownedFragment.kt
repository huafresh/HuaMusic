package hua.music.huamusic.pages.down

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hua.music.huamusic.R
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.pages.adapters.LocalSingleSongAdapter
import kotterknife.bindView

/**
 * 已下载列表
 * Created by hua on 2017/12/23.
 */
class DownedFragment : Fragment() {

    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private lateinit var mAdapter: LocalSingleSongAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_single_song, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        setListeners()
    }

    private fun initViews() {
        //初始化RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        mAdapter = LocalSingleSongAdapter(activity)
        mAdapter.setDataList(generateTempData())
        recyclerView.adapter = mAdapter
    }

    private fun generateTempData(): List<Music> {
        val list = mutableListOf<Music>()
        for (i in 0..30) {
            list.add(Music("","","","","",""))
        }
        return list
    }

    private fun setListeners() {
        mAdapter.setOnItemClickListener { view, position ->

        }


    }


}