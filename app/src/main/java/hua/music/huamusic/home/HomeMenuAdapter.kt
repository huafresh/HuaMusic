package hua.music.huamusic.home

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.View
import hua.music.huamusic.R
import hua.music.huamusic.entitys.MenuEntity
import hua.music.huamusic.utils.CommonUtil
import hua.music.huamusic.utils.CommonUtil.tintDrawable
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter


/**
 * 首页菜单适配器
 *
 * @author hua
 * @version 2017/12/21 15:45
 */
class HomeMenuAdapter(context: Context, @LayoutRes id: Int) :
        SingleRvAdapter<MenuEntity>(context, id) {
    private val mContext: Context = context

    constructor(context: Context) : this(context, R.layout.item_home_menu)

    override fun convert(holder: MyViewHolder, data: MenuEntity, position: Int) {
        holder.setImageDrawable(R.id.iv_icon, tintDrawable(mContext, data.iconResId,
                CommonUtil.getColor(mContext, R.color.color_theme, null)))
                .setText(R.id.tv_name, data.name)
                .setText(R.id.tv_sum, "(${data.sum})")
                .setVisibility(R.id.iv_playing, if (data.isPlay) View.VISIBLE else View.GONE)
    }


}