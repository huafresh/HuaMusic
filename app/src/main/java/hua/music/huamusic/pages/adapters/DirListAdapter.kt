package hua.music.huamusic.pages.adapters

import android.content.Context
import hua.music.huamusic.R
import hua.music.huamusic.entitys.DirEntity
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter

/**
 * 本地音乐文件夹列表适配器
 * Created by hua on 2017/12/23.
 */
class DirListAdapter(context: Context, layoutId: Int) :
        SingleRvAdapter<DirEntity>(context, layoutId) {
    private val mContext = context

    constructor(context: Context) : this(context, R.layout.item_dir)

    override fun convert(holder: MyViewHolder, data: DirEntity, position: Int) {
        holder.setText(R.id.tv_dir_name, data.name ?: mContext.getString(R.string.default_dir_name))
        holder.setText(R.id.tv_sum, "${data.sum ?: 0}首")
        holder.setText(R.id.tv_dir_path, data.path ?: mContext.getString(R.string.default_dir_path))
    }
}