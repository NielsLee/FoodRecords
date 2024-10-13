package lying.fengfeng.foodrecords.utils

import lying.fengfeng.foodrecords.entity.FoodInfo
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel

expect object FileUtil {
    fun isFileExist(filePath: String): Boolean

    fun getPictureFilePath(uuid: String): String

    fun deleteFood(appViewModel: FoodRecordsAppViewModel, foodInfo: FoodInfo)
}