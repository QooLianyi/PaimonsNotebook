package com.lianyi.paimonsnotebook.ui.screen.achievement.util.helper

import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.list.split
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement.AchievementData

object AchievementHelper {

    private val achievementsDao = PaimonsNotebookDatabase.database.achievementsDao

    //ids的最大个数
    private const val IDS_MAX_COUNT = 900

    /*
    * 获取成就的完成数量
    * 防止sqlite
    *
    * 此方法必须在非UI线程上调用
    * */
    fun getAchievementsFinishCountByList(userId: Int, list: List<AchievementData>): Int {
        //此处通过将不同分类的成就id与数据库对比,查询是否在数据库中存在
        val ids = list.map {
            it.id
        }

        return getAchievementsFinishCountByIds(userId, ids)
    }

    fun getAchievementsFinishCountByIds(userId: Int, ids: List<Int>): Int {
        if (ids.isEmpty()) return 0

        //当ids的尺寸超过IDS_MAX_COUNT时进行分割
        return if (ids.size > IDS_MAX_COUNT) {
            ids.split(IDS_MAX_COUNT).sumOf { splitIds ->
                getAchievementsFinishCountByIds(userId, splitIds)
            }
        } else {
            achievementsDao.getCountByAchievementIdsAndUserId(ids, userId)
        }
    }
}