package ir.hfathi.permission.bundle

import ir.hfathi.permission.DigiPermission

internal class RequestBodySensorsBackgroundPermission internal constructor(permissionBuilder: PermissionBuilder)
    : BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestBodySensorsBackgroundPermission()) {
            if (DigiPermission.isGranted(pb.activity, BODY_SENSORS_BACKGROUND)) {
                finish()
                return
            }
        }
        finish()
    }

    override fun requestAgain(permissions: List<String>) {
        pb.requestBodySensorsBackgroundPermissionNow(this)
    }

    companion object {
        const val BODY_SENSORS_BACKGROUND = "android.permission.BODY_SENSORS_BACKGROUND"
    }
}