package lying.fengfeng.foodrecords.ui.components.insertionDialog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import lying.fengfeng.foodrecords.utils.DateUtil

class InsertionDialogViewModel: ViewModel() {

    var isDialogShown = mutableStateOf(false)
    var isPreviewing = mutableStateOf(false)
    var isCaptured = mutableStateOf(false)

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
            "1Day",
            "1Day",
            "1Day",
            "7Days",
            "15Days",
            "30Days",
            "60Days",
            "90Days",
            "180Days"
        )
    }
}
