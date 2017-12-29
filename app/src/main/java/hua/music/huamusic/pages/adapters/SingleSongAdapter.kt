package hua.music.huamusic.pages.adapters

import android.content.Context
import hua.music.huamusic.R
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter

/**
 * 本地音乐单曲列表适配器
 * Created by hua on 2017/12/23.
 */
class SingleSongAdapter(context: Context, layoutId: Int) :
        SingleRvAdapter<Music>(context, layoutId) {

    constructor(context: Context) : this(context, R.layout.item_music)

    override fun convert(holder: MyViewHolder, data: Music, position: Int) {
        holder.setText(R.id.tv_title, data.title ?: "未知歌名")
        holder.setText(R.id.tv_author, data.author ?: "未知歌手")
    }
}