package lying.fengfeng.foodrecords.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.rememberDynamicColorScheme

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun FoodRecordsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    themeOption: ThemeOptions = ThemeOptions.DYNAMIC,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeOption) {
        ThemeOptions.RED -> rememberDynamicColorScheme(Color.Red, darkTheme)
        ThemeOptions.YELLOW -> rememberDynamicColorScheme(Color.Yellow, darkTheme)
        ThemeOptions.GREEN -> rememberDynamicColorScheme(Color.Green, darkTheme)
        ThemeOptions.BLUE -> rememberDynamicColorScheme(Color.Blue, darkTheme)
        ThemeOptions.MAGENTA -> rememberDynamicColorScheme(Color.Magenta, darkTheme)
        else -> when {
            isDynamicColor(dynamicColor) -> {
                setDynamicColor(darkTheme)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    }

//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primaryContainer.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

expect fun isDynamicColor(dynamicColor: Boolean): Boolean

expect fun setDynamicColor(isDarkTheme: Boolean): ColorScheme