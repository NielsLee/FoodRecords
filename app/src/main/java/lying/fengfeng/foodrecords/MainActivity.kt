package lying.fengfeng.foodrecords

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.work.WorkManager
import androidx.work.WorkQuery
import lying.fengfeng.foodrecords.ui.FoodRecordsApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodRecordsApp()
        }
        WorkManager.getInstance(this.applicationContext)
                .getWorkInfosLiveData(WorkQuery.fromTags("FridgeyNotification"))
            .observe(this) {
                Log.d("LLF", "workInfo: ${it}")
            }
    }
}