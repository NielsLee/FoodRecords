package lying.fengfeng.foodrecords.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lying.fengfeng.foodrecords.MainActivity
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.utils.DateUtil

class ScreenOnReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_SCREEN_ON == intent.action) {

            if (!AppRepo.isNotificationEnabled()) {
                return
            }

            val currentMillis = System.currentTimeMillis()
            val nextNotificationMillis = AppRepo.getNextNotificationMillis()

            if (nextNotificationMillis in 1..currentMillis) {
                sendNotification(context.applicationContext)
                AppRepo.setNextNotificationMillis(currentMillis + DateUtil.millisFromNowTo(10))
            }
        }
    }

    private fun sendNotification(applicationContext: Context) {
        CoroutineScope(Dispatchers.IO).launch {

            val foodInfoList = AppRepo.getAllFoodInfo()
            val nearExpiredItems = foodInfoList.filter { foodInfo ->
                DateUtil.getRemainingDays(
                    foodInfo.productionDate,
                    foodInfo.shelfLife,
                    foodInfo.expirationDate
                ).let {
                    it < AppRepo.getDaysBeforeNotification() && it >= 0
                }
            }
            if (nearExpiredItems.isNotEmpty()) {

                val size = nearExpiredItems.size

                val notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channelId = "food_expiry_channel"
                val channelName = "Food Expiry Notifications"

                val intent = Intent(applicationContext, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent: PendingIntent = PendingIntent.getActivity(
                    applicationContext,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                val channel =
                    NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                notificationManager.createNotificationChannel(channel)

                val notification = NotificationCompat.Builder(applicationContext, channelId)
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentTitle(applicationContext.getString(R.string.expire_notification_title))
                    .setContentText("$size${applicationContext.getString(R.string.expire_notification_content)}")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                notificationManager.notify(1, notification)
            }
        }
    }
}