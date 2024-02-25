package lying.fengfeng.foodrecords.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {

    fun dateWithFormat(date: Long, format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(date)
    }

    fun todayMillis(): Long {
        return System.currentTimeMillis().let {
            (it - it % 86400000)
        }
    }
}