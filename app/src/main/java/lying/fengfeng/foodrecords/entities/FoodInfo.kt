package lying.fengfeng.foodrecords.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodInfo(
    @ColumnInfo val foodName: String,
    @ColumnInfo val productionDate: String,
    @ColumnInfo val foodType: String,
    @ColumnInfo val shelfLife: String,
    @PrimaryKey val uuid: String
) {
}