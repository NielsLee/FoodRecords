package lying.fengfeng.foodrecords.ui

import android.content.Context

actual class ScreenParams actual constructor(androidContext: Any?) {

    private val widthPixels = (androidContext as Context).resources.displayMetrics.widthPixels
    private val dpi = (androidContext as Context).resources.displayMetrics.densityDpi
    private val widthDp = widthPixels / (dpi / 160f)

    actual fun getListColumnCount(): Int {
        return if (widthDp > 600) 3 else 2
    }
    actual fun getInsertDialogWidthPercent(): Float {
        return if (widthDp > 600) 0.6f else 1f
    }
}