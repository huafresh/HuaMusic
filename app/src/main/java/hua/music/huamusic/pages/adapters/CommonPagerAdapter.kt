package hua.music.huamusic.pages.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * 通用ViewPager适配器，每一页都是一个Fragment
 * Created by hua on 2017/12/23.
 */
class CommonPagerAdapter(fm: FragmentManager, dataArray: Array<Fragment>, titleArray: Array<String>) :
        FragmentPagerAdapter(fm) {
    private val mDataArray = dataArray
    private val mTitleArray = titleArray

    override fun getItem(position: Int): Fragment {
        return mDataArray[position]
    }

    override fun getCount(): Int {
        return mDataArray.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitleArray[position]
    }
}