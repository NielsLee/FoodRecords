package lying.fengfeng.foodrecords.utils

actual object LaunchUtil {
    actual fun launchGithub() {
        ToastUtil.show("Launch Github")
    }

    actual fun launchEmail(thanksEmailStr: String, noEmailStr: String) {
        ToastUtil.show("Launch Email")
    }

}