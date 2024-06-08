package com.lianyi.paimonsnotebook.ui.screen.items.components.cultivate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.screen.items.data.cultivate.CultivateConfigData
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5WeaponColor
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun WeaponCultivateConfigCard(
    weapon: WeaponData,
    list: List<CultivateConfigData>
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CultivateConfigHeaderSlot(
            iconUrl = weapon.iconUrl,
            name = weapon.name,
            starCount = weapon.rankLevel,
            tagSlot = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InformationItem(
                        iconUrl = weapon.weaponIconUrl,
                        text = weapon.weaponTypeName,
                        textColor = White,
                        backgroundColor = GachaStar5WeaponColor,
                        textSize = 12.sp,
                        paddingValues = PaddingValues(6.dp, 2.5.dp)
                    )
                }
            }
        )

        list.forEach { config ->
            CultivateConfigListItem(config)
        }
    }
}