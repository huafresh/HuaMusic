package hua.music.huamusic.data.network
import com.fasterxml.jackson.annotation.JsonProperty


/**
 * 酷狗api：http://krcs.kugou.com/search接口返回的json数据
 *
 * @author hua
 * @version 2018/1/19 17:17
 *
 */
data class KuGouSearchLrc(
		@JsonProperty("ugccandidates") val ugccandidates: List<Any>,
		@JsonProperty("ugc") val ugc: Int, //0
		@JsonProperty("info") val info: String, //OK
		@JsonProperty("status") val status: Int, //200
		@JsonProperty("proposal") val proposal: String, //23961431
		@JsonProperty("keyword") val keyword: String, //林俊杰 - 爱要怎么说出口 (Live)
		@JsonProperty("candidates") val candidates: List<Candidate>
)

data class Candidate(
		@JsonProperty("soundname") val soundname: String,
		@JsonProperty("krctype") val krctype: Int, //1
		@JsonProperty("nickname") val nickname: String,
		@JsonProperty("originame") val originame: String,
		@JsonProperty("accesskey") val accesskey: String, //CDE62C1A49D7DA2CE1EECD5878CFFE71
		@JsonProperty("parinfo") val parinfo: List<Any>,
		@JsonProperty("origiuid") val origiuid: String, //0
		@JsonProperty("score") val score: Int, //60
		@JsonProperty("hitlayer") val hitlayer: Int, //1
		@JsonProperty("duration") val duration: Int, //238000
		@JsonProperty("sounduid") val sounduid: String, //0
		@JsonProperty("song") val song: String, //爱要怎么说出口
		@JsonProperty("uid") val uid: String, //1000000010
		@JsonProperty("transuid") val transuid: String, //0
		@JsonProperty("transname") val transname: String,
		@JsonProperty("adjust") val adjust: Int, //0
		@JsonProperty("id") val id: String, //23961431
		@JsonProperty("singer") val singer: String, //林俊杰
		@JsonProperty("language") val language: String //国语
)