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
        val BUFFERING = 1
        val PREPARING = 2
        val PREPARED = 3
        val STARTED = 4
        val UPDATING_PROGRESS = 5
        val PAUSED = 6
        val STOPPED = 7
        val COMPLETED = 8
        val ENDED = 9
        val ERROR = 10
    }

    /**
     * 播放器状态改变时调用，[type]是事件的类型，值是上面定义的常量
     * [arg]是事件的参数，可能是空的
     */
    fun onEvent(type: Int, vararg arg: Any)

}