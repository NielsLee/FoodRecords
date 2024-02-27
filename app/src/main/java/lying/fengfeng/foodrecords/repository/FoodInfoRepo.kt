package lying.fengfeng.foodrecords.repository

import android.app.Application

object FoodInfoRepo {

    private lateinit var db: FoodInfoDatabaseHelper
    private lateinit var app: Application

    fun init(application: Application) {
        app = application
        db = FoodInfoDatabaseHelper(application)
    }

    fun getAbsolutePictureDir(): String {
        return app.filesDir.absolutePath + "/"
    }

}
