package lying.fengfeng.foodrecords.ui

expect class ScreenParams(androidContext: Any?) {
    fun getListColumnCount(): Int

    fun getInsertDialogWidthPercent(): Float
}