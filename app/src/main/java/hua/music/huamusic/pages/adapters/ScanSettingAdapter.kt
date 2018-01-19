package hua.music.huamusic.pages.adapters

import android.content.Context
import hua.music.huamusic.R
import hua.music.huamusic.entitys.SelectableDirEntity
import hua.music.huamusic.utils.CommonUtil
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter
import java.io.File

/**
 * 扫描过滤设置页面适配器
 * Created by hua on 2017/12/23.
 */
class ScanSettingAdapter(context: Context, layoutId: Int) :
        SingleRvAdapter<SelectableDirEntity>(context, layoutId) {
    private val mContext = context

    constructor(context: Context) : this(context, R.layout.item_scan_setting)

    override fun convert(holder: MyViewHolder, data: SelectableDirEntity, position: Int) {
        holder.setChecked(R.id.check_box, data.isSelected)
        holder.setText(R.id.tv_dir_name, CommonUtil.getSuffix(data.dirPath))
        holder.setText(R.id.tv_dir_path, "${File(data.dirPath).parent}/")
    }
}