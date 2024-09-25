package lying.fengfeng.foodrecords.backup

import lying.fengfeng.foodrecords.MainActivityDelegate

actual object BackupLauncherDelegate {
    actual fun launchImportLauncher() {
        MainActivityDelegate.context.importLauncher.launch("")
    }

    actual fun launchExportLauncher() {
        MainActivityDelegate.context.exportLauncher.launch("")
    }

}