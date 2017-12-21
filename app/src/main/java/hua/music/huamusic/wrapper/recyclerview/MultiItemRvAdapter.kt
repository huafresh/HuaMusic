package hua.music.huamusic.wrapper.recyclerview

import android.content.ContentValues.TAG
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

/**
 * recyclerView通用多类型适配器
 *
 * @author hua
 * @date 2017/8/14
 */

abstract class MultiItemRvAdapter<T>(val mContext: Context)
    : RecyclerView.Adapter<MyViewHolder>() {

    companion object {
        private val TAG = "MultiItemRvAdapter"
    }

    var mDataList: MutableList<T>? = null

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var mOnTouchListener: OnTouchListener? = null

    /**
     * 设置适配器数据源
     *
     * @param list 适配器数据源
     */
    fun setDataList(list: List<T>?) {
        if (list != null) {
            if (mDataList == null) {
                mDataList = ArrayList()
            }
            mDataList?.clear()
            mDataList?.addAll(list)
        }
    }

    /**
     * 获取适配器数据源
     *
     * @param <T>
     * @return 适配器数据源
     */
    fun getDataList(): List<T>? {
        return mDataList
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutId = getLayoutId(parent, viewType)
        if (layoutId == -1) {
            throw IllegalArgumentException("no layoutId was setted")
        }
        val itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false)
        val holder = MyViewHolder(itemView)
        setListeners(itemView, holder)
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemData: T? = mDataList?.get(position)
        multiConvert(holder, itemData, position)
    }

    private fun setListeners(itemView: View, myViewHolder: MyViewHolder) {
        itemView.setOnClickListener { v ->
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.onClick(v, myViewHolder.layoutPosition)
            }
        }

        itemView.setOnLongClickListener { v ->
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener!!.onLongClick(v, myViewHolder.layoutPosition)
            }
            true
        }

        itemView.setOnTouchListener { v, event ->
            if (mOnTouchListener != null) {
                mOnTouchListener!!.onTouch(v, event, myViewHolder.layoutPosition)
            } else false
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    interface OnItemClickListener {
        /**
         * item被点击时调用
         *
         * @param view     被点击的item
         * @param position item的位置
         */
        fun onClick(view: View, position: Int)
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mOnItemLongClickListener = listener
    }

    interface OnItemLongClickListener {
        /**
         * item被长按时调用
         *
         * @param view     被长按的item
         * @param position item的位置
         */
        fun onLongClick(view: View, position: Int)
    }

    interface OnTouchListener {
        /**
         * item被触摸时调用
         *
         * @param v        被触摸的item
         * @param event    触摸事件
         * @param position item的位置
         * @return 是否消费
         */
        fun onTouch(v: View, event: MotionEvent, position: Int): Boolean
    }

    fun setOnTouchListener(listener: OnTouchListener) {
        mOnTouchListener = listener
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
