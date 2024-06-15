package lying.fengfeng.foodrecords.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lying.fengfeng.foodrecords.utils.DateUtil

@Entity
data class FoodInfo(
    @ColumnInfo val foodName: String,
    @ColumnInfo val productionDate: String,
    @ColumnInfo val foodType: String,
    @ColumnInfo val shelfLife: String,
    @ColumnInfo val expirationDate: String,
    @PrimaryKey val uuid: String,
    @ColumnInfo val tips: String = ""
)