package lying.fengfeng.foodrecords.utils

import lying.fengfeng.foodrecords.entity.FoodInfo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale

actual object DateUtil {

    actual fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat(AppRepo.getDateFormat(), Locale.getDefault())
        return dateFormat.format(Date())
    }

    actual fun dateWithFormat(date: Long, format: String): String {
        if (date == 0L) return ""
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(date)
    }

    actual fun todayMillis(): Long {
        val localDate = LocalDate.now()
        val startTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT)
        val startInstant = startTime.toInstant(ZoneOffset.UTC)
        val startTimestamp = startInstant.toEpochMilli()
        return startTimestamp
    }

    actual fun getRemainingDays(foodInfo: FoodInfo): Pair<Int, Boolean> {
        val productionDate = foodInfo.productionDate
        val shelfLife = foodInfo.shelfLife
        val expirationDate = foodInfo.expirationDate
        if (expirationDate == "0") {
            val productionTimeMillis = productionDate.toLong()
            val expirationTimeMillis = productionTimeMillis + shelfLife.toLong() * (24 * 60 * 60 * 1000)
            val result = ((expirationTimeMillis - System.currentTimeMillis()) / (24 * 60 * 60 * 1000)).toInt()
            return if (result > 0) Pair(result, false) else Pair(-result, true)
        } else {
            val expirationTimeMillis = expirationDate.toLong()
            val result = ((expirationTimeMillis - System.currentTimeMillis()) / (24 * 60 * 60 * 1000)).toInt()
            return if (result > 0) Pair(result, false) else Pair(-result, true)
        }
    }

    actual fun getExpirationDate(foodInfo: FoodInfo): String {
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

    actual fun validateDateFormat(date: String): String {
        val dateFormatter = SimpleDateFormat(AppRepo.getDateFormat(), Locale.getDefault())
        return try {
            val parsedDate = dateFormatter.parse(date)
            parsedDate?.time.toString()
        } catch (e: ParseException) {
            "0"
        }
    }

    actual fun millisFromNowTo(hour: Int): Long {
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
}
