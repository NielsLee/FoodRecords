package lying.fengfeng.foodrecords

import AppRepo
import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import lying.fengfeng.foodrecords.notification.ScreenOnReceiver


class FoodRecordsApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        AppRepo.init(this)
        AppContextDelegate.init(this)

        val screenOnReceiver = ScreenOnReceiver()
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        registerReceiver(screenOnReceiver, filter)
    }
}
