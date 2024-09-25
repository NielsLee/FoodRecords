import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.entities.FoodTypeInfo
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo
import lying.fengfeng.foodrecords.ui.theme.ThemeOptions

actual object AppRepo {
    actual fun init(androidApplication: Any?) {
    }

    actual fun getPicturePath(uuid: String): String {
        TODO("Not yet implemented")
    }

    actual fun addFoodInfo(foodInfo: FoodInfo) {
    }

    actual suspend fun getAllFoodInfo(): List<FoodInfo> {
        TODO("Not yet implemented")
    }

    actual fun removeFoodInfo(foodInfo: FoodInfo) {
    }

    actual suspend fun getAllTypeInfo(): List<FoodTypeInfo> {
        TODO("Not yet implemented")
    }

    actual fun addTypeInfo(typeInfo: FoodTypeInfo) {
    }

    actual fun removeTypeInfo(typeInfo: FoodTypeInfo) {
    }

    actual suspend fun getAllShelfLifeInfo(): List<ShelfLifeInfo> {
        TODO("Not yet implemented")
    }

    actual fun addShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
    }

    actual fun removeShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
    }

    actual fun isNotificationEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun setNotificationEnabled(boolean: Boolean) {
    }

    actual fun getDaysBeforeNotification(): Int {
        TODO("Not yet implemented")
    }

    actual fun setDaysBeforeNotification(days: Int) {
    }

    actual fun setNextNotificationMillis(time: Long) {
    }

    actual fun getNextNotificationMillis(): Long {
        TODO("Not yet implemented")
    }

    actual fun setDateFormat(format: String) {
    }

    actual fun getDateFormat(): String {
        TODO("Not yet implemented")
    }

    actual fun setThemeOption(option: ThemeOptions) {
    }

    actual fun getThemeOption(): ThemeOptions {
        TODO("Not yet implemented")
    }

    actual fun setIsNewUI(isNewUI: Boolean) {
    }

    actual fun isNewUI(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun setNewUITried(isTried: Boolean) {
    }

    actual fun isNewUITried(): Boolean {
        TODO("Not yet implemented")
    }

}