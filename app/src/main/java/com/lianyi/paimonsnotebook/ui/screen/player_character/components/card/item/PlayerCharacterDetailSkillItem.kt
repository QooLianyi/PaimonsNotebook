package com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ElementType
import com.lianyi.paimonsnotebook.ui.theme.Black_80
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun PlayerCharacterDetailSkillItem(
    iconUrl: String,
    index: Int,
    elementTypeName: String,
    activateCount: Int,
    level: Int,
    clickable: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            NetworkImageForMetadata(
                url = iconUrl,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(34.dp)
                    .background(ElementType.getElementColorByName(elementTypeName))
                    .then(
                        if (clickable) {
                            Modifier.clickable {
                                onClick.invoke()
                            }
                        } else Modifier
                    )
                    .padding(1.dp),
                tint = White
            )

            if (activateCount != -1 && activateCount < index + 1) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_lock),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp),
                    tint = Black_80
                )
            }
        }

        if (level != -1) {
            PrimaryText(text = "$level")
        }
    }
}