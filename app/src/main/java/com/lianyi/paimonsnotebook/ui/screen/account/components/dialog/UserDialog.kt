package com.lianyi.paimonsnotebook.ui.screen.account.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Info

@Composable
fun UserDialog(
    lastUseFrom: String = "",
    onButtonClick: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onClickUser: (user: User) -> Unit,
){
    val stateList = remember {
        mutableStateListOf<User>().apply {
            addAll(AccountHelper.userListFlow.value)
        }
    }

    LaunchedEffect(Unit) {
        if (stateList.isEmpty()) {
            AccountHelper.userListFlow.collect {
                stateList.addAll(it)
            }
        }
    }

    LazyColumnDialog(
        title = "选择用户",
        titleSpacer = 24.dp,
        onClickButton = onButtonClick,
        onDismissRequest = onDismissRequest
    ) {
        items(stateList) { user ->
            Row(
                modifier = Modifier
                    .radius(4.dp)
                    .fillMaxWidth()
                    .clickable {
                        onClickUser.invoke(user)
                    }
                    .padding(0.dp, 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NetworkImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp),
                    diskCache = DiskCache(
                        url = user.userInfo.avatar_url,
                        lastUseFrom = lastUseFrom
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${user.userInfo.nickname} | ${user.userInfo.uid}",
                    fontSize = 20.sp, color = Info
                )
            }
        }
    }
}