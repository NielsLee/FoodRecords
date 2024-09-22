package lying.fengfeng.foodrecords

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import lying.fengfeng.foodrecords.notification.ScreenOnReceiver
import lying.fengfeng.foodrecords.repository.AppRepo


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
