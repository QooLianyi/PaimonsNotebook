package com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.item

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.theme.Primary_2
import com.lianyi.paimonsnotebook.ui.theme.White
import java.io.File

@Composable
fun ImageItem(
    imageCacheFile: File?,
    selected: Boolean,
    isSelectionMode: Boolean,
    modifier: Modifier
) {
    val scaleAnim = animateFloatAsState(targetValue = if (selected) .8f else 1f, label = "")

    Box {
        Image(
            painter = rememberAsyncImagePainter(
                model = imageCacheFile
            ),
            contentDescription = null,
            modifier = modifier
                .aspectRatio(1f)
                .scale(scaleAnim.value),
            contentScale = ContentScale.Crop
        )

        if (isSelectionMode) {
            val (resId, color) = if (selected) {
                R.drawable.ic_checkmark_circle_full to Primary_2
            } else {
                R.drawable.ic_ring to White
            }

            Icon(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .size(26.dp),
                tint = color
            )
        }
    }
}