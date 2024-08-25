package lying.fengfeng.foodrecords.ui.components.insertionDialog

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.utils.DateUtil
import java.util.UUID

class InsertionDialogViewModel : ViewModel() {

    var cameraStatus: MutableState<CameraStatus> = mutableStateOf(CameraStatus.IDLE)

    var foodName: MutableState<String> = mutableStateOf("")
    var productionDate: MutableState<String> = mutableStateOf(
        DateUtil.dateWithFormat(
            DateUtil.todayMillis(),
            AppRepo.getDateFormat()
        )
    )

    private lateinit var foodTypeList: List<String>
    private lateinit var shelfLifeList: List<String>

    var foodType: MutableState<String> = mutableStateOf("")
    var shelfLife: MutableState<String> = mutableStateOf("")
    var expirationDate: MutableState<String> = mutableStateOf("")
    var uuid: MutableState<String> = mutableStateOf(UUID.randomUUID().toString())
    var tips: MutableState<String> = mutableStateOf("")
    var amount: MutableIntState = mutableIntStateOf(1)

    fun fillParams(foodInfo: FoodInfo? = null) {

        if (foodInfo != null) {
            // foodInfo is not empty(editing), fill with them
            uuid.value = foodInfo.uuid
            foodName.value = foodInfo.foodName
            productionDate.value = DateUtil.dateWithFormat(
                foodInfo.productionDate.toLong(),
                AppRepo.getDateFormat()
            )
            foodType.value = foodInfo.foodType
            shelfLife.value = foodInfo.shelfLife
            expirationDate.value = foodInfo.expirationDate
            tips.value = foodInfo.tips
            amount.intValue = foodInfo.amount

        } else {
            // foodInfo is empty(adding), fill with new params
            productionDate.value = DateUtil.dateWithFormat(
                DateUtil.todayMillis(),
                AppRepo.getDateFormat()
            )
            CoroutineScope(Dispatchers.IO).launch {
                foodTypeList = AppRepo.getAllTypeInfo().map { it.type }
                shelfLifeList = AppRepo.getAllShelfLifeInfo().map { it.life }

                withContext(Dispatchers.Main) {
                    foodType.value = foodTypeList[0]
                    shelfLife.value = shelfLifeList[0]
                }
            }
        }
    }

    enum class CameraStatus {
        IDLE,
        PREVIEWING,
        IMAGE_READY
    }
}
