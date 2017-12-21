package hua.music.huamusic.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 通用工具类，聚集常用工具方法
 *
 * @author hua
 * @date 2017/6/8
 */
object CommonUtil {

    /**
     * 设置状态栏颜色。需要4.4以上，否则无法设置。
     *
     * @param activity Activity
     * @param color    颜色
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = color
            return
        }

        //透明化状态栏
        setStatusBarTranslucent(activity)
        //添加一个与状态栏等高的view到布局中
        val contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
        val childView = contentView.getChildAt(0)
        if (childView != null && childView is LinearLayout) {
            //偏移内容布局
            (childView.layoutParams as FrameLayout.LayoutParams).topMargin = getStatusBarHeight(activity)
            //添加状态栏布局
            val statusView = View(activity)
            statusView.setBackgroundColor(color)
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity))
            contentView.addView(statusView, params)
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @param context Context
     * @return 状态栏的高度
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resId = context.resources.getIdentifier("status_bar_height",
                "dimen", "android")
        if (resId > 0) {
            result = context.resources.getDimensionPixelSize(resId)
        }
        return result
    }

    /**
     * 透明化状态栏。4.4以下无效
     *
     * @param activity Activity
     */
    private fun setStatusBarTranslucent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            val lp = window.attributes
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.attributes = lp
        }
    }

    /**
     * 获取颜色资源id对应的颜色值，主要是兼容高版本
     *
     * @return 颜色值
     */
    fun getColor(context: Context, @ColorRes color: Int,
                 theme: Resources.Theme?): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(color, theme)
        } else {
            context.resources.getColor(color)
        }
    }

}
