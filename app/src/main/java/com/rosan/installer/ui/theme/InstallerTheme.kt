package com.rosan.installer.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.kieronquinn.monetcompat.core.MonetCompat
import dev.kdrag0n.monet.theme.ColorSwatch

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inverseSurface = inverseSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceTint = primaryLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inverseSurface = inverseSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceTint = primaryDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InstallerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 添加动态颜色支持：仅对 Android 12 (API 31) 及以上版本有效，否则使用 MonetCompat 库
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme: ColorScheme = when {
        // 添加版本检查
        dynamicColor -> when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)

            else -> monetCompatColorScheme(darkTheme)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // New Status Bar Color Logic
    val view = LocalView.current
    // TODO not needed since targetSDK 35
    /*if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as ComponentActivity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }*/

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        DisableNavigationBarContrast(view)
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = MotionScheme.expressive(),
        typography = Typography,
        content = content
    )
}

/**
 * Disable navigation bar contrast enforcement.
 * This is useful for devices where the navigation bar color should not be enforced
 * to match the system theme, allowing for custom colors.
 */
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DisableNavigationBarContrast(view: View) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.isNavigationBarContrastEnforced = false
        }
    }
}

@Composable
fun monetCompatColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme()
): ColorScheme {
    // 工具函数 Utility function
    fun select(swatch: ColorSwatch, shade: Int): Color {
        val rgb = swatch[shade]?.toLinearSrgb()
        return if (rgb != null) Color(
            rgb.r.toFloat(),
            rgb.g.toFloat(),
            rgb.b.toFloat()
        ) else Color.Unspecified
    }

    val wallPaperColors = MonetCompat.getInstance().getMonetColors()

    val wallPaperLightColorScheme = lightColorScheme(
        primary              = select(wallPaperColors.accent1, 600),
        onPrimary            = select(wallPaperColors.accent1, 50),
        primaryContainer     = select(wallPaperColors.accent1, 100),
        onPrimaryContainer   = select(wallPaperColors.accent1, 900),

        secondary            = select(wallPaperColors.accent2, 2600),
        onSecondary          = select(wallPaperColors.accent2, 250),
        secondaryContainer   = select(wallPaperColors.accent2, 2100),
        onSecondaryContainer = select(wallPaperColors.accent2, 2900),

        tertiary             = select(wallPaperColors.accent3, 600),
        onTertiary           = select(wallPaperColors.accent3, 50),
        tertiaryContainer    = select(wallPaperColors.accent3, 100),
        onTertiaryContainer  = select(wallPaperColors.accent3, 900),

        background           = select(wallPaperColors.neutral1, 50),
        onBackground         = select(wallPaperColors.neutral1, 900),

        surface              = select(wallPaperColors.neutral1, 50),
        onSurface            = select(wallPaperColors.neutral1, 900),
        surfaceTint          = select(wallPaperColors.accent1, 600),
        surfaceVariant       = select(wallPaperColors.neutral2, 100),
        onSurfaceVariant     = select(wallPaperColors.neutral2, 700),

        outline              = select(wallPaperColors.neutral2, 500),
        outlineVariant       = select(wallPaperColors.neutral2, 200),

        inversePrimary       = select(wallPaperColors.accent1, 200),
        inverseSurface       = select(wallPaperColors.neutral1, 800),
        inverseOnSurface     = select(wallPaperColors.neutral1, 100),

        scrim                = select(wallPaperColors.neutral1, 900).copy(alpha = 0.32f),

        error                = errorLight,
        onError              = onErrorLight,
        errorContainer       = errorContainerLight,
        onErrorContainer     = onErrorContainerLight
    )

    val wallPaperDarkColorScheme = darkColorScheme(
        primary              = select(wallPaperColors.accent1, 200),
        onPrimary            = select(wallPaperColors.accent1, 800),
        primaryContainer     = select(wallPaperColors.accent1, 700),
        onPrimaryContainer   = select(wallPaperColors.accent1, 100),

        secondary            = select(wallPaperColors.accent2, 2200),
        onSecondary          = select(wallPaperColors.accent2, 2800),
        secondaryContainer   = select(wallPaperColors.accent2, 2700),
        onSecondaryContainer = select(wallPaperColors.accent2, 2100),

        tertiary             = select(wallPaperColors.accent3, 200),
        onTertiary           = select(wallPaperColors.accent3, 800),
        tertiaryContainer    = select(wallPaperColors.accent3, 700),
        onTertiaryContainer  = select(wallPaperColors.accent3, 100),

        background           = select(wallPaperColors.neutral1, 900),
        onBackground         = select(wallPaperColors.neutral1, 100),

        surface              = select(wallPaperColors.neutral1, 900),
        onSurface            = select(wallPaperColors.neutral1, 100),
        surfaceTint          = select(wallPaperColors.accent1, 200),
        surfaceVariant       = select(wallPaperColors.neutral2, 700),
        onSurfaceVariant     = select(wallPaperColors.neutral2, 200),

        outline              = select(wallPaperColors.neutral2, 400),
        outlineVariant       = select(wallPaperColors.neutral2, 600),
        
        inversePrimary       = select(wallPaperColors.accent1, 600),
        inverseSurface       = select(wallPaperColors.neutral1, 50),
        inverseOnSurface     = select(wallPaperColors.neutral1, 900),
        scrim                = select(wallPaperColors.neutral1, 0).copy(alpha = 0.32f),

        error                = errorDark,
        onError              = onErrorDark,
        errorContainer       = errorContainerDark,
        onErrorContainer     = onErrorContainerDark
    )

    return if (darkTheme) wallPaperDarkColorScheme else wallPaperLightColorScheme
}
