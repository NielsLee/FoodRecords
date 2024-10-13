package lying.fengfeng.foodrecords.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lying.fengfeng.foodrecords.entity.ShelfLifeInfo

//@Dao
interface ShelfLifeInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shelfLifeInfo: ShelfLifeInfo)

    @Query(" SELECT * FROM ShelfLifeInfo ")
    suspend fun getAll(): List<ShelfLifeInfo>

    @Delete
    suspend fun remove(shelfLifeInfo: ShelfLifeInfo)
}