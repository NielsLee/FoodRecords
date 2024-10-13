package lying.fengfeng.foodrecords

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import lying.fengfeng.foodrecords.ui.FoodRecordsApp
import lying.fengfeng.foodrecords.ui.LocalScreenParams
import lying.fengfeng.foodrecords.ui.ScreenParams

fun MainViewController() = ComposeUIViewController {
    val screenParams = ScreenParams(null)
    CompositionLocalProvider(LocalScreenParams provides screenParams) {
        FoodRecordsApp()
    }
}