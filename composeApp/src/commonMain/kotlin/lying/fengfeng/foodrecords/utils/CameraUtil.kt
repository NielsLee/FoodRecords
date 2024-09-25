package lying.fengfeng.foodrecords.utils

expect object CameraUtil {
    fun checkCameraPermission(): Boolean

    fun requestCameraPermission()
}