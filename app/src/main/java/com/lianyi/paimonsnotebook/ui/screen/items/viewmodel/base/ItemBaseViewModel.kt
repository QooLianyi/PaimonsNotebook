package com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.base

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.cultivate.data.CultivateEntityType
import com.lianyi.paimonsnotebook.common.database.cultivate.data.CultivateItemType
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateEntity
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItemMaterials
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItems
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateProject
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.event.calculate.BatchCalculatePromotionDetail
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.event.calculate.BatchComputeData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.event.calculate.CalculateClient
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.view.CultivateProjectOptionScreen
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.items.data.cultivate.CultivateConfigData
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType

/*
* item基本viewModel
* */
open class ItemBaseViewModel<T> : ViewModel() {

    var currentItem: T? by mutableStateOf(null)

    var itemAddedToCurrentCultivateProject by mutableStateOf(false)
    var compareItem: T? by mutableStateOf(null)
    var loadingState: LoadingState by mutableStateOf(LoadingState.Loading)
    var showLoadingDialog by mutableStateOf(false)

    val materialList = mutableStateListOf<Material>()

    var currentItemLevel by mutableIntStateOf(1)
    var selectCompareItem = false

    var showNoCultivateProjectNoticeDialog by mutableStateOf(false)
    var showItemConfigDialog by mutableStateOf(false)

    open val tabs: Array<String> = arrayOf()
    val cultivateConfigList = mutableStateListOf<CultivateConfigData>()

    val itemConfigDialogButtons: Array<String> = arrayOf("取消", "确定")

    private var calculateItemId: Int = 0

    /*
    * 当前养成计划缓存
    * 在点击添加按钮时设置
    * */
    private var currentSelectedCultivateProjectCache: CultivateProject? = null

    private var currentGameRoleCache: UserGameRoleData.Role? = null

    init {
        viewModelScope.launchIO {
            snapshotFlow { currentItem }.collect {
                if (it == null) {
                    itemAddedToCurrentCultivateProject = false
                    return@collect
                }

                itemAddedToCurrentCultivateProject =
                    getEntityHasAddedSelectedProject(getCurrentItemId())
            }
        }
    }

    private val projectDao by lazy {
        PaimonsNotebookDatabase.database.cultivateProjectDao
    }

    private val cultivateEntityDao by lazy {
        PaimonsNotebookDatabase.database.cultivateEntityDao
    }

    private val cultivateItemsDao by lazy {
        PaimonsNotebookDatabase.database.cultivateItemsDao
    }

    private val cultivateItemMaterialsDao by lazy {
        PaimonsNotebookDatabase.database.cultivateItemMaterialsDao
    }

    private val calculateClient by lazy {
        CalculateClient()
    }

    open fun init(intent: Intent) {
    }

    open fun updateMaterial() {
    }

    open fun onClickCompareItem() {
    }

    open fun onChangeItemLevel(value: Int, promoted: Boolean) {
        this.currentItemLevel = value
    }

    open fun onPromotedChange(promoted: Boolean) {
    }

    open fun toggleFilterContent() {
    }

    fun dismissNoCultivateProjectNoticeDialog() {
        showNoCultivateProjectNoticeDialog = false
    }

    fun addCurrentItemToCultivateProject() {
        val user = AccountHelper.selectedUserFlow.value
        val itemId = getCurrentItemId()

        if (user == null) {
            "必须设置一个默认用户才能使用此功能".warnNotify(false)
            return
        }

        if (user.userGameRoles.isEmpty()) {
            "当前用户没有找到游戏角色,请更换账号或稍后再试".warnNotify()
            return
        }

        currentGameRoleCache = user.getSelectedGameRole()

        if (currentGameRoleCache == null && user.userGameRoles.isNotEmpty()) {
            val role = user.userGameRoles.first()
            currentGameRoleCache = role

            "由于当前用户[${user.userInfo.nickname}]没有设置默认用户,已自动选择角色列表中的第一个角色[${role.nickname}](uid:${role.game_uid})作为本次请求的角色".warnNotify()
        }

        calculateItemId = itemId
        viewModelScope.launchIO {
            currentSelectedCultivateProjectCache = projectDao.getSelectedProject()

            if (currentSelectedCultivateProjectCache == null) {
                showNoCultivateProjectNoticeDialog = true
                return@launchIO
            }

            onShowItemConfigDialog()
        }
    }

