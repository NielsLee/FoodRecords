package lying.fengfeng.foodrecords.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
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

object AppRepo {

    private lateinit var app: Application
    private lateinit var sp: SharedPreferences

    private lateinit var foodInfoDB: FoodInfoDatabase
    private lateinit var foodInfoDao: FoodInfoDao

    private lateinit var typeInfoDB: FoodTypeInfoDatabase
    private lateinit var typeInfoDao: FoodTypeInfoDao

    private lateinit var shelfLifeDB: ShelfLifeInfoDatabase
    private lateinit var shelfLifeDao: ShelfLifeInfoDao

    fun init(application: Application) {

        app = application
        sp = application.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

        foodInfoDB = Room.databaseBuilder(app, FoodInfoDatabase::class.java, DB_NAME_FOOD_INFO).build()
        foodInfoDao = foodInfoDB.foodInfoDao()

        typeInfoDB = Room.databaseBuilder(app, FoodTypeInfoDatabase::class.java, DB_NAME_FOOD_TYPE_INFO).build()
        typeInfoDao = typeInfoDB.foodTypeInfoDao()

        shelfLifeDB = Room.databaseBuilder(app, ShelfLifeInfoDatabase::class.java, DB_NAME_SHELF_LIFE_INFO).build()
        shelfLifeDao = shelfLifeDB.shelfLifeDao()

        if (!sp.getBoolean("hasInitialized", false)) {
            addInitializedData()
        }
    }

    fun getPicturePath(uuid: String): String {
        return app.filesDir.absolutePath + "/" + uuid
    }

    fun addFoodInfo(foodInfo: FoodInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoDao.insert(foodInfo)
        }
    }

    fun getAllFoodInfo(): List<FoodInfo> {
        return foodInfoDao.getAll()
    }

    fun removeFoodInfo(foodInfo: FoodInfo) {
        foodInfoDao.remove(foodInfo)
    }

    fun getAllTypeInfo(): List<FoodTypeInfo> {
        return typeInfoDao.getAll()
    }

    fun addTypeInfo(typeInfo: FoodTypeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            typeInfoDao.insert(typeInfo)
        }
    }

    fun removeTypeInfo(typeInfo: FoodTypeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            typeInfoDao.remove(typeInfo)
        }
    }

    fun getAllShelfLifeInfo(): List<ShelfLifeInfo> {
        return shelfLifeDao.getAll()
    }

    fun addShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            shelfLifeDao.insert(shelfLifeInfo)
        }
    }

    fun removeShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            shelfLifeDao.remove(shelfLifeInfo)
        }
    }

    fun isNotificationEnabled(): Boolean {
        return sp.getBoolean("notification_enabled", false)
    }

    fun setNotificationEnabled(boolean: Boolean) {
        sp.edit().putBoolean("notification_enabled", boolean).apply()
    }

    fun getDaysBeforeNotification(): Int {
        return sp.getInt("days_before_notification", 3)
    }

    fun setDaysBeforeNotification(days: Int) {
        sp.edit().putInt("days_before_notification", days).apply()
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

            shelfLifeDao.insert(ShelfLifeInfo(life = "1"))
            shelfLifeDao.insert(ShelfLifeInfo(life = "2"))
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
}
