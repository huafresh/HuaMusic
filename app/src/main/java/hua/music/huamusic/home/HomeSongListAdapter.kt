package hua.music.huamusic.home

import android.content.Context
import android.support.annotation.LayoutRes
import hua.music.huamusic.R
import hua.music.huamusic.entitys.SongListEntity
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter

/**
 * 首页歌单适配器
 * @author hua
 * @version 2017/12/22 15:14
 *
 */
class HomeSongListAdapter(mContext: Context, @LayoutRes id: Int) :
        SingleRvAdapter<SongListEntity>(mContext, id) {

    constructor(context: Context) : this(context, R.layout.item_home_song_list)

    override fun convert(holder: MyViewHolder, data: SongListEntity, position: Int) {
        holder.setImageResId(R.id.iv_image, data.iconResId)
                .setText(R.id.tv_name, data.name)
                .setText(R.id.tv_sum, "${data.sum}")
    }

}