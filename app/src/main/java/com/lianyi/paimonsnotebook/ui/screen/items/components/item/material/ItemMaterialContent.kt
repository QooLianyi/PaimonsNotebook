package com.lianyi.paimonsnotebook.ui.screen.items.components.item.material

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.components.text.AutoSizeText
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.theme.White_40

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ItemMaterialContent(
    list: List<Material>
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .radius(4.dp)
            .background(White_40)
            .padding(8.dp, 0.dp, 8.dp, 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        list.forEach { material ->

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .radius(4.dp)
                    .size(60.dp, 80.dp)
                    .border(1.dp, Black_10, RoundedCornerShape(4.dp))
                    .clickable {

                    }
            ) {

                Image(
                    painter = painterResource(id = material.qualityResId),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .size(62.dp, 82.dp)
                        .align(Alignment.Center)
                )

                Column(modifier = Modifier.fillMaxSize()) {
                    NetworkImageForMetadata(
                        url = material.iconUrl,
                        modifier = Modifier
                            .size(60.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .background(White)
                            .padding(2.dp, 0.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AutoSizeText(
                            text = material.Name,
                            targetTextSize = 10.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            minTextSize = 10.sp
                        )
                    }
                }

            }
        }
    }
}