package lying.fengfeng.foodrecords.cache

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import lying.fengfeng.foodrecords.FoodRecordDatabase

class ShelfLifeInfoDatabaseDriver(private val context: Context) : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(FoodRecordDatabase.Schema, context, "shelf_life_info.db")
    }
}