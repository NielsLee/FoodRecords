package lying.fengfeng.foodrecords

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import lying.fengfeng.foodrecords.notification.ScreenOnReceiver
import lying.fengfeng.foodrecords.repository.AppRepo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FoodRecordsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepo.init(this)

        val uuid = AppRepo.getOrCreateUuid()
        val openTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        try {
            Statistic.uploadOpenData(uuid, openTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val screenOnReceiver = ScreenOnReceiver()
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        registerReceiver(screenOnReceiver, filter)
    }
}
