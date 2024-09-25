package lying.fengfeng.foodrecords.ui.components.insertionDialog

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.utils.DateUtil
import lying.fengfeng.foodrecords.utils.UUID

class InsertionDialogViewModel(
    existedFoodInfo: FoodInfo? = null
) : ViewModel() {

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
    var uuid: MutableState<String> = mutableStateOf(UUID.getRandom())
    var tips: MutableState<String> = mutableStateOf("")
    var amount: MutableIntState = mutableIntStateOf(1)

    init {
        if (existedFoodInfo != null) {
            // foodInfo is not empty(editing), fill with them
            uuid.value = existedFoodInfo.uuid
            foodName.value = existedFoodInfo.foodName
            productionDate.value = DateUtil.dateWithFormat(
                existedFoodInfo.productionDate.toLong(),
                AppRepo.getDateFormat()
            )
            foodType.value = existedFoodInfo.foodType
            shelfLife.value = existedFoodInfo.shelfLife
            expirationDate.value = DateUtil.dateWithFormat(
                existedFoodInfo.expirationDate.toLong(),
                AppRepo.getDateFormat()
            )
            tips.value = existedFoodInfo.tips
            amount.intValue = existedFoodInfo.amount
            if (existedFoodInfo.pictureExists()) {
                cameraStatus.value = CameraStatus.IMAGE_READY
            }
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
