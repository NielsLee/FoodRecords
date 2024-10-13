package lying.fengfeng.foodrecords

import android.os.Build

class AndroidPlatform() : Platform {
    override val name: String = "Android"

    override val version: String = Build.VERSION.SDK_INT.toString()
}

actual fun getPlatform(): Platform = AndroidPlatform()