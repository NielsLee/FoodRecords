package lying.fengfeng.foodrecords.repository

import androidx.room.RoomDatabase

//@Database(entities = [FoodTypeInfo::class], version = FOOD_TYPE_DB_VERSION)
abstract class FoodTypeInfoDatabase: RoomDatabase() {

    abstract fun foodTypeInfoDao(): FoodTypeInfoDao
}