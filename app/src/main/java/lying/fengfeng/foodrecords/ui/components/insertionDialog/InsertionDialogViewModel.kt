package lying.fengfeng.foodrecords.ui.components.insertionDialog

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import lying.fengfeng.foodrecords.utils.DateUtil

class InsertionDialogViewModel : ViewModel() {

    var isDialogShown: MutableState<Boolean> = mutableStateOf(false)
    var cameraStatus: MutableState<CameraStatus> = mutableStateOf(CameraStatus.IDLE)

    lateinit var foodName: MutableState<String>
    lateinit var productionDate: MutableState<String>
    lateinit var foodType: MutableState<String>
    lateinit var shelfLife: MutableState<String>
    lateinit var uuid: MutableState<String>

    init {
        initParams()
    }

    fun initParams() {
        foodName = mutableStateOf("FoodName")
        productionDate = mutableStateOf(
            DateUtil.dateWithFormat(
                DateUtil.todayMillis(),
                "YYYY-MM-dd"
            )
        )
        foodType = mutableStateOf(TempData.foodTypes[0])
        shelfLife = mutableStateOf(TempData.shelfLifeList[0])
        uuid = mutableStateOf("")
    }

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
