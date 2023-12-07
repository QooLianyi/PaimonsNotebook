package com.lianyi.paimonsnotebook.common.util.metadata.genshin.uiaf

object UIAFHelper {
    object Field {

        //数据原型,导入用
        object Data {
            const val Description = "Description"
            const val FinishReward = "FinishReward"
            const val Goal = "Goal"
            const val Id = "Id"
            const val IsDeleteWatcherAfterFinish = "IsDeleteWatcherAfterFinish"
            const val Order = "Order"
            const val Progress = "Progress"
            const val Title = "Title"
            const val Version = "Version"
            const val Icon = "Icon"
            const val PreviousId = "PreviousId"

            object Reward {
                const val Count = "Count"
                const val Id = "Id"
                //用于与data的id区分
                const val RewardId = "Reward.Id"
            }
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

}