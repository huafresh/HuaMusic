package hua.music.huamusic.home

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.View
import hua.music.huamusic.R
import hua.music.huamusic.entitys.MenuEntity
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter


/**
 * 首页菜单适配器
 * @author hua
 * @version 2017/12/21 15:45
 */
class HomeMenuAdapter(mContext: Context, @LayoutRes id: Int) :
        SingleRvAdapter<MenuEntity>(mContext, id) {

    constructor(context: Context) : this(context, R.layout.item_home_menu)

    override fun convert(holder: MyViewHolder, data: MenuEntity?, position: Int) {
        holder.setImageResId(R.id.iv_icon, data?.iconId ?: 0)
                .setText(R.id.tv_name, data?.name ?: "")
                .setText(R.id.tv_sum, "${data?.sum}")
                .setVisibility(R.id.iv_playing, if (data?.isPlay == true) View.VISIBLE else View.GONE)
    }
}