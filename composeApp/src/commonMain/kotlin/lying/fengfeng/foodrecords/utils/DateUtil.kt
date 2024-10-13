package lying.fengfeng.foodrecords.utils

import lying.fengfeng.foodrecords.entity.FoodInfo

expect object DateUtil {

    fun getCurrentDate(): String

    fun dateWithFormat(date: Long, format: String): String

    fun todayMillis(): Long

    fun getRemainingDays(foodInfo: FoodInfo): Pair<Int, Boolean>

    fun getExpirationDate(foodInfo: FoodInfo): String

    fun validateDateFormat(date: String): String

    fun millisFromNowTo(hour: Int): Long
}