    fun updateCurrentItemSelectedState(itemId: Int) {
        //判断当前item是否存在于当前养成计划中
        viewModelScope.launchIO {
            cultivateEntityDao.entityHasAddedSelectedProject(itemId = itemId)
        }
    }

    //这个方法需要子类重写
    open fun getCurrentItemId(): Int = -1

    fun onClickItemConfigDialogButton(index: Int) {
        if (index == 0) {
            showItemConfigDialogRequestDismiss()
            return
        }

        val role = this.currentGameRoleCache

        if (role == null) {
            "没有找到缓存的用户角色数据,请稍后再试".warnNotify()
            showItemConfigDialogRequestDismiss()
            return
        }

        val cultivateConfigDataMap = cultivateConfigList.groupBy {
            it.type
        }

        var avatar: CultivateConfigData? = null
        var weapon: BatchCalculatePromotionDetail.Weapon? = null
        var cultivateSkillList: List<BatchCalculatePromotionDetail.Skill>? = null

        val avatarList = cultivateConfigDataMap[CultivateItemType.Avatar]
        val weaponList = cultivateConfigDataMap[CultivateItemType.Weapon]
        val skillList = cultivateConfigDataMap[CultivateItemType.Skill]

        val items = mutableListOf<BatchCalculatePromotionDetail.Item>()

        if (!skillList.isNullOrEmpty()) {
            cultivateSkillList = skillList.map {
                BatchCalculatePromotionDetail.Skill(
                    id = it.id,
                    level_current = it.fromLevel,
                    level_target = it.toLevel
                )
            }
        }

        if (!avatarList.isNullOrEmpty() && cultivateSkillList != null) {
            avatar = avatarList.first()

            items += BatchCalculatePromotionDetail.Item(
                avatar_id = avatar.id,
                avatar_level_current = avatar.fromLevel,
                avatar_level_target = avatar.toLevel,
                element_attr_id = avatar.itemTypeId,
                skill_list = cultivateSkillList
            )
        }

        if (!weaponList.isNullOrEmpty()) {
            weapon = weaponList.first().let {
                BatchCalculatePromotionDetail.Weapon(
                    id = it.id,
                    level_current = it.fromLevel,
                    level_target = it.toLevel
                )
            }

            items += BatchCalculatePromotionDetail.Item(
                weapon = weapon
            )
        }

        val promotionDetail = BatchCalculatePromotionDetail(
            items = items,
            region = role.region,
            uid = role.game_uid
        )

        compute(promotionDetail)
        showItemConfigDialogRequestDismiss()
    }

    protected fun onMissingFile() {
        loadingState = LoadingState.Error
    }

    fun showItemConfigDialogRequestDismiss() {
        cultivateConfigList.clear()
        showItemConfigDialog = false
    }

    open fun onShowItemConfigDialog() {
        showItemConfigDialog = true
    }

    open fun getItemDataContent(item: T, type: ItemFilterType, isList: Boolean): String = ""

    open fun onClickItem(item: T) {
        this.currentItem = item
    }

    private fun compute(promotionDetail: BatchCalculatePromotionDetail) {
        val user = AccountHelper.selectedUserFlow.value

        if (user == null) {
            "必须设置一个默认用户才能使用此功能".warnNotify(false)
            return
        }

        viewModelScope.launchIO {
            val res = calculateClient.getCalculateBatchCompute(user, promotionDetail)

            if (!res.success) {
                "添加至养成计划失败:${res.message}".warnNotify(false)
                return@launchIO
            }

            try {
                saveAvatarComputeResult(res.data, promotionDetail)
                saveWeaponComputeResult(res.data, promotionDetail)
            } catch (e: Exception) {
                e.printStackTrace()

                "添加数据至数据库时出现错误:${e.message}".errorNotify()
            }
        }
    }

