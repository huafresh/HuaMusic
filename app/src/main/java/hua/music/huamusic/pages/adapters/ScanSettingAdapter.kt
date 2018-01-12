package hua.music.huamusic.pages.adapters

import android.content.Context
import hua.music.huamusic.R
import hua.music.huamusic.entitys.SelectableDirEntity
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter

/**
 * 扫描设置目录适配器
 * Created by hua on 2017/12/23.
 */
class ScanSettingAdapter(context: Context, layoutId: Int) :
        SingleRvAdapter<SelectableDirEntity>(context, layoutId) {
    private val mContext = context

    constructor(context: Context) : this(context, R.layout.item_scan_setting)

    override fun convert(holder: MyViewHolder, data: SelectableDirEntity, position: Int) {
        holder.setSwitchOnOff(R.id.switch_compat, data.isSelected == true)
        holder.setText(R.id.tv_dir_name, data.dirName ?: mContext.getString(R.string.default_un_know))
        holder.setText(R.id.tv_dir_path, data.dirParentPath ?: mContext.getString(R.string.default_un_know))
    }
}