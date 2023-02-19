package ir.hfathi.permission.bundle

import android.Manifest
import android.os.Build
import android.provider.Settings

internal class RequestSystemAlertWindowPermission internal constructor(permissionBuilder: PermissionBuilder) :
    BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestSystemAlertWindowPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pb.targetSdkVersion >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(pb.activity)) {
                    finish()
                    return
                }
            } else {
                pb.grantedPermissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
                pb.specialPermissions.remove(Manifest.permission.SYSTEM_ALERT_WINDOW)
                finish()
            }
        } else {
            finish()
        }
    }

    override fun requestAgain(permissions: List<String>) {
        pb.requestSystemAlertWindowPermissionNow(this)
    }
}