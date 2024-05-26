package lying.fengfeng.foodrecords.ui.components.insertionDialog

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.utils.DateUtil

class InsertionDialogViewModel : ViewModel() {

    var isDialogShown: MutableState<Boolean> = mutableStateOf(false)
    var cameraStatus: MutableState<CameraStatus> = mutableStateOf(CameraStatus.IDLE)

    lateinit var foodName: MutableState<String>
    lateinit var productionDate: MutableState<String>
    lateinit var foodType: MutableState<String>
    lateinit var shelfLife: MutableState<String>
    lateinit var uuid: MutableState<String>

    lateinit var foodTypes: List<String>
    lateinit var shelfLifeList: List<String>

    init {
        initParams()
    }

    fun initParams() {
        CoroutineScope(Dispatchers.IO).launch {

            runBlocking {
                foodTypes = AppRepo.getAllTypeInfo().map { it.type }
                shelfLifeList = AppRepo.getAllShelfLifeInfo().map { it.life }
            }

            foodName = mutableStateOf("FoodName")
            productionDate = mutableStateOf(
                DateUtil.dateWithFormat(
                    DateUtil.todayMillis(),
                    "YYYY-MM-dd"
                )
            )
            foodType = mutableStateOf(foodTypes[0])
            shelfLife = mutableStateOf(shelfLifeList[0])
            uuid = mutableStateOf("")
        }
    }

    enum class CameraStatus {
        IDLE,
        PREVIEWING,
        CAPTURED,
        IMAGE_READY
    }
}
