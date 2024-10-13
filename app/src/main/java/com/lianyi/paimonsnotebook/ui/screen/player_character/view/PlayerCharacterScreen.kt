package com.lianyi.paimonsnotebook.ui.screen.player_character.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.layout.column.TopSlotColumnLayout
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingLayout
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.account.components.dialog.UserGameRolesDialog
import com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.PlayerCharacterListCard
import com.lianyi.paimonsnotebook.ui.screen.player_character.viewmodel.PlayerCharacterScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class PlayerCharacterScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[PlayerCharacterScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme(this) {

                ContentLoadingLayout(loadingState = viewModel.loadingState,
                    errorContent = {
                        ErrorPlaceholder("加载失败,请稍后再试")
                    },
                    loadingContent = {
                        ContentLoadingPlaceholder()
                    }
                ) {
                    TopSlotColumnLayout(
                        topSlot = {
                            Row(
                                Modifier.padding(12.dp, 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier
                                        .radius(4.dp)
                                        .clickable {
                                            viewModel.showChooseGameRoleDialog()
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_user_outline),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )

                                    PrimaryText(
                                        text = viewModel.currentGameRole?.nickname ?: "旅行者"
                                    )
                                }
                            }
                        }
                    ) {
                        ContentSpacerLazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(12.dp, 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            statusBarPaddingEnabled = false
                        ) {

                            items(viewModel.characterList) { characterData ->
                                PlayerCharacterListCard(
                                    characterData = characterData,
                                    getAvatarDataById = viewModel::getAvatarDataById,
                                    getWeaponDataById = viewModel::getWeaponDataById,
                                    getWeaponFightPropertyFormatList = viewModel::getWeaponFightPropertyFormatList,
                                    onClick = viewModel::onClickListItem
                                )
                            }
                        }
                    }
                }

                if (viewModel.showGameRoleDialog) {
                    UserGameRolesDialog(
                        onButtonClick = viewModel::onUserGameRoleDialogButtonClick,
                        onDismissRequest = viewModel::onUserGameRoleDialogDismissRequest,
                        onSelectRole = viewModel::onSelectedGameRole
                    )
                }
            }
        }
    }
}