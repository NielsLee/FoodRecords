package lying.fengfeng.foodrecords.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeParseException
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

    fun getRemainingDays(dateString: String, shelfLife: String, expirationDate: String): Int {
        if (expirationDate.isEmpty()) {
            val passedDays =
                ((System.currentTimeMillis() - dateTimeStamp(dateString)) / (24 * 60 * 60 * 1000)).toInt()
            val shelfDays = shelfLife.toInt()
            return shelfDays - passedDays
        } else {
            val dateFormatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
            val parsedExpirationDate = dateFormatter.parse(expirationDate)?.time ?: 0
            val remainingMillis = parsedExpirationDate - System.currentTimeMillis()
            return (remainingMillis / (24 * 60 * 60 * 1000)).toInt()
        }
    }

    fun getExpirationDate(productionDate: String, shelfLife: String): String {
        return try {
            val dateFormatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
            val parsedProductionDate = dateFormatter.parse(productionDate)?.time ?: 0
            val shelfLifeDays = shelfLife.toLong() * (24 * 60 * 60 * 1000)
            val expirationDate = parsedProductionDate + shelfLifeDays
            dateFormatter.format(expirationDate)
        } catch (e: DateTimeParseException) {
            "Invalid date format"
        } catch (e: NumberFormatException) {
            "Invalid shelf life"
        }
    }
}