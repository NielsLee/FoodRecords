package lying.fengfeng.foodrecords.utils

import lying.fengfeng.foodrecords.App
import lying.fengfeng.foodrecords.repository.AppRepo
import java.io.File

actual object FileUtil {
    actual fun isFileExist(filePath: String): Boolean {
        return File(filePath).exists() && File(filePath).length() > 0
    }

    actual fun getPictureFilePath(uuid: String): String {
        return AppRepo.getPicturePath(uuid)
    }
}