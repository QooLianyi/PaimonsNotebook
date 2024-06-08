package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.user

/*
* 此类以及子类是删减过的
* 去除了用不到的字段,保留了用得到与可能会用到的字段
* */
data class UserFullInfoData(
    val auth_relations: List<Any>,
    val follow_relation: Any,
    val is_creator: Boolean,
    val is_has_collection: Boolean,
    val is_in_blacklist: Boolean,
    val user_info: UserInfo,
) {
    data class UserInfo(
        val avatar: String,
        val avatar_url: String,
        val gender: Int,
        val introduce: String,
        val nickname: String,
        val pendant: String,
        val uid: String,
    ) {

        companion object {
            //返回一个空的占位
            fun getEmptyPlaceholder(mid: String) =
                UserInfo(
                    avatar = "",
                    avatar_url = "",
                    gender = 0,
                    introduce = "",
                    nickname = mid,
                    pendant = "",
                    uid = "Cookie失效,请重新登录"
                )
        }
    }
}