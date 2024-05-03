package lying.fengfeng.foodrecords.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {

    fun dateWithFormat(date: Long, format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(date)
    }

    fun dateTimeStamp(dateString: String): Long {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormatter.parse(dateString)?.time ?: 0
    }

    fun todayMillis(): Long {
        return System.currentTimeMillis().let {
            (it - it % 86400000)
        }
    }

    fun getRemainingDays(dateString: String, shelfLife: String): Int {
        val passedDays = ((System.currentTimeMillis() - dateTimeStamp(dateString)) / (24 * 60 * 60 * 1000)).toInt()
        val shelfDays = shelfLife.toInt()
        return shelfDays - passedDays
    }
}