package com.lianyi.paimonsnotebook.ui.screen.items.components.item.avatar.content.skill

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.blur_card.widget.ItemSlider
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.text.RichText
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.compose.provider.NoRippleThemeProvides
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.AvatarSkillFormat
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.theme.White_40

@Composable
internal fun AvatarSkillContent(
    skillList: List<AvatarSkillFormat>,
    iconBackgroundColor: Color,
    enabledIconBorder: Boolean = false
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        skillList.forEach { skill ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .wrapContentHeight()
                    .background(White_40)
                    .clickable {
                        skill.toggleShow()
                    }
                    .padding(8.dp)
            ) {

                Row(modifier = Modifier.fillMaxWidth()) {

                    NetworkImage(
                        url = skill.iconUrl,
                        modifier = Modifier
                            .clip(CircleShape)
                            .apply {
                                if (enabledIconBorder) {
                                    this.border(3.dp, White, CircleShape)
                                }
                            }
                            .size(55.dp)
                            .background(iconBackgroundColor)
                            .padding(4.dp),
                        tint = White
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(skill.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(text = skill.subName, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                RichText(text = skill.description, fontSize = 14.sp)

                AnimatedVisibility(visible = skill.maxLevel != 1 && skill.show) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(.5.dp)
                                .background(Black_10)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        skill.proud.descriptions(skill.currentLevel.toInt()).forEach {
                            Row(
                                modifier = Modifier
                                    .radius(4.dp)
                                    .fillMaxWidth()
                                    .background(White_40)
                                    .padding(6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = it.first, fontSize = 12.sp)
                                Text(text = it.second, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        NoRippleThemeProvides {
                            ItemSlider(
                                value = skill.currentLevel,
                                range = (1f..skill.maxLevel.toFloat()),
                                onValueChange = skill::setLevel
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}