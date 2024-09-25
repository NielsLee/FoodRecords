package lying.fengfeng.foodrecords

import android.app.Activity
import android.app.Application
import android.content.Context

object MainActivityDelegate {

    lateinit var context: MainActivity

    fun init(mainActivity: MainActivity) {
        this.context = mainActivity
    }
}