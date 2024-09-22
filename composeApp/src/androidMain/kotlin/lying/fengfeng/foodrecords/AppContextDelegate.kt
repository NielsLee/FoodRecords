package lying.fengfeng.foodrecords

import android.app.Application

object AppContextDelegate {

    lateinit var context: Application

    fun init(appContext: Application) {
        this.context = appContext
    }
}