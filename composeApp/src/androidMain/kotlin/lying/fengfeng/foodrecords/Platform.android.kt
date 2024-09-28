package lying.fengfeng.foodrecords

import android.os.Build

class AndroidPlatform() : Platform {
    override val name: String = "Android"

    override val version: Int = Build.VERSION.SDK_INT
}

actual fun getPlatform(): Platform = AndroidPlatform()