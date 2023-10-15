package com.lianyi.paimonsnotebook.ui.theme

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lianyi.paimonsnotebook.common.components.components.PaimonsNotebookNotificationComponents
import com.lianyi.paimonsnotebook.common.components.components.SlideExitBox
import com.lianyi.paimonsnotebook.common.extension.context.findActivity

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = colorPrimary,
    primaryVariant = colorPrimaryDark,
    secondary = colorAccent

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun PaimonsNotebookTheme(
    activity: Activity? = null,
    hideStatusBar:Boolean = false,
    hideNavigationBar:Boolean = false,
    lightStatusBar:Boolean = false,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        SideEffect {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).apply {
                //设置状态栏与底部导航栏的显示模式(Light/Night) true为黑色
                isAppearanceLightStatusBars = !lightStatusBar
                isAppearanceLightNavigationBars = true

                //隐藏状态栏
                if(hideStatusBar){
                    hide(WindowInsetsCompat.Type.statusBars())
                }

                //隐藏导航栏
                if(hideNavigationBar){
                    hide(WindowInsetsCompat.Type.navigationBars())
                }

                if(hideStatusBar || hideNavigationBar){
                    systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        }
    }

    //动态设置Density
    val context = LocalContext.current
    val fontScale = LocalDensity.current.fontScale
    val displayMetrics = context.resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = widthPixels / 360f,
                    fontScale = fontScale
                )
            ) {
                PaimonsNotebookNotificationComponents()
                if (activity != null) {
                    SlideExitBox(activity = activity, content = content)
                } else {
                    content()
                }
            }
        }
    )
}

