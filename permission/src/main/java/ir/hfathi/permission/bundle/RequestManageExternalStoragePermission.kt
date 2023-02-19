package ir.hfathi.permission.bundle

import android.os.Build
import android.os.Environment

internal class RequestManageExternalStoragePermission internal constructor(permissionBuilder: PermissionBuilder) :
    BaseTask(permissionBuilder) {

    override fun request() {
        if (pb.shouldRequestManageExternalStoragePermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                finish()
                return
            }
            return
        }
        finish()
    }

    override fun requestAgain(permissions: List<String>) {
        pb.requestManageExternalStoragePermissionNow(this)
    }

    companion object {
        const val MANAGE_EXTERNAL_STORAGE = "android.permission.MANAGE_EXTERNAL_STORAGE"
    }
}