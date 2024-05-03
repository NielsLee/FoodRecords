package lying.fengfeng.foodrecords.ui.components.insertionDialog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import lying.fengfeng.foodrecords.utils.DateUtil

class InsertionDialogViewModel: ViewModel() {

    var isDialogShown = mutableStateOf(false)
    var cameraStatus = mutableStateOf(CameraStatus.IDLE)

    var foodName = mutableStateOf("FoodName")
    var productionDate = mutableStateOf(
        DateUtil.dateWithFormat(
            DateUtil.todayMillis(),
            "YYYY-MM-dd"
        )
    )
    var foodType = mutableStateOf(TempData.foodTypes[0])
    var shelfLife = mutableStateOf(TempData.shelfLifeList[0])
    var pictureUUID = mutableStateOf("")

    object TempData {

        val foodTypes = listOf("CXK", "FCC", "CLN", "MJQ")
        var shelfLifeList = listOf(
            "1",
            "2",
            "3",
            "5",
            "7",
            "10",
            "14",
            "21",
            "30",
            "60",
            "90"
        )
    }

    enum class CameraStatus {
        IDLE,
        PREVIEWING,
        CAPTURED,
        IMAGE_READY
    }
}
