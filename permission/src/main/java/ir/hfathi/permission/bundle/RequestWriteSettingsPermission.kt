package ir.hfathi.permission.bundle

import android.Manifest
import android.os.Build
import android.provider.Settings

internal class RequestWriteSettingsPermission internal constructor(permissionBuilder: PermissionBuilder) :
    BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestWriteSettingsPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pb.targetSdkVersion >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(pb.activity)) {
                    finish()
                    return
                }
            } else {
                pb.grantedPermissions.add(Manifest.permission.WRITE_SETTINGS)
                pb.specialPermissions.remove(Manifest.permission.WRITE_SETTINGS)
                finish()
            }
        } else {
            finish()
        }
    }

    override fun requestAgain(permissions: List<String>) {
        pb.requestWriteSettingsPermissionNow(this)
    }
}