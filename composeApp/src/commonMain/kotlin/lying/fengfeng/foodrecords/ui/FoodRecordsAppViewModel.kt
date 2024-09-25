package lying.fengfeng.foodrecords.ui

import AppRepo
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.entities.FoodTypeInfo
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo
import lying.fengfeng.foodrecords.notification.NotificationDelegate
import lying.fengfeng.foodrecords.ui.theme.ThemeOptions

class FoodRecordsAppViewModel: ViewModel() {

    var isDialogShown: MutableState<Boolean> = mutableStateOf(false)

    var foodInfoList = mutableStateListOf<FoodInfo>()
    var foodTypeList = mutableStateListOf<FoodTypeInfo>()
    var shelfLifeList = mutableStateListOf<ShelfLifeInfo>()
    var isNotificationEnabled = mutableStateOf(false)
    var daysBeforeNotification = mutableIntStateOf(AppRepo.getDaysBeforeNotification())
    var dateFormat = mutableStateOf(AppRepo.getDateFormat())
    var themeOption = mutableStateOf(AppRepo.getThemeOption())

    var isNewUITried = mutableStateOf(AppRepo.isNewUITried())

    var isNewUI = mutableStateOf(AppRepo.isNewUI())

    init {
        CoroutineScope(Dispatchers.Main).launch {
            foodInfoList.addAll(
                withContext(Dispatchers.IO) {
                    AppRepo.getAllFoodInfo()
                }
            )
            foodTypeList.addAll(
                withContext(Dispatchers.IO) {
                    AppRepo.getAllTypeInfo()
                }
            )
            shelfLifeList.addAll(
                withContext(Dispatchers.IO) {
                    AppRepo.getAllShelfLifeInfo()
                }
            )
            isNotificationEnabled.value = AppRepo.isNotificationEnabled()
        }
    }

    fun getFoodInfoList(): List<FoodInfo> {
        return foodInfoList
    }

    fun getFoodTypeList(): List<FoodTypeInfo> {
        return foodTypeList
    }

    fun getShelfLifeList(): List<ShelfLifeInfo> {
        return shelfLifeList
    }

    fun addOrUpdateFoodInfo(foodInfo: FoodInfo) {
        AppRepo.addFoodInfo(foodInfo)
        foodInfoList.also { list ->
            if (!list.any { it.uuid == foodInfo.uuid}) {
                list.add(foodInfo)
            } else {
                list.removeAll { it.uuid == foodInfo.uuid }
                list.add(foodInfo)
            }
        }
    }

    fun addFoodTypeInfo(foodTypeInfo: FoodTypeInfo) {
        AppRepo.addTypeInfo(foodTypeInfo)
        foodTypeList.also {
            if (!it.contains(foodTypeInfo)) {
                it.add(foodTypeInfo)
            }
        }
    }

    fun addShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        AppRepo.addShelfLifeInfo(shelfLifeInfo)
        shelfLifeList.also { list ->
            if (!list.contains(shelfLifeInfo)) {
                list.add(shelfLifeInfo)
                list.sortBy { it.life.toInt() }
            }
        }
    }

    fun removeFoodInfo(foodInfo: FoodInfo) {
        foodInfoList.remove(foodInfo)
        AppRepo.removeFoodInfo(foodInfo)
    }

    fun removeFoodTypeInfo(foodTypeInfo: FoodTypeInfo) {
        foodTypeList.remove(foodTypeInfo)
        AppRepo.removeTypeInfo(foodTypeInfo)
    }

    fun removeShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        shelfLifeList.remove(shelfLifeInfo)
        AppRepo.removeShelfLifeInfo(shelfLifeInfo)
    }

    fun updateFoodInfo(foodInfo: FoodInfo) {
        foodInfoList.also {
            if (it.contains(foodInfo)) {
                it.remove(foodInfo)
                it.add(foodInfo)
                AppRepo.addFoodInfo(foodInfo)
            }
        }
    }

    fun updateDaysBeforeNotification(days: Int) {
        daysBeforeNotification.value = days
        AppRepo.setDaysBeforeNotification(days)
    }

    fun enableNotification() {
        NotificationDelegate.enableNotification()
    }

    fun disableNotification() {
        NotificationDelegate.enableNotification()
    }

    fun updateDateFormat(dateFormat: String) {
        this.dateFormat.value = dateFormat
        AppRepo.setDateFormat(dateFormat)
    }

    fun setThemeOption(option: ThemeOptions) {
        themeOption.value = option
        AppRepo.setThemeOption(option)
    }

    fun setIsNewUI(isNewUI: Boolean) {
        this.isNewUI.value = isNewUI
        AppRepo.setIsNewUI(isNewUI)
    }

    fun setIsNewUITried(isNewUITried: Boolean) {
        this.isNewUITried.value = isNewUITried
        AppRepo.setNewUITried(isNewUITried)
    }

    private fun setNotificationEnabled(boolean: Boolean) {
        isNotificationEnabled.value = boolean
        AppRepo.setNotificationEnabled(boolean)
    }
}