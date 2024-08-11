package lying.fengfeng.foodrecords

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import lying.fengfeng.foodrecords.repository.AppRepo
import lying.fengfeng.foodrecords.repository.FoodInfoDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        FoodInfoDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        var db = helper.createDatabase("FoodInfoDatabase", 1).apply {

            // Prepare for the next version.
            close()
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE FoodInfo ADD COLUMN expirationDate TEXT NOT NULL DEFAULT '' ")
                db.execSQL("ALTER TABLE FoodInfo ADD COLUMN tips TEXT NOT NULL DEFAULT '' ")
            }
        }

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate("FoodInfoDatabase", 2, true, MIGRATION_1_2)

        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        var db = helper.createDatabase("FoodInfoDatabase", 2).apply {

            val contentValues = ContentValues().apply {
                put("foodName", "FoodName")
                put("productionDate", "24-07-30")
                put("foodType", "Test")
                put("shelfLife", "5")
                put("expirationDate", "--")
                put("uuid", "uuid-test")
                put("tips", "")
            }

            insert("FoodInfo", SQLiteDatabase. CONFLICT_REPLACE, contentValues)
            close()
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {

                val cursor = db.query("SELECT uuid, productionDate, expirationDate FROM FoodInfo",
                    emptyArray())
                if (cursor.moveToFirst()) {
                    do {
                        val uuid = cursor.getString(cursor.getColumnIndex("uuid"))
                        val productionDate = cursor.getString(cursor.getColumnIndex("productionDate"))
                        val expirationDate = cursor.getString(cursor.getColumnIndex("expirationDate"))

                        val dateFormatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
                        val productionDateTimestamp = dateFormatter.parse(productionDate)?.time ?: 0
                        val expirationDateTimestamp = if (expirationDate == "--") 0 else dateFormatter.parse(expirationDate)?.time ?: 0
                        val updateQuery = "UPDATE FoodInfo SET productionDate = ?, expirationDate = ? " +
                                "WHERE uuid = ?"

                        db.execSQL(updateQuery, arrayOf(productionDateTimestamp, expirationDateTimestamp,
                            uuid))

                    } while (cursor.moveToNext())
                }
                cursor.close()

            }
        }

        db = helper.runMigrationsAndValidate("FoodInfoDatabase", 3, true, MIGRATION_2_3)
    }

    @Test
    @Throws(IOException::class)
    fun migrate3To4() {
        var db = helper.createDatabase("FoodInfoDatabase", 3).apply {
            close()
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE FoodInfo ADD COLUMN amount INTEGER NOT NULL DEFAULT 1")
            }
        }

        db = helper.runMigrationsAndValidate("FoodInfoDatabase", 3, true, MIGRATION_3_4)
    }
}