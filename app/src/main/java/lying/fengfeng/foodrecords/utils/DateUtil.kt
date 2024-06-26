package lying.fengfeng.foodrecords.utils

import lying.fengfeng.foodrecords.entities.FoodInfo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale

object DateUtil {

    fun dateWithFormat(date: Long, format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(date)
    }

    fun dateTimeStamp(dateString: String): Long {
        val dateFormatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        return dateFormatter.parse(dateString)?.time ?: 0
    }

    fun todayMillis(): Long {
        val localDate = LocalDate.now()
        val startTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT)
        val startInstant = startTime.toInstant(ZoneOffset.UTC)
        val startTimestamp = startInstant.toEpochMilli()
        return startTimestamp
    }

    fun getRemainingDays(productionDate: String, shelfLife: String, expirationDate: String): Int {
        val dateFormatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        return try {
            dateFormatter.parse(expirationDate)
            val expirationTimeMillis = dateFormatter.parse(expirationDate).time
            val remainingMillis = expirationTimeMillis - System.currentTimeMillis()
            (remainingMillis / (24 * 60 * 60 * 1000)).toInt()
        } catch (e: ParseException) {
            try {
                val productionTimeMillis = dateFormatter.parse(productionDate).time
                val expirationTimeMillis = productionTimeMillis + shelfLife.toInt() * (24 * 60 * 60 * 1000)
                ((expirationTimeMillis - System.currentTimeMillis()) / (24 * 60 * 60 * 1000)).toInt()
            } catch (e: ParseException) {
                0
            }
        }
    }

    fun getExpirationDate(foodInfo: FoodInfo): String {
        val productionDate = foodInfo.productionDate
        val shelfLife = foodInfo.shelfLife
        val expirationDate = foodInfo.expirationDate
        val dateFormatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        return try {
            dateFormatter.parse(expirationDate)
            expirationDate
        } catch (e: ParseException) {
            try {
                val productionTimeMillis = dateFormatter.parse(productionDate).time
                val expirationTimeMillis = productionTimeMillis + shelfLife.toInt() * 24 * 60 * 60 * 1000
                dateFormatter.format(expirationTimeMillis)
            } catch (e: ParseException) {
                "--"
            }
        }
    }

    fun validateDateFormat(date: String): String {
        val dateFormatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        return try {
            dateFormatter.parse(date)
            date
        } catch (e: ParseException) {
            "--"
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
}