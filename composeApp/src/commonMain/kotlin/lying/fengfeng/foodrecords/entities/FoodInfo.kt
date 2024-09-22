package lying.fengfeng.foodrecords.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import lying.fengfeng.foodrecords.utils.FileUtil
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
        var (remainingDate, isExpired) = getRemainingDays(this)
        if (isExpired) remainingDate = -remainingDate
        val nameHashNum = foodName.hashCode().absoluteValue % 10
        return 10 * remainingDate + nameHashNum
    }

    fun pictureExists(): Boolean {
        return FileUtil.isFileExist(FileUtil.getPictureFilePath(uuid))
    }

    private fun getRemainingDays(foodInfo: FoodInfo): Pair<Int, Boolean> {
        val productionDate = foodInfo.productionDate
        val shelfLife = foodInfo.shelfLife
        val expirationDate = foodInfo.expirationDate
        if (expirationDate == "0") {
            val productionTimeMillis = productionDate.toLong()
            val expirationTimeMillis = productionTimeMillis + shelfLife.toLong() * (24 * 60 * 60 * 1000)
            val result = ((expirationTimeMillis - Clock.System.now().toEpochMilliseconds()) / (24 * 60 * 60 * 1000)).toInt()
            if (result > 0) return Pair(result, false) else return Pair(-result, true)
        } else {
            val expirationTimeMillis = expirationDate.toLong()
            val result = ((expirationTimeMillis - Clock.System.now().toEpochMilliseconds()) / (24 * 60 * 60 * 1000)).toInt()
            if (result > 0) return Pair(result, false) else return Pair(-result, true)
        }
    }
}