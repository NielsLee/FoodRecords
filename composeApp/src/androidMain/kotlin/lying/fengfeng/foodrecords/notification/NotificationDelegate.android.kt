package lying.fengfeng.foodrecords.notification

import AppRepo.setNotificationEnabled
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import lying.fengfeng.foodrecords.MainActivityDelegate
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.utils.DateUtil

actual object NotificationDelegate {

    actual fun enableNotification() {
        val context = MainActivityDelegate.context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                try {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        888
                    )
                } catch (e: Exception) {
                    Toast.makeText(context, context.getString(R.string.notification_permisson_failed), Toast.LENGTH_SHORT).show()
                }
            } else {
                scheduleNotifications(context)
            }
        } else {
            scheduleNotifications(context)
        }
    }

    actual fun disableNotification() {
        Toast.makeText(MainActivityDelegate.context, "⏰❌", Toast.LENGTH_SHORT).show()
        setNotificationEnabled(false)

        AppRepo.setNextNotificationMillis(-1)
    }

    private fun scheduleNotifications(context: Context) {
        Toast.makeText(context, "⏰✅", Toast.LENGTH_SHORT).show()
        setNotificationEnabled(true)

        AppRepo.setNextNotificationMillis(System.currentTimeMillis() + DateUtil.millisFromNowTo(10))
    }

}