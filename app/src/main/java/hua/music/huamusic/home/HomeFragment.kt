package hua.music.huamusic.home

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
import hua.music.huamusic.entitys.MenuEntity
import hua.music.huamusic.entitys.SongListEntity
import hua.music.huamusic.utils.CommonUtil
import hua.music.huamusic.utils.JumpUtil
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

    private val mMenuIcons = intArrayOf(
            R.drawable.ic_music,
            R.drawable.ic_recent,
            R.drawable.ic_down)
    private val mMenuNames = intArrayOf(
            R.string.menu_local,
            R.string.menu_recent,
            R.string.menu_down)
    private val mMenuTypes = arrayOf(
            JumpUtil.ACTIVITY_TYPE_LOCAL,
            JumpUtil.ACTIVITY_TYPE_RECENT,
            JumpUtil.ACTIVITY_TYPE_DOWNLOAD)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_main, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        setListeners()
    }

    private fun initViews() {
        swipeRefreshLayout.setColorSchemeColors(CommonUtil.getColor(activity, R.color.color_theme, null))

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
        val count: Int = mMenuIcons.size
        val list: MutableList<MenuEntity> = mutableListOf()
        (0 until count).mapTo(list) {
            MenuEntity(mMenuIcons[it], getString(mMenuNames[it]), 0, false, mMenuTypes[it])
        }
        return list
    }

    private fun getDefaultSongList(): MutableList<SongListEntity> {
        return mutableListOf(SongListEntity(
                R.mipmap.ic_launcher,
                "我喜欢的音乐",
                0,
                JumpUtil.ACTIVITY_TYPE_SONG_LIST))
    }

    private fun setListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            refreshPage()
        }
        mMenuAdapter.setOnItemClickListener { view, position ->
            val data: MenuEntity = mMenuAdapter.getDataList()[position]
            JumpUtil.startActivity(activity, data.type)
        }
        mSongListAdapter.setOnItemClickListener { view, position ->
            val data: SongListEntity = mSongListAdapter.getDataList()[position]
            JumpUtil.startActivity(activity, data.type)
        }
        viewBacSongList.setOnClickListener {
            if (isShowSongList) {
                dismissSongList(true)
            } else {
                showSongList()
            }
        }
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
        TODO("刷新界面")
    }
}
