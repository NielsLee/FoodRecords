package lying.fengfeng.foodrecords.storage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FoodInfoDatabaseHelper(context: Context): SQLiteOpenHelper(context, "Name", null, 100) {
    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}