    private suspend fun saveAvatarComputeResult(
        result: BatchComputeData,
        promotionDetail: BatchCalculatePromotionDetail
    ) {
        if (result.items.isEmpty()) return

        /*
        * TODO 支持一次性添加多名角色
        * 获取第一个角色id不为空的数据
        * */
        val avatarPromotion = promotionDetail.items.takeFirstIf { it.avatar_id != null } ?: return
        val avatarId = avatarPromotion.avatar_id ?: return
        val projectId = currentSelectedCultivateProjectCache?.projectId ?: return

        val firstResult = result.items.first()
        /*
        * 全部材料类型,包含缺少的数量
        *
        * cultivateItemId = 负的角色id
        * */
        val overallMaterials = result.overall_consume.map {
            CultivateItemMaterials(
                itemId = it.id,
                cultivateItemId = -avatarId,
                projectId = projectId,
                count = it.num,
                lackCount = it.lack_num,
                status = if (it.lack_num > 0) {
                    0
                } else {
                    1
                }
            )
        }

        /*
        * 角色突破所需材料
        *
        * item id = 材料id
        * cultivateItemId = 角色id
        * */
        val avatarMaterials = firstResult.avatar_consume.map {
            CultivateItemMaterials(
                itemId = it.id,
                cultivateItemId = avatarId,
                projectId = projectId,
                count = it.num,
                lackCount = 0,
                status = 0
            )
        }

        /*
        * 技能突破所需材料
        *
        * 约束的id始终为当前角色的元素爆发技能id,如果后续接口返回每个技能的材料则改为每个技能的id
        * itemId = 材料id
        * cultivateItemId = 角色技能id
        * */
        val avatarSkillMaterials = firstResult.skills_consume.map { skillConsume ->
            skillConsume.consume_list.map { consume ->
                CultivateItemMaterials(
                    itemId = consume.id,
                    cultivateItemId = skillConsume.skill_info.id.toInt(),
                    projectId = projectId,
                    count = consume.num,
                    lackCount = 0,
                    status = 0
                )
            }
        }.flatten()

        if (overallMaterials.isEmpty() && avatarMaterials.isEmpty() && avatarSkillMaterials.isEmpty()) {
            error("当前角色养成配置没有所需的养成材料")
        }

        /*
        * 如果已经添加过了,需要更新数据库
        * 为避免不同等级产生不同数量的材料(lv1跟lv10所需要的材料数量是不同的)
        * 这里直接把原来的给删了,重新添加
        * 外键约束会自动删除引用的表的数据
        * */
        if (itemAddedToCurrentCultivateProject) {
            cultivateEntityDao.deleteEntityByItemIdAndProjectId(avatarId, projectId)
        }

        /*
        * 创建角色养成计划实体
        * */
        val avatarEntity = CultivateEntity(
            itemId = avatarId,
            projectId = projectId,
            type = CultivateEntityType.Avatar,
            status = 0
        )

        cultivateEntityDao.insert(avatarEntity)

        /*
        * 全部材料计算项(存储全部材料的数量与所需个数)
        * */
        val overallItems = CultivateItems(
            itemId = -avatarId,
            entityItemId = avatarId,
            projectId = projectId,
            itemType = CultivateItemType.Overall,
            fromLevel = 0,
            toLevel = 0,
            status = 0
        )


        /*
        * 角色养成计算项
        *
        * item id = 角色id
        * entity id = 角色id
        * item type = 角色,区分角色养成计算项与技能养成计算项
        * */
        val avatarItem = CultivateItems(
            itemId = avatarId,
            entityItemId = avatarId,
            projectId = projectId,
            itemType = CultivateItemType.Avatar,
            fromLevel = avatarPromotion.avatar_level_current ?: 0,
            toLevel = avatarPromotion.avatar_level_target ?: 0,
            status = 0
        )

        /*
        * 创建角色技能计算项
        *
        * item id = 技能id
        * entity id = 角色id
        * item type = 技能,区分角色养成计算项与技能养成计算项
        * */
        val skillList = avatarPromotion.skill_list ?: listOf()
        val avatarSkillItems = skillList.map {
            CultivateItems(
                itemId = it.id,
                entityItemId = avatarId,
                projectId = projectId,
                itemType = CultivateItemType.Skill,
                fromLevel = it.level_current,
                toLevel = it.level_target,
                status = 0
            )
        }

        cultivateItemsDao.insert(overallItems)
        cultivateItemsDao.insert(avatarItem)
        cultivateItemsDao.insert(avatarSkillItems)



        cultivateItemMaterialsDao.insert(overallMaterials)
        cultivateItemMaterialsDao.insert(avatarMaterials)
        cultivateItemMaterialsDao.insert(avatarSkillMaterials)

        onDataAddSuccess("角色", avatarId)
    }

