package lying.fengfeng.foodrecords.cache

import lying.fengfeng.foodrecords.FoodRecordDatabase
import lying.fengfeng.foodrecords.entity.FoodInfo
import lying.fengfeng.foodrecords.entity.FoodTypeInfo
import lying.fengfeng.foodrecords.entity.ShelfLifeInfo

class FoodRecordDatabase(factory: DatabaseDriverFactory) {
    private val foodRecordDatabase = FoodRecordDatabase(factory.createDriver())
    private val foodInfoQuery = foodRecordDatabase.foodInfoDatabseQueries

//    private val foodTypeInfoDatabase = FoodTypeInfoDatabase(factory.createDriver())
//    private val foodTypeQuery = foodRecordDatabase.foodTypeInfoDatabaseQueries
//
//    private val shelfLifeInfoDatabase = ShelfLifeInfoDatabase(factory.createDriver())
//    private val shelfLifeQuery = shelfLifeInfoDatabase.shelfLifeInfoDatabaseQueries

    fun getAllFoodInfo(): List<FoodInfo> {
        return foodInfoQuery.getAllFoodInfo().executeAsList().map {
            FoodInfo(
                it.foodName,
                it.productionDate,
                it.foodType,
                it.shelfLife,
                it.expirationDate,
                it.uuid,
                it.amount.toInt(),
                it.tips
            )
        }
    }

    fun insertFoodInfo(foodInfo: FoodInfo) {
        foodInfoQuery.insertFoodInfo(
            foodInfo.foodName,
            foodInfo.productionDate,
            foodInfo.foodType,
            foodInfo.shelfLife,
            foodInfo.expirationDate,
            foodInfo.uuid,
            foodInfo.amount.toLong(),
            foodInfo.tips
        )
    }

    fun removeFoodInfo(foodInfo: FoodInfo) {
        foodInfoQuery.deleteFoodInfo(foodInfo.uuid)
    }

    fun getAllFoodTypeInfo(): List<FoodTypeInfo> {
        return foodInfoQuery.selectAllFoodTypeInfo().executeAsList().map {
            FoodTypeInfo(it)
        }
    }

    fun insertFoodTypeInfo(foodTypeInfo: FoodTypeInfo) {
        foodInfoQuery.insertFoodTypeInfo(foodTypeInfo.type)
    }

    fun removeFoodTypeInfo(foodTypeInfo: FoodTypeInfo) {
        foodInfoQuery.deleteFoodTypeInfo(foodTypeInfo.type)
    }

    fun getAllShelfLifeInfo(): List<ShelfLifeInfo> {
        return foodInfoQuery.selectAllShelfLifeInfo().executeAsList().map {
            ShelfLifeInfo(it)
        }
    }

    fun insertShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        foodInfoQuery.insertShelfLifeInfo(shelfLifeInfo.life)
    }

    fun removeShelfLifeInfo(shelfLifeInfo: ShelfLifeInfo) {
        foodInfoQuery.deleteShelfLifeInfo(shelfLifeInfo.life)
    }
}