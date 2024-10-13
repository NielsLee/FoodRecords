import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
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
import lying.fengfeng.foodrecords.Constants.SP_NAME
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.cache.FoodInfoDatabaseDriver
import lying.fengfeng.foodrecords.cache.FoodRecordDatabase
import lying.fengfeng.foodrecords.entity.FoodInfo
import lying.fengfeng.foodrecords.entity.FoodTypeInfo
import lying.fengfeng.foodrecords.entity.ShelfLifeInfo
import lying.fengfeng.foodrecords.ui.theme.ThemeOptions
import org.jetbrains.compose.resources.stringResource

actual object AppRepo {

    private lateinit var app: Application
    private lateinit var sp: SharedPreferences
    private lateinit var foodInfoDb: FoodRecordDatabase

    fun init(application: Application) {

        app = application
        sp = application.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        foodInfoDb = FoodRecordDatabase(FoodInfoDatabaseDriver(app))
    }

    actual fun getPicturePath(uuid: String): String {
        return app.filesDir.absolutePath + "/" + uuid
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
        return sp.getBoolean("notification_enabled", false)
    }

    actual fun setNotificationEnabled(boolean: Boolean) {
        sp.edit().putBoolean("notification_enabled", boolean).apply()
    }

    actual fun getDaysBeforeNotification(): Int {
        return sp.getInt("days_before_notification", 3)
    }

    actual fun setDaysBeforeNotification(days: Int) {
        sp.edit().putInt("days_before_notification", days).apply()
    }

    actual fun setNextNotificationMillis(time: Long) {
        sp.edit().putLong("next_notification_time", time).apply()
    }

    actual fun getNextNotificationMillis(): Long {
        return sp.getLong("next_notification_time", -1)
    }

    actual fun setDateFormat(format: String) {
        sp.edit().putString("date_format", format).apply()
    }

    actual fun getDateFormat(): String {
        return sp.getString("date_format", "yy-MM-dd") ?: "yy-MM-dd"
    }

    actual fun setThemeOption(option: ThemeOptions) {
        sp.edit().putInt("theme_option", option.int).apply()
    }

    actual fun getThemeOption(): ThemeOptions {
        val themeValue = sp.getInt("theme_option", 0)
        return ThemeOptions.fromInt(themeValue)
    }

    actual fun setIsNewUI(isNewUI: Boolean) {
        sp.edit().putBoolean("is_new_ui", isNewUI).apply()
    }

    actual fun isNewUI(): Boolean {
        return sp.getBoolean("is_new_ui", false)
    }

    actual fun setNewUITried(isTried: Boolean) {
        sp.edit().putBoolean("is_new_ui_tried", isTried).apply()
    }

    actual fun isNewUITried(): Boolean {
        return sp.getBoolean("is_new_ui_tried", false)
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    actual fun addInitializedData() {
        if (!sp.getBoolean("hasInitialized", false)) {
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

                sp.edit().putBoolean("hasInitialized", true).commit()            }
        }
    }

    actual fun init(androidApplication: Any?) {
    }
}