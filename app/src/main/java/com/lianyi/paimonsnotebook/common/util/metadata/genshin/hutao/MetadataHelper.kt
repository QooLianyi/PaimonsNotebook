package com.lianyi.paimonsnotebook.common.util.metadata.genshin.hutao

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.hash.XXHash
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsText
import com.lianyi.paimonsnotebook.common.util.request.getAsTextResult
import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.LocaleNames

/*
* 元数据名称
* */
object MetadataHelper {
    //启动时检查的元数据列表
    private val checkList by lazy {
        listOf(
            FileNameAvatar,
            FileNameAvatarCurve,
            FileNameAvatarPromote,
            FileNameWeapon,
            FileNameWeaponCurve,
            FileNameWeaponPromote,
            FileNameMaterial,
            FileNameMonster,
            FileNameTowerSchedule
        )
    }

    private val hashMap = mutableMapOf<String, String>()

    /*
    * 检查元数据,判断是否有元数据需要更新
    *
    * 返回需要更新的文件列表
    * */
    private fun checkMetadata(): List<String> {
        val updateFileList = mutableListOf<String>()

        val xxHash = XXHash()
        checkList.forEach { name ->
            val file = FileHelper.getMetadata(name)
            val arr = (file?.readText() ?: "").replace("\n", "\r\n").toByteArray()

            val xxh64 = xxHash.hash64(arr).toULong().toString(16).uppercase()
            val metaXxH64 = hashMap[name]

            if (metaXxH64 != xxh64 && !metaXxH64.isNullOrEmpty()) {
                updateFileList += name
            }
        }

        return updateFileList
    }

    /*
    * 判断所需的元数据文件是否全都存在
    *
    * true为全部存在
    * */
    fun allMetadataExists() =
        checkList.map { name ->
            FileHelper.getMetadata(name)
        }.count { it != null }.let { count ->
            count == checkList.size
        }

    suspend fun metadataNeedUpdate(): Boolean {
        updateMetadataHashMap()

        return checkMetadata().isNotEmpty()
    }

    /*
    * 更新元数据
    * */
    suspend fun updateMetadata(
        onFailed: suspend () -> Unit,
        onSuccess: suspend () -> Unit,
        finally: suspend () -> Unit
    ) {
        updateMetadataHashMap()

        checkMetadata().forEach { name ->
            loadAndSaveFile(name)
        }

        if (checkMetadata().isEmpty()) {
            onSuccess.invoke()
        } else {
            onFailed.invoke()
        }

        finally.invoke()
    }

    //更新元数据map
    private suspend fun updateMetadataHashMap() {
        val metaJson = buildRequest {
            url(HutaoEndpoints.hutaoMetadata2File(LocaleNames.CHS, "$MetaFileName.json"))
        }.getAsText()

        hashMap.clear()
        hashMap.putAll(
            JSON.parse<Map<String, String>>(
                metaJson,
                getParameterizedType(Map::class.java, String::class.java, String::class.java)
            )
        )
    }

    //重新载入单个文件
    private suspend fun loadAndSaveFile(name: String) {
        val pair = buildRequest {
            url(HutaoEndpoints.hutaoMetadata2File(LocaleNames.CHS, "${name}.json"))
        }.getAsTextResult()

        if (pair.first) {
            FileHelper.getMetadataSaveFile(name).apply {
                writeText(pair.second)
            }
        }
    }

    private val MetaFileName by lazy { "Meta" }

    val FileNameAchievement by lazy { "Achievement" }
    val FileNameAchievementGoal by lazy { "AchievementGoal" }
    val FileNameAvatar by lazy { "Avatar" }
    val FileNameAvatarCurve by lazy { "AvatarCurve" }
    val FileNameAvatarPromote by lazy { "AvatarPromote" }
    val FileNameDisplayItem by lazy { "DisplayItem" }
    val FileNameGachaEvent by lazy { "GachaEvent" }
    val FileNameMaterial by lazy { "Material" }
    val FileNameMonster by lazy { "Monster" }
    val FileNameMonsterCurve by lazy { "MonsterCurve" }
    val FileNameReliquary by lazy { "Reliquary" }
    val FileNameReliquaryAffixWeight by lazy { "ReliquaryAffixWeight" }
    val FileNameReliquaryMainAffix by lazy { "ReliquaryMainAffix" }
    val FileNameReliquaryMainAffixLevel by lazy { "ReliquaryMainAffixLevel" }
    val FileNameReliquarySet by lazy { "ReliquarySet" }
    val FileNameReliquarySubAffix by lazy { "ReliquarySubAffix" }
    val FileNameTowerFloor by lazy { "TowerFloor" }
    val FileNameTowerLevel by lazy { "TowerLevel" }
    val FileNameTowerSchedule by lazy { "TowerSchedule" }
    val FileNameWeapon by lazy { "Weapon" }
    val FileNameWeaponCurve by lazy { "WeaponCurve" }
    val FileNameWeaponPromote by lazy { "WeaponPromote" }
}