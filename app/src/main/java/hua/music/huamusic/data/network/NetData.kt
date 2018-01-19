package hua.music.huamusic.data.network

import android.os.Looper
import hua.music.huamusic.entitys.LrcEntity
import hua.music.huamusic.utils.JsonParseUtil
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import java.io.*
import java.util.zip.GZIPInputStream

/**
 * 获取网络资源实现
 *
 * @author hua
 * @version 2018/1/18 16:56
 *
 */
class NetData private constructor() : INetData {
    private val mOkHttpClient: OkHttpClient = OkHttpClient.Builder()
            .build()

    companion object {

        /**
         * 获取单例实例
         */
        fun getInstance(): NetData {
            return Holder.sInstance
        }

        private object Holder {

            val sInstance = NetData()
        }
    }

    /**
     * 歌词搜索接口：
     * http://krcs.kugou.com/search?ver=1&man=yes&client=mobi&keyword=%E6%9E%97%E4%BF%8A%E6%9D%B0%20-%20%E7%88%B1%E8%A6%81%E6%80%8E%E4%B9%88%E8%AF%B4%E5%87%BA%E5%8F%A3%20%28Live%29&duration=237581&hash=a860533d41c9c7da676c6eb81d5e7478
     */
    override fun searchLrc(songName: String, author: String, duration: String,
                           callBack: (any: Any) -> Unit) {
        Flowable.just("")
                .flatMap {
                    val url = StringBuilder("http://krcs.kugou.com/search?ver=1&man=yes&client=mobi&")
                            .append("keyword=$author-$songName&")
                            .append("duration=$duration&")
                            .toString()
                    val request = Request.Builder()
                            .url(url)
                            .build()
                    Flowable.just(mOkHttpClient.newCall(request).execute())
                }
                .map(Function<Response, List<KuGouSearchLrc>> { t ->
                    val responseString = t.body()?.string()
                    if (responseString != null) {
                        val list = JsonParseUtil.parseJsonToList(responseString, KuGouSearchLrc::class.java)
                        return@Function list ?: emptyList()
                    }
                    return@Function emptyList()
                })
                .map(Function<List<KuGouSearchLrc>, List<LrcEntity>> {
                    if (!it.isEmpty()) {
                        val resultList = mutableListOf<LrcEntity>()
                        it[0].candidates.forEach {
                            val entity = LrcEntity(it.id,it.accesskey)
                            entity.songName = it.song
                            entity.author = it.singer
                            entity.duration = it.duration.toString()
                            entity.score = it.score.toString()
                            entity.language = it.language
                            resultList.add(entity)

                        }
                        return@Function resultList
                    }
                    return@Function emptyList()
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callBack.invoke(it)
                }

    }

    override fun downLoadLrc(name: String, callBack: (any: Any) -> Unit) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            throw RuntimeException("downLoadLrc can't be called from the main thread")
        }

        val request = Request.Builder()
                .url("")
                .build()
        val response = mOkHttpClient.newCall(request).execute()
        try {
            val gzipInput = GZIPInputStream(response.body()?.byteStream())
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = -1
            do {
                len = gzipInput.read(buffer)
                if (len != -1) {
                    out.write(buffer, 0, len)
                }
            } while (len != -1)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}