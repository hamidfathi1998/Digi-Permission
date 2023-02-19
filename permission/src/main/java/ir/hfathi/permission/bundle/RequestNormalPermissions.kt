package ir.hfathi.permission.bundle

import ir.hfathi.permission.DigiPermission
import java.util.*

internal class RequestNormalPermissions internal constructor(permissionBuilder: PermissionBuilder) :
    BaseTask(permissionBuilder) {

    override fun request() {
        val requestList = ArrayList<String>()
        for (permission in pb.normalPermissions) {
            if (DigiPermission.isGranted(pb.activity, permission)) {
                pb.grantedPermissions.add(permission)
            } else {
                requestList.add(permission)
            }
        }
        if (requestList.isEmpty()) {
            finish()
            return
        }
        if (pb.explainReasonBeforeRequest  ) {
            pb.explainReasonBeforeRequest = false
            pb.deniedPermissions.addAll(requestList)
        } else {
            pb.requestNow(pb.normalPermissions, this)
        }
    }

    override fun requestAgain(permissions: List<String>) {
        val permissionsToRequestAgain: MutableSet<String> = HashSet(pb.grantedPermissions)
        permissionsToRequestAgain.addAll(permissions)
        if (permissionsToRequestAgain.isNotEmpty()) {
            pb.requestNow(permissionsToRequestAgain, this)
        } else {
            finish()
        }
    }
}