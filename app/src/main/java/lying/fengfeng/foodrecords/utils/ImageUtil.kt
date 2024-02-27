package lying.fengfeng.foodrecords.utils

import androidx.exifinterface.media.ExifInterface
import java.io.FileInputStream
import java.io.IOException


object ImageUtil {

    fun getImageOrientation(imagePath: String): Int {
        var orientation = ExifInterface.ORIENTATION_NORMAL
        try {
            val inputStream = FileInputStream(imagePath)
            val exifInterface = ExifInterface(inputStream)
            orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }
}