package lying.fengfeng.foodrecords.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.utils.DateUtil
import java.io.File
import kotlin.math.absoluteValue

@Entity
data class FoodInfo(
    @ColumnInfo val foodName: String,
    @ColumnInfo val productionDate: String,
    @ColumnInfo val foodType: String,
    @ColumnInfo val shelfLife: String,
    @ColumnInfo val expirationDate: String,
    @PrimaryKey val uuid: String,
    @ColumnInfo var amount: Int,
    @ColumnInfo val tips: String = ""
) {
    fun getSortIndex(): Int {
        val remainingDate = DateUtil.getRemainingDays(productionDate, shelfLife, expirationDate)
        val nameHashNum = foodName.hashCode().absoluteValue % 10
        return 10 * remainingDate + nameHashNum
    }

    fun pictureExists(): Boolean {
        return File(AppRepo.getPicturePath(uuid)).exists()
    }
}