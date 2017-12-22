package hua.music.huamusic.wrapper.recyclerview

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.RuntimeException

/**
 * recyclerView通用多类型适配器
 *
 * @author hua
 * @date 2017/8/14
 */

abstract class MultiItemRvAdapter<T>(private val mContext: Context)
    : RecyclerView.Adapter<MyViewHolder>() {

    companion object {
        private val TAG = "MultiItemRvAdapter"
    }

    private val mDataList: MutableList<T> = mutableListOf()

    /**
     * 设置适配器数据源
     * @param list 适配器数据源
     */
    fun setDataList(list: List<T>?) {
        if (list != null) {
            mDataList.clear()
            mDataList.addAll(list)
        }
    }

    /**
     * 获取适配器数据源
     *
     * @param <T>
     * @return 适配器数据源
     */
    fun getDataList(): List<T> {
        return mDataList
    }

    /**
     * 针对item的监听
     */
    private var mOnItemClickListener: ((view: View, position: Int) -> Unit)? = null
    private var mOnItemLongClickListener: ((view: View, position: Int) -> Unit)? = null
    private var mOnItemTouchListener: ((view: View, event: MotionEvent, position: Int) -> Boolean)? = null

    public fun setOnItemClickListener(listener: (view: View, position: Int) -> Unit) {
        mOnItemClickListener = listener
    }

    public fun setOnItemLongClickListener(listener: (view: View, position: Int) -> Unit) {
        mOnItemLongClickListener = listener
    }

    public fun setOnTouchListener(listener: (view: View, event: MotionEvent, position: Int) -> Boolean) {
        mOnItemTouchListener = listener
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutId = getLayoutId(parent, viewType)
        if (layoutId <= 0) {
            throw RuntimeException("invalid layout id")
        }
        val itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false)
        val holder = MyViewHolder(itemView)
        setListeners(itemView, holder)
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemData: T? = mDataList[position]
        multiConvert(holder, itemData, position)
    }

    private fun setListeners(itemView: View, myViewHolder: MyViewHolder) {
        itemView.setOnClickListener { v ->
            mOnItemClickListener?.invoke(v, myViewHolder.adapterPosition)
        }

        itemView.setOnLongClickListener { v ->
            mOnItemLongClickListener?.invoke(v, myViewHolder.adapterPosition)
            true
        }

        itemView.setOnTouchListener { v, event ->
            mOnItemTouchListener?.invoke(v, event, myViewHolder.adapterPosition) ?: false
        }
    }


    /**
     * 提供指定类型的item的布局id
     *
     * @param parent   recyclerView
     * @param viewType 该item的类型
     * @return 布局id
     */
    @LayoutRes
    abstract protected fun getLayoutId(parent: ViewGroup, viewType: Int): Int


    /**
     * 具体的bean与item绑定时调用此方法
     *
     * @param holder   itemView holder
     * @param data     item对应的bean
     * @param position item位置
     */
    abstract fun multiConvert(holder: MyViewHolder, data: T?, position: Int)


}
