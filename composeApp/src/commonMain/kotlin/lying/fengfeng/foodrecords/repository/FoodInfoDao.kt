package lying.fengfeng.foodrecords.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lying.fengfeng.foodrecords.entity.FoodInfo

//@Dao
interface FoodInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodInfo: FoodInfo)

    @Query(" SELECT * FROM FOODINFO ")
    suspend fun getAll(): List<FoodInfo>

    @Delete
    suspend fun remove(foodInfo: FoodInfo)

}