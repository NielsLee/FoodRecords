package lying.fengfeng.foodrecords

import android.app.Application
import lying.fengfeng.foodrecords.repository.FoodInfoRepo

class FoodRecordsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FoodInfoRepo.init(this)
    }
}