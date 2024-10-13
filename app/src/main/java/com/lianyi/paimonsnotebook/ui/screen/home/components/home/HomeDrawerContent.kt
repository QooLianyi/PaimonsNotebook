package com.lianyi.paimonsnotebook.ui.screen.home.components.home

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.account.view.AccountManagerScreen
import com.lianyi.paimonsnotebook.ui.screen.home.components.card.account.AccountInfoCard
import com.lianyi.paimonsnotebook.ui.screen.home.components.menu.SideBarMenuList
import com.lianyi.paimonsnotebook.ui.screen.home.data.ModalItemData
import com.lianyi.paimonsnotebook.ui.screen.setting.view.SettingsScreen
import com.lianyi.paimonsnotebook.ui.theme.Black

@Composable
fun HomeDrawerContent(
    selectedUser: User?,
    modalItems: List<ModalItemData>,
    onScanQRCode: () -> Unit,
    goSignWeb: () -> Unit,
    functionNavigate: (Class<out Activity>) -> Unit
) {
    Box {
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 44.dp)
        ) {

            AccountInfoCard(selectedUser, onAddAccount = {
                functionNavigate.invoke(AccountManagerScreen::class.java)
            })

            SideBarMenuList(list = modalItems) {
                functionNavigate.invoke(it.target)
            }

            NavigationBarPaddingSpacer()
        }


        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null,
                    modifier = Modifier
                        .radius(2.dp)
                        .size(36.dp)
                        .clickable {
                            functionNavigate.invoke(SettingsScreen::class.java)
                        }
                        .padding(4.dp), tint = Black
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_scan),
                    contentDescription = null,
                    modifier = Modifier
                        .radius(2.dp)
                        .size(36.dp)
                        .clickable {
                            onScanQRCode.invoke()
                        }
                        .padding(6.dp), tint = Black
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_gift),
                    contentDescription = null,
                    modifier = Modifier
                        .radius(2.dp)
                        .size(36.dp)
                        .clickable {
                            goSignWeb.invoke()
                        }
                        .padding(4.dp), tint = Black
                )
            }

            NavigationBarPaddingSpacer()
        }
    }
}