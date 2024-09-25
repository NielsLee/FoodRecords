package lying.fengfeng.foodrecords.utils

import android.widget.Toast
import lying.fengfeng.foodrecords.MainActivityDelegate

actual object ToastUtil {
    actual fun show(text: String) {
        Toast.makeText(MainActivityDelegate.context, text, Toast.LENGTH_SHORT).show()
    }
}