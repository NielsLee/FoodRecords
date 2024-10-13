package lying.fengfeng.foodrecords.repository

import androidx.room.RoomDatabase

//@Database(entities = [FoodInfo::class], version = FOOD_INFO_DB_VERSION)
abstract class FoodInfoDatabase: RoomDatabase() {

    abstract fun foodInfoDao(): FoodInfoDao
}