package com.lianyi.paimonsnotebook.common.components.media

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

@Composable
fun NetworkImageForMetadata(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    tint: Color? = null,
    alignment: Alignment = Alignment.Center,
) {
    NetworkImage(
        url = url,
        modifier = modifier,
        contentScale = contentScale,
        headers = HutaoEndpoints.Headers,
        tint = tint,
        alignment = alignment
    )
}