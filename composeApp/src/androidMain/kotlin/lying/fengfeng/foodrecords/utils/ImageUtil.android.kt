package lying.fengfeng.foodrecords.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import lying.fengfeng.foodrecords.MainActivityDelegate
import java.io.FileNotFoundException


actual object ImageUtil {

    fun preProcessImage(imageFilePath: String): ImageBitmap? {
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
            }?.asImageBitmap()
        } catch (e: FileNotFoundException) {
            return null
        }
    }

    fun getImageSize(imageFilePath: String): Pair<Int, Int> {

        val exifInterface = ExifInterface(imageFilePath)

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

        BitmapFactory.decodeFile(imageFilePath, BitmapFactory.Options()).let { bitmap ->
            if (degree == 0 || degree == 180) {
                return Pair(bitmap.width, bitmap.height)
            } else {
                return Pair(bitmap.height, bitmap.width)
            }
        }
    }

    fun isImageLandscape(imageFilePath: String): Boolean {
        val exifInterface = ExifInterface(imageFilePath)

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

        return !(degree == 0 || degree == 180)
    }

    actual fun getImageBitmapFromFile(path: String): ImageBitmap {
        return Glide.with(MainActivityDelegate.context as Context).asBitmap()
            .load(path)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .skipMemoryCache(true)
            .submit().get().asImageBitmap()
    }
}