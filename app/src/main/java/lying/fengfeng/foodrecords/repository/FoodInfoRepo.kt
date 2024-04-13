package lying.fengfeng.foodrecords.repository

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.Constants.DB_NAME
import lying.fengfeng.foodrecords.entities.FoodInfo

object FoodInfoRepo {

    private lateinit var db: FoodInfoDatabase
    private lateinit var app: Application
    private lateinit var foodInfoDao: FoodInfoDao

    fun init(application: Application) {
        app = application
        db = Room.databaseBuilder(app, FoodInfoDatabase::class.java, DB_NAME).build()
        foodInfoDao = db.foodInfoDao()
    }

    fun getAbsolutePictureDir(): String {
        return app.filesDir.absolutePath + "/"
    }

    fun insert(foodInfo: FoodInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoDao.insert(foodInfo)
        }
    }

    fun getAll(): List<FoodInfo> {
        return foodInfoDao.getAll()
    }

    fun remove(foodInfo: FoodInfo) {
        foodInfoDao.remove(foodInfo)
    }


}
