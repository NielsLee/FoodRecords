package lying.fengfeng.foodrecords.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodInfo(
    @PrimaryKey val foodName: String,
    @ColumnInfo val productionDate: String,
    @ColumnInfo val foodType: String,
    @ColumnInfo val shelfLife: String,
    @ColumnInfo val pictureUUID: String
) {
}