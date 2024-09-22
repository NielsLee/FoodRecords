package lying.fengfeng.foodrecords.ui

actual class ScreenParams actual constructor(androidContext: Any?) {
    actual fun getListColumnCount(): Int {
        return 2
    }
    actual fun getInsertDialogWidthPercent(): Float {
        return 1.0f
    }
}