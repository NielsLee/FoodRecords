package lying.fengfeng.foodrecords.ext

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun ImageBitmap?.scaleToHalfScreenWidth(context: Context): ImageBitmap? {
    if (this == null) return null

    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels

    val newWidth = screenWidth / 2
    val aspectRatio = this.height.toFloat() / this.width.toFloat()
    val newHeight = (newWidth * aspectRatio).toInt()

    return Bitmap.createScaledBitmap(this.asAndroidBitmap(), newWidth, newHeight, true).asImageBitmap()
}