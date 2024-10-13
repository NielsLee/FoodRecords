package lying.fengfeng.foodrecords.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import lying.fengfeng.foodrecords.FoodRecordDatabase

class FoodInfoDatabaseDriver: DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(FoodRecordDatabase.Schema, "food_info.db")
    }
}