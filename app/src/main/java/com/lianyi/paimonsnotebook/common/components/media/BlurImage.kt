package com.lianyi.paimonsnotebook.common.components.media

import android.graphics.Bitmap
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
fun BlurImage(
    modifier: Modifier = Modifier,
    bitmap:Bitmap,
    blurRadius:Float
) {

    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S_V2){
        val renderScript = RenderScript.create(LocalContext.current)
        val bitmapAllocation = Allocation.createFromBitmap(renderScript,bitmap)
        ScriptIntrinsicBlur.create(renderScript,bitmapAllocation.element).apply {
            setRadius(blurRadius)
            setInput(bitmapAllocation)
            forEach(bitmapAllocation)
        }

        bitmapAllocation.copyTo(bitmap)
        renderScript.destroy()


        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "")
    }

}