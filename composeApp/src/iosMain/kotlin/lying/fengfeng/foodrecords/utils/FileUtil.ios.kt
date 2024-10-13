package lying.fengfeng.foodrecords.utils

import lying.fengfeng.foodrecords.entity.FoodInfo
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel

actual object FileUtil {
    actual fun isFileExist(filePath: String): Boolean {
        TODO("Not yet implemented")
    }

    actual fun getPictureFilePath(uuid: String): String {
        TODO("Not yet implemented")
    }

    actual fun deleteFood(
        appViewModel: FoodRecordsAppViewModel,
        foodInfo: FoodInfo
    ) {
        // TODO remove picture file
        appViewModel.removeFoodInfo(foodInfo)
    }
}