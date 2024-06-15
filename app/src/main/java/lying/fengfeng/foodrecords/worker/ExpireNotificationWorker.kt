package lying.fengfeng.foodrecords.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import lying.fengfeng.foodrecords.R
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.utils.DateUtil

class ExpireNotificationWorker(
    context: Context, workerParameters: WorkerParameters
) : CoroutineWorker(
    context,
    workerParameters
) {
    override suspend fun doWork(): Result {
        val foodInfoList = AppRepo.getAllFoodInfo()
        val nearExpiredItems = foodInfoList.filter {
            DateUtil.getRemainingDays(it.productionDate, it.shelfLife, it.expirationDate).let {
                it < AppRepo.getDaysBeforeNotification() && it >= 0
            }
        }

        if (nearExpiredItems.isNotEmpty()) {
            sendNotification(nearExpiredItems.size)
        }

        return Result.success()
    }

    private fun sendNotification(size: Int) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "food_expiry_channel"
        val channelName = "Food Expiry Notifications"

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle(applicationContext.getString(R.string.expire_notification_title))
            .setContentText("$size${applicationContext.getString(R.string.expire_notification_content)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }
}