package lying.fengfeng.foodrecords.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodTypeInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val type: String
)
