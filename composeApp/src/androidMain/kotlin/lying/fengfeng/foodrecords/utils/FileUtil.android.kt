package lying.fengfeng.foodrecords.utils

import lying.fengfeng.foodrecords.entity.FoodInfo
import lying.fengfeng.foodrecords.ui.FoodRecordsAppViewModel
import java.io.File

actual object FileUtil {
    actual fun isFileExist(filePath: String): Boolean {
        return File(filePath).exists() && File(filePath).length() > 0
    }

    actual fun getPictureFilePath(uuid: String): String {
        return AppRepo.getPicturePath(uuid)
    }

    actual fun deleteFood(
        appViewModel: FoodRecordsAppViewModel,
        foodInfo: FoodInfo
    ) {
        File(AppRepo.getPicturePath(foodInfo.uuid)).also {
            if (it.exists()) {
                it.delete()
            }
        }
        appViewModel.removeFoodInfo(foodInfo)
    }
}