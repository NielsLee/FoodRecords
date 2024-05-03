package lying.fengfeng.foodrecords.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import androidx.compose.ui.graphics.toArgb
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
            val bitmap = Bitmap.createBitmap(300, 400, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = android.graphics.Paint().apply {
                color = Color.argb(127, androidx.compose.ui.graphics.Color.Black.toArgb(), androidx.compose.ui.graphics.Color.Black.toArgb(), androidx.compose.ui.graphics.Color.Black.toArgb())
            }
            canvas.drawRect(0f, 0f, 300f, 400f, paint)
            return bitmap
        }
    }
}