package com.lianyi.paimonsnotebook.common.util.metadata.genshin.uiaf

import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf.UIGFHelper

object UIAFHelper {

    const val UIAF_VERSION = "v1.1"

    //与UIGF相同的主页
    const val UIAF_HOME_PAGE = UIGFHelper.UIGF_HOME_PAGE

    //无效日期
    const val INVALID_TIMESTAMP = 253402271999

    //无效进度
    const val INVALID_CURRENT = 0

    //有效进度
    const val VALID_CURRENT = 30

    //无效的分类id
    const val INVALID_GOAL = -1


    object Field {

        //数据原型,导入用
        object Data {
            const val Description = "Description"
            const val Goal = "Goal"
            const val Id = "Id"
            const val IsDeleteWatcherAfterFinish = "IsDeleteWatcherAfterFinish"
            const val IsDailyQuest = "IsDailyQuest"
            const val Order = "Order"
            const val Progress = "Progress"
            const val Title = "Title"
            const val Version = "Version"
            const val Icon = "Icon"
            const val PreviousId = "PreviousId"
        }

        object FinishReward {
            const val Count = "Count"
            const val Id = "Id"
            const val FinishReward = "FinishReward"

            //用于与data的id区分
            const val RewardId = "Reward.Id"
        }

        object Goal {
            const val Id = "Id"
            const val Name = "Name"
            const val Order = "Order"
            const val Icon = "Icon"
            const val Count = "Count"
        }


        //导出信息字段
        object Info {
            const val ExportApp = "export_app"
            const val ExportTimestamp = "export_timestamp"
            const val ExportAppVersion = "export_app_version"
            const val UIAFVersion = "uiaf_version"

            val fields = arrayOf(
                ExportTimestamp,
                ExportApp,
                ExportAppVersion,
                UIAFVersion
            )
        }

        //导出item字段
        object Item {
            const val Timestamp = "timestamp"
            const val Status = "status"
            const val Current = "current"
            const val Id = "id"

            val fields = arrayOf(
                Timestamp,
                Status,
                Current,
                Id
            )
        }
    }

    object AchievementStatus {
        /// <summary>
        /// 未识别
        /// </summary>
        const val STATUS_UNRECOGNIZED = -1

        /// <summary>
        /// 不使用的成就
        /// </summary>
        const val STATUS_INVALID = 0

        /// <summary>
        /// 未完成
        /// </summary>
        const val STATUS_UNFINISHED = 1

        /// <summary>
        /// 已完成
        /// </summary>
        const val STATUS_FINISHED = 2

        /// <summary>
        /// 奖励已领取
        /// </summary>
        const val STATUS_REWARD_TAKEN = 3
    }

}