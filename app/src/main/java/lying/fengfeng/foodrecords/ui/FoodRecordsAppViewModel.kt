package lying.fengfeng.foodrecords.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.entities.FoodTypeInfo
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.ui.theme.ThemeOptions
import lying.fengfeng.foodrecords.utils.DateUtil

class FoodRecordsAppViewModel: ViewModel() {

    var isDialogShown: MutableState<Boolean> = mutableStateOf(false)
    var isSearchDialogShown: MutableState<Boolean> = mutableStateOf(false)

    var foodInfoList = mutableStateListOf<FoodInfo>()
    var foodTypeList = mutableStateListOf<FoodTypeInfo>()
    var shelfLifeList = mutableStateListOf<ShelfLifeInfo>()
    var isNotificationEnabled = mutableStateOf(false)
    var daysBeforeNotification = mutableIntStateOf(AppRepo.getDaysBeforeNotification())
    var dateFormat = mutableStateOf(AppRepo.getDateFormat())
    var themeOption = mutableStateOf(AppRepo.getThemeOption())

    var isNewUITried = mutableStateOf(AppRepo.isNewUITried())

    var isNewUI = mutableStateOf(AppRepo.isNewUI())

    var filterStr: MutableState<String?> = mutableStateOf(null)

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

    fun addOrUpdateFoodInfo(foodInfo: FoodInfo) {
        AppRepo.addFoodInfo(foodInfo)
        foodInfoList.also { list ->
            if (!list.any { it.uuid == foodInfo.uuid}) {
                list.add(foodInfo)
            } else {
                list.removeIf { it.uuid == foodInfo.uuid }
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

    fun enableNotification(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                try {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        888
                    )
                } catch (e: Exception) {
                    Toast.makeText(context, context.getString(R.string.notification_permisson_failed), Toast.LENGTH_SHORT).show()
                }
            } else {
                scheduleNotifications(context)
            }
        } else {
            scheduleNotifications(context)
        }
    }

    fun updateDateFormat(dateFormat: String) {
        this.dateFormat.value = dateFormat
        AppRepo.setDateFormat(dateFormat)
    }

    private fun scheduleNotifications(context: Context) {
        Toast.makeText(context, "⏰✅", Toast.LENGTH_SHORT).show()
        setNotificationEnabled(true)

        AppRepo.setNextNotificationMillis(System.currentTimeMillis() + DateUtil.millisFromNowTo(10))
    }

    fun disableNotification(context: Context) {
        Toast.makeText(context, "⏰❌", Toast.LENGTH_SHORT).show()
        setNotificationEnabled(false)

        AppRepo.setNextNotificationMillis(-1)
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