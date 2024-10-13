package lying.fengfeng.foodrecords.utils

import AppRepo
import lying.fengfeng.foodrecords.entity.FoodInfo
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.timeIntervalSinceDate

actual object DateUtil {
    actual fun getCurrentDate(): String {
        val dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = AppRepo.getDateFormat()
        dateFormatter.locale = NSLocale.currentLocale

        val currentDate = NSDate()
        return dateFormatter.stringFromDate(currentDate)
    }

    actual fun dateWithFormat(date: Long, format: String): String {
        if (date == 0L) return ""
        val dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = format
        dateFormatter.locale = NSLocale.currentLocale
        val dateSecond = date / 1000.0

        return dateFormatter.stringFromDate(NSDate.dateWithTimeIntervalSince1970(dateSecond))
    }

    actual fun todayMillis(): Long {
        val calendar = NSCalendar.currentCalendar
        val components = calendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = NSDate()
        )

        components.hour = 0
        components.minute = 0
        components.second = 0

        val midnightDate = calendar.dateFromComponents(components) ?: NSDate()

        return (midnightDate.timeIntervalSince1970.times(1000)).toLong()
    }

    actual fun getRemainingDays(foodInfo: FoodInfo): Pair<Int, Boolean> {
        val productionDate = foodInfo.productionDate
        val shelfLife = foodInfo.shelfLife
        val expirationDate = foodInfo.expirationDate

        val calendar = NSCalendar.currentCalendar
        val currentDate = NSDate()

        if (expirationDate == "0") {
            // 将生产日期从字符串转为时间戳（毫秒）
            val productionTimeMillis = productionDate.toLong() / 1000.0 // iOS 中需要转换为秒

            // 使用生产时间戳创建 NSDate 对象
            val productionNSDate = NSDate.dateWithTimeIntervalSince1970(productionTimeMillis)

            // 计算保质期（天数）后得到的到期日期
            val expirationNSDate = calendar.dateByAddingUnit(
                NSCalendarUnitDay,
                shelfLife.toLong(),
                productionNSDate,
                options = 0u
            ) ?: NSDate()

            // 计算当前时间与到期时间的差值（天数）
            val timeInterval = expirationNSDate.timeIntervalSinceDate(currentDate)
            val result = (timeInterval / (24 * 60 * 60)).toInt()

            return if (result > 0) Pair(result, false) else Pair(-result, true)
        } else {
            // expirationDate 不为 "0" 时，直接计算与当前时间的差值
            val expirationTimeMillis = expirationDate.toLong() / 1000.0 // iOS 需要使用秒
            val expirationNSDate = NSDate.dateWithTimeIntervalSince1970(expirationTimeMillis)

            // 计算当前时间与到期时间的差值（天数）
            val timeInterval = expirationNSDate.timeIntervalSinceDate(currentDate)
            val result = (timeInterval / (24 * 60 * 60)).toInt()

            return if (result > 0) Pair(result, false) else Pair(-result, true)
        }
    }

    actual fun getExpirationDate(foodInfo: FoodInfo): String {
        val dateFormatter = NSDateFormatter().apply {
            dateFormat = AppRepo.getDateFormat()
            locale = NSLocale.currentLocale
        }

        // If expirationDate is "0", calculate it using shelfLife
        if (foodInfo.expirationDate == "0") {
            // convert milliseconds to seconds
            val productionDateSecond = foodInfo.productionDate.toDouble() / 1000
            val shelfLifeDays = foodInfo.shelfLife.toInt()
            val expirationDataSecond = productionDateSecond + (shelfLifeDays * 3600 * 24)

            return dateFormatter.stringFromDate(NSDate.dateWithTimeIntervalSince1970(expirationDataSecond))
        } else {
            // Otherwise, directly format the expirationDate
            val expirationDateSecond = foodInfo.expirationDate.toDouble() / 1000
            val expirationNSDate = NSDate.dateWithTimeIntervalSince1970(expirationDateSecond)
            return dateFormatter.stringFromDate(expirationNSDate)
        }
    }

    actual fun validateDateFormat(date: String): String {
        val dateFormatter = NSDateFormatter().apply {
            dateFormat = AppRepo.getDateFormat()
            locale = NSLocale.currentLocale
        }

        return try {
            val parsedDate = dateFormatter.dateFromString(date)
            // Convert seconds to milliseconds
            (parsedDate?.timeIntervalSince1970?.times(1000))?.toLong()?.toString() ?: "0"
        } catch (e: Exception) {
            "0"
        }
    }

    actual fun millisFromNowTo(hour: Int): Long {
        val calendar = NSCalendar.currentCalendar
        val currentTime = NSDate()

        // 获取当前日期的组件
        val currentComponents = calendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond,
            fromDate = currentTime
        )

        // 创建目标通知时间的组件
        val notificationComponents = calendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = currentTime
        ).apply {
            this.hour = hour.toLong()
            this.minute = 0
            this.second = 0
        }

        // 将目标时间转换为 NSDate
        var notificationTime = calendar.dateFromComponents(notificationComponents) ?: NSDate()

        // 如果当前时间已经超过了设定的小时，需要将通知时间加一天
        if (currentTime.timeIntervalSinceDate(notificationTime) > 0) {
            notificationTime = calendar.dateByAddingUnit(
                NSCalendarUnitDay,
                1,
                notificationTime,
                options = 0u
            ) ?: NSDate()
        }

        // 返回从当前时间到目标通知时间的毫秒差值
        return (notificationTime.timeIntervalSinceDate(currentTime) * 1000).toLong()
    }

}