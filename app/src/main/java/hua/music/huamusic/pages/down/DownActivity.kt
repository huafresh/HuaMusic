package hua.music.huamusic.pages.down

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import hua.music.huamusic.R
import hua.music.huamusic.base.BaseActivity
import hua.music.huamusic.utils.CommonUtil

/**
 * 下载管理
 *
 * @author hua
 * @version 2017/12/22 10:00
 *
 */
class DownActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {
    override fun initToolBar(toolbar: Toolbar) {
        super.initToolBar(toolbar)
        toolbar.setTitle(R.string.menu_down)

        //排序按钮没图片，只能从别的地方搞了一样的，因此需要自定义它的颜色
        val imageView = ImageView(this)
        imageView.setImageDrawable(CommonUtil.tintDrawable(this, R.drawable.action_order,
                CommonUtil.getColor(this, android.R.color.white, null)))
        val lp = Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.END or Gravity.CENTER_VERTICAL)
        toolbar.addView(imageView, lp)
        imageView.setOnClickListener {
            Toast.makeText(this, "排序", Toast.LENGTH_SHORT).show()
        }

        toolbar.inflateMenu(R.menu.menu_down)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.search -> {
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
        return item!=null
    }


    override fun onCreateContent(): Fragment? {
        return DownFragment()
    }

}