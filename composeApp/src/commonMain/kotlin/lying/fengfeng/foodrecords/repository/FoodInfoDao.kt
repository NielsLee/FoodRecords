package lying.fengfeng.foodrecords.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lying.fengfeng.foodrecords.entities.FoodInfo

@Dao
interface FoodInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(foodInfo: FoodInfo)

    @Query(" SELECT * FROM FOODINFO ")
    fun getAll(): List<FoodInfo>

    @Delete
    fun remove(foodInfo: FoodInfo)

}