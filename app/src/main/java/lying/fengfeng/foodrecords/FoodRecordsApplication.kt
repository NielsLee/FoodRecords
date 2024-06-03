package lying.fengfeng.foodrecords

import android.app.Application
import lying.fengfeng.foodrecords.repository.AppRepo

class FoodRecordsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepo.init(this)
    }
}
