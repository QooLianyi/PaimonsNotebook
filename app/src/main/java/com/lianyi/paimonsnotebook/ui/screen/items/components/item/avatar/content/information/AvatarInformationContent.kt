package com.lianyi.paimonsnotebook.ui.screen.items.components.item.avatar.content.information

import androidx.compose.foundation.background
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.layout.FoldContent
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.text.RichText
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.AvatarCardConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.AvatarIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.AssociationType
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.White_40

@Composable
internal fun AvatarInformationContent(
    avatar: AvatarData
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ItemIconCard(url = avatar.iconUrl, star = avatar.starCount, 4.dp)

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${avatar.name}\t${avatar.fetterInfo.Title}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = avatar.description,
                    fontSize = 12.sp
                )
            }

        }

        val list = remember(avatar) {
            val fetterInfo = avatar.fetterInfo
            listOf(
                "命之座" to fetterInfo.ConstellationBefore,
                "生日" to "${fetterInfo.BirthMonth} 月 ${fetterInfo.BirthDay} 日",
                "所属" to "${AssociationType.getAssociationNameByType(fetterInfo.Association)} ${fetterInfo.Native}",
                "汉语 CV" to fetterInfo.CvChinese,
                "日语 CV" to fetterInfo.CvJapanese,
                "英语 CV" to fetterInfo.CvEnglish,
                "韩语 CV" to fetterInfo.CvKorean,
                "实装日期" to avatar.beginTimeFormat
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .radius(4.dp)
                .background(White_40)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            list.forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = pair.first, fontSize = 14.sp)

                        Text(
                            text = pair.second,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }


        var costumeOpen by remember {
            mutableStateOf(false)
        }


        FoldContent(
            open = costumeOpen, titleSlot = {
                Text(text = "衣装", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }, modifier = Modifier
                .radius(4.dp)
                .background(White_40)
                .clickable {
                    costumeOpen = !costumeOpen
                }
                .padding(6.dp)
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp)
                    .height(.5.dp)
                    .background(Black_10)
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                avatar.costumes.forEach {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        NetworkImage(
                            url = if (it.IsDefault) {
                                AvatarCardConverter.iconNameToUrl("UI_AvatarIcon_Costume_Card")
                            } else {
                                AvatarIconConverter.iconNameToUrl(it.FrontIcon)
                            },
                            modifier = Modifier
                                .size(60.dp)
                                .radius(4.dp)
                                .background(Color(0XFFF2ECE3))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = it.Name, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = it.Description, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        val contentList = remember(avatar) {
            listOf(
                "资料" to avatar.fetterInfo.Fetters.map {
                    it.Title to it.Context
                },
                "故事" to avatar.fetterInfo.FetterStories.map {
                    it.Title to it.Context
                }
            )
        }

        contentList.forEach {
            var showContent by remember {
                mutableStateOf(false)
            }
            FoldContent(
                open = showContent, titleSlot = {
                    Text(text = it.first, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }, modifier = Modifier
                    .radius(4.dp)
                    .background(White_40)
                    .clickable {
                        showContent = !showContent
                    }
                    .padding(6.dp)
            ) {

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 6.dp)
                        .height(.5.dp)
                        .background(Black_10)
                )

                it.second.forEachIndexed { index, pair ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = pair.first,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        RichText(text = pair.second, fontSize = 12.sp)
                    }

                    if (avatar.fetterInfo.Fetters.size - 1 != index) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 6.dp)
                                .height(.5.dp)
                                .background(Black_10)
                        )
                    }
                }
            }
        }
    }
}