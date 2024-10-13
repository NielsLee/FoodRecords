import androidx.compose.runtime.Composable
import fridgey_kmf.composeapp.generated.resources.Res
import fridgey_kmf.composeapp.generated.resources.type_can
import fridgey_kmf.composeapp.generated.resources.type_cereal
import fridgey_kmf.composeapp.generated.resources.type_condiment
import fridgey_kmf.composeapp.generated.resources.type_fruits_or_vegetables
import fridgey_kmf.composeapp.generated.resources.type_meat
import fridgey_kmf.composeapp.generated.resources.type_milk
import fridgey_kmf.composeapp.generated.resources.type_seafood
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.cache.FoodInfoDatabaseDriver
import lying.fengfeng.foodrecords.cache.FoodRecordDatabase
import lying.fengfeng.foodrecords.entity.FoodInfo
import lying.fengfeng.foodrecords.entity.FoodTypeInfo
import lying.fengfeng.foodrecords.entity.ShelfLifeInfo
import lying.fengfeng.foodrecords.ui.theme.ThemeOptions
import org.jetbrains.compose.resources.stringResource
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDomainMask
import platform.Foundation.setValue

actual object AppRepo {

    private val defaults = NSUserDefaults.standardUserDefaults
    private val foodInfoDb = FoodRecordDatabase(FoodInfoDatabaseDriver())

    actual fun init(androidApplication: Any?) {
    }

    actual fun getPicturePath(uuid: String): String {
        // Get the app's document directory path
        val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
        val documentsDirectory = paths.first() as String

        // Return the complete file path with uuid
        return "$documentsDirectory/$uuid"
    }

    actual fun addFoodInfo(foodInfo: FoodInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoDb.insertFoodInfo(foodInfo)
        }
    }

    actual suspend fun getAllFoodInfo(): List<FoodInfo> {
        return foodInfoDb.getAllFoodInfo()
    }

    actual fun removeFoodInfo(foodInfo: FoodInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoDb.removeFoodInfo(foodInfo)
        }
    }

    actual suspend fun getAllTypeInfo(): List<FoodTypeInfo> {
        return foodInfoDb.getAllFoodTypeInfo()
    }

    actual fun addTypeInfo(typeInfo: FoodTypeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoDb.insertFoodTypeInfo(typeInfo)
        }
    }

    actual fun removeTypeInfo(typeInfo: FoodTypeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoDb.removeFoodTypeInfo(typeInfo)
        }
    }

    actual suspend fun getAllShelfLifeInfo(): List<ShelfLifeInfo> {
        return foodInfoDb.getAllShelfLifeInfo().sortedBy { it.life.toInt() }
    }

    actual fun addShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoDb.insertShelfLifeInfo(shelfLifeInfo)
        }
    }

    actual fun removeShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoDb.removeShelfLifeInfo(shelfLifeInfo)
        }
    }

    actual fun isNotificationEnabled(): Boolean {
        return defaults.boolForKey("notification_enabled")
    }

    actual fun setNotificationEnabled(boolean: Boolean) {
        defaults.setBool(boolean, "notification_enabled")
    }

    actual fun getDaysBeforeNotification(): Int {
        return defaults.integerForKey("days_before_notification").toInt()
    }

    actual fun setDaysBeforeNotification(days: Int) {
        defaults.setInteger(days.toLong(), "days_before_notification")
    }

    actual fun setNextNotificationMillis(time: Long) {
        defaults.setInteger(time, "next_notification_time")
    }

    actual fun getNextNotificationMillis(): Long {
        return defaults.integerForKey("next_notification_time")
    }

    actual fun setDateFormat(format: String) {
        defaults.setValue(format, "date_format")
    }

    actual fun getDateFormat(): String {
        return defaults.stringForKey("date_format") ?: "yy-MM-dd"
    }

    actual fun setThemeOption(option: ThemeOptions) {
        defaults.setInteger(option.int.toLong(), "theme_option")
    }

    actual fun getThemeOption(): ThemeOptions {
        val themeValue = defaults.integerForKey("theme_option")
        return ThemeOptions.fromInt(themeValue.toInt())
    }

    actual fun setIsNewUI(isNewUI: Boolean) {
        defaults.setBool(isNewUI, "is_new_ui")
    }

    actual fun isNewUI(): Boolean {
        return defaults.boolForKey("is_new_ui")
    }

    actual fun setNewUITried(isTried: Boolean) {
        defaults.setBool(isTried, "is_new_ui_tried")
    }

    actual fun isNewUITried(): Boolean {
        return defaults.boolForKey("is_new_ui_tried")
    }

    @Composable
    actual fun addInitializedData() {
        if (!defaults.boolForKey("hasInitialized")) {

            val typeFruitsOrVegetable = stringResource(Res.string.type_fruits_or_vegetables)
            val typeMeat = stringResource(Res.string.type_meat)
            val typeMilk = stringResource(Res.string.type_milk)
            val typeSeafood = stringResource(Res.string.type_seafood)
            val typeCereal = stringResource(Res.string.type_cereal)
            val typeCan = stringResource(Res.string.type_can)
            val typeCondiment = stringResource(Res.string.type_condiment)

            CoroutineScope(Dispatchers.IO).launch {

                foodInfoDb.insertFoodTypeInfo(FoodTypeInfo(type = typeFruitsOrVegetable))
                foodInfoDb.insertFoodTypeInfo(FoodTypeInfo(type = typeMeat))
                foodInfoDb.insertFoodTypeInfo(FoodTypeInfo(type = typeMilk))
                foodInfoDb.insertFoodTypeInfo(FoodTypeInfo(type = typeSeafood))
                foodInfoDb.insertFoodTypeInfo(FoodTypeInfo(type = typeCereal))
                foodInfoDb.insertFoodTypeInfo(FoodTypeInfo(type = typeCan))
                foodInfoDb.insertFoodTypeInfo(FoodTypeInfo(type = typeCondiment))

                foodInfoDb.insertShelfLifeInfo(ShelfLifeInfo(life = "3"))
                foodInfoDb.insertShelfLifeInfo(ShelfLifeInfo(life = "7"))
                foodInfoDb.insertShelfLifeInfo(ShelfLifeInfo(life = "14"))
                foodInfoDb.insertShelfLifeInfo(ShelfLifeInfo(life = "30"))
                foodInfoDb.insertShelfLifeInfo(ShelfLifeInfo(life = "90"))
                foodInfoDb.insertShelfLifeInfo(ShelfLifeInfo(life = "180"))
                foodInfoDb.insertShelfLifeInfo(ShelfLifeInfo(life = "360"))

                defaults.setBool(true, "hasInitialized")
            }
        }
    }
}