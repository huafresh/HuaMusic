package hua.music.huamusic.wrapper.recyclerview

import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * recyclerView viewHolder封装
 *
 * @author hua
 * @date 2017/6/16
 */

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val viewHashMap = SparseArray<View>()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(@IdRes id: Int): T? {
        var view: View? = viewHashMap.get(id)
        if (view == null) {
            view = itemView.findViewById(id)
            if (view != null) {
                viewHashMap.put(id, view)
            }
        }
        return view as T?
    }

    fun setText(@IdRes id: Int, text: String): MyViewHolder {
        val textView = getView<TextView>(id)
        if (textView != null) {
            textView.text = text
        }
        return this
    }

    fun setText(@IdRes id: Int, text: Int): MyViewHolder {
        val textView = getView<TextView>(id)
        if (textView != null) {
            textView.text = text.toString()
        }
        return this
    }

    fun setVisibility(@IdRes id: Int, visible: Int): MyViewHolder {
        val view = getView<View>(id)
        if (view != null) {
            if (view.visibility != visible) {
                view.visibility = visible
            }
        }
        return this
    }

    fun getVisibility(@IdRes id: Int): Int {
        val view = getView<View>(id)
        return view?.visibility ?: -1
    }

    fun setSwitchOnOff(@IdRes id: Int, onOff: Boolean): MyViewHolder {
        val switchCompat = getView<SwitchCompat>(id)
        if (switchCompat != null) {
            switchCompat.isSelected = onOff
        }
        return this
    }

    fun setImageResId(@IdRes id: Int, @DrawableRes imgId: Int): MyViewHolder {
        val ImageView = getView<ImageView>(id)
        ImageView?.setImageResource(imgId)
        return this
    }

}
