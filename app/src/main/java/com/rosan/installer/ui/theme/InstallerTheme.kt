package com.rosan.installer.ui.theme

import android.os.Build
import androidx.annotation.IntRange
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.kieronquinn.monetcompat.core.MonetCompat
import com.kieronquinn.monetcompat.extensions.toArgb
import top.yukonga.miuix.kmp.theme.MiuixTheme

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
fun InstallerMaterialExpressiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor -> when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            else -> monetCompatColorScheme(darkTheme)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // TODO not needed since targetSDK 35
    /*if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as ComponentActivity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }*/

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = MotionScheme.expressive(),
        typography = Typography,
        content = content
    )
}

@Composable
fun InstallerMiuixTheme(
    colorMode: Int = 0,
    content: @Composable () -> Unit
) {
    val darkTheme = isSystemInDarkTheme()
    return MiuixTheme(
        colors = when (colorMode) {
            1 -> top.yukonga.miuix.kmp.theme.lightColorScheme()
            2 -> top.yukonga.miuix.kmp.theme.darkColorScheme()
            else -> if (darkTheme) top.yukonga.miuix.kmp.theme.darkColorScheme() else top.yukonga.miuix.kmp.theme.lightColorScheme()
        },
        content = content
    )
}

enum class MonetSwatch {
    NEUTRAL1, NEUTRAL2, ACCENT1, ACCENT2, ACCENT3
}

@Composable
fun monetCompatColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme()
): ColorScheme {
    val accent1 = MonetSwatch.ACCENT1
    val accent2 = MonetSwatch.ACCENT2
    val accent3 = MonetSwatch.ACCENT3
    val neutral1 = MonetSwatch.NEUTRAL1
    val neutral2 = MonetSwatch.NEUTRAL2

    fun getMonetColor(
        swatch: MonetSwatch,
        @IntRange(from = 50, to = 900) shade: Int
    ): Color {
        val monet = MonetCompat.getInstance()
        return when (swatch) {
            MonetSwatch.ACCENT1 -> monet.getMonetColors().accent1[shade]
            MonetSwatch.ACCENT2 -> monet.getMonetColors().accent2[shade]
            MonetSwatch.ACCENT3 -> monet.getMonetColors().accent3[shade]
            MonetSwatch.NEUTRAL1 -> monet.getMonetColors().neutral1[shade]
            MonetSwatch.NEUTRAL2 -> monet.getMonetColors().neutral2[shade]
        }?.toArgb()?.let { Color(it) } ?: Color.Unspecified
    }

    return if (darkTheme) darkColorScheme(
        primary              = getMonetColor(accent1, 200),
        onPrimary            = getMonetColor(accent1, 800),
        primaryContainer     = getMonetColor(accent1, 600),
        onPrimaryContainer   = getMonetColor(accent2, 100),
        inversePrimary       = getMonetColor(accent1, 600),

        secondary            = getMonetColor(accent2, 200),
        onSecondary          = getMonetColor(accent2, 800),
        secondaryContainer   = getMonetColor(accent2, 700),
        onSecondaryContainer = getMonetColor(accent2, 100),

        tertiary             = getMonetColor(accent3, 200),
        onTertiary           = getMonetColor(accent3, 700),
        tertiaryContainer    = getMonetColor(accent3, 700),
        onTertiaryContainer  = getMonetColor(accent3, 100),

        background           = getMonetColor(neutral1, 900),
        onBackground         = getMonetColor(neutral1, 100),

        surface              = getMonetColor(neutral1, 900),
        onSurface            = getMonetColor(neutral1, 100),
        surfaceVariant       = getMonetColor(neutral2, 700),
        onSurfaceVariant     = getMonetColor(neutral2, 200),
        inverseSurface       = getMonetColor(neutral1, 100),
        inverseOnSurface     = getMonetColor(neutral1, 800),

        outline              = getMonetColor(neutral2, 500),

        error                = errorDark,
        onError              = onErrorDark,
        errorContainer       = errorContainerDark,
        onErrorContainer     = onErrorContainerDark
    ) else lightColorScheme(
        primary              = getMonetColor(accent1, 700),
        onPrimary            = getMonetColor(neutral1, 50),
        primaryContainer     = getMonetColor(accent2, 100),
        onPrimaryContainer   = getMonetColor(accent1, 900),
        inversePrimary       = getMonetColor(accent1, 200),

        secondary            = getMonetColor(accent2, 700),
        onSecondary          = getMonetColor(neutral1, 50),
        secondaryContainer   = getMonetColor(accent2, 100),
        onSecondaryContainer = getMonetColor(accent2, 900),

        tertiary             = getMonetColor(accent3, 600),
        onTertiary           = getMonetColor(neutral1, 50),
        tertiaryContainer    = getMonetColor(accent3, 100),
        onTertiaryContainer  = getMonetColor(accent3, 900),

        background           = getMonetColor(neutral1, 50),
        onBackground         = getMonetColor(neutral1, 900),

        surface              = getMonetColor(neutral1, 50),
        onSurface            = getMonetColor(neutral1, 900),
        surfaceVariant       = getMonetColor(neutral2, 100),
        onSurfaceVariant     = getMonetColor(neutral2, 700),
        inverseSurface       = getMonetColor(neutral1, 800),
        inverseOnSurface     = getMonetColor(neutral2, 50),

        outline              = getMonetColor(accent2, 500),

        error                = errorLight,
        onError              = onErrorLight,
        errorContainer       = errorContainerLight,
        onErrorContainer     = onErrorContainerLight
    )
}