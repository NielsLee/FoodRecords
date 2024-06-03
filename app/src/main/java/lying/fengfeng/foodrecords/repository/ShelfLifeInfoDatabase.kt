package lying.fengfeng.foodrecords.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import lying.fengfeng.foodrecords.Constants.DB_VERSION
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo

@Database(entities = [ShelfLifeInfo::class], version = DB_VERSION)
abstract class ShelfLifeInfoDatabase: RoomDatabase() {

    abstract fun shelfLifeDao(): ShelfLifeInfoDao
}