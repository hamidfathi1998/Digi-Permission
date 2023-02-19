package ir.hfathi.permission.bundle

import android.os.Build

internal class RequestInstallPackagesPermission internal constructor(permissionBuilder: PermissionBuilder) :
    BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestInstallPackagesPermission()
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && pb.targetSdkVersion >= Build.VERSION_CODES.O) {
            if (pb.activity.packageManager.canRequestPackageInstalls()) {
                finish()
                return
            }
        } else {
            finish()
        }
    }

    override fun requestAgain(permissions: List<String>) {
        pb.requestInstallPackagePermissionNow(this)
    }

    companion object {
        const val REQUEST_INSTALL_PACKAGES = "android.permission.REQUEST_INSTALL_PACKAGES"
    }
}