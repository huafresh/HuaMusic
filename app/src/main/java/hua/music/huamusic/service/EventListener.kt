package hua.music.huamusic.service

/**
 * 音乐播放事件监听
 *
 * @author hua
 * @version 2017/12/29 15:38
 *
 */
interface EventListener {

    companion object {
        val BUFFERING = 1 //正在缓冲
        val PREPARING = 2 //正在准备
        val PREPARED = 3 //准备完毕
        val STARTED = 4 //播放中
        val UPDATING_PROGRESS = 5 //进度更新
        val PAUSED = 6 //已暂停
        val STOPPED = 7 //已停止
        val COMPLETED = 8 //播放完毕
        val RESOURCE_CHANGED = 9 //播放源改变
        val ENDED = 10 //结束
        val ERROR = 11 //播放错误
    }

    /**
     * 播放器状态改变时调用，[type]是事件的类型，值是上面定义的常量
     * [arg]是事件的参数，可能是空的
     */
    fun onEvent(type: Int, vararg arg: Any)

}