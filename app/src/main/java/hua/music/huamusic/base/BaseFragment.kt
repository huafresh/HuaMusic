package hua.music.huamusic.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hua.music.huamusic.R

/**
 * @author hua
 * @version 2018/1/11 9:44
 *
 */
abstract class BaseFragment : Fragment() {

    protected var mView: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (mView == null) {
            mView = inflater?.inflate(getLayoutId(), container, false)
        }
        return mView
    }

    /**
     * 提供视图id
     */
    protected abstract @LayoutRes
    fun getLayoutId(): Int

}