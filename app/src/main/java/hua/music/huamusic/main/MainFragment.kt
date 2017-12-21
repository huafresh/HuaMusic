package hua.music.huamusic.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hua.music.huamusic.R
import hua.music.huamusic.entitys.HomeRvEntity
import hua.music.huamusic.utils.CommonUtil
import kotterknife.bindView

/**
 * 主页Fragment
 * @author hua
 * @version 2017/12/21 15:09
 *
 */
class MainFragment : Fragment() {
    private val swipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.swipe_refresh_layout)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
    private var mView: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_main, container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()

    }

    private fun initViews() {
        swipeRefreshLayout.setColorSchemeColors(CommonUtil.getColor(activity, R.color.color_theme, null))

        HomeRvEntity().iconId

    }
}
