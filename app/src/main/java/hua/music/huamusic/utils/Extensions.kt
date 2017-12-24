package hua.music.huamusic.utils

import android.util.TypedValue
import android.view.View

/**
 * kotlin扩展
 * Created by hua on 2017/12/23.
 */

/**
 * dp转px
 */
fun View.dp2px(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
}

/**
 * sp转px
 */
fun View.sp2px(sp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics).toInt()
}