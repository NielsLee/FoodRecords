package lying.fengfeng.foodrecords.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lying.fengfeng.foodrecords.entities.FoodTypeInfo

@Dao
interface FoodTypeInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(foodTypeInfo: FoodTypeInfo)

    @Query(" SELECT * FROM FOODTYPEINFO ")
    suspend fun getAll(): List<FoodTypeInfo>

    @Delete
    suspend fun remove(foodTypeInfo: FoodTypeInfo)
}