package lying.fengfeng.foodrecords.ext

import android.content.Context
import android.graphics.Bitmap

fun Bitmap?.scaleToHalfScreenWidth(context: Context): Bitmap? {
    if (this == null) return null

    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels

    val newWidth = screenWidth / 2
    val aspectRatio = this.height.toFloat() / this.width.toFloat()
    val newHeight = (newWidth * aspectRatio).toInt()

    return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
}