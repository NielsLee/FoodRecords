package lying.fengfeng.foodrecords.repository

import androidx.room.RoomDatabase

//@Database(entities = [ShelfLifeInfo::class], version = SHELF_LIFE_DB_VERSION)
abstract class ShelfLifeInfoDatabase: RoomDatabase() {

    abstract fun shelfLifeDao(): ShelfLifeInfoDao
}