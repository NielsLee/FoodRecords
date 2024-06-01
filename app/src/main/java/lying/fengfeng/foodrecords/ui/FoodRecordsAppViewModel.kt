package lying.fengfeng.foodrecords.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.entities.FoodTypeInfo
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo
import lying.fengfeng.foodrecords.repository.AppRepo

class FoodRecordsAppViewModel: ViewModel() {

    var foodTypeList = mutableStateListOf<FoodTypeInfo>()
    var shelfLifeList = mutableStateListOf<ShelfLifeInfo>()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            foodTypeList.addAll(
                withContext(Dispatchers.IO) {
                    AppRepo.getAllTypeInfo()
                }
            )
            shelfLifeList.addAll(
                withContext(Dispatchers.IO) {
                    AppRepo.getAllShelfLifeInfo()
                }
            )
        }
    }

    fun getFoodTypeList(): List<FoodTypeInfo> {
        return foodTypeList
    }

    fun getShelfLifeList(): List<ShelfLifeInfo> {
        return shelfLifeList
    }

    fun addFoodTypeInfo(foodTypeInfo: FoodTypeInfo) {
        AppRepo.addTypeInfo(foodTypeInfo)
        CoroutineScope(Dispatchers.IO).launch {
            foodTypeList.clear()
            foodTypeList.addAll(AppRepo.getAllTypeInfo())
        }
    }

    fun addShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        AppRepo.addShelfLifeInfo(shelfLifeInfo)
        CoroutineScope(Dispatchers.IO).launch {
            shelfLifeList.clear()
            shelfLifeList.addAll(AppRepo.getAllShelfLifeInfo())
        }
    }

    fun removeFoodTypeInfo(foodTypeInfo: FoodTypeInfo) {
        foodTypeList.remove(foodTypeInfo)
        AppRepo.removeTypeInfo(foodTypeInfo)
    }

    fun removeShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        shelfLifeList.remove(shelfLifeInfo)
        AppRepo.removeShelfLifeInfo(shelfLifeInfo)
    }
}