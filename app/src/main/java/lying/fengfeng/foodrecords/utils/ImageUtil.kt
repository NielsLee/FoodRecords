package lying.fengfeng.foodrecords.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.FileNotFoundException


object ImageUtil {

    fun preProcessImage(imageFilePath: String): Bitmap? {
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            val exifInterface = ExifInterface(imageFilePath)
            // 获取图片的旋转信息
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val degree = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            return BitmapFactory.decodeFile(imageFilePath, BitmapFactory.Options())?.let { bitmap ->

                val matrix = Matrix()
                matrix.reset()
                matrix.postRotate(degree.toFloat());

                Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
            }
        } catch (e: FileNotFoundException) {
            return null
        }
    }
}