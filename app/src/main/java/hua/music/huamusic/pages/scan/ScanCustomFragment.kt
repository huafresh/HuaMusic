package hua.music.huamusic.pages.scan

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity
import hua.music.huamusic.base.BaseFragment
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.entitys.SelectableDirEntity
import hua.music.huamusic.pages.adapters.ScanCustomAdapter
import hua.music.huamusic.utils.CommonUtil
import hua.music.huamusic.wrapper.recyclerview.LinearItemDecoration
import kotterknife.bindView
import java.io.File

/**
 * 自定义扫描
 *
 * @author hua
 * @version 2018/1/11 19:25
 *
 */
@SuppressLint("SetTextI18n")
class ScanCustomFragment : BaseFragment() {
    private val viewTop: View by bindView(R.id.view_top)
    private val ivDirBack: ImageView by bindView(R.id.iv_dir_back)
    private val tvCurrentParentPath: TextView by bindView(R.id.tv_current_parent_path)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private val viewBottom: View by bindView(R.id.view_bottom)
    private val ivScanImage: ImageView by bindView(R.id.iv_scan_image)
    private val tvScanStart: TextView by bindView(R.id.tv_scan_start)

    private lateinit var mAdapter: ScanCustomAdapter
    private var isAllSelect: Boolean? = null


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
        tvCurrentParentPath.text = rootPath

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(LinearItemDecoration(activity))
        mAdapter = ScanCustomAdapter(activity)
        recyclerView.adapter = mAdapter

        refreshPage()
    }

    fun setAllSelect(b: Boolean?) {
        isAllSelect = b
    }

    fun refreshPage() {
        val dataList = resolveDataList(tvCurrentParentPath.text.toString())
        if (isAllSelect == true) {
            dataList.forEach {
                it.isSelected = true
            }
        } else {
            dataList.forEach {
                it.isSelected = false
            }
        }
        mAdapter.setDataList(dataList)
        mAdapter.notifyDataSetChanged()
    }

    private fun setListeners() {
        viewTop.setOnClickListener {
            backToPreviousDir()
        }
        mAdapter.setOnItemClickListener { view, position ->
            val entity = mAdapter.getDataList()[position]
            if (hasDirectory(entity.dirPath)) {
                tvCurrentParentPath.text = entity.dirPath + "/"
                refreshPage()
            } else {
                entity.isSelected = !entity.isSelected
                mAdapter.notifyDataSetChanged()
            }
        }
        viewBottom.setOnClickListener {
            jumpScanPage()
        }
        if (activity is BaseActivity) {
            (activity as BaseActivity).addOnBackPressListener(object : BaseActivity.OnBackPressListener {
                override fun onBackPress(): Boolean {
                    return backToPreviousDir()
                }
            })
        }
    }

    private fun jumpScanPage() {
        val list = getSelectedList()
        if (list.size == 0) {
            Toast.makeText(activity, "请选择需要扫描的文件夹", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(activity, ScanActivity::class.java)
        val arguments = Bundle()
        arguments.putStringArrayList(ScanActivity.KEY_SCAN_PATH_LIST, list)
        intent.putExtras(arguments)
        startActivity(intent)
    }

    private fun getSelectedList(): ArrayList<String> {
        val list = ArrayList<String>()
        mAdapter.getDataList().forEach {
            if (it.isSelected) {
                list.add(it.dirPath)
            }
        }
        return list
    }

    private fun backToPreviousDir(): Boolean {
        val currentPath = tvCurrentParentPath.text.toString()
        if (rootPath != currentPath) {
            val parentPath = File(currentPath).parent + "/"
            tvCurrentParentPath.text = parentPath
            refreshPage()
            return true
        }
        return false
    }

    private fun addObservers() {
        MusicLiveModel.getInstance().scanFilterList.observe(this, Observer {
            val list = mutableListOf<SelectableDirEntity>()
            Environment.getExternalStorageDirectory().listFiles().forEach { file ->
                val entity = SelectableDirEntity(isFilter(it, file.absolutePath), file.absolutePath)
                list.add(entity)
            }
        })
    }

    private fun isFilter(list: List<SelectableDirEntity>?, path: String): Boolean {
        list?.forEach {
            if (it.dirPath == path) {
                return it.isSelected
            }
        }
        return false
    }


    companion object {
        private val rootPath = Environment.getExternalStorageDirectory().absolutePath + "/"

        /**
         * 根据父目录解析得到数据列表
         */
        private fun resolveDataList(parentPath: String): List<SelectableDirEntity> {
            val parentFile = File(parentPath)
            val resultList = mutableListOf<SelectableDirEntity>()
            parentFile.listFiles()?.forEach {
                if (it.isDirectory && !it.name.startsWith(".")) {
                    val entity = SelectableDirEntity(
                            it.isDirectory && isFilter(it.absolutePath),
                            it.absolutePath)
                    resultList.add(entity)
                }
            }
            return resultList
        }

        /**
         * 判断给定目录是否有子目录
         */
        private fun hasDirectory(path: String): Boolean {
            File(path).listFiles().forEach {
                if (it.isDirectory) {
                    return true
                }
            }
            return false
        }

        private fun isFilter(path: String): Boolean {
            MusicLiveModel.getInstance().scanFilterList.value?.forEach {
                if (path.startsWith(it.dirPath)) {
                    return true
                }
            }
            return false
        }
    }

}
