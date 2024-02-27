package lying.fengfeng.foodrecords.repository

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FoodInfoDatabaseHelper(application: Application): SQLiteOpenHelper(application, "Name", null, 100) {

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}