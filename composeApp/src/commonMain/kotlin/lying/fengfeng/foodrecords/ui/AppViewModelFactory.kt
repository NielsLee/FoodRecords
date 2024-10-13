package lying.fengfeng.foodrecords.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class AppViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == FoodRecordsAppViewModel::class) {
            return FoodRecordsAppViewModel() as T
        }
        throw IllegalArgumentException("illegal ViewModel class")
    }
}