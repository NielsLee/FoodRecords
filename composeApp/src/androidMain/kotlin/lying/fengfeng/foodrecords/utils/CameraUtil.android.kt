package lying.fengfeng.foodrecords.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import lying.fengfeng.foodrecords.MainActivityDelegate

actual object CameraUtil {
    actual fun checkCameraPermission(): Boolean {
        val context = MainActivityDelegate.context
        val permission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        return permission == PackageManager.PERMISSION_GRANTED
    }

    actual fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            MainActivityDelegate.context,
            arrayOf(Manifest.permission.CAMERA),
            888
        )
    }
}