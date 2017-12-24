package hua.music.huamusic.utils


import android.text.TextUtils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory

import org.json.JSONArray

import java.io.IOException
import java.util.ArrayList

/**
 * Jackson框架实现的json解析工具
 *
 * @author hua
 * @date 2017/6/5
 */
object JsonParseUtil {

    private val objectMapper = ObjectMapper()
    private val typeFactory = TypeFactory.defaultInstance()

    /**
     * json字符串解析成list列表
     *
     * @param cls list集合元素的类型
     * @return list对象
     */
    fun <T> parseJsonToList(json: String, cls: Class<T>): List<T>? {
        return objectMapper.readValue(json,
                typeFactory.constructCollectionType(ArrayList::class.java, cls))
    }

    /**
     * JSONArray解析成list列表
     *
     * @param cls list集合元素的类型
     * @return list对象
     */
    fun <T> parseJsonToList(jsonArray: JSONArray, cls: Class<T>): List<T>? {
        return parseJsonToList(jsonArray.toString(), cls)
    }

    /**
     * json字符串解析成bean对象
     *
     * @return bean对象
     */
    fun <T> parseJsonToObject(json: String, model: Class<T>): T? {
        return objectMapper.readValue(json, model)
    }

    /**
     * 对象转json字符串
     *
     * @param object 对象
     * @return json字符串
     */
    fun parseObjectToString(`object`: Any?): String? {
        return objectMapper.writeValueAsString(`object`)
    }

}
