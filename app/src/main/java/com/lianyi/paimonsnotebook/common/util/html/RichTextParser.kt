package com.lianyi.paimonsnotebook.common.util.html

import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostStructuredContent
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostStructuredContentData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostViewType
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.StructuredBackupText
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.StructuredContentAttributes
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.StructuredContentType
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.view_type.ViewTypeImageContent
import org.json.JSONArray
import org.json.JSONObject

/*
* 帖子详情富文本解析器
* */
object RichTextParser {

    //插入对象
    const val insert = "insert"

    //自定义图标
    const val backup_text = "backup_text"

    //图片
    const val image = "image"

    //视频
    const val video = "video"

    //投票
    const val vote = "vote"

    //视频
    const val vod = "vod"

    //分割
    const val divider = "divider"

    //属性
    const val attributes = "attributes"

    //折叠内容
    const val fold = "fold"

    //分割线
    const val mention = "mention"

    //链接卡片
    const val link_card = "link_card"

    //抽奖
    const val lottery = "lottery"

    const val header = "header"

    //对齐方式
    const val align = "align"

    fun parsePostStructuredContent(content: String): List<List<PostStructuredContentData>> {
        val jsonArray = try {
            JSONArray(content)
        } catch (e: Exception) {
            "解析帖子内容时发生错误:${e.message}".errorNotify()
            return listOf()
        }

        val list = mutableListOf<List<PostStructuredContentData>>()

        var tempList = mutableListOf<PostStructuredContentData>()
        repeat(jsonArray.length()) {
            val obj = jsonArray.getJSONObject(it)

            var insert = ""
            if (obj.has("insert")) {
                insert = obj.get("insert").toString()
            }

            var attributes = "{}"
            if (obj.has("attributes")) {
                attributes = obj.getJSONObject("attributes").toString()
            }

            val attr = JSON.parse<PostStructuredContentData.Attributes>(attributes)

            if (insert.startsWith("{") && insert.endsWith("}")) {
                val com = if (obj.has("backup_text")) {
                    insert
                } else {
                    insert.replace("\"[", "[").replace("]\"", "]")
                }.replace("\\\"", "\"")

                if (tempList.isNotEmpty()) {
                    list += tempList
                    tempList = mutableListOf()
                }

                list += listOf(PostStructuredContentData(attr, JSON.parse(com)))
            } else {
                tempList += PostStructuredContentData(
                    attr,
                    JSON.parse("{\"insert\":\"${insert}\"}")
                )
            }
        }

        if (tempList.isNotEmpty()) {
            list += tempList
        }

        return list
    }

