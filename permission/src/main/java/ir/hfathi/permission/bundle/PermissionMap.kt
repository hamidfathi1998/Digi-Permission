
package ir.hfathi.permission.bundle

import android.Manifest

val allSpecialPermissions = setOf(
    RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION,
    Manifest.permission.SYSTEM_ALERT_WINDOW,
    Manifest.permission.WRITE_SETTINGS,
    RequestManageExternalStoragePermission.MANAGE_EXTERNAL_STORAGE,
    RequestInstallPackagesPermission.REQUEST_INSTALL_PACKAGES,
    RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND,
)


