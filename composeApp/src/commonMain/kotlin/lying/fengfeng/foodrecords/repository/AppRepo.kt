import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.entities.FoodTypeInfo
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo
import lying.fengfeng.foodrecords.ui.theme.ThemeOptions

expect object AppRepo {

    fun init(androidApplication: Any?)

    fun getPicturePath(uuid: String): String

    fun addFoodInfo(foodInfo: FoodInfo)

    suspend fun getAllFoodInfo(): List<FoodInfo>

    fun removeFoodInfo(foodInfo: FoodInfo)

    suspend fun getAllTypeInfo(): List<FoodTypeInfo>

    fun addTypeInfo(typeInfo: FoodTypeInfo)

    fun removeTypeInfo(typeInfo: FoodTypeInfo)

    suspend fun getAllShelfLifeInfo(): List<ShelfLifeInfo>

    fun addShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo)

    fun removeShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo)

    fun isNotificationEnabled(): Boolean

    fun setNotificationEnabled(boolean: Boolean)

    fun getDaysBeforeNotification(): Int

    fun setDaysBeforeNotification(days: Int)

    fun setNextNotificationMillis(time: Long)

    fun getNextNotificationMillis(): Long

    fun setDateFormat(format: String)

    fun getDateFormat(): String

    fun setThemeOption(option: ThemeOptions)

    fun getThemeOption(): ThemeOptions

    fun setIsNewUI(isNewUI: Boolean)

    fun isNewUI(): Boolean

    fun setNewUITried(isTried: Boolean)

    fun isNewUITried(): Boolean
}