package lying.fengfeng.foodrecords.ext

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import lying.fengfeng.foodrecords.MainActivityDelegate

actual fun ImageBitmap?.scaleToHalfScreenWidth(): ImageBitmap? {
    if (this == null) return null

    val context = MainActivityDelegate.context

    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels

    val newWidth = screenWidth / 2
    val aspectRatio = this.height.toFloat() / this.width.toFloat()
    val newHeight = (newWidth * aspectRatio).toInt()

    return Bitmap.createScaledBitmap(this.asAndroidBitmap(), newWidth, newHeight, true).asImageBitmap()
}