package lying.fengfeng.foodrecords.utils

import platform.UIKit.UIApplication
import platform.UIKit.UIAlertController
import platform.UIKit.UIDevice
import platform.UIKit.UIAlertControllerStyle
import platform.UIKit.UIAlertAction

actual object ToastUtil {
    actual fun show(text: String) {
        val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController?.modalViewController
        val alertController = UIAlertController.alertControllerWithTitle(
            title = UIDevice.currentDevice.systemName,
            message = text,
            preferredStyle = UIAlertControllerStyle.MAX_VALUE
        )
        alertController.addAction(
            UIAlertAction.actionWithTitle(
                "OK",
                style = UIAlertControllerStyle.MAX_VALUE,
                handler = null
            )
        )
        viewController?.presentViewController(alertController, animated = true, completion = null)
    }
}