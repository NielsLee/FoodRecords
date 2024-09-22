package lying.fengfeng.foodrecords.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShelfLifeInfo(
    @PrimaryKey val life: String
)