package lying.fengfeng.foodrecords.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import lying.fengfeng.foodrecords.MainActivityDelegate

actual fun isDynamicColor(dynamicColor: Boolean): Boolean {
    return dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

@RequiresApi(Build.VERSION_CODES.S)
actual fun setDynamicColor(isDarkTheme: Boolean): ColorScheme {
    val context = MainActivityDelegate.context
    return if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
}