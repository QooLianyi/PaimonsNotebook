package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.FightProperty
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.GrowCurveType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.wiki.PropertyCurveValue
import org.json.JSONArray
import java.io.File

/*
* 角色服务
* */
class AvatarService(onMissingFile: () -> Unit) {
    //角色列表
    var avatarList = listOf<AvatarData>()
        private set

    lateinit var fightPropertyValueCalculateService: FightPropertyValueCalculateService
        private set

    init {
        val avatarFile = FileHelper.getMetadata(MetadataHelper.FileNameAvatar)
        val avatarCurveFile = FileHelper.getMetadata(MetadataHelper.FileNameAvatarCurve)
        val avatarPromoteFile = FileHelper.getMetadata(MetadataHelper.FileNameAvatarPromote)

        if (avatarFile != null && avatarCurveFile != null && avatarPromoteFile != null) {
            setAvatarList(JSONArray(avatarFile.readText()))

            initFightPropertyValueCalculateService(avatarCurveFile, avatarPromoteFile)
        } else {
            onMissingFile.invoke()
        }
    }

    private fun initFightPropertyValueCalculateService(
        avatarCurveFile: File,
        avatarPromoteFile: File
    ) {
        fightPropertyValueCalculateService = FightPropertyValueCalculateService(
            growCurveFile = avatarCurveFile,
            promoteFile = avatarPromoteFile
        )
    }

    /*
    * 获得格式化的战斗属性列表
    *
    * avatar:角色
    * level:等级
    * promoted:是否突破
    * */
    fun getFightPropertyFormatList(
        avatar: AvatarData,
        level: Int,
        promoted: Boolean
    ): List<FightPropertyFormat> {
        val promoteProperty =
            fightPropertyValueCalculateService.getPromoteMapByPromoteId(avatar.promoteId)
                ?.get(0)?.AddProperties?.last()?.Type ?: FightProperty.FIGHT_PROP_NONE

        val avatarGrowCurve = avatar.growCurves.associate {
            it.Type to it.Value
        }

        val list = listOf(
            PropertyCurveValue(
                FightProperty.FIGHT_PROP_BASE_HP,
                avatarGrowCurve[FightProperty.FIGHT_PROP_BASE_HP] ?: 0,
                avatar.baseValue.HpBase
            ),
            PropertyCurveValue(
                FightProperty.FIGHT_PROP_BASE_ATTACK,
                avatarGrowCurve[FightProperty.FIGHT_PROP_BASE_ATTACK] ?: 0,
                avatar.baseValue.AttackBase
            ),
            PropertyCurveValue(
                FightProperty.FIGHT_PROP_BASE_DEFENSE,
                avatarGrowCurve[FightProperty.FIGHT_PROP_BASE_DEFENSE] ?: 0,
                avatar.baseValue.DefenseBase
            ),
            PropertyCurveValue(
                promoteProperty,
                GrowCurveType.GROW_CURVE_NONE,
                0f
            )
        )

        return fightPropertyValueCalculateService.getFightPropertyFormatList(
            propertyCurveValues = list,
            promoteId = avatar.promoteId,
            promoted = promoted,
            level = level
        )
    }

    private fun setAvatarList(jsonArray: JSONArray) {
        val list = mutableListOf<AvatarData>()
        repeat(jsonArray.length()) {
            val jsonString = jsonArray.getJSONObject(it).toString()
            list += JSON.parse<AvatarData>(jsonString)
        }

        list.sortBy { it.sort }

        avatarList = list
    }
}