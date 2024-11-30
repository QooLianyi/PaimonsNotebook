package com.lianyi.paimonsnotebook.ui.screen.player_character.viewmodel

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.UserAndUid
import com.lianyi.paimonsnotebook.common.data.popup.IconTitleInformationPopupWindowData
import com.lianyi.paimonsnotebook.common.data.popup.PopupWindowPositionProvider
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.GameRecordClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterDetailData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterListData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.AvatarService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.ReliquaryService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.WeaponService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.RelicIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ReliquaryType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary.ReliquaryData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemSearchOptionHelper
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.base.ItemBaseViewModel
import com.lianyi.paimonsnotebook.ui.screen.player_character.view.PlayerCharacterDetailScreen

class PlayerCharacterDetailScreenViewModel : ItemBaseViewModel<AvatarData>() {

    private lateinit var userAndUid: UserAndUid
    private lateinit var characterList: List<CharacterListData.CharacterData>
    private lateinit var characterMap: Map<Int, CharacterListData.CharacterData>

    var currentIndex by mutableIntStateOf(0)

    var currentCharacterDetail by mutableStateOf<CharacterDetailData.DetailItem?>(null)
        private set

    private val avatarService by lazy {
        AvatarService(onMissingFile = this::onMissingFile)
    }

    private val weaponService by lazy {
        WeaponService(onMissingFile = this::onMissingFile)
    }

    private val reliquaryService by lazy {
        ReliquaryService(onMissingFile = this::onMissingFile)
    }

    private val gameRecordClient by lazy {
        GameRecordClient()
    }

    val itemFilterViewModel by lazy {
        ItemSearchOptionHelper.getPlayerCharacterItemFilterVieModel(
            list = characterList.mapNotNull {
                avatarService.avatarMap[it.id]
            },
            getCharacterListDataById = this::getCharacterListDataById
        )
    }

    var showReliquarySetInfoPopupWindow by mutableStateOf(false)
        private set

    lateinit var reliquarySetInfoDataSet: IconTitleInformationPopupWindowData

    lateinit var reliquarySetInfoPopupWindowProvider: PopupWindowPositionProvider

    override val tabs = arrayOf<String>()

    fun init(intent: Intent?, onInitFail: () -> Unit) {
        if (intent == null) {
            "参数为空".errorNotify()
            onInitFail.invoke()
            return
        }

        val characterListJson =
            intent.getStringExtra(PlayerCharacterDetailScreen.PARAM_CHARACTER_LIST_JSON)
                ?: JSON.EMPTY_LIST

        val selectedCharacterId =
            intent.getIntExtra(PlayerCharacterDetailScreen.PARAM_SELECTED_CHARACTER_ID, -1)

        val playerUidJson =
            intent.getStringExtra(PlayerCharacterDetailScreen.PARAM_USER_AND_UID_JSON)
                ?: JSON.EMPTY_OBJ

        if (characterListJson == JSON.EMPTY_LIST || selectedCharacterId == -1 || playerUidJson == JSON.EMPTY_OBJ) {
            "缺少参数".errorNotify()
            onInitFail.invoke()
            return
        }

        characterList = JSON.parse<List<CharacterListData.CharacterData>>(
            characterListJson,
            getParameterizedType(List::class.java, CharacterListData.CharacterData::class.java)
        )

        characterMap = characterList.associateBy { it.id }

        val tempUserAndUid: UserAndUid? = JSON.parse(playerUidJson)

        if (characterList.isEmpty() || tempUserAndUid == null) {
            "角色列表为空或未选择角色".errorNotify()
            onInitFail.invoke()
            return
        }

        userAndUid = tempUserAndUid

        viewModelScope.launchIO {
            initService()
            setCharacterDetail(selectedCharacterId)
        }
    }

    private fun initService() {
        avatarService.avatarList
        weaponService.weaponList
        reliquaryService.reliquaryFullMap
    }

    private suspend fun setCharacterDetail(characterId: Int) {
        val res = gameRecordClient.getCharacterDetail(userAndUid, listOf(characterId))

        if (!res.success || res.data.list.isEmpty()) {
            "获取角色数据失败:${res.message}"
            loadingState = LoadingState.Error
            return
        }

        val characterDetail = res.data.list.first()
        currentCharacterDetail = characterDetail

        setCurrentItem(characterDetail.base.id)
    }

    private fun setCurrentItem(characterId: Int) {
        val avatar = avatarService.avatarMap[characterId]

        if (avatar == null) {
            loadingState = LoadingState.Error
            "元数据中未匹配到当前角色,请尝试更新元数据后再次尝试".errorNotify()
            return
        }

        currentItem = avatar

        loadingState = LoadingState.Success
    }

    fun onClickRelicIcon(reliquaryData: ReliquaryData, size: IntSize, offset: Offset) {
        showReliquarySetInfoPopupWindow = true

        reliquarySetInfoPopupWindowProvider = PopupWindowPositionProvider(
            contentOffset = offset,
            itemSize = size,
            itemSpace = 12.dp
        )

        val setData = reliquaryService.reliquarySetMap[reliquaryData.SetId]

        reliquarySetInfoDataSet = IconTitleInformationPopupWindowData(
            title = reliquaryData.Name,
            subTitle = ReliquaryType.getReliquaryNameByType(reliquaryData.EquipType),
            iconUrl = RelicIconConverter.iconNameToUrl(reliquaryData.Icon),
            content = StringBuilder().apply {
                append(reliquaryData.Description)

                if (setData != null && setData.Descriptions.size == setData.NeedNumber.size) {
                    append("\n")
                    setData.NeedNumber.forEachIndexed { index, num ->
                        append("${num}件套:\n")
                        append(setData.Descriptions[index])
                        append("\n")
                    }
                }

            }.toString(),
            width = 240.dp,
            maxLine = 30
        )
    }

    fun onPopupWindowDismissRequest() {
        this.showReliquarySetInfoPopupWindow = false
    }

    fun getCharacterListDataById(id: Int) = characterMap[id]

    fun getAvatarDataById(id: Int) = avatarService.avatarMap[id]

    fun getWeaponDataById(id: Int) = weaponService.weaponMap[id]

    fun getRelicById(id: Int) = reliquaryService.reliquaryFullMap[id]

    fun getWeaponFightPropertyFormatList(
        weaponData: WeaponData,
        level: Int,
        promoted: Boolean
    ) = weaponService.getFightPropertyFormatList(
        weapon = weaponData,
        level = level,
        promoted = promoted
    )

    override fun onClickItem(item: AvatarData) {
        super.onClickItem(item)

        viewModelScope.launchIO {
            setCharacterDetail(item.id)
        }
    }

    override fun toggleFilterContent() {
        itemFilterViewModel.toggleFilterContent()
    }

    override fun getItemDataContent(
        item: AvatarData,
        type: ItemFilterType,
        isList: Boolean
    ): String {
        val simpleCharacterListData = getCharacterListDataById(item.id) ?: return ""

        return when (type) {
            ItemFilterType.Level -> "Lv.${simpleCharacterListData.level}"
            ItemFilterType.Fetter -> "好感${simpleCharacterListData.fetter}"
            ItemFilterType.ActiveConstellation -> "命座${simpleCharacterListData.actived_constellation_num}"
            else -> item.name
        }
    }

    override fun getCurrentItemId(): Int = tabs.size
}
