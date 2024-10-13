package com.lianyi.paimonsnotebook.ui.screen.player_character.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.extension.list.split
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterDetailData
import com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.item.PlayerCharacterPropertyItem
import com.lianyi.paimonsnotebook.ui.theme.White_40

@Composable
fun PlayerCharacterPropertyCard(
    propertyList: List<CharacterDetailData.Property>,
    extraPropertyList: List<CharacterDetailData.Property>,
) {
    var showExtra by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .radius(6.dp)
            .fillMaxWidth()
            .background(White_40)
            .clickable {
                showExtra = !showExtra
            }
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (showExtra) {
            extraPropertyList
        } else {
            propertyList
        }.split(2).forEach { list ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                list.forEach { data ->
                    PlayerCharacterPropertyItem(modifier = Modifier.weight(1f), data = data)
                }
            }
        }
    }
}