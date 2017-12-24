package hua.music.huamusic

import android.annotation.SuppressLint
import android.app.Application

/**
 * 应用程序入口
 * Created by hua on 2017/12/24.
 */
@SuppressLint("StaticFieldLeak")
class App : Application() {

    companion object {
        lateinit var sInstance: Application
    }

    init {
        sInstance = this
    }

}