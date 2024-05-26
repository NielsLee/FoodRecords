package lying.fengfeng.foodrecords.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo

@Dao
interface ShelfLifeInfoDao {

    @Insert
    fun insert(shelfLifeInfo: ShelfLifeInfo)

    @Query(" SELECT * FROM ShelfLifeInfo ")
    fun getAll(): List<ShelfLifeInfo>

    @Delete
    fun remove(shelfLifeInfo: ShelfLifeInfo)
}