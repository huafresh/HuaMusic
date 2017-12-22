package hua.music.huamusic.base

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.PopupWindow

/**
 * 实际应用中常常见到容器 + 内容的业务场景。一个通用的容器，可以动态添加多个不同的内容。并且同一个
 * 内容又可以复用被添加进不同的容器。
 *
 * 比如一个ViewGroup布局是一个容器，那么它的子View就是内容；再比如安卓系统的[PopupWindow]和
 * [Dialog]是一个容器，它里面展示的视图就是内容。
 *
 * 这个接口的两个内部接口就是分别对应上面提到的容器和内容。
 *
 * @author hua
 * @version 2017/6/29
 */
interface IWindow {

    /**
     * 容器接口。
     */
    interface IContainer {
        /**
         * 往容器添加内容
         *
         * @param contentView 要添加的内容
         */
        fun addContentView(contentView: IContentView)

        /**
         * 移除指定内容
         *
         * @param contentView 要移除的内容
         */
        fun removeContentView(contentView: IContentView)
    }

    /**
     * 内容视图接口
     */
    interface IContentView {

        /**
         * 获取内容实体视图
         *
         * @param context Context
         * @return 内容实体视图
         */
        fun getContentView(context: Context): View

        /**
         * 被添加进容器时调用
         *
         * @param container 被添加进的容器
         */
        fun onAttachToContainer(container: IContainer)

        /**
         * 被移除出容器时调用
         *
         * @param container 移除内容的容器
         */
        fun onDetachContainer(container: IContainer)
    }

}
