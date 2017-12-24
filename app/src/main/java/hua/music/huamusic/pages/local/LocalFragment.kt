package hua.music.huamusic.pages.local

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hua.music.huamusic.R
import hua.music.huamusic.pages.adapters.CommonPagerAdapter
import kotterknife.bindView

/**
 * 本地音乐内容主界面
 * Created by hua on 2017/12/23.
 */
class LocalFragment : Fragment() {
    private val tabLayout: TabLayout by bindView(R.id.tab_layout)
    private val viewPager: ViewPager by bindView(R.id.view_pager)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_local, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        //初始化ViewPager
        val fragments = arrayOf(
                SingleSongFragment(),
                AuthorFragment(),
                AlbumFragment(),
                FileDirFragment()
        )
        val titles = arrayOf(
                getString(R.string.local_tab_single_song),
                getString(R.string.local_tab_author),
                getString(R.string.local_tab_album),
                getString(R.string.local_tab_file_dir)
        )
        val pagerAdapter = CommonPagerAdapter(childFragmentManager, fragments, titles)
        viewPager.adapter = pagerAdapter

        //初始化tabLayout
        tabLayout.setupWithViewPager(viewPager)
    }

}