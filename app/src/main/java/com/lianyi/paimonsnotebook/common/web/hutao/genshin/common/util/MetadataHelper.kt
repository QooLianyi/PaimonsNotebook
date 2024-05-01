package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.hash.XXHash
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.common.util.request.applicationOkHttpClient
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsText
import com.lianyi.paimonsnotebook.common.util.request.getAsTextResult
import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.LocaleNames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
* 元数据名称
* */
object MetadataHelper {
    //启动时检查的元数据列表
    private val metadataCheckList by lazy {
        listOf(
            FileNameAvatar,
            FileNameAvatarCurve,
            FileNameAvatarPromote,
            FileNameWeapon,
            FileNameWeaponCurve,
            FileNameWeaponPromote,
            FileNameMaterial,
            FileNameMonster,
            FileNameTowerSchedule,
            FileNameAchievement,
            FileNameAchievementGoal
        )
    }

    //图包检查列表
    private val zipCheckList by lazy {
        listOf(
            ZipFileNameAvatarIcon,
            ZipFileNameEquipIcon,
            ZipFileNameMonsterIcon,
            ZipFileNameGachaAvatarImg,
            ZipFileNameSkill,
            ZipFileNameTalent,
            ZipFileNameLoadingPic
        )
    }

    private val hashMap = mutableMapOf<String, String>()

    /*
    * 检查元数据,判断是否有元数据需要更新
    *
    * 返回需要更新的文件列表
    * */
    @OptIn(ExperimentalStdlibApi::class)
    private fun checkMetadata(): List<String> {
        val updateFileList = mutableListOf<String>()

        val xxHash = XXHash()
        metadataCheckList.forEach { name ->
            val file = FileHelper.getMetadata(name)
            val arr = (file?.readText() ?: "").replace("\n", "\r\n").toByteArray()

            //此处使用.toString(16)会导致第一位如果为15会变F,导致无法与Map的0F对应
            val xxh64 = xxHash.hash64(arr).toULong().toHexString().uppercase()
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
        metadataCheckList.map { name ->
            FileHelper.getMetadata(name)
        }.count { it != null }.let { count ->
            count == metadataCheckList.size
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
        onLoadMetadataFile: (Int) -> Unit,
        finally: suspend () -> Unit
    ) {
        updateMetadataHashMap()

        //当元数据哈希表比所需列表小时,直接返回失败
        if (hashMap.keys.size < metadataCheckList.size) {
            onFailed.invoke()
            finally.invoke()
            return
        }

        withContext(Dispatchers.IO) {
            checkMetadata().forEach { name ->
                launch {
                    loadAndSaveFile(name)
                    onLoadMetadataFile.invoke(metadataCheckList.size)
                }
            }
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
            url(HutaoEndpoints.metadata(LocaleNames.CHS, "$MetaFileName.json"))
        }.getAsText(applicationOkHttpClient)

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
            url(HutaoEndpoints.metadata(LocaleNames.CHS, "${name}.json"))
        }.getAsTextResult(applicationOkHttpClient)

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

    val ZipFileNameAchievementIcon by lazy { "AchievementIcon" }
    val ZipFileNameAvatarCard by lazy { "AvatarCard" }
    val ZipFileNameAvatarIcon by lazy { "AvatarIcon" }
    val ZipFileNameBg by lazy { "Bg" }
    val ZipFileNameChapterIcon by lazy { "ChapterIcon" }
    val ZipFileNameCostume by lazy { "Costume" }
    val ZipFileNameEmotionIcon by lazy { "EmotionIcon" }
    val ZipFileNameEquipIcon by lazy { "EquipIcon" }
    val ZipFileNameGachaAvatarIcon by lazy { "GachaAvatarIcon" }
    val ZipFileNameGachaAvatarImg by lazy { "GachaAvatarImg" }
    val ZipFileNameGachaEquipIcon by lazy { "GachaEquipIcon" }
    val ZipFileNameIconElement by lazy { "IconElement" }
    val ZipFileNameItemIcon by lazy { "ItemIcon" }
    val ZipFileNameLoadingPic by lazy { "LoadingPic" }
    val ZipFileNameMonsterIcon by lazy { "MonsterIcon" }
    val ZipFileNameMonsterSmallIcon by lazy { "MonsterSmallIcon" }
    val ZipFileNameNameCardIcon by lazy { "NameCardIcon" }
    val ZipFileNameNameCardPic by lazy { "NameCardPic" }
    val ZipFileNameProperty by lazy { "Property" }
    val ZipFileNameRelicIcon by lazy { "RelicIcon" }
    val ZipFileNameSkill by lazy { "Skill" }
    val ZipFileNameTalent by lazy { "Talent" }

}