    fun parse(
        post: PostFullData.Post
    ): List<PostStructuredContent> {
        val structuredContent = post.post.structured_content

        val arrays = try {
            JSONArray(structuredContent)
        } catch (_: Exception) {
            JSONArray()
        }

        val list = mutableListOf<PostStructuredContent>()

        when (post.post.view_type) {
            PostViewType.TYPE_IMAGE -> {
                try {
                    val content = JSON.parse<ViewTypeImageContent>(post.post.content)
                    list += PostStructuredContent.getTextItem(content.describe)
                    list += content.imgs.map {
                        PostStructuredContent.getImageItem(it)
                    }
                } catch (_: Exception) {

                }
            }

            PostViewType.TYPE_VIDEO -> {
                list += PostStructuredContent.getTextItem(post.post.content)
                list += post.vod_list.map {
                    PostStructuredContent.getVodItem(structuredVod = it)
                }
            }

            //将结构内容进行解析
            else -> {
                //遍历结构内容
                repeat(arrays.length()) { index ->
                    //获取插入对象整体
                    val obj = JSONObject(arrays.getString(index))

                    val objMap = mutableMapOf<String, Any>()

                    val keys = obj.keys()

                    keys.forEach {
                        objMap[it] = obj.get(it)
                    }

                    //尝试获取属性
                    val attributes = try {
                        JSON.parse<StructuredContentAttributes>((objMap[RichTextParser.attributes] as JSONObject).toString())
                    } catch (_: Exception) {
                        null
                    }

                    lateinit var structuredContentItem: PostStructuredContent.Item
                    lateinit var type: StructuredContentType

                    //获取插入对象
                    when (val insert = objMap[RichTextParser.insert]) {
                        is JSONObject -> {

                            //获取第一个属性的key
                            //通过key判断是什么类型的对象
                            val (data, contentType) = when (val key = insert.keys().next()) {
                                fold -> {
                                    PostStructuredContent.Item() to StructuredContentType.LinkCard
                                }

                                link_card -> {
                                    //link_card内部包含一个link_card对象
                                    val data = getObject<PostFullData.Post.LinkCard>(insert, key)
                                    PostStructuredContent.Item(linkCard = data) to StructuredContentType.LinkCard
                                }

                                image -> {
                                    val url = insert.getString(image)
                                    PostStructuredContent.Item(url = url) to StructuredContentType.Image
                                }

                                divider -> {
                                    PostStructuredContent.Item() to StructuredContentType.Divider
                                }

                                backup_text -> {
                                    var structType = StructuredContentType.Empty
                                    try {
                                        insert.getJSONObject(fold)
                                        structType = StructuredContentType.Fold
                                    } catch (_: Exception) {
                                    }

                                    try {
                                        insert.getJSONObject(lottery)
                                        structType = StructuredContentType.Lottery
                                    } catch (_: Exception) {
                                    }


                                    PostStructuredContent.Item(
                                        backupText = getObject<StructuredBackupText>(insert)
                                    ) to structType
                                }

                                vod -> {
                                    val data = getObject<PostFullData.Post.Vod>(insert, key)
                                    PostStructuredContent.Item(
                                        vod = data
                                    ) to StructuredContentType.Vod
                                }

                                else -> {
                                    PostStructuredContent.Item() to StructuredContentType.Error
                                }
                            }

                            structuredContentItem = data
                            type = contentType
                        }

                        is String -> {
                            //判断是否有属性
                            type = if (attributes != null) {
                                //如果是链接内部会有link对象存储目标url
                                if (attributes.link != null) {
                                    StructuredContentType.Link
                                } else {
                                    StructuredContentType.Text
                                }
                            } else {
                                StructuredContentType.Text
                            }

                            structuredContentItem = PostStructuredContent.Item(
                                content = insert
                            )
                        }
                    }

                    list += PostStructuredContent(
                        type = type,
                        attributes = attributes,
                        insert = structuredContentItem
                    )

                }
            }
        }

        return list
    }

    //转换分组
    fun parseGroup(post: PostFullData.Post): List<Pair<StructuredContentType, List<PostStructuredContent>>> {
        val list = parse(post)

        return mutableListOf<Pair<StructuredContentType, List<PostStructuredContent>>>().apply {
            var tempList = mutableListOf<PostStructuredContent>()
            list.forEach { postStructuredContent ->
                when (postStructuredContent.type) {
                    StructuredContentType.Link, StructuredContentType.Text -> {
                        tempList += postStructuredContent
                    }

                    else -> {
                        if (tempList.isNotEmpty()) {
                            this += StructuredContentType.Text to tempList
                            tempList = mutableListOf()
                        }
                        //非文本与链接的元素直接新建一个集合添加,因此first永远都会有值
                        this += postStructuredContent.type to listOf(postStructuredContent)
                    }
                }
            }

            if (tempList.isNotEmpty()) {
                //当最后一条记录是链接时，添加一个空白字符，防止链接出现意外的作用域
                if (tempList.last().type == StructuredContentType.Link) {
                    val template = tempList.last()
                    tempList += template.copy(
                        type = StructuredContentType.Text,
                        insert = template.insert.copy(content = " ")
                    )
                }
                this += StructuredContentType.Text to tempList
            }
        }
    }

    private inline fun <reified T> getObject(obj: Any, key: String = ""): T? = when (obj) {
        is JSONObject -> {
            if (key.isBlank()) {
                JSON.parse<T>(obj.toString())
            } else {
                JSON.parse<T>(obj.getString(key))
            }
        }

        else -> {
            null
        }
    }

}