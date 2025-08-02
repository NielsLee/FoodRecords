package lying.fengfeng.foodrecords.utils

import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.repository.AppRepo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale

object DateUtil {

    val oneDayMillis = 24 * 3600 * 1000

    fun dateWithFormat(date: Long, format: String): String {
        if (date == 0L) return ""
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(date)
    }

    fun dateTimeStamp(dateString: String): Long {
        val dateFormatter = SimpleDateFormat(AppRepo.getDateFormat(), Locale.getDefault())
        return dateFormatter.parse(dateString)?.time ?: 0
    }

    fun todayMillis(): Long {
        val localDate = LocalDate.now()
        val startTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT)
        val startInstant = startTime.toInstant(ZoneOffset.UTC)
        val startTimestamp = startInstant.toEpochMilli()
        return startTimestamp
    }

    fun getRemainingDays(foodInfo: FoodInfo): Pair<Int, Boolean> {
        val productionDate = foodInfo.productionDate
        val shelfLife = foodInfo.shelfLife
        val expirationDate = foodInfo.expirationDate
        // 加1是因爲保質期當天不算過期
        if (expirationDate == "0") {
            val todayBeginningMillis = getBeginningMillis(System.currentTimeMillis())
            val expirationTimeMillis = getBeginningMillis(productionDate.toLong()) + shelfLife.toLong() * oneDayMillis
            val result = ((expirationTimeMillis - todayBeginningMillis) / oneDayMillis).toInt() + 1
            return if (result > 0) Pair(result, false) else Pair(-result, true)
        } else {
            val result = ((getBeginningMillis(expirationDate.toLong()) - getBeginningMillis(System.currentTimeMillis()))/ oneDayMillis)
                .toInt() + 1
            return if (result > 0) Pair(result, false) else Pair(-result, true)
        }
    }

    fun getExpirationDate(foodInfo: FoodInfo): String {
        val productionDate = foodInfo.productionDate
        val shelfLife = foodInfo.shelfLife
        val expirationDate = foodInfo.expirationDate
        val dateFormatter = SimpleDateFormat(AppRepo.getDateFormat(), Locale.getDefault())
        if (expirationDate == "0") {
            val productionTimeMillis = productionDate.toLong()
            val expirationTimeMillis = productionTimeMillis + shelfLife.toLong() * 24 * 60 * 60 * 1000
            return dateFormatter.format(expirationTimeMillis)
        } else {
            return dateFormatter.format(expirationDate.toLong())
        }
    }

    fun validateDateFormat(date: String): String {
        val dateFormatter = SimpleDateFormat(AppRepo.getDateFormat(), Locale.getDefault())
        return try {
            val parsedDate = dateFormatter.parse(date)
            parsedDate?.time.toString()
        } catch (e: ParseException) {
            "0"
        }
    }

    fun millisFromNowTo(hour: Int): Long {
        val currentTime = Calendar.getInstance()
        val notificationTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        if (currentTime.after(notificationTime)) {
            notificationTime.add(Calendar.DAY_OF_YEAR, 1)
        }
        return notificationTime.timeInMillis - currentTime.timeInMillis
    }

    private fun getBeginningMillis(ts: Long): Long {
        return (ts - (ts % oneDayMillis))
    }
}
