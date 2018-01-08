package hua.music.huamusic.pages.adapters

import android.content.Context
import hua.music.huamusic.R
import hua.music.huamusic.entitys.AuthorEntity
import hua.music.huamusic.entitys.Music
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter

/**
 * 本地音乐歌手列表适配器
 * Created by hua on 2017/12/23.
 */
class AuthorListAdapter(context: Context, layoutId: Int) :
        SingleRvAdapter<AuthorEntity>(context, layoutId) {

    private val mContext = context

    constructor(context: Context) : this(context, R.layout.item_author)

    override fun convert(holder: MyViewHolder, data: AuthorEntity, position: Int) {
        holder.setText(R.id.tv_author, data.name ?: mContext.getString(R.string.default_author))
        holder.setText(R.id.tv_sum, "${data.sum ?: 0}首")
    }
}