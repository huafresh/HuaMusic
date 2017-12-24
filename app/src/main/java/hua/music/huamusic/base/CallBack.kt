package hua.music.huamusic.base

/**
 * 通用回调接口
 * Created by hua on 2017/12/24.
 */
interface CallBack<in T> {

    /**
     * 失败时调用
     */
    fun onFailed(e: Exception)

    /**
     * 成功时调用
     */
    fun onSuccess(data: T)

}