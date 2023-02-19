package ir.hfathi.permission.bundle

import android.os.Build
import ir.hfathi.permission.DigiPermission

internal class RequestBackgroundLocationPermission internal constructor(permissionBuilder: PermissionBuilder)
    : BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestBackgroundLocationPermission()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                pb.specialPermissions.remove(ACCESS_BACKGROUND_LOCATION)
                pb.permissionsWontRequest.add(ACCESS_BACKGROUND_LOCATION)
                finish()
                return
            }
            if (DigiPermission.isGranted(pb.activity, ACCESS_BACKGROUND_LOCATION)) {
                finish()
                return
            }
        }
        finish()
    }


    override fun requestAgain(permissions: List<String>) {
        pb.requestAccessBackgroundLocationPermissionNow(this)
    }

    companion object {
        const val ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION"
    }
}