package lying.fengfeng.foodrecords.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lying.fengfeng.foodrecords.entities.FoodInfo
import lying.fengfeng.foodrecords.entities.FoodTypeInfo
import lying.fengfeng.foodrecords.entities.ShelfLifeInfo
import lying.fengfeng.foodrecords.repository.AppRepo

class FoodRecordsAppViewModel: ViewModel() {

    var foodInfoList = mutableStateListOf<FoodInfo>()
    var foodTypeList = mutableStateListOf<FoodTypeInfo>()
    var shelfLifeList = mutableStateListOf<ShelfLifeInfo>()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            foodInfoList.addAll(
                withContext(Dispatchers.IO) {
                    AppRepo.getAllFoodInfo()
                }
            )
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

    fun getFoodInfoList(): List<FoodInfo> {
        return foodInfoList
    }

    fun getFoodTypeList(): List<FoodTypeInfo> {
        return foodTypeList
    }

    fun getShelfLifeList(): List<ShelfLifeInfo> {
        return shelfLifeList
    }

    fun addFoodInfo(foodInfo: FoodInfo) {
        AppRepo.addFoodInfo(foodInfo)
        CoroutineScope(Dispatchers.IO).launch {
            foodInfoList.clear()
            foodInfoList.addAll(AppRepo.getAllFoodInfo())
        }
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

    fun removeFoodInfo(foodInfo: FoodInfo) {
        foodInfoList.remove(foodInfo)
        AppRepo.removeFoodInfo(foodInfo)
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