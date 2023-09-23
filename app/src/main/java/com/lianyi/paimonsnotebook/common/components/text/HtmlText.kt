package com.lianyi.paimonsnotebook.common.components.text

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import com.lianyi.paimonsnotebook.common.util.html.setTextLinkOpenByWebView
import com.lianyi.paimonsnotebook.ui.theme.Black
import kotlinx.coroutines.launch

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    htmlText:String,
    fontSize:Float = 6f,
    textColor: Color = Black
) {
    val density = LocalDensity.current.density
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    AndroidView(factory = {
        TextView(it).apply {
            textSize = fontSize * density + .5f
            setTextColor(textColor.toArgb())
            movementMethod = LinkMovementMethod.getInstance()
        }
    }, modifier = modifier) { textView->
        setTextLinkOpenByWebView(context,htmlText){
            scope.launch {
                textView.text = it
            }
        }
    }
}