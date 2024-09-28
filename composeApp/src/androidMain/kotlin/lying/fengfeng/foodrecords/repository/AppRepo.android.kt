import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.Constants.DB_NAME_FOOD_INFO
import lying.fengfeng.foodrecords.Constants.DB_NAME_FOOD_TYPE_INFO
import lying.fengfeng.foodrecords.Constants.DB_NAME_SHELF_LIFE_INFO
import lying.fengfeng.foodrecords.Constants.SP_NAME
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.entities.FoodTypeInfo
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo
import lying.fengfeng.foodrecords.repository.FoodInfoDao
import lying.fengfeng.foodrecords.repository.FoodInfoDatabase
import lying.fengfeng.foodrecords.repository.FoodTypeInfoDao
import lying.fengfeng.foodrecords.repository.FoodTypeInfoDatabase
import lying.fengfeng.foodrecords.repository.ShelfLifeInfoDao
import lying.fengfeng.foodrecords.repository.ShelfLifeInfoDatabase
import lying.fengfeng.foodrecords.ui.theme.ThemeOptions
import java.text.SimpleDateFormat
import java.util.Locale

actual object AppRepo {

    private lateinit var app: Application
    private lateinit var sp: SharedPreferences

    private lateinit var foodInfoDB: FoodInfoDatabase
    private lateinit var foodInfoDao: FoodInfoDao

    private lateinit var typeInfoDB: FoodTypeInfoDatabase
    private lateinit var typeInfoDao: FoodTypeInfoDao

    private lateinit var shelfLifeDB: ShelfLifeInfoDatabase
    private lateinit var shelfLifeDao: ShelfLifeInfoDao

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE FoodInfo ADD COLUMN expirationDate TEXT NOT NULL DEFAULT '' ")
            db.execSQL("ALTER TABLE FoodInfo ADD COLUMN tips TEXT NOT NULL DEFAULT '' ")
        }
    }
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        @SuppressLint("Range")
        override fun migrate(db: SupportSQLiteDatabase) {
            val cursor = db.query("SELECT uuid, productionDate, expirationDate FROM FoodInfo",
                emptyArray())
            if (cursor.moveToFirst()) {
                do {
                    val uuid = cursor.getString(cursor.getColumnIndex("uuid"))
                    val productionDate = cursor.getString(cursor.getColumnIndex("productionDate"))
                    val expirationDate = cursor.getString(cursor.getColumnIndex("expirationDate"))

                    val dateFormatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
                    val productionDateTimestamp = dateFormatter.parse(productionDate)?.time ?: 0
                    val expirationDateTimestamp = if (expirationDate == "--") 0 else dateFormatter.parse(expirationDate)?.time ?: 0


                    val updateQuery = "UPDATE FoodInfo SET productionDate = ?, expirationDate = ? " +
                            "WHERE uuid = ?"

                    db.execSQL(updateQuery, arrayOf(productionDateTimestamp, expirationDateTimestamp,
                        uuid))

                } while (cursor.moveToNext())
            }
            cursor.close()
        }
    }
    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE FoodInfo ADD COLUMN amount INTEGER NOT NULL DEFAULT 1")
        }
    }

    fun init(application: Application) {

        app = application
        sp = application.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

        foodInfoDB = Room.databaseBuilder(app, FoodInfoDatabase::class.java, DB_NAME_FOOD_INFO)
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .addMigrations(MIGRATION_3_4)
            .build()
        foodInfoDao = foodInfoDB.foodInfoDao()

        typeInfoDB = Room.databaseBuilder(app, FoodTypeInfoDatabase::class.java, DB_NAME_FOOD_TYPE_INFO).build()
        typeInfoDao = typeInfoDB.foodTypeInfoDao()

        shelfLifeDB = Room.databaseBuilder(app, ShelfLifeInfoDatabase::class.java, DB_NAME_SHELF_LIFE_INFO).build()
        shelfLifeDao = shelfLifeDB.shelfLifeDao()

        if (!sp.getBoolean("hasInitialized", false)) {
            addInitializedData()
        }
    }

    actual fun getPicturePath(uuid: String): String {
        return app.filesDir.absolutePath + "/" + uuid
    }

    actual fun addFoodInfo(foodInfo: FoodInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoDao.insert(foodInfo)
        }
    }

    actual suspend fun getAllFoodInfo(): List<FoodInfo> {
        return foodInfoDao.getAll()
    }

    actual fun removeFoodInfo(foodInfo: FoodInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoDao.remove(foodInfo)
        }
    }

    actual suspend fun getAllTypeInfo(): List<FoodTypeInfo> {
        return typeInfoDao.getAll()
    }

    actual fun addTypeInfo(typeInfo: FoodTypeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            typeInfoDao.insert(typeInfo)
        }
    }

    actual fun removeTypeInfo(typeInfo: FoodTypeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            typeInfoDao.remove(typeInfo)
        }
    }

    actual suspend fun getAllShelfLifeInfo(): List<ShelfLifeInfo> {
        return shelfLifeDao.getAll().sortedBy { it.life.toInt() }
    }

    actual fun addShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            shelfLifeDao.insert(shelfLifeInfo)
        }
    }

    actual fun removeShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            shelfLifeDao.remove(shelfLifeInfo)
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

    private fun addInitializedData() {
        CoroutineScope(Dispatchers.IO).launch {

            typeInfoDao.insert(FoodTypeInfo(type = app.getString(R.string.type_fruits_or_vegetables)))
            typeInfoDao.insert(FoodTypeInfo(type = app.getString(R.string.type_meat)))
            typeInfoDao.insert(FoodTypeInfo(type = app.getString(R.string.type_milk)))
            typeInfoDao.insert(FoodTypeInfo(type = app.getString(R.string.type_seafood)))
            typeInfoDao.insert(FoodTypeInfo(type = app.getString(R.string.type_cereal)))
            typeInfoDao.insert(FoodTypeInfo(type = app.getString(R.string.type_can)))
            typeInfoDao.insert(FoodTypeInfo(type = app.getString(R.string.type_condiment)))

            shelfLifeDao.insert(ShelfLifeInfo(life = "3"))
            shelfLifeDao.insert(ShelfLifeInfo(life = "7"))
            shelfLifeDao.insert(ShelfLifeInfo(life = "14"))
            shelfLifeDao.insert(ShelfLifeInfo(life = "30"))
            shelfLifeDao.insert(ShelfLifeInfo(life = "90"))
            shelfLifeDao.insert(ShelfLifeInfo(life = "180"))
            shelfLifeDao.insert(ShelfLifeInfo(life = "360"))

            sp.edit().putBoolean("hasInitialized", true).commit()
        }
    }

    actual fun init(androidApplication: Any?) {
    }
}