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
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.pages.adapters.SingleSongAdapter
import kotterknife.bindView

/**
 * 本地音乐单曲列表
 * Created by hua on 2017/12/23.
 */
class SingleSongFragment : Fragment() {

    private val btnNoMusic: TextView by bindView(R.id.btn_no_music)
    private val includeNoData: ConstraintLayout by bindView(R.id.include_no_data)

    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private lateinit var mAdapter: SingleSongAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_local_common, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        setListeners()
        setObserver()
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
            mAdapter.getDataList()[position]
        }
        btnNoMusic.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setObserver() {
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