package lying.fengfeng.foodrecords.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.utils.DateUtil


class HomeViewModel: ViewModel() {

    var foodInfoList = mutableStateOf(listOf<FoodInfo>())

    fun updateList(list: List<FoodInfo>) {
        foodInfoList.value = list.sortedBy {
            DateUtil.getRemainingDays(it.productionDate, it.shelfLife)
        }
    }
}