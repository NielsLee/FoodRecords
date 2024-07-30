package lying.fengfeng.foodrecords.ui.components.insertionDialog

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.utils.DateUtil

class InsertionDialogViewModel : ViewModel() {

    var cameraStatus: MutableState<CameraStatus> = mutableStateOf(CameraStatus.IDLE)

    var foodName: MutableState<String> = mutableStateOf("")
    var productionDate: MutableState<String> = mutableStateOf(
        DateUtil.dateWithFormat(
            DateUtil.todayMillis(),
            AppRepo.getDateFormat()
        )
    )

    private lateinit var foodTypes: List<String>
    private lateinit var shelfLifeList: List<String>

    var foodType: MutableState<String> = mutableStateOf("")
    var shelfLife: MutableState<String> = mutableStateOf("")
    var expirationDate: MutableState<String> = mutableStateOf("")
    var uuid: MutableState<String> = mutableStateOf("")
    var tips: MutableState<String> = mutableStateOf("")

    fun refreshParams() {
        productionDate.value = DateUtil.dateWithFormat(
            DateUtil.todayMillis(),
            AppRepo.getDateFormat()
        )
        CoroutineScope(Dispatchers.IO).launch {
            foodTypes = AppRepo.getAllTypeInfo().map { it.type }
            shelfLifeList = AppRepo.getAllShelfLifeInfo().map { it.life }

            withContext(Dispatchers.Main) {
                foodType.value = foodTypes[0]
                shelfLife.value = shelfLifeList[0]
            }
        }
    }

    fun initParams() {
        CoroutineScope(Dispatchers.IO).launch {

            foodTypes = AppRepo.getAllTypeInfo().map { it.type }
            shelfLifeList = AppRepo.getAllShelfLifeInfo().map { it.life }

            withContext(Dispatchers.Main) {
                foodType.value = foodTypes[0]
                shelfLife.value = shelfLifeList[0]
            }

            foodName = mutableStateOf("")
            productionDate = mutableStateOf(
                DateUtil.dateWithFormat(
                    DateUtil.todayMillis(),
                    AppRepo.getDateFormat()
                )
            )
            expirationDate = mutableStateOf("")
            uuid = mutableStateOf("")
            tips = mutableStateOf("")
        }
    }

    enum class CameraStatus {
        IDLE,
        PREVIEWING,
        IMAGE_READY
    }
}
