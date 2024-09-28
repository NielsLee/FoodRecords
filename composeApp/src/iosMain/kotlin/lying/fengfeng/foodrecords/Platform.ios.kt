package lying.fengfeng.foodrecords

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val version: Int
        get() = TODO("Not yet implemented")
}

actual fun getPlatform(): Platform = IOSPlatform()