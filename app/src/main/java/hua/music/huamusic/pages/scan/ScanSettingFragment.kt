package hua.music.huamusic.pages.scan

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.TextView
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseFragment
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.entitys.SelectableDirEntity
import hua.music.huamusic.pages.adapters.ScanSettingAdapter
import hua.music.huamusic.storage.StorageManager
import hua.music.huamusic.wrapper.recyclerview.LinearItemDecoration
import kotterknife.bindView

/**
 * @author hua
 * @version 2018/1/11 11:29
 */
class ScanSettingFragment : BaseFragment() {
    private val tvFilterTitle: TextView by bindView(R.id.tv_filter_title)
    private val tvFilterDec: TextView by bindView(R.id.tv_filter_dec)
    private val switchCompat: SwitchCompat by bindView(R.id.switch_compat)
    private val viewDivider: View by bindView(R.id.view_divider)
    private val tvDirTitle: TextView by bindView(R.id.tv_dir_title)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)

    private lateinit var mAdapter: ScanSettingAdapter

    companion object {
        /**
         * 存储是否过滤小于60秒的音频文件
         */
        val SCAN_SETTING_FILTER_LESS_60 = "scan_setting_filter_less_60"

        /**
         * 存储选中的要扫描的文件夹路径，多个用";"分割
         */
        val SCAN_SETTING_SELECTED_DIR_PATH = "scan_setting_selected_dir_path"

        /**
         * 存储未选中的要扫描的文件夹路径，多个用";"分割
         */
        val SCAN_SETTING_UNSELECTED_DIR_PATH = "scan_setting_unselected_dir_path"
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_scan_setting
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
        setListeners()
        addObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        StorageManager.getInstance.saveToDisk(SCAN_SETTING_FILTER_LESS_60,
                if (switchCompat.isSelected) StorageManager.TRUE else StorageManager.FALSE)
        val selectedBuilder = StringBuilder()
        val unSelectedBuilder = StringBuilder()
        val dataList = mAdapter.getDataList()
        for (i in 0 until dataList.size) {
            val entity = dataList[i]
            val dirPath = entity.dirParentPath + entity.dirName
            if (entity.isSelected == true) {
                selectedBuilder.append(dirPath)
                if (i != dataList.size - 1) {
                    selectedBuilder.append(";")
                }
            } else {
                unSelectedBuilder.append(dirPath)
                if (i != dataList.size - 1) {
                    unSelectedBuilder.append(";")
                }
            }
        }
        MusicLiveModel.getInstance().scanFilterList.value?.addAll(mAdapter.getDataList())
    }

    private fun initViews() {
        val isFilterLess60 = StorageManager.getInstance.getFromDisk(SCAN_SETTING_FILTER_LESS_60)
        switchCompat.isSelected = StorageManager.TRUE == isFilterLess60

        //初始化目录列表RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(LinearItemDecoration(activity))
        mAdapter = ScanSettingAdapter(activity)
    }

    private fun setListeners() {
        mAdapter.setOnItemClickListener { view, position ->
            val entity = mAdapter.getDataList()[position]
            entity.isSelected = true
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun addObservers() {
        MusicLiveModel.getInstance().getFilterDirList(activity)
        MusicLiveModel.getInstance().scanFilterList.observe(this, Observer {
            if (it != null) {
                mAdapter.setDataList(it)
                changeSelectedState(it)
                mAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun changeSelectedState(list: MutableList<SelectableDirEntity>) {
        val selectedPath = StorageManager.getInstance.getFromDisk(SCAN_SETTING_SELECTED_DIR_PATH)
        val unSelectedPath = StorageManager.getInstance.getFromDisk(SCAN_SETTING_UNSELECTED_DIR_PATH)
        val selectedSplits = selectedPath?.split(";") ?: return
        val unSelectedSplits = unSelectedPath?.split(";") ?: return

        list.forEach {
            selectedSplits.filter { split ->
                it.dirParentPath + it.dirName == split
            }.forEach { split ->
                it.isSelected = true
            }
            unSelectedSplits.filter { split ->
                it.dirParentPath + it.dirName == split
            }.forEach { split ->
                it.isSelected = false
            }
        }
    }

}