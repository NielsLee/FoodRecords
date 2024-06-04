package lying.fengfeng.foodrecords.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
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

    fun getRemainingDays(dateString: String, shelfLife: String): Int {
        val passedDays = ((System.currentTimeMillis() - dateTimeStamp(dateString)) / (24 * 60 * 60 * 1000)).toInt()
        val shelfDays = shelfLife.toInt()
        return shelfDays - passedDays
    }
}