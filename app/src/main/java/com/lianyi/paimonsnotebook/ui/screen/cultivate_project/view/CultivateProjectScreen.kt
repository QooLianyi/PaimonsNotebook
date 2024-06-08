package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.ConfirmDialog
import com.lianyi.paimonsnotebook.common.components.layout.column.TabBarColumnLayout
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingLayout
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.EmptyPagePlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.components.popup.IconTitleInformationPopupWindow
import com.lianyi.paimonsnotebook.common.components.widget.button.TitleAndDescriptionActionButton
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.database.cultivate.data.CultivateEntityType
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.items.CultivateAvatarCard
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.items.CultivateItemWeaponCard
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.page.CultivateProjectOverallPage
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.viewmodel.CultivateProjectScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class CultivateProjectScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[CultivateProjectScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme(this) {
                ContentLoadingLayout(
                    loadingState = viewModel.loadingState,
                    loadingContent = {
                        ContentLoadingPlaceholder()
                    },
                    emptyContent = {
                        EmptyPagePlaceholder(title = "当前没有养成计划") {
                            TitleAndDescriptionActionButton(
                                title = "前往养成计划设置页",
                                description = "前往养成计划设置界面添加一个养成计划",
                                modifier = Modifier
                                    .padding(16.dp, 0.dp)
                                    .fillMaxWidth(),
                                onClick = viewModel::goOptionScreen
                            )
                        }
                    },
                    errorContent = {
                        ErrorPlaceholder()
                    },
                    defaultContent = {}
                ) {
                    TabBarColumnLayout(
                        tabs = viewModel.tabs,
                        onTabBarSelect = viewModel::onSelectTabBar,
                        tabsSpace = 12.dp,
                        topSlot = {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                horizontalArrangement = Arrangement.End,
                            ) {
                                Crossfade(
                                    targetState = viewModel.currentPageIndex == 1,
                                    label = ""
                                ) {
                                    if (it) {
                                        Icon(
                                            painter = painterResource(id = if (viewModel.showOverallPageGridList) R.drawable.ic_list_square else R.drawable.ic_grid_1),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(2.dp)
                                                .radius(3.dp)
                                                .size(32.dp)
                                                .clickable {
                                                    viewModel.switchShowOverallPageGridList()
                                                }
                                                .padding(4.dp)
                                        )
                                    }
                                }

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_navigation),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .radius(3.dp)
                                        .size(32.dp)
                                        .clickable {
                                            viewModel.goOptionScreen()
                                        }
                                        .padding(4.dp)
                                )
                            }
                        }
                    ) {

                        Crossfade(targetState = viewModel.currentPageIndex, label = "") {
                            when (it) {
                                1 -> CultivateProjectOverallPage(
                                    showGridLayout = viewModel.showOverallPageGridList,
                                    overallMaterialBaseInfoGroupList = viewModel.overallMaterialBaseInfoGroupList,
                                    overallMaterialBaseInfoGroupListFlatten = viewModel.overallMaterialBaseInfoGroupListFlatten,
                                    onShowMaterialInfoPopupDialog = viewModel::onShowMaterialInfoPopupDialog,
                                    onShowEntityInfoPopupDialog = viewModel::onShowEntityInfoPopupDialog,
                                    getOverallEntityBaseInfoListByMaterialId = viewModel::getOverallEntityBaseInfoListByMaterialId
                                )

                                else -> CultivateProjectPage()
                            }
                        }
                    }
                }

                if (viewModel.showDeleteEntityConfirmDialog) {
                    ConfirmDialog(
                        title = "删除养成项",
                        content = viewModel.deleteEntityConfirmDialogContent,
                        onConfirm = viewModel::deleteCurrentSelectedEntity,
                        onCancel = viewModel::entityConfirmDialogDismissRequest
                    )
                }

                if (viewModel.showPopupWindow) {
                    IconTitleInformationPopupWindow(
                        data = viewModel.showPopupWindowInfo,
                        popupProvider = viewModel.popupWindowProvider,
                        onDismissRequest = viewModel::onPopupWindowDismissRequest
                    )
                }
            }
        }
    }

    @Composable
    private fun CultivateProjectPage() {
        ContentSpacerLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            statusBarPaddingEnabled = false
        ) {
            viewModel.entityCultivateItemsPairList.forEach { (entity, items) ->
                when (entity.type) {

                    CultivateEntityType.Weapon -> {
                        val weaponData =
                            viewModel.getWeaponData(entity.itemId) ?: return@forEach

                        val cultivateItemsMap =
                            items.associateBy { it.itemId }

                        item(
                            key = "${entity.projectId}_${entity.itemId}_${entity.addTime}"
                        ) {
                            CultivateItemWeaponCard(
                                weaponData = weaponData,
                                entity = entity,
                                cultivateItemsMap = cultivateItemsMap,
                                getMaterialsByCultivateItemId = viewModel::getMaterialListByCultivateItemId,
                                getMaterialInfo = viewModel::getMaterialData,
                                onClickDelete = viewModel::onClickCultivateCardDelete,
                                onEmitMaterialItemUpdateQueue = viewModel::onEmitMaterialItemUpdateQueue,
                                onShowMaterialInfoPopupDialog = viewModel::onShowMaterialInfoPopupDialog
                            )
                        }
                    }

                    CultivateEntityType.Avatar -> {
                        val avatarData =
                            viewModel.getAvatarData(entity.itemId) ?: return@forEach

                        //角色养成id map
                        val avatarCultivateMap =
                            viewModel.getAvatarCultivateIdMap(avatarData)

                        //计算项map
                        val cultivateItemsMap =
                            items.associateBy { it.itemId }

                        item(
                            key = "${entity.projectId}_${entity.itemId}_${entity.addTime}"
                        ) {
                            CultivateAvatarCard(
                                avatarData = avatarData,
                                entity = entity,
                                cultivateItems = items,
                                avatarCultivateMap = avatarCultivateMap,
                                cultivateItemsMap = cultivateItemsMap,
                                getCultivateMaterialsListByCultivateItemId = viewModel::getMaterialListByCultivateItemId,
                                getMaterialInfo = viewModel::getMaterialData,
                                onClickDelete = viewModel::onClickCultivateCardDelete,
                                onEmitMaterialItemUpdateQueue = viewModel::onEmitMaterialItemUpdateQueue,
                                onShowMaterialInfoPopupDialog = viewModel::onShowMaterialInfoPopupDialog
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}