package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.wiki.PropertyCurveValue
import org.json.JSONArray
import java.io.File

/*
* 武器服务
* */
class WeaponService(onMissingFile: () -> Unit) {

    companion object{
        //忽略的武器
        private val skippedWeapons by lazy {
            setOf(
                12304, 14306, 15306, 13304, // 石英大剑, 琥珀玥, 黑檀弓, 「旗杆」
                11419, 11420, 11421, // 「一心传」名刀
            )
        }
    }

    //武器列表
    var weaponList = listOf<WeaponData>()
        private set

    val weaponMap by lazy {
        weaponList.associateBy { it.id }
    }

    lateinit var fightPropertyValueCalculateService: FightPropertyValueCalculateService
        private set

    init {
        val weaponFile = FileHelper.getMetadata(MetadataHelper.FileNameWeapon)
        val weaponCurveFile = FileHelper.getMetadata(MetadataHelper.FileNameWeaponCurve)
        val weaponPromoteFile = FileHelper.getMetadata(MetadataHelper.FileNameWeaponPromote)

        if (weaponFile != null && weaponCurveFile != null && weaponPromoteFile != null) {
            setWeaponList(JSONArray(weaponFile.readText()))

            initFightPropertyValueCalculateService(weaponCurveFile, weaponPromoteFile)
        } else {
            onMissingFile.invoke()
        }
    }

    private fun initFightPropertyValueCalculateService(
        curveFile: File,
        promoteFile: File
    ) {
        fightPropertyValueCalculateService = FightPropertyValueCalculateService(
            growCurveFile = curveFile,
            promoteFile = promoteFile
        )
    }

    /*
    * 获得格式化的战斗属性列表
    *
    * weapon:武器
    * level:等级
    * promoted:是否突破
    * */
    fun getFightPropertyFormatList(
        weapon: WeaponData,
        level: Int,
        promoted: Boolean
    ): List<FightPropertyFormat> {

        val list = weapon.growCurves.map { growCurve ->
            PropertyCurveValue(
                growCurve.Type,
                growCurve.Value,
                growCurve.InitValue
            )
        }

        return fightPropertyValueCalculateService.getFightPropertyFormatList(
            propertyCurveValues = list,
            promoteId = weapon.promoteId,
            promoted = promoted,
            level = level
        )
    }

    private fun setWeaponList(jsonArray: JSONArray) {
        val list = mutableListOf<WeaponData>()
        repeat(jsonArray.length()) {
            val jsonString = jsonArray.getJSONObject(it).toString()
            val item = JSON.parse<WeaponData>(jsonString)

            if(!skippedWeapons.contains(item.id)){
                list += item
            }
        }

        list.sortBy { it.id }

        weaponList = list
    }
}