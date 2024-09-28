package lying.fengfeng.foodrecords.utils

import androidx.compose.ui.graphics.ImageBitmap

expect object ImageUtil {
    fun getImageBitmapFromFile(path: String): ImageBitmap

    fun preProcessImage(imageFilePath: String): ImageBitmap?

    fun createPreviewBitmap(): ImageBitmap
}