package com.lianyi.paimonsnotebook.ui.screen.items.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.core.ui.components.text.InfoText
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.RelicIconConverter
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen.ReliquaryScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class ReliquaryScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[ReliquaryScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme(this) {

                ContentSpacerLazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor),
                    contentPadding = PaddingValues(12.dp, 6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    items(viewModel.reliquarySetList) { reliquarySet ->
                        Column(
                            modifier = Modifier
                                .radius(6.dp)
                                .background(CardBackGroundColor)
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {

                            Row {
                                ItemIconCard(
                                    url = RelicIconConverter.iconNameToUrl(reliquarySet.Icon),
                                    star = viewModel.getReliquaryStar(reliquarySet.SetId),
                                    size = 36.dp,
                                    borderRadius = 6.dp
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                PrimaryText(text = reliquarySet.Name, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            //套装效果
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                reliquarySet.NeedNumber.forEachIndexed { index, setNum ->
                                    //判断是否超出描述的集合长度
                                    if (index >= reliquarySet.Descriptions.size) {
                                        return@items
                                    }
                                    Column {
                                        PrimaryText(text = "${setNum}件套", fontSize = 12.sp)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        InfoText(text = reliquarySet.Descriptions[index])
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}