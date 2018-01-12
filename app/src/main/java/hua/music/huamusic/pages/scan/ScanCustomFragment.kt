package hua.music.huamusic.pages.scan

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseFragment
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.entitys.SelectableDirEntity
import hua.music.huamusic.pages.adapters.ScanCustomAdapter
import hua.music.huamusic.wrapper.recyclerview.LinearItemDecoration
import io.reactivex.functions.Function
import kotterknife.bindView

/**
 * 自定义扫描
 *
 * @author hua
 * @version 2018/1/11 19:25
 *
 */
class ScanCustomFragment : BaseFragment() {
    private val viewTop: View by bindView(R.id.view_top)
    private val ivDirBack: ImageView by bindView(R.id.iv_dir_back)
    private val tvCurrentPath: TextView by bindView(R.id.tv_current_path)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private val viewBottom: View by bindView(R.id.view_bottom)
    private val ivScanImage: ImageView by bindView(R.id.iv_scan_image)
    private val tvScanStart: TextView by bindView(R.id.tv_scan_start)

    private lateinit var mAdapter: ScanCustomAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_scan_custom
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        setListeners()
        addObservers()
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(LinearItemDecoration(activity))
        mAdapter = ScanCustomAdapter(activity)
        recyclerView.adapter = mAdapter
    }

    private fun setListeners() {
        viewTop.setOnClickListener {

        }
        mAdapter.setOnItemClickListener { view, position ->

        }
        viewBottom.setOnClickListener {

        }
    }

    private fun addObservers() {
        MusicLiveModel.getInstance().scanFilterList.observe(this, Observer {
            val list = mutableListOf<SelectableDirEntity>()
            Environment.getExternalStorageDirectory().listFiles().forEach { file->
                val entity = SelectableDirEntity(isFilter(it,file.absolutePath),file.absolutePath)
                list.add(entity)
            }
        })
    }

    private fun isFilter(list: List<SelectableDirEntity>?, path: String): Boolean {
        list?.forEach {
            if(it.dirPath == path){
                return it.isSelected
            }
        }
        return false
    }

}