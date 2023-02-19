package ir.hfathi.permission.bundle

import android.annotation.SuppressLint
import android.os.Build
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import ir.hfathi.permission.DigiPermission
import java.util.ArrayList

class InvisibleFragment : Fragment() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var pb: PermissionBuilder
    private lateinit var task: ChainTask


    private val requestNormalPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
            postForResult {
                onRequestNormalPermissionsResult(grantResults)
            }
        }

    private val requestBackgroundLocationLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            postForResult {
                onRequestBackgroundLocationPermissionResult(granted)
            }
        }

    private val requestSystemAlertWindowLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            postForResult {
                onRequestSystemAlertWindowPermissionResult()
            }
        }

    private val requestWriteSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            postForResult {
                onRequestWriteSettingsPermissionResult()
            }
        }

    private val requestManageExternalStorageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            postForResult {
                onRequestManageExternalStoragePermissionResult()
            }
        }

    private val requestInstallPackagesLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            postForResult {
                onRequestInstallPackagesPermissionResult()
            }
        }

    private val requestBodySensorsBackgroundLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            postForResult {
                onRequestBodySensorsBackgroundPermissionResult(granted)
            }
        }

    fun requestNow(
        permissionBuilder: PermissionBuilder,
        permissions: Set<String>,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        requestNormalPermissionLauncher.launch(permissions.toTypedArray())
    }

    fun requestAccessBackgroundLocationPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        requestBackgroundLocationLauncher.launch(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
    }

    fun requestSystemAlertWindowPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(requireContext())) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:${requireActivity().packageName}")
            requestSystemAlertWindowLauncher.launch(intent)
        } else {
            onRequestSystemAlertWindowPermissionResult()
        }
    }

    fun requestWriteSettingsPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(requireContext())) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:${requireActivity().packageName}")
            requestWriteSettingsLauncher.launch(intent)
        } else {
            onRequestWriteSettingsPermissionResult()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun requestManageExternalStoragePermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            var intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:${requireActivity().packageName}")
            if (intent.resolveActivity(requireActivity().packageManager) == null) {
                intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            }
            requestManageExternalStorageLauncher.launch(intent)
        } else {
            onRequestManageExternalStoragePermissionResult()
        }
    }

    fun requestInstallPackagesPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            intent.data = Uri.parse("package:${requireActivity().packageName}")
            requestInstallPackagesLauncher.launch(intent)
        } else {
            onRequestInstallPackagesPermissionResult()
        }
    }

    fun requestBodySensorsBackgroundPermissionNow(
        permissionBuilder: PermissionBuilder,
        chainTask: ChainTask
    ) {
        pb = permissionBuilder
        task = chainTask
        requestBodySensorsBackgroundLauncher.launch(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (checkForGC()) {
            pb.currentDialog?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
        }
    }

    private fun onRequestNormalPermissionsResult(grantResults: Map<String, Boolean>) {
        if (checkForGC()) {
            pb.grantedPermissions.clear()
            val showReasonList: MutableList<String> = ArrayList()
            val forwardList: MutableList<String> = ArrayList()
            for ((permission, granted) in grantResults) {
                if (granted) {
                    pb.grantedPermissions.add(permission)
                    pb.deniedPermissions.remove(permission)
                    pb.permanentDeniedPermissions.remove(permission)
                } else {
                    val shouldShowRationale = shouldShowRequestPermissionRationale(permission)
                    if (shouldShowRationale) {
                        showReasonList.add(permission)
                        pb.deniedPermissions.add(permission)
                    } else {
                        forwardList.add(permission)
                        pb.permanentDeniedPermissions.add(permission)
                        pb.deniedPermissions.remove(permission)
                    }
                }
            }
            val deniedPermissions: MutableList<String> = ArrayList()
            deniedPermissions.addAll(pb.deniedPermissions)
            deniedPermissions.addAll(pb.permanentDeniedPermissions)
            for (permission in deniedPermissions) {
                if (DigiPermission.isGranted(requireContext(), permission)) {
                    pb.deniedPermissions.remove(permission)
                    pb.grantedPermissions.add(permission)
                }
            }
            val allGranted = pb.grantedPermissions.size == pb.normalPermissions.size
            if (allGranted) {
                task.finish()
            } else {
                var shouldFinishTheTask = true
                if (showReasonList.isNotEmpty()) {
                    shouldFinishTheTask = false
                    pb.tempPermanentDeniedPermissions.addAll(forwardList)
                } else if (forwardList.isNotEmpty() || pb.tempPermanentDeniedPermissions.isNotEmpty()) {
                    shouldFinishTheTask = false
                    pb.tempPermanentDeniedPermissions.clear()
                }
                if (shouldFinishTheTask || !pb.showDialogCalled) {
                    task.finish()
                }
                pb.showDialogCalled = false
            }
        }
    }

    private fun onRequestBackgroundLocationPermissionResult(granted: Boolean) {
        if (checkForGC()) {
            postForResult {
                if (granted) {
                    pb.grantedPermissions.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    pb.deniedPermissions.remove(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    pb.permanentDeniedPermissions.remove(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    task.finish()
                } else {
                    var goesToRequestCallback = true
                    val shouldShowRationale =
                        shouldShowRequestPermissionRationale(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    if (shouldShowRationale) {
                        goesToRequestCallback = false
                        val permissionsToExplain: MutableList<String> = ArrayList()
                        permissionsToExplain.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    }
                    if (goesToRequestCallback || !pb.showDialogCalled) {
                        task.finish()
                    }
                }
            }
        }
    }

    private fun onRequestSystemAlertWindowPermissionResult() {
        if (checkForGC()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(requireContext())) {
                    task.finish()
                }
            } else {
                task.finish()
            }
        }
    }

    private fun onRequestWriteSettingsPermissionResult() {
        if (checkForGC()) {
            postForResult {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.System.canWrite(requireContext())) {
                        task.finish()
                    }
                } else {
                    task.finish()
                }
            }
        }
    }

    private fun onRequestManageExternalStoragePermissionResult() {
        if (checkForGC()) {
            postForResult {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        task.finish()
                    }
                } else {
                    task.finish()
                }
            }
        }
    }

    private fun onRequestInstallPackagesPermissionResult() {
        if (checkForGC()) {
            postForResult {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (requireActivity().packageManager.canRequestPackageInstalls()) {
                        task.finish()
                    }
                } else {
                    task.finish()
                }
            }
        }
    }

    private fun onRequestBodySensorsBackgroundPermissionResult(granted: Boolean) {
        if (checkForGC()) {
            postForResult {
                if (granted) {
                    pb.grantedPermissions.add(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                    pb.deniedPermissions.remove(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                    pb.permanentDeniedPermissions.remove(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                    task.finish()
                } else {
                    var goesToRequestCallback = true
                    val shouldShowRationale =
                        shouldShowRequestPermissionRationale(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                    if (shouldShowRationale) {
                        goesToRequestCallback =
                            false
                        val permissionsToExplain: MutableList<String> = ArrayList()
                        permissionsToExplain.add(RequestBodySensorsBackgroundPermission.BODY_SENSORS_BACKGROUND)
                    }
                    if (goesToRequestCallback || !pb.showDialogCalled) {
                        task.finish()
                    }
                }
            }
        }
    }

    private fun checkForGC(): Boolean {
        if (!::pb.isInitialized || !::task.isInitialized) {
            Log.w(
                "DigiPermission",
                "PermissionBuilder and ChainTask should not be null at this time, so we can do nothing in this case."
            )
            return false
        }
        return true
    }

    private fun postForResult(callback: () -> Unit) {
        handler.post {
            callback()
        }
    }
}