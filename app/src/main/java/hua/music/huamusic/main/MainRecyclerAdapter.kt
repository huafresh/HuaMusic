package hua.music.huamusic.main

import android.content.Context
import android.support.annotation.LayoutRes
import hua.music.huamusic.R
import hua.music.huamusic.entitys.HomeRvEntity
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter

/**
 * 主页RecyclerView适配器
 *
 * @author hua
 * @version 2017/12/21 15:45
 *
 */
class MainRecyclerAdapter(mContext: Context, @LayoutRes id: Int) :
        SingleRvAdapter<HomeRvEntity>(mContext, id) {

    constructor(context: Context) : this(context, R.layout.fragment_main)

    override fun convert(holder: MyViewHolder, data: HomeRvEntity?, position: Int) {

    }

}