    private suspend fun saveWeaponComputeResult(
        result: BatchComputeData,
        promotionDetail: BatchCalculatePromotionDetail
    ) {
        if (result.items.isEmpty()) return

        val weapon = promotionDetail.items.takeFirstIf { it.weapon != null }?.weapon ?: return
        val projectId = currentSelectedCultivateProjectCache?.projectId ?: return

        val weaponMaterials = result.overall_consume.map {
            CultivateItemMaterials(
                itemId = it.id,
                cultivateItemId = -weapon.id,
                projectId = projectId,
                count = it.num,
                lackCount = it.lack_num,
                status = if (it.lack_num <= 0) {
                    1
                } else {
                    0
                }
            )
        }


        //检查材料是否为空
        if (weaponMaterials.isEmpty()) {
            error("当前武器养成配置没有所需的养成材料")
        }

        if (itemAddedToCurrentCultivateProject) {
            cultivateEntityDao.deleteEntityByItemIdAndProjectId(weapon.id, projectId)
        }

        val weaponEntity = CultivateEntity(
            itemId = weapon.id,
            projectId = projectId,
            type = CultivateEntityType.Weapon,
            status = 0
        )

        cultivateEntityDao.insert(weaponEntity)

        val overallItem = CultivateItems(
            itemId = -weapon.id,
            entityItemId = weapon.id,
            projectId = projectId,
            itemType = CultivateItemType.Overall,
            fromLevel = 0,
            toLevel = 0,
            status = 0
        )

        val weaponItem = CultivateItems(
            itemId = weapon.id,
            entityItemId = weapon.id,
            projectId = projectId,
            itemType = CultivateItemType.Weapon,
            fromLevel = weapon.level_current,
            toLevel = weapon.level_target,
            status = 0
        )

        cultivateItemsDao.insert(overallItem)
        cultivateItemsDao.insert(weaponItem)


        cultivateItemMaterialsDao.insert(weaponMaterials)

        onDataAddSuccess("武器", weapon.id)
    }

    private suspend fun onDataAddSuccess(tag: String, id: Int) {
        "${tag}成功${if (itemAddedToCurrentCultivateProject) "更新" else "添加"}至养成计划[${currentSelectedCultivateProjectCache?.projectName}]中".notify()

        itemAddedToCurrentCultivateProject = getEntityHasAddedSelectedProject(id)
    }

    private suspend fun getEntityHasAddedSelectedProject(itemId: Int): Boolean {
        return cultivateEntityDao.entityHasAddedSelectedProject(itemId)
    }

    fun goCultivateProjectOptionScreen() {
        HomeHelper.goActivityByIntentNewTask {
            setComponentName(CultivateProjectOptionScreen::class.java)
            putExtra("add", true)
        }
        dismissNoCultivateProjectNoticeDialog()
    }

}