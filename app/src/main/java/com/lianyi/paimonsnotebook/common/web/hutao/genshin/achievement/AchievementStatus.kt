package com.lianyi.paimonsnotebook.common.web.hutao.genshin.achievement

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