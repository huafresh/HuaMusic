package hua.music.huamusic.home

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import hua.music.huamusic.R
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.entitys.MenuEntity
import hua.music.huamusic.entitys.SongListEntity
import hua.music.huamusic.pages.down.DownActivity
import hua.music.huamusic.pages.local.LocalActivity
import hua.music.huamusic.pages.recent.RecentActivity
import hua.music.huamusic.utils.CommonUtil
import hua.music.huamusic.utils.dp2px
import hua.music.huamusic.wrapper.recyclerview.LinearItemDecoration
import kotterknife.bindView

/**
 * 主页Fragment
 * @author hua
 * @version 2017/12/21 15:09
 *
 */
class HomeFragment : Fragment() {
    private val ivArrow: ImageView by bindView(R.id.iv_arrow)
    private val viewBacSongList: View by bindView(R.id.view_bac_song_list)
    private val recyclerSongList: RecyclerView by bindView(R.id.recycler_song_list)
    private val swipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swipe_refresh_layout)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_menu)
    private var mView: View? = null
    private var isShowSongList = false

    private lateinit var mMenuAdapter: HomeMenuAdapter
    private lateinit var mSongListAdapter: HomeSongListAdapter

    companion object {
        /**
         * 菜单id
         */
        val MENU_ID_LOCAL = 1
        val MENU_ID_RECENT = 2
        val MENU_ID_DOWNLOAD = 3
        val MENU_ID_SONG_LIST = 4
        /**
         * 歌单列表id
         */
        val SONG_LIST_LOVE = 1
    }

    private val mMenuIconResIds = intArrayOf(
            R.drawable.ic_music,
            R.drawable.ic_recent,
            R.drawable.ic_down)
    private val mMenuNames = intArrayOf(
            R.string.menu_local,
            R.string.menu_recent,
            R.string.menu_down)
    private val mMenuIds = intArrayOf(
            MENU_ID_LOCAL,
            MENU_ID_RECENT,
            MENU_ID_DOWNLOAD)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (mView == null) {
            mView = inflater?.inflate(R.layout.fragment_main, container, false)
        }
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        setListeners()
        setObservers()
    }


    private fun initViews() {
        swipeRefreshLayout.setColorSchemeColors(CommonUtil.getColor(activity,
                R.color.color_theme, null))

        //菜单recyclerView初始化
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val menuDivider = LinearItemDecoration(activity)
        menuDivider.setLeftPadding(recyclerView.dp2px(50f))
        recyclerView.addItemDecoration(menuDivider)
        mMenuAdapter = HomeMenuAdapter(activity)
        mMenuAdapter.setDataList(getMenuDataList())
        recyclerView.adapter = mMenuAdapter

        //歌单RecyclerView初始化
        recyclerSongList.layoutManager = LinearLayoutManager(activity)
        val songDivider = HasLastLinearDivider(activity)
        songDivider.setLeftPadding(recyclerView.dp2px(50f))
        recyclerSongList.addItemDecoration(songDivider)
        mSongListAdapter = HomeSongListAdapter(activity)
        mSongListAdapter.setDataList(getDefaultSongList())
        recyclerSongList.adapter = mSongListAdapter

        //隐藏歌单列表
        dismissSongList(false)
    }

    private fun getMenuDataList(): MutableList<MenuEntity> {
        val count: Int = mMenuIconResIds.size
        val list: MutableList<MenuEntity> = mutableListOf()
        (0 until count).mapTo(list) {
            MenuEntity(mMenuIds[it], mMenuIconResIds[it],
                    getString(mMenuNames[it]), 0, false)
        }
        return list
    }

    private fun getDefaultSongList(): MutableList<SongListEntity> {
        return mutableListOf(SongListEntity(
                SONG_LIST_LOVE,
                R.mipmap.ic_launcher,
                "我喜欢的音乐",
                0
        ))
    }

    private fun setListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            refreshPage()
        }
        mMenuAdapter.setOnItemClickListener { view, position ->
            if (!swipeRefreshLayout.isRefreshing) {
                val data: MenuEntity = mMenuAdapter.getDataList()[position]
                val intent = when (data.id) {
                    MENU_ID_LOCAL -> {
                        Intent(activity, LocalActivity::class.java)
                    }
                    MENU_ID_RECENT -> {
                        Intent(activity, RecentActivity::class.java)
                    }
                    MENU_ID_DOWNLOAD -> {
                        Intent(activity, DownActivity::class.java)
                    }
                    else -> {
                        null
                    }
                }
                if (intent != null) {
                    startActivity(intent)
                }
            }
        }
        mSongListAdapter.setOnItemClickListener { view, position ->
            if (!swipeRefreshLayout.isRefreshing) {
                val data: SongListEntity = mSongListAdapter.getDataList()[position]
                val intent = when (data.id) {
                    SONG_LIST_LOVE -> {
                        //todo 跳转音乐列表页面
                        null
                    }
                    else -> {
                        null
                    }
                }
                if (intent != null) {
                    startActivity(intent)
                }
            }
        }
        viewBacSongList.setOnClickListener {
            if (!swipeRefreshLayout.isRefreshing) {
                if (isShowSongList) {
                    dismissSongList(true)
                } else {
                    showSongList()
                }
            }
        }
    }

    private fun setObservers() {
        swipeRefreshLayout.isRefreshing = true

        MusicLiveModel.getInstance().singleSongList.observe(this, Observer {
            val dataList = mMenuAdapter.getDataList()
            dataList.forEach {
                if (it.id == MENU_ID_LOCAL) {
                    it.sum = MusicLiveModel.getInstance().singleSongList.value?.size ?: 0
                    mMenuAdapter.notifyItemChanged(dataList.indexOf(it))
                }
            }
            swipeRefreshLayout.isRefreshing = false
        })
        MusicLiveModel.getInstance().recentList.observe(this, Observer {
            val dataList = mMenuAdapter.getDataList()
            dataList.forEach {
                if (it.id == MENU_ID_RECENT) {
                    it.sum = MusicLiveModel.getInstance().recentList.value?.size ?: 0
                    mMenuAdapter.notifyItemChanged(dataList.indexOf(it))
                }
            }
        })
        MusicLiveModel.getInstance().downedList.observe(this, Observer {
            val dataList = mMenuAdapter.getDataList()
            dataList.forEach {
                if (it.id == MENU_ID_DOWNLOAD) {
                    it.sum = MusicLiveModel.getInstance().downedList.value?.size ?: 0
                    mMenuAdapter.notifyItemChanged(dataList.indexOf(it))
                }
            }
        })
    }

    private fun showSongList() {
        ivArrow.animate()
                .setDuration(300)
                .rotation(90f)
        recyclerSongList.visibility = View.VISIBLE
        isShowSongList = true
    }

    private fun dismissSongList(isAnim: Boolean) {
        if (isAnim) {
            ivArrow.animate()
                    .setDuration(300)
                    .rotation(0f)
        }
        recyclerSongList.visibility = View.GONE
        isShowSongList = false
    }

    private fun refreshPage() {
        MusicLiveModel.getInstance().singleSongList.scanLocalMusic(activity)
    }
}
