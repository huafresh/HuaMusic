package hua.music.huamusic.pages.adapters

import android.content.Context
import android.widget.CheckBox
import hua.music.huamusic.R
import hua.music.huamusic.data.MusicLiveModel
import hua.music.huamusic.entitys.SelectableDirEntity
import hua.music.huamusic.utils.CommonUtil
import hua.music.huamusic.wrapper.recyclerview.MyViewHolder
import hua.music.huamusic.wrapper.recyclerview.SingleRvAdapter
import java.io.File

/**
 * 自定义扫描适配器
 * Created by hua on 2017/12/23.
 */
class ScanCustomAdapter(context: Context, layoutId: Int) :
        SingleRvAdapter<SelectableDirEntity>(context, layoutId) {
    private val mContext = context

    constructor(context: Context) : this(context, R.layout.item_scan_custom)

    override fun convert(holder: MyViewHolder, data: SelectableDirEntity, position: Int) {
        //设置目录名称，取第一个item的父目录
        holder.setText(R.id.tv_dir_name, CommonUtil.getSuffix(data.dirPath))
        //计算目录下的歌曲数目
        //todo 异步计算，后续处理
        holder.setText(R.id.tv_sum, "0首")
        //是否选中
        holder.setChecked(R.id.check_box, data.isSelected)
        holder.getView<CheckBox>(R.id.check_box)?.setOnCheckedChangeListener { view, isChecked ->
            data.isSelected = isChecked
        }
    }

}