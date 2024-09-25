package lying.fengfeng.foodrecords.utils

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import lying.fengfeng.foodrecords.MainActivityDelegate

actual object LaunchUtil {
    actual fun launchGithub() {

        val context = MainActivityDelegate.context

        val githubPackageName = "com.github.android"
        val githubRepoUrl = "https://github.com/NielsLee/FoodRecords"
        val githubAppUri = Uri.parse("github://github.com/NielsLee/FoodRecords")
        val webUri = Uri.parse(githubRepoUrl)

        val githubIntent = Intent(Intent.ACTION_VIEW, githubAppUri).apply {
            setPackage(githubPackageName)
        }

        val webIntent = Intent(Intent.ACTION_VIEW, webUri)

        if (isAppInstalled(context.packageManager, githubPackageName)) {
            try {
                context.startActivity(githubIntent)
            } catch (e: Exception) {
                context.startActivity(webIntent)
            }
        } else {
            context.startActivity(webIntent)
        }
    }

    actual fun launchEmail(thanksEmailStr: String, noEmailStr: String) {
        val context = MainActivityDelegate.context

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("Niels_Lee@outlook.com"))
        }

        if (emailIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(emailIntent)
            ToastUtil.show(thanksEmailStr)
        } else {
            ToastUtil.show(noEmailStr)
        }
    }

    private fun isAppInstalled(packageManager: PackageManager, packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

}