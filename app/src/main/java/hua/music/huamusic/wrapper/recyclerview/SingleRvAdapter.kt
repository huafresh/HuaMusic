package hua.music.huamusic.wrapper.recyclerview

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.ViewGroup
import java.util.*

/**
 * 当RecyclerView的列表item单一时使用
 *
 * @author hua
 * @version 2017/12/21 17:02
 *
 */
abstract class SingleRvAdapter<T>(mContext: Context, @LayoutRes layoutId: Int) :
        MultiItemRvAdapter<T>(mContext) {

    private val mLayoutId: Int = layoutId

    override fun multiConvert(holder: MyViewHolder, data: T?, position: Int) {
        convert(holder, data, position);
    }

    override fun getLayoutId(parent: ViewGroup, viewType: Int): Int {
        return mLayoutId
    }

    /**
     * 具体的bean与item绑定时调用此方法
     *
     * @param holder   itemView holder
     * @param data     bean
     * @param position item位置
     */
    protected abstract fun convert(holder: MyViewHolder, data: T?, position: Int)

}