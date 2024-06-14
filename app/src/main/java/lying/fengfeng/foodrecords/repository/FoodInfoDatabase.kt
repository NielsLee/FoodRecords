package lying.fengfeng.foodrecords.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import lying.fengfeng.foodrecords.Constants.FOOD_INFO_DB_VERSION
import lying.fengfeng.foodrecords.entities.FoodInfo

@Database(entities = [FoodInfo::class], version = FOOD_INFO_DB_VERSION)
abstract class FoodInfoDatabase: RoomDatabase() {

    abstract fun foodInfoDao(): FoodInfoDao
}