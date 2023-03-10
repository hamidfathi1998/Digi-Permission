package ir.hfathi.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


object DigiPermission {

    fun initialize(activity: FragmentActivity): PermissionIntermediary = PermissionIntermediary(activity)

    fun initialize(fragment: Fragment): PermissionIntermediary = PermissionIntermediary(fragment)

    fun isGranted(context: Context, permission: String): Boolean = ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED

}
