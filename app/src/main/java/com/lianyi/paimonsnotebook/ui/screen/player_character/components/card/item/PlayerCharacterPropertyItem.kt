package com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterDetailData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.FightProperty
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White_40

@Composable
fun PlayerCharacterPropertyItem(
    modifier: Modifier,
    data: CharacterDetailData.Property
) {
    val name = FightProperty.getNameByProperty(data.property_type)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = FightProperty.getIconResourceByProperty(data.property_type)),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(20.dp)
                .background(White_40)
                .padding(if (name.contains("元素伤害") || name.contains("元素抗性") ) 1.dp else 3.dp),
            tint = Black
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )

        Text(
            text = data.final,
            fontSize = 14.sp
        )
    }
}