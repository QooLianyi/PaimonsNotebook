package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util

import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.hash.XXHash
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.notification.PaimonsNotebookNotification
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.common.util.request.applicationOkHttpClient
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsText
import com.lianyi.paimonsnotebook.common.util.request.getAsTextResult
import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.LocaleNames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

/*
* 元数据名称
* */
object MetadataHelper {
    //启动时检查的元数据列表
    private val metadataCheckList by lazy {
        listOf(
//            FileNameAvatar, //角色拆分为单独的文件
            FileNameAvatarCurve,
            FileNameAvatarPromote,
            FileNameWeapon,
            FileNameWeaponCurve,
            FileNameWeaponPromote,
            FileNameMaterial,
            FileNameMonster,
            FileNameTowerSchedule,
            FileNameAchievement,
            FileNameAchievementGoal,
            FileNameReliquary,
            FileNameReliquarySet
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

    private val hashMap = mutableMapOf<String?, String?>()


    private var latestCheckHashMapTime = 0L

    //哈希表的检查时间
    private const val HashMapCheckInterval = 60000L

    /*
    * 返回map中哈希值不同的文件名集合
    * */
    @OptIn(ExperimentalStdlibApi::class)
    private fun getDownloadFileNameListFromMetadataMap(): List<String> {
        val updateFileList = mutableListOf<String>()

        val xxHash = XXHash()

        hashMap.forEach { (fileName, hashValue) ->

            val file = FileHelper.getMetadata(fileName ?: "")
            val arr = (file?.readText() ?: "").replace("\n", "\r\n").toByteArray()

            //此处使用.toString(16)会导致第一位如果为15会变F,导致无法与Map的0F对应
            val xxh64 = xxHash.hash64(arr).toULong().toHexString().uppercase()

            if (hashValue != xxh64 && !hashValue.isNullOrEmpty() && !fileName.isNullOrEmpty()) {
                updateFileList += fileName
            }
        }

        return updateFileList
    }

    private suspend fun metadataNeedUpdate(): Boolean {
        updateMetadataHashMap()

        return getDownloadFileNameListFromMetadataMap().isNotEmpty()
    }

    private var isUpdating = false

    suspend fun checkAndUpdateMetadata(
        notify: Boolean = false,
        onSuccess: suspend () -> Unit = {}
    ) {
        //防止重复更新
        if (isUpdating) {
            if (notify) {
                "元数据正在进行更新".warnNotify(false)
            }
            return
        }

        isUpdating = true

        if (!metadataNeedUpdate()) {
            if (notify) {
                "当前元数据已是最新".notify()
            }

            isUpdating = false
            onSuccess.invoke()
            return
        }

        val notifyId = "发现新的元数据,正在更新...".notify(keepShow = true)

        updateMetadata(
            onFailed = {
                "更新元数据时发生错误,现在使用的仍是旧数据,显示的内容可能会与最新的游戏内容有所差异".warnNotify()
            },
            onSuccess = {
                "元数据更新完毕".notify()
                onSuccess.invoke()
            },
            onLoadMetadataFile = {},
            finally = {
                PaimonsNotebookNotification.removeNotifyById(notifyId)
                isUpdating = false
            }
        )
    }

    /*
    * 更新元数据
    * */
    suspend fun updateMetadata(
        updateMap: Boolean = false,
        onFailed: suspend () -> Unit,
        onSuccess: suspend () -> Unit,
        onLoadMetadataFile: (Int) -> Unit,
        finally: suspend () -> Unit
    ) {
        if (updateMap) {
            updateMetadataHashMap()
        }

        withContext(Dispatchers.IO) {
            val downloadFileList = getDownloadFileNameListFromMetadataMap()

            downloadFileList.forEachIndexed { index, name ->
                launchIO {
                    val time = measureTimeMillis {
                        loadAndSaveFile(name)
                        onLoadMetadataFile.invoke(downloadFileList.size)
                    }
                    println("name = ${name} , time = ${time}")
                }
//                if(index + 1 % 10 == 0){
//                    delay(500)
//                }
            }
        }

        if (getDownloadFileNameListFromMetadataMap().isEmpty()) {
            onSuccess.invoke()
        } else {
            onFailed.invoke()
        }

        finally.invoke()
    }

    //更新元数据map
    private suspend fun updateMetadataHashMap() {

        println("updateMetadataHashMap = ${System.currentTimeMillis()}")

        val skipUpdateHashMap =
            System.currentTimeMillis() - latestCheckHashMapTime < HashMapCheckInterval

        if (skipUpdateHashMap) {
            return
        }

        val metaJson = buildRequest {
            url(HutaoEndpoints.metadata(LocaleNames.CHS, "$MetaFileName.json"))
        }.getAsText(applicationOkHttpClient)

        hashMap.clear()

//        try {
        hashMap.putAll(
            JSON.parse<Map<String, String>>(
                metaJson,
                getParameterizedType(Map::class.java, String::class.java, String::class.java)
            )
        )

        latestCheckHashMapTime = System.currentTimeMillis()
//        } catch (e: Exception) {
//            hashMap += "error" to "${e.message}"
//        }

        println(hashMap)
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

    private const val MetaFileName = "Meta"

    const val DirNameAvatar = "Avatar"

    const val FileNameAchievement = "Achievement"
    const val FileNameAchievementGoal = "AchievementGoal"
    const val FileNameAvatar = "Avatar"
    const val FileNameAvatarCurve = "AvatarCurve"
    const val FileNameAvatarPromote = "AvatarPromote"
    const val FileNameDisplayItem = "DisplayItem"
    const val FileNameGachaEvent = "GachaEvent"
    const val FileNameMaterial = "Material"
    const val FileNameMonster = "Monster"
    const val FileNameMonsterCurve = "MonsterCurve"
    const val FileNameReliquary = "Reliquary"
    const val FileNameReliquaryAffixWeight = "ReliquaryAffixWeight"
    const val FileNameReliquaryMainAffix = "ReliquaryMainAffix"
    const val FileNameReliquaryMainAffixLevel = "ReliquaryMainAffixLevel"
    const val FileNameReliquarySet = "ReliquarySet"
    const val FileNameReliquarySubAffix = "ReliquarySubAffix"
    const val FileNameTowerFloor = "TowerFloor"
    const val FileNameTowerLevel = "TowerLevel"
    const val FileNameTowerSchedule = "TowerSchedule"
    const val FileNameWeapon = "Weapon"
    const val FileNameWeaponCurve = "WeaponCurve"
    const val FileNameWeaponPromote = "WeaponPromote"

    val ZipFileNameAchievementIcon = "AchievementIcon"
    val ZipFileNameAvatarCard = "AvatarCard"
    val ZipFileNameAvatarIcon = "AvatarIcon"
    val ZipFileNameBg = "Bg"
    val ZipFileNameChapterIcon = "ChapterIcon"
    val ZipFileNameCostume = "Costume"
    val ZipFileNameEmotionIcon = "EmotionIcon"
    val ZipFileNameEquipIcon = "EquipIcon"
    val ZipFileNameGachaAvatarIcon = "GachaAvatarIcon"
    val ZipFileNameGachaAvatarImg = "GachaAvatarImg"
    val ZipFileNameGachaEquipIcon = "GachaEquipIcon"
    val ZipFileNameIconElement = "IconElement"
    val ZipFileNameItemIcon = "ItemIcon"
    val ZipFileNameLoadingPic = "LoadingPic"
    val ZipFileNameMonsterIcon = "MonsterIcon"
    val ZipFileNameMonsterSmallIcon = "MonsterSmallIcon"
    val ZipFileNameNameCardIcon = "NameCardIcon"
    val ZipFileNameNameCardPic = "NameCardPic"
    val ZipFileNameProperty = "Property"
    val ZipFileNameRelicIcon = "RelicIcon"
    val ZipFileNameSkill = "Skill"
    val ZipFileNameTalent = "Talent"

}