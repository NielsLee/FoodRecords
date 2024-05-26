package lying.fengfeng.foodrecords.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import lying.fengfeng.foodrecords.Constants
import lying.fengfeng.foodrecords.Constants.DB_VERSION
import lying.fengfeng.foodrecords.entities.FoodTypeInfo

@Database(entities = [FoodTypeInfo::class], version = DB_VERSION)
abstract class FoodTypeInfoDatabase: RoomDatabase() {

    abstract fun foodTypeInfoDao(): FoodTypeInfoDao
}