package hua.music.huamusic.pages.adapters

import android.content.Context
import hua.music.huamusic.R
import hua.music.huamusic.entitys.AlbumEntity
import hua.music.huamusic.entitys.AuthorEntity
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter

/**
 * 本地音乐专辑列表适配器
 * Created by hua on 2017/12/23.
 */
class AlbumListAdapter(context: Context, layoutId: Int) :
        SingleRvAdapter<AlbumEntity>(context, layoutId) {
    private val mContext = context

    constructor(context: Context) : this(context, R.layout.item_album)

    override fun convert(holder: MyViewHolder, data: AlbumEntity, position: Int) {
        holder.setText(R.id.tv_name, data.name ?: mContext.getString(R.string.default_album))
        holder.setText(R.id.tv_sum, "${data.sum ?: 0}首")
        holder.setText(R.id.tv_author, data.author ?: mContext.getString(R.string.default_author))
